package net.myriantics.myrror.datagen.template.dynamic;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.myriantics.myrror.core.MyrrorProvider;
import net.myriantics.myrror.core.RegistryDependentProvider;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public final class MyrrorDynamicRegistryProvider extends FabricDynamicRegistryProvider implements MyrrorProvider {
    final String namespace;
    private final ArrayList<DynamicRegistrySubProviderFactory<?>> factories = new ArrayList<>();

    public MyrrorDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String namespace) {
        super(output, registriesFuture);
        this.namespace = namespace;
    }

    public static RegistryDependentProvider<MyrrorDynamicRegistryProvider> of(String namespace) {
        return (output, registriesFuture) -> new MyrrorDynamicRegistryProvider(output, registriesFuture, namespace);
    }

    public MyrrorDynamicRegistryProvider add(DynamicRegistrySubProviderFactory<?> factory) {
        this.factories.add(factory);
        return this;
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }

    public interface DynamicRegistrySubProviderFactory<T extends MyrrorDynamicRegistrySubProvider<?>> {
        T create(MyrrorDynamicRegistryProvider provider, HolderLookup.Provider lookup, ILoveGenericsConsumer consumer);
    }

    public interface ILoveGenericsConsumer {
        <T> void consume(ResourceKey<T> key, T value);
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
