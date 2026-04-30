package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.effects.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class BFEffects {
	public static final Holder<MobEffect> REVELATION =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(BloomFestal.ID, "revelation"), new RevelationEffect());

	public static final Holder<MobEffect> HEXED = Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
		Identifier.fromNamespaceAndPath(BloomFestal.ID, "hexed"),
		(new HexedEffect().addAttributeModifier(Attributes.MAX_HEALTH, Identifier.withDefaultNamespace("effect.hexed"), (double)-0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)));

	public static final Holder<MobEffect> STILLNESS =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(BloomFestal.ID, "stillness"), new StillnessEffect()
			.addAttributeModifier(Attributes.MOVEMENT_SPEED, Identifier.withDefaultNamespace("effect.stillness"), (double)-1.00F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)

		);

	public static final Holder<MobEffect> SILENCE =
		Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, Identifier.fromNamespaceAndPath(BloomFestal.ID, "silence"), new SilenceEffect());


	public static void init() {}
}
