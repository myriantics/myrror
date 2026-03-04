package net.myriantics.myrror.core;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;

import java.io.DataOutput;
import java.util.concurrent.CompletableFuture;

public interface RegistryDependentProvider<T extends DataProvider> {
    T create(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture);
}
