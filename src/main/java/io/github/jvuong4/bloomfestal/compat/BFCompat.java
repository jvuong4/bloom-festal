package io.github.jvuong4.bloomfestal.compat;

import io.github.jvuong4.bloomfestal.BloomFestal;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.InvocationTargetException;

public interface BFCompat {

	void initialize();

	String COMPAT_PACKAGE_PREFIX = "io.github.jvuong4.bloomfestal.compat.";

	private static void tryInit(String compatClass, String modRef) {
		var loader = FabricLoader.getInstance();
		if (!loader.isModLoaded(modRef)) {
			BloomFestal.LOGGER.debug("Did not find mod " + modRef + ", skipping compat check");
			return;
		}

		BloomFestal.LOGGER.debug("Loading compat for mod " + modRef);

		String className = COMPAT_PACKAGE_PREFIX + compatClass;
		BFCompat compat;
		try {
			compat = (BFCompat)Class.forName(className).getDeclaredConstructor().newInstance();
		} catch (ClassNotFoundException err) {
			BloomFestal.LOGGER.error("Unable to find compatibility class: " + className);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				throw new RuntimeException(err);
			}
			return;
		} catch (NoSuchMethodException | InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException | ClassCastException err) {
			// you can tell from the number of exceptions we handle here that our code
			// is robust and sensible
			BloomFestal.LOGGER.error("Failed to run constructor for: " + className);
			if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
				throw new RuntimeException(err);
			}
			return;
		}
		compat.initialize();
	}

	static void init() {
		BloomFestal.LOGGER.debug("[Bloom Festal] Loading compatibilities");
		tryInit("DualStanceCompat", "dual-stance");
	}
}
