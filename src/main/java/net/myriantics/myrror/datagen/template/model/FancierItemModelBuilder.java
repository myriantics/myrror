package net.myriantics.myrror.datagen.template.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.myriantics.myrror.mixin.ModelTemplateAccessor;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class FancierItemModelBuilder {
    private final ResourceLocation modelId;
    private final ModelTemplate parentModel;
    private final ModelTemplate model;
    private final Map<TextureSlot, ResourceLocation> defaultTextureMap;
    private final ArrayList<FancyOverride> overrides = new ArrayList<>();

    private FancierItemModelBuilder(ModelTemplate parentModel, ResourceLocation modelId, Map<TextureSlot, ResourceLocation> defaultTextureMap) {
        this.parentModel = parentModel;
        this.modelId = modelId;
        this.defaultTextureMap = defaultTextureMap;
        this.model = new ModelTemplate(Optional.of(modelId), Optional.empty(), ((ModelTemplateAccessor) parentModel).klaxon$getRequiredTextureSlots().toArray(new TextureSlot[0]));
    }

    public static FancierItemModelBuilder of(ModelTemplate rootModel, ResourceLocation rootModelId, Map<TextureSlot, ResourceLocation> defaultTextureMap) {
        return new FancierItemModelBuilder(rootModel, rootModelId, defaultTextureMap);
    }

    public FancierTextureOverrideBuilder textureOverride(ResourceLocation predicateId, String TextureSlot) {
        return textureOverride(predicateId.toString(), TextureSlot);
    }

    public FancierTextureOverrideBuilder textureOverride(String predicateId, String TextureSlot) {
        this.validatePredicateId(predicateId);
        return new FancierTextureOverrideBuilder(this, predicateId, TextureSlot);
    }

    public FancierModelOverrideBuilder modelOverride(ResourceLocation predicateId) {
        return modelOverride(predicateId.toString());
    }

    public FancierModelOverrideBuilder modelOverride(String predicateId) {
        this.validatePredicateId(predicateId);
        return new FancierModelOverrideBuilder(this, predicateId);
    }

    private void validatePredicateId(String predicateId) {
        for (FancyOverride fancyOverride : overrides) {
            for (char c : fancyOverride.predicateId.toCharArray()) {
                if (!ResourceLocation.isAllowedInResourceLocation(c)) {
                    throw new IllegalArgumentException("Invalid character \"" + c + "\" present in predicate \"" + predicateId + "\" of: [" + this.getClass().getName() + ":" + this.modelId + "]");
                }
            }

            if (fancyOverride.predicateId.equals(predicateId)) {
                throw new IllegalArgumentException("Predicate ID \"" + predicateId + "\" already present in: [" + this.getClass().getName() + ":" + this.modelId + "]");
            }
        }
    }

    private void validateValues(String predicateId, List<Number> values) {
        for (Number n : values) {
            if (n.floatValue() < 0 || n.floatValue() > 1) {
                throw new IllegalArgumentException("Value [" + n + "] of predicate \"" + predicateId + "\" out of bounds. Should be between [0, 1]. Problematic ModelTemplate ID: \"" + this.modelId + "\"");
            }
        }
    }

    public void build(ItemModelGenerators generator) {
        this.build(generator.output);
    }

    public void build(BiConsumer<ResourceLocation, Supplier<JsonElement>> modelCollector) {
        // collect the amount of overrides
        int overrideCount = 0;
        for (FancyOverride override : this.overrides) {
            if (overrideCount == 0) {
                overrideCount++;
            }
            overrideCount *= override.values.size();
        }

        JsonArray overrides = new JsonArray(0);

        int[] max = new int[this.overrides.size()];
        for (int i = 0; i < max.length; i++) {
            max[i] = this.overrides.get(i).values.size();
        }

        int[] incrementor = new int[this.overrides.size()];
        while (incrementor.length > 0 && incrementor[incrementor.length - 1] != max[max.length - 1]) {
            ModelTemplate overrideModel = this.model;
            HashMap<TextureSlot, ResourceLocation> textureMap = HashMap.newHashMap(this.defaultTextureMap.size());
            textureMap.putAll(this.defaultTextureMap);

            JsonObject predicates = new JsonObject();

            StringBuilder pathBuilder = new StringBuilder(modelId.getPath() + "/");

            for (int i = 0; i < this.overrides.size(); i++) {
                FancyOverride override = this.overrides.get(i);

                String predicateId = override.predicateId.lastIndexOf(ResourceLocation.NAMESPACE_SEPARATOR) == -1 ? override.predicateId : override.predicateId.substring(override.predicateId.lastIndexOf(ResourceLocation.NAMESPACE_SEPARATOR) + 1);
                Number selectedValue = override.values.get(incrementor[i]);
                if (override instanceof FancyModelOverride modelOverride) {

                    overrideModel = modelOverride.getModel(incrementor[i]);
                    pathBuilder.append(predicateId).append("_").append(selectedValue).append("/");

                } else if (override instanceof FancyTextureOverride textureOverride) {

                    boolean succeeded = false;
                    for (TextureSlot TextureSlot : textureMap.keySet()) {
                        if (TextureSlot.getId().equals(textureOverride.textureSlot)) {
                            textureMap.replace(TextureSlot, textureOverride.getTexture(incrementor[i]));
                            succeeded = true;
                            break;
                        }
                    }
                    if (!succeeded) {
                        throw new RuntimeException("Texture Key [" + textureOverride.textureSlot + "] not found in texture map + [" + textureMap + "]. Override failed.");
                    }

                    if (pathBuilder.toString().charAt(pathBuilder.length() - 1) != '/') {
                        pathBuilder.append('_');
                    }
                    pathBuilder.append(predicateId).append('_').append(selectedValue);
                }
                // add the property to the predicate list
                predicates.addProperty(override.predicateId, selectedValue);
            }

            ResourceLocation modelId = this.modelId.withPath(pathBuilder.toString());

            // build the model
            FancierItemModelBuilder.of(overrideModel, modelId, textureMap).build(modelCollector);

            // assemble the override object and add it to the array
            JsonObject override = new JsonObject();
            override.add("predicate", predicates);
            override.addProperty("model", modelId.toString());
            overrides.add(override);

            // update the incrementor for the next go-round
            for (int i = 0; i < incrementor.length; i++) {
                incrementor[i]++;
                if (incrementor[i] < max[i]) {
                    break;
                } else if (i != incrementor.length - 1) {
                    incrementor[i] = 0;
                }
            }
        }

        // build the parent model, overrides and all
        modelCollector.accept(
                modelId,
                () -> {
                    JsonObject modelJson = parentModel.createBaseTemplate(modelId, defaultTextureMap);
                    if (!overrides.isEmpty()) {
                        modelJson.add("overrides", overrides);
                    }
                    return modelJson;
                }
        );
    }

    public static class FancierTextureOverrideBuilder {
        private final FancierItemModelBuilder builder;
        private final String textureSlot;
        private final String predicateId;
        private final List<Number> values = new ArrayList<>();
        private final List<ResourceLocation> textures = new ArrayList<>();

        private FancierTextureOverrideBuilder(FancierItemModelBuilder builder, String predicateId, String TextureSlot) {
            this.builder = builder;
            this.textureSlot = TextureSlot;
            this.predicateId = predicateId;
        }

        public FancierTextureOverrideBuilder add(Number value, ResourceLocation texture) {
            this.values.add(value);
            this.textures.add(texture);
            return this;
        }

        public FancierItemModelBuilder endOverride() {
            builder.validateValues(predicateId, values);
            builder.overrides.add(new FancyTextureOverride(predicateId, textureSlot, values, textures));
            return builder;
        }
    }

    public static class FancierModelOverrideBuilder {
        private final FancierItemModelBuilder builder;
        private final String predicateId;
        private final ArrayList<ModelTemplate> models = new ArrayList<>();
        private final ArrayList<Number> values = new ArrayList<>();

        private FancierModelOverrideBuilder(FancierItemModelBuilder builder, String predicateId) {
            this.builder = builder;
            this.predicateId = predicateId;
        }

        public FancierModelOverrideBuilder add(Number number, ModelTemplate model) {
            this.models.add(model);
            this.values.add(number);
            return this;
        }

        public FancierItemModelBuilder endOverride() {
            builder.validateValues(predicateId, values);
            this.builder.overrides.add(new FancyModelOverride(this.predicateId, values, models));
            return builder;
        }
    }

    private static abstract class FancyOverride {
        protected final List<Number> values;
        protected final String predicateId;

        protected FancyOverride(String predicateId, List<Number> values) {
            this.values = values;
            this.predicateId = predicateId;
        }

        public List<Number> getValues() {
            return this.values;
        }

        public String getPredicateId() {
            return this.predicateId;
        }
    }

    private static final class FancyModelOverride extends FancyOverride {
        private final List<ModelTemplate> models;

        FancyModelOverride(String predicateId, List<Number> values, List<ModelTemplate> models) {
            super(predicateId, values);
            this.models = models;
        }

        public ModelTemplate getModel(int index) {
            return this.models.get(index);
        }
    }

    private static final class FancyTextureOverride extends FancyOverride {
        private final String textureSlot;
        private final List<ResourceLocation> textureIds;

        FancyTextureOverride(String predicateId, String textureSlot, List<Number> values, List<ResourceLocation> textureIds) {
            super(predicateId, values);
            this.textureSlot = textureSlot;
            this.textureIds = textureIds;
        }

        public ResourceLocation getTexture(int index) {
            return this.textureIds.get(index);
        }
    }
}