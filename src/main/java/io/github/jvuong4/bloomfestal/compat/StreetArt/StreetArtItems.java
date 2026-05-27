package io.github.jvuong4.bloomfestal.compat.StreetArt;

import com.streetart.AllDataComponents;
import com.streetart.StreetArt;
import com.streetart.component.ColorComponent;
import com.streetart.item.PaintBalloonItem;
import io.github.jvuong4.bloomfestal.BloomFestal;
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

	public static Map<DyeColor, DyeblastItem> DYEBLASTS = registerDyed("dyeblast", DyeblastItem::new,
		dye -> new Item.Properties().stacksTo(1).useCooldown(0.6667f)
			.component(AllDataComponents.COLOR, ColorComponent.fromDye(dye)).enchantable(1)
	);

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
