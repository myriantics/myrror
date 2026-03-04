package net.myriantics.myrror.datagen.template.dynamic.providers;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistryProvider;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistrySubProvider;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public abstract class ArmorTrimMaterialProvider extends MyrrorDynamicRegistrySubProvider<TrimMaterial> {
    public ArmorTrimMaterialProvider(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, MyrrorDynamicRegistryProvider.ILoveGenericsConsumer consumer) {
        super(provider, lookup, consumer, Registries.TRIM_MATERIAL);
    }

    protected TrimMaterial addTrimMaterial(ResourceKey<TrimMaterial> key, Item ingredient, float modelIndex, UnaryOperator<Builder> operator) {
        TrimMaterial material = operator.apply(Builder.of(key, ingredient, modelIndex)).build();
        this.add(key, material);
        return material;
    }

    protected static final class Builder {
        private final ResourceKey<TrimMaterial> key;
        private final Item ingredient;
        private final float modelIndex;
        private Component description;
        private HashMap<Holder<ArmorMaterial>, String> overrideMaterials = new HashMap<>();

        private Builder(ResourceKey<TrimMaterial> key, Item ingredient, float modelIndex) {
            this.key = key;
            this.ingredient = ingredient;
            this.modelIndex = modelIndex;
            this.description = Component.translatable(Util.makeDescriptionId(Registries.TRIM_MATERIAL.location().getPath(), key.location()));
        }

        private static Builder of(ResourceKey<TrimMaterial> key, Item ingredient, float modelIndex) {
            return new Builder(key, ingredient, modelIndex);
        }

        public Builder description(Component description) {
            this.description = description;
            return this;
        }

        public Builder descColor(int color) {
            return this.descStyle(this.description.getStyle().withColor(color));
        }

        public Builder descStyle(Style style) {
            this.description = this.description.copy().setStyle(style);
            return this;
        }

        public Builder override(Holder<ArmorMaterial> materialHolder, String path) {
            this.overrideMaterials.put(materialHolder, path);
            return this;
        }

        public TrimMaterial build() {
            return TrimMaterial.create(this.key.location().getPath(), this.ingredient, this.modelIndex, this.description, this.overrideMaterials);
        }
    }
}
