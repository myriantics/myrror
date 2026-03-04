package net.myriantics.myrror;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.myriantics.myrror.datagen.template.dynamic.MyrrorDynamicRegistryProvider;

public class MyrrorDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		// example
		// yeah i did name my test class that
		// so what
		/*
		pack.addProvider((output, registriesFuture) -> new MyrrorDynamicRegistryProvider(output, registriesFuture, MyrrorCommon.MOD_ID)
				.add(Skibidi::new)
		);
		*/
	}
}
