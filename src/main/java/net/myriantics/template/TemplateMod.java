package net.myriantics.template;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateMod implements ModInitializer {
	public static final String MOD_ID = "template";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");
	}

	public static ResourceLocation locate(String name) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
	}
}