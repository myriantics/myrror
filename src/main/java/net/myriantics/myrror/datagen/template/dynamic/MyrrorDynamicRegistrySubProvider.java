package net.myriantics.myrror.datagen.template.dynamic;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.function.BiConsumer;

public abstract class MyrrorDynamicRegistrySubProvider<T> {
    protected final MyrrorDynamicRegistryProvider provider;
    protected final HolderLookup.Provider lookup;
    private final MyrrorDynamicRegistryProvider.ILoveGenericsConsumer consumer;
    private final ResourceKey<Registry<T>> registry;

    public MyrrorDynamicRegistrySubProvider(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, MyrrorDynamicRegistryProvider.ILoveGenericsConsumer consumer, ResourceKey<Registry<T>> registry) {
        this.provider = provider;
        this.lookup = lookup;
        this.consumer = consumer;
        this.registry = registry;
    }

    protected abstract void build();

    protected T add(ResourceKey<T> key, T value) {
        this.consumer.consume(key, value);
        if (!key.location().getNamespace().equals(this.provider.namespace)) {
            throw new IllegalArgumentException("Namespace of resource key [" + key + "] mismatched with larger provider namespace [" + this.provider.namespace + "]");
        }
        return value;
    }
}
