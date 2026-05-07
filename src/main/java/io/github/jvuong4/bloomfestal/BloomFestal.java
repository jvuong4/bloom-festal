package io.github.jvuong4.bloomfestal;

import io.github.jvuong4.bloomfestal.compat.BFCompat;
import io.github.jvuong4.bloomfestal.registry.*;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloomFestal implements ModInitializer {
	public static final String ID = "bloom_festal";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Bloom Festal] you are the ocean's BLOOM waves");
		BFItems.init();
		BFEntities.init();
		BFSounds.init();
		BFDamageTypes.init();
		BFEffects.init();
		BFParticles.init();
		BFCompat.init();
	}
}
