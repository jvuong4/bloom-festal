package io.github.jvuong4.bloomfestal.compat.StreetArt;

import com.streetart.AllDataComponents;
import com.streetart.StreetArt;
import com.streetart.component.ColorComponent;
import com.streetart.item.PaintBalloonItem;
import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.compat.StreetArt.itemColors.*;
import io.github.jvuong4.bloomfestal.item.Thoron;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

public class StreetArtItems {

	/*
	public static Map<DyeColor, DyeblastItem> DYEBLASTS = registerDyed("dyeblast", DyeblastItem::new,
		dye -> new Item.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(dye)).enchantable(1)
	);
	 */

	public static RedDyeblastItem RED_DYEBLAST = register("red_dyeblast", RedDyeblastItem:: new,
		new RedDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.RED)).enchantable(1));
	public static OrangeDyeblastItem ORANGE_DYEBLAST = register("orange_dyeblast", OrangeDyeblastItem:: new,
		new OrangeDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.ORANGE)).enchantable(1));
	public static YellowDyeblastItem YELLOW_DYEBLAST = register("yellow_dyeblast", YellowDyeblastItem:: new,
		new YellowDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.YELLOW)).enchantable(1));
	public static LimeDyeblastItem LIME_DYEBLAST = register("lime_dyeblast", LimeDyeblastItem:: new,
		new LimeDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.LIME)).enchantable(1));
	public static GreenDyeblastItem GREEN_DYEBLAST = register("green_dyeblast", GreenDyeblastItem:: new,
		new GreenDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.GREEN)).enchantable(1));
	public static CyanDyeblastItem CYAN_DYEBLAST = register("cyan_dyeblast", CyanDyeblastItem:: new,
		new CyanDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.CYAN)).enchantable(1));
	public static BlueDyeblastItem BLUE_DYEBLAST = register("blue_dyeblast", BlueDyeblastItem:: new,
		new BlueDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.BLUE)).enchantable(1));
	public static LightBlueDyeblastItem LIGHT_BLUE_DYEBLAST = register("light_blue_dyeblast", LightBlueDyeblastItem:: new,
		new LightBlueDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.LIGHT_BLUE)).enchantable(1));
	public static PinkDyeblastItem PINK_DYEBLAST = register("pink_dyeblast", PinkDyeblastItem:: new,
		new PinkDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.PINK)).enchantable(1));

	public static MagentaDyeblastItem MAGENTA_DYEBLAST = register("magenta_dyeblast", MagentaDyeblastItem:: new,
		new MagentaDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.MAGENTA)).enchantable(1));

	public static PurpleDyeblastItem PURPLE_DYEBLAST = register("purple_dyeblast", PurpleDyeblastItem:: new,
		new PurpleDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.PURPLE)).enchantable(1));

	public static WhiteDyeblastItem WHITE_DYEBLAST = register("white_dyeblast", WhiteDyeblastItem:: new,
		new WhiteDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.WHITE)).enchantable(1));
	public static LightGrayDyeblastItem LIGHT_GRAY_DYEBLAST = register("light_gray_dyeblast", LightGrayDyeblastItem:: new,
		new LightGrayDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.LIGHT_GRAY)).enchantable(1));
	public static GrayDyeblastItem GRAY_DYEBLAST = register("gray_dyeblast", GrayDyeblastItem:: new,
		new GrayDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.GRAY)).enchantable(1));
	public static BlackDyeblastItem BLACK_DYEBLAST = register("black_dyeblast", BlackDyeblastItem:: new,
		new BlackDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.BLACK)).enchantable(1));
	public static BrownDyeblastItem BROWN_DYEBLAST = register("brown_dyeblast", BrownDyeblastItem:: new,
		new BrownDyeblastItem.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(DyeColor.BROWN)).enchantable(1));

	private static <T extends Item> EnumMap<DyeColor, T> registerDyed(final String baseName,
	                                                                  final Function<Item.Properties, T> factory,
	                                                                  final Function<DyeColor, Item.Properties> properties) {
		final EnumMap<DyeColor, T> map = new EnumMap<>(DyeColor.class);

		for (final DyeColor value : DyeColor.values()) {
			map.put(value, register(value.getName() + "_" + baseName, factory, properties.apply(value)));
		}

		return map;
	}

	private static <T extends Item> T register(final String name, final Function<Item.Properties, T> factory, final Item.Properties properties) {
		final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BloomFestal.ID, name));

		final T item = factory.apply(properties.setId(key));

		Registry.register(BuiltInRegistries.ITEM, key, item);

		return item;
	}

	public static void init() {}
}
