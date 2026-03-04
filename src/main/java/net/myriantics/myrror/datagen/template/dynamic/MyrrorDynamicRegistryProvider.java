package net.myriantics.myrror.datagen.template.dynamic;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.myriantics.myrror.core.RegistryDependentProvider;
import net.myriantics.myrror.datagen.template.advancement.MyrrorAdvancementProvider;
import net.myriantics.myrror.datagen.template.advancement.MyrrorAdvancementSubProvider;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public final class MyrrorDynamicRegistryProvider extends FabricDynamicRegistryProvider {
    final String namespace;

    public MyrrorDynamicRegistryProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture, String namespace) {
        super(output, registriesFuture);
        this.namespace = namespace;
    }

    public static RegistryDependentProvider<MyrrorDynamicRegistryProvider> of(String namespace) {
        return (output, registriesFuture) -> new MyrrorDynamicRegistryProvider(output, registriesFuture, namespace);
    }

    public interface AdvancementSubProviderFactory {
        MyrrorAdvancementSubProvider create(MyrrorAdvancementProvider provider, Consumer<AdvancementHolder> consumer);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {

    }

    @Override
    public String getName() {
        return "[" + namespace + "] Dynamic Registry Provider";
    }
}
