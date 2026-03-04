package net.myriantics.myrror.datagen.template.advancement;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public final class MyrrorAdvancementProvider extends AdvancementProvider {
    private final ArrayList<AdvancementSubProviderFactory> subProviders = new ArrayList<>();
    private final CompletableFuture<HolderLookup.Provider> registryLookup;
    private final PackOutput.PathProvider pathResolver;
    private final String namespace;

    public MyrrorAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String namespace) {
        super(output, registries, List.of());
        this.pathResolver = output.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
        this.registryLookup = registries;
        this.namespace = namespace;
    }

    public interface AdvancementSubProviderFactory {
        MyrrorAdvancementSubProvider create(MyrrorAdvancementProvider provider, Consumer<AdvancementHolder> consumer);
    }

    public void addProvider(AdvancementSubProviderFactory factory) {
        this.subProviders.add(factory);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return this.registryLookup.thenCompose(lookup -> {
            final Set<ResourceLocation> identifiers = Sets.newHashSet();
            final List<CompletableFuture<?>> futures = new ArrayList<>();

            Consumer<AdvancementHolder> consumer = advancementHolder -> {
                if (!identifiers.add(advancementHolder.id())) {
                    throw new IllegalStateException("Duplicate advancement " + advancementHolder.id());
                } else {
                    Path path = this.pathResolver.json(advancementHolder.id());
                    futures.add(DataProvider.saveStable(output, lookup, Advancement.CODEC, advancementHolder.value(), path));
                }
            };

            for (AdvancementSubProviderFactory factory : this.subProviders) {
                factory.create(this, consumer).generate();
            }

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    public String getNamespace() {
        return namespace;
    }
}
