package net.myriantics.myrror;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistryProvider;
import net.myriantics.myrror.datagen.template.recipe.MyrrorRecipeProvider;

public class MyrrorDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		fabricDataGenerator.createPack().addProvider(MyrrorDynamicRegistryProvider.of(MyrrorCommon.MOD_ID)));
	}
}
