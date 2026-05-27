package io.github.jvuong4.bloomfestal.compat.StreetArt;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.compat.StreetArt.orbColors.*;
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

	public static final EntityType<RedDyeblastOrb> RED_DYEBLAST_ORB = register(
		"red_dyeblast_orb",
		EntityType.Builder.<RedDyeblastOrb>of(RedDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<OrangeDyeblastOrb> ORANGE_DYEBLAST_ORB = register(
		"orange_dyeblast_orb",
		EntityType.Builder.<OrangeDyeblastOrb>of(OrangeDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<YellowDyeblastOrb> YELLOW_DYEBLAST_ORB = register(
		"yellow_dyeblast_orb",
		EntityType.Builder.<YellowDyeblastOrb>of(YellowDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<LimeDyeblastOrb> LIME_DYEBLAST_ORB = register(
		"lime_dyeblast_orb",
		EntityType.Builder.<LimeDyeblastOrb>of(LimeDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<GreenDyeblastOrb> GREEN_DYEBLAST_ORB = register(
		"green_dyeblast_orb",
		EntityType.Builder.<GreenDyeblastOrb>of(GreenDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<CyanDyeblastOrb> CYAN_DYEBLAST_ORB = register(
		"cyan_dyeblast_orb",
		EntityType.Builder.<CyanDyeblastOrb>of(CyanDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<LightBlueDyeblastOrb> LIGHT_BLUE_DYEBLAST_ORB = register(
		"light_blue_dyeblast_orb",
		EntityType.Builder.<LightBlueDyeblastOrb>of(LightBlueDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<BlueDyeblastOrb> BLUE_DYEBLAST_ORB = register(
		"blue_dyeblast_orb",
		EntityType.Builder.<BlueDyeblastOrb>of(BlueDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<PinkDyeblastOrb> PINK_DYEBLAST_ORB = register(
		"pink_dyeblast_orb",
		EntityType.Builder.<PinkDyeblastOrb>of(PinkDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<MagentaDyeblastOrb> MAGENTA_DYEBLAST_ORB = register(
		"magenta_dyeblast_orb",
		EntityType.Builder.<MagentaDyeblastOrb>of(MagentaDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<PurpleDyeblastOrb> PURPLE_DYEBLAST_ORB = register(
		"purple_dyeblast_orb",
		EntityType.Builder.<PurpleDyeblastOrb>of(PurpleDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<BrownDyeblastOrb> BROWN_DYEBLAST_ORB = register(
		"brown_dyeblast_orb",
		EntityType.Builder.<BrownDyeblastOrb>of(BrownDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<WhiteDyeblastOrb> WHITE_DYEBLAST_ORB = register(
		"white_dyeblast_orb",
		EntityType.Builder.<WhiteDyeblastOrb>of(WhiteDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<GrayDyeblastOrb> GRAY_DYEBLAST_ORB = register(
		"gray_dyeblast_orb",
		EntityType.Builder.<GrayDyeblastOrb>of(GrayDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<LightGrayDyeblastOrb> LIGHT_GRAY_DYEBLAST_ORB = register(
		"light_gray_dyeblast_orb",
		EntityType.Builder.<LightGrayDyeblastOrb>of(LightGrayDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);
	public static final EntityType<BlackDyeblastOrb> BLACK_DYEBLAST_ORB = register(
		"black_dyeblast_orb",
		EntityType.Builder.<BlackDyeblastOrb>of(BlackDyeblastOrb::new, MobCategory.MISC)
			.noLootTable().sized(0.3F, 0.3F).clientTrackingRange(4).updateInterval(10)
	);

	private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
		ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BloomFestal.ID, name));
		return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
	}

	public static void init() {

	}
}
