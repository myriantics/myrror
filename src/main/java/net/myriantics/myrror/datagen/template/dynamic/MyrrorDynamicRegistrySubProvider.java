package net.myriantics.myrror.datagen.template.dynamic;

import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;

public abstract class MyrrorDynamicRegistrySubProvider<T> {
    protected final MyrrorDynamicRegistryProvider provider;
    protected final HolderLookup.Provider lookup;
    private final FabricDynamicRegistryProvider.Entries entries;
    private final ResourceKey<Registry<T>> registry;

    public MyrrorDynamicRegistrySubProvider(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, FabricDynamicRegistryProvider.Entries entries, ResourceKey<Registry<T>> registry) {
        this.provider = provider;
        this.lookup = lookup;
        this.entries = entries;
        this.registry = registry;
    }

    protected abstract void build();

    protected T add(ResourceKey<T> id, T value) {
        this.entries.add(id, value);
        return value;
    }

    protected ResourceLocation locate(String name) {
        return ResourceLocation.fromNamespaceAndPath(this.provider.namespace, name);
    }

    protected ResourceKey<T> resourceKey(String name) {
        return this.resourceKey(this.locate(name));
    }

    protected ResourceKey<T> resourceKey(ResourceLocation resourceLocation) {
        return ResourceKey.create(this.registry, resourceLocation);
    }
}
