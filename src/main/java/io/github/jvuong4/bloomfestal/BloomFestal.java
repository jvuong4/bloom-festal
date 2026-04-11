package io.github.jvuong4.bloomfestal;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloomFestal implements ModInitializer {
	public static final String ID = "bloom_festal";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Bloom Festal] you are the ocean's BLOOM waves");
	}
}
