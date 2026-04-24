package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class BFSounds {
	public static final SoundEvent THUNDER = registerSound("thunder");
	public static final SoundEvent HEAL = registerSound("heal");

	// actual registration of all the custom SoundEvents
	private static SoundEvent registerSound(String id) {
		Identifier identifier = Identifier.fromNamespaceAndPath(BloomFestal.ID, id);
		return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
	}

	public static void init()
	{

	}
}
