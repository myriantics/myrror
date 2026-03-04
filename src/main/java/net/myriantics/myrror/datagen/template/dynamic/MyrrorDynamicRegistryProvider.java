package net.myriantics.myrror.datagen.template.dynamic;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceKey;
import net.myriantics.myrror.core.RegistryDependentProvider;
import net.myriantics.myrror.datagen.template.advancement.MyrrorAdvancementProvider;
import net.myriantics.myrror.datagen.template.advancement.MyrrorAdvancementSubProvider;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class MyrrorDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    final String namespace;
    private final ArrayList<DynamicRegistrySubProviderFactory<?>> factories = new ArrayList<>();

    public MyrrorDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String namespace) {
        super(output, registriesFuture);
        this.namespace = namespace;
    }

    public static RegistryDependentProvider<MyrrorDynamicRegistryProvider> of(String namespace) {
        return (output, registriesFuture) -> new MyrrorDynamicRegistryProvider(output, registriesFuture, namespace);
    }

    public void register(DynamicRegistrySubProviderFactory<?> factory) {
        this.factories.add(factory);
    }

    public interface DynamicRegistrySubProviderFactory<T> {
        MyrrorDynamicRegistrySubProvider<T> create(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, BiConsumer<ResourceKey<T>, T> biConsumer);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        for (DynamicRegistrySubProviderFactory<?> factory : this.factories) {
            factory.create(this, registries, entries::add).build();
        }
    }

    @Override
    public String getName() {
        return "[" + namespace + "] Dynamic Registry Provider";
    }
}
