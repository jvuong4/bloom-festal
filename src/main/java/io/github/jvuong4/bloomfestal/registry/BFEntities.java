package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.entity.*;
import io.github.jvuong4.bloomfestal.entity.LightningBolt.VisualLightning;
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
			.noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<EclipseOrb> ECLIPSE_ORB = register(
		"eclipse_orb",
		EntityType.Builder.<EclipseOrb>of(EclipseOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<ThoronOrb> THORON_ORB = register(
		"thoron_orb",
		EntityType.Builder.<ThoronOrb>of(ThoronOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<RexcaliburOrb> REXCALIBUR_ORB = register(
		"rexcalibur_orb",
		EntityType.Builder.<RexcaliburOrb>of(RexcaliburOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<BolganoneOrb> BOLGANONE_ORB = register(
		"bolganone_orb",
		EntityType.Builder.<BolganoneOrb>of(BolganoneOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10)
	);

	public static final EntityType<VisualLightning> VISUAL_LIGHTNING = register(
		"visual_lightning", EntityType.Builder.<VisualLightning>of(VisualLightning::new, MobCategory.MISC)
			.noLootTable()
			.noSave()
			.sized(0.0F, 0.0F)
			.clientTrackingRange(16)
			.updateInterval(Integer.MAX_VALUE)
	);

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
