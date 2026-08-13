package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class BFTags {
	public static final TagKey<Item> SILENCEABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BloomFestal.ID, "silenceable"));
}
