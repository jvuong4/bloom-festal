package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class BFDamageTypes {
	public static final ResourceKey<DamageType> BRAVE_LANCE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, "brave_lance"));
	public static final ResourceKey<DamageType> ECLIPSE_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, "eclipse"));


	public static void init() {}
}
