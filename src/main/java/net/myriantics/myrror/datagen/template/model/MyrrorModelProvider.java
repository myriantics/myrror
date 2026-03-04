package net.myriantics.myrror.datagen.template.model;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.ModelProvider;
import net.myriantics.myrror.core.MyrrorProvider;

import java.util.ArrayList;

public final class MyrrorModelProvider extends FabricModelProvider implements MyrrorProvider {

    private final ArrayList<BlockModelSubProviderFactory> blockModelSubProviderFactories = new ArrayList<>();
    private final ArrayList<ItemModelSubProviderFactory> itemModelSubProviderFactories = new ArrayList<>();
    final String namespace;

    public MyrrorModelProvider(FabricDataOutput output, String namespace) {
        super(output);
        this.namespace = namespace;
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        for (BlockModelSubProviderFactory factory : this.blockModelSubProviderFactories) {
            factory.create(this, blockStateModelGenerator).generate();
        }
    }

    @Override
    public String getNamespace() {
        return this.namespace;
    }


    public interface BlockModelSubProviderFactory {
        MyrrorBlockModelSubProvider create(MyrrorModelProvider provider, BlockModelGenerators generators);
    }

    public MyrrorModelProvider add(BlockModelSubProviderFactory factory) {
        this.blockModelSubProviderFactories.add(factory);
        return this;
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        for (ItemModelSubProviderFactory factory : this.itemModelSubProviderFactories) {
            factory.create(this, itemModelGenerator).generate();
        }
    }

    public interface ItemModelSubProviderFactory {
        MyrrorBlockModelSubProvider create(MyrrorModelProvider provider, ItemModelGenerators generators);
    }

    public MyrrorModelProvider add(ItemModelSubProviderFactory factory) {
        this.itemModelSubProviderFactories.add(factory);
        return this;
    }
}
