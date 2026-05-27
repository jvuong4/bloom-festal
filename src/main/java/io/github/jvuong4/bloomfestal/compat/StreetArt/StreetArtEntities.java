package io.github.jvuong4.bloomfestal.compat.StreetArt;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.entity.HealOrb;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class StreetArtEntities {

	public static final EntityType<DyeblastOrb> DYEBLAST_ORB = register(
		"dyeblast_orb",
		EntityType.Builder.<DyeblastOrb>of(DyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);

	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}

	public static void init() {

	}
}
