package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.entity.HealOrb;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;

public class BFEntities {
	//who up pondering they orb?

	public static final EntityType<HealOrb> HEAL_ORB = register(
		"heal_orb",
		EntityType.Builder.<HealOrb>of(HealOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3125F, 0.3125F).clientTrackingRange(4).updateInterval(10)
	);

	/*
	public static final EntityType<SmallFireball> SMALL_FIREBALL = register(
		"small_fireball",
		EntityType.Builder.<SmallFireball>of(SmallFireball::new, MobCategory.MISC)
	);
	*/


	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}

	public static void registerModEntityTypes() {
		//BloomFestal.LOGGER.info("Registering EntityTypes for " + ExampleMod.MOD_ID);
	}

	/*
	public static void registerAttributes() {
		FabricDefaultAttributeRegistry.register(HEAL_ORB, HealOrb.createCubeAttributes());
	}
	 */

	public static void init()
	{

	}
}
