package net.myriantics.myrror.datagen.template.model;

import net.minecraft.data.models.BlockModelGenerators;

public abstract class MyrrorBlockModelSubProvider {
    private final MyrrorModelProvider provider;
    protected final BlockModelGenerators generators;

    protected MyrrorBlockModelSubProvider(MyrrorModelProvider provider, BlockModelGenerators generators) {
        this.provider = provider;
        this.generators = generators;
    }

    public abstract void generate();

}
