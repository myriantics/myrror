package net.myriantics.myrror.datagen.template.model;

import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.function.UnaryOperator;

public abstract class MyrrorItemModelSubProvider {
    private final MyrrorModelProvider provider;
    protected final ItemModelGenerators generators;

    protected MyrrorItemModelSubProvider(MyrrorModelProvider provider, ItemModelGenerators generators) {
        this.provider = provider;
        this.generators = generators;
    }

    public abstract void generate();

    protected void add(ModelTemplate base, ResourceLocation modelId, Map<TextureSlot, ResourceLocation> baseTextures, UnaryOperator<FancierItemModelBuilder> operator) {
        operator.apply(FancierItemModelBuilder.of(base, modelId, baseTextures)).build(this.generators);
    }

    public ResourceLocation getItemId(String name) {
        return getItemId(ResourceLocation.fromNamespaceAndPath(this.provider.namespace, name));
    }

    public ResourceLocation getItemId(ResourceLocation id) {
        return id.withPath((path) -> "item/" + path);
    }
}
