package io.github.jvuong4.bloomfestal.registry;

import com.mojang.serialization.Codec;
import io.github.jvuong4.bloomfestal.BloomFestal;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class BFDataComponents {

	public static final DataComponentType<Integer> SILENCE_TICKS = Registry.register(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		Identifier.fromNamespaceAndPath(BloomFestal.ID, "silence_ticks"),
		DataComponentType.<Integer>builder().persistent(Codec.INT).build()
	);

	public static void init() {}
}
