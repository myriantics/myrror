package net.myriantics.myrror.core;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public final class MyrrorPack {
    private final DataGenerator.PackGenerator pack;

    public MyrrorPack(DataGenerator.PackGenerator pack) {
        this.pack = pack;
    }

    public static MyrrorPack of(DataGenerator.PackGenerator pack) {
        return new MyrrorPack(pack);
    }

    public <T extends MyrrorProvider & DataProvider> void add(DataProvider.Factory<T> provider) {
        this.pack.addProvider(provider);
    }
}
