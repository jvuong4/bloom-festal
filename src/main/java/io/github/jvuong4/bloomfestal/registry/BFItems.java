package io.github.jvuong4.bloomfestal.registry;

import io.github.jvuong4.bloomfestal.BloomFestal;
import io.github.jvuong4.bloomfestal.item.*;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.*;

import java.util.Optional;
import java.util.function.Function;

public class BFItems {
	/* //scrapped for now
	public static Item LEVIN_SWORD = register("levin_sword", Item::new,
		new Item.Properties().())
	*/

	//who needs readable code when you can just do this very simple trick!
	public static Item BRAVE_LANCE = register("brave_lance", Item :: new, new Item.Properties().durability(188)
		//.repairable() screw anvil repairability
		.enchantable(10)
		.delayedHolderComponent(DataComponents.DAMAGE_TYPE, BFDamageTypes.BRAVE_LANCE_DAMAGE)
		.component(
			DataComponents.KINETIC_WEAPON,
			new KineticWeapon(
				10,
				(int)(0.2F * 20.0F),
				KineticWeapon.Condition.ofAttackerSpeed((int)(2.5F * 20.0F), 11.0F),
				KineticWeapon.Condition.ofAttackerSpeed((int)(5.5F * 20.0F), 5.1F),
				KineticWeapon.Condition.ofRelativeSpeed((int)(8.75F * 20.0F), 4.6F),
				0.38F,
				0.5F,
				Optional.of(SoundEvents.SPEAR_USE),
				Optional.of(SoundEvents.SPEAR_HIT)
			)
		)
		.component(
			DataComponents.PIERCING_WEAPON,
			new PiercingWeapon(
				true,
				false,
				Optional.of(SoundEvents.SPEAR_ATTACK),
				Optional.of(SoundEvents.SPEAR_HIT)
			)
		)
		.component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
		.component(DataComponents.MINIMUM_ATTACK_CHARGE, 1.0F)
		//2.5F atk speed means 0.4F attack duration
		.component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(0.4F * 20.0F)))
		.attributes(
			ItemAttributeModifiers.builder()
				.add(
					Attributes.ATTACK_DAMAGE,
					//0 + 3 + 1 baseatk = 4 attack damage
					new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0F + 3.0F, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.add(
					Attributes.ATTACK_SPEED,
					//2.5F atk speed means 0.4F attack duration
					new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 1.0F / 0.4F - 4.0, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.build()
		)
		.component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
		.component(DataComponents.WEAPON, new Weapon(1)));

	//who needs readable code when you can just do this very simple trick!
	public static Item GUARD_NAGINATA = register("guard_naginata", Item :: new, new Item.Properties().durability(210)
		//.repairable(material.repairItems())
		.enchantable(14)
		.delayedHolderComponent(DataComponents.DAMAGE_TYPE, DamageTypes.SPEAR)
		.component(
			DataComponents.KINETIC_WEAPON,
			new KineticWeapon(
				10,
				(int)(0.65F * 20.0F),
				KineticWeapon.Condition.ofAttackerSpeed((int)(4.0F * 20.0F), 12.0F),
				KineticWeapon.Condition.ofAttackerSpeed((int)(8.25F * 20.0F), 5.1F),
				KineticWeapon.Condition.ofRelativeSpeed((int)(12.5F * 20.0F), 4.6F),
				0.38F,
				0.82F,
				Optional.of(SoundEvents.SPEAR_USE),
				Optional.of(SoundEvents.SPEAR_HIT)
			)
		)
		.component(
			DataComponents.PIERCING_WEAPON,
			new PiercingWeapon(
				true,
				false,
				Optional.of(SoundEvents.SPEAR_ATTACK),
				Optional.of(SoundEvents.SPEAR_HIT)
			)
		)
		.component(DataComponents.ATTACK_RANGE, new AttackRange(2.0F, 4.5F, 2.0F, 6.5F, 0.125F, 0.5F))
		.component(DataComponents.MINIMUM_ATTACK_CHARGE, 1.0F)
		.component(DataComponents.SWING_ANIMATION, new SwingAnimation(SwingAnimationType.STAB, (int)(0.85F * 20.0F)))
		.attributes(
			ItemAttributeModifiers.builder()
				.add(
					Attributes.ATTACK_DAMAGE,
					new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 0.0F + 2.0F, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.add(
					Attributes.ATTACK_SPEED,
					new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, 1.0F / 0.85F - 4.0, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.add(
					Attributes.ARMOR,
					new AttributeModifier(Identifier.withDefaultNamespace("armor.mainhand"), 5.0F, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.add(
					Attributes.ARMOR_TOUGHNESS,
					new AttributeModifier(Identifier.withDefaultNamespace("armor.mainhand"), 2.0F, AttributeModifier.Operation.ADD_VALUE),
					EquipmentSlotGroup.MAINHAND
				)
				.build()
		)
		.component(DataComponents.USE_EFFECTS, new UseEffects(true, false, 1.0F))
		.component(DataComponents.WEAPON, new Weapon(1)));

	public static HealingStaff BLOOM_FESTAL = register("bloom_festal", HealingStaff:: new,
		new HealingStaff.Properties().durability(40).useCooldown(0.8f).enchantable(1));

	public static SunFestal SUN_FESTAL = register("sun_festal", SunFestal:: new,
		new SunFestal.Properties().durability(30).useCooldown(1.2f).enchantable(1));

	public static WaneFestal WANE_FESTAL = register("wane_festal", WaneFestal:: new,
		new WaneFestal.Properties().durability(10).useCooldown(2f).enchantable(1));

	public static MoonFestal MOON_FESTAL = register("moon_festal", MoonFestal:: new,
		new MoonFestal.Properties().durability(10).useCooldown(2f).enchantable(1));

	public static GreatFestal GREAT_FESTAL = register("great_festal", GreatFestal:: new,
		new GreatFestal.Properties().durability(4).useCooldown(4f).enchantable(1));

	public static RevelationFestal REVELATION_FESTAL = register("revelation_festal", RevelationFestal:: new,
		new RevelationFestal.Properties().durability(40).useCooldown(1f).enchantable(1));
	public static HexingRod HEXING_ROD = register("hexing_rod", HexingRod:: new,
		new HexingRod.Properties().durability(6).useCooldown(10f).enchantable(1));
	public static StillnessRod STILLNESS_ROD = register("stillness_rod", StillnessRod:: new,
		new StillnessRod.Properties().durability(6).useCooldown(10f).enchantable(1));
	public static SilentRod SILENT_ROD = register("silent_rod", SilentRod:: new,
		new SilentRod.Properties().durability(6).useCooldown(10f).enchantable(1));

	public static Eclipse ECLIPSE = register("eclipse", Eclipse:: new,
		new Eclipse.Properties().durability(10).useCooldown(9f).enchantable(1));

	public static Thoron THORON = register("thoron", Thoron:: new,
		new Thoron.Properties().durability(50).useCooldown(4f).enchantable(1));
	public static Thoron LEVIN_SWORD = register("levin_sword", Thoron:: new,
		new Thoron.Properties().sword(ToolMaterial.GOLD, 9.0F, -2.4F).durability(25).useCooldown(5f).enchantable(64));

	public static Rexcalibur REXCALIBUR = register("rexcalibur", Rexcalibur:: new,
		new Rexcalibur.Properties().durability(50).useCooldown(3f).enchantable(1));
	public static Bolganone BOLGANONE = register("bolganone", Bolganone:: new,
		new Bolganone.Properties().durability(50).useCooldown(3.5f).enchantable(1));

	public static <T extends Item> T register(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
		// Create the item key.
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BloomFestal.ID, name));

		// Create the item instance.
		T item = itemFactory.apply(settings.setId(itemKey));

		// Register the item.
		Registry.register(BuiltInRegistries.ITEM, itemKey, item);

		return item;
	}

	public static final ResourceKey<CreativeModeTab> CUSTOM_CREATIVE_TAB_KEY = ResourceKey.create(
		BuiltInRegistries.CREATIVE_MODE_TAB.key(), Identifier.fromNamespaceAndPath(BloomFestal.ID, "creative_tab")
	);
	public static final CreativeModeTab CUSTOM_CREATIVE_TAB = FabricCreativeModeTab.builder()
		.icon(() -> new ItemStack(BLOOM_FESTAL))
		.title(Component.translatable("creativeTab.bloom_festal"))
		.displayItems((params, output) -> {
			output.accept(BLOOM_FESTAL);
			output.accept(SUN_FESTAL);
			output.accept(WANE_FESTAL);
			output.accept(MOON_FESTAL);
			output.accept(GREAT_FESTAL);
			output.accept(REVELATION_FESTAL);
			output.accept(STILLNESS_ROD);
			output.accept(SILENT_ROD);
			output.accept(HEXING_ROD);

			output.accept(LEVIN_SWORD);


			output.accept(BRAVE_LANCE);
			output.accept(GUARD_NAGINATA);

			output.accept(THORON);
			output.accept(BOLGANONE);
			output.accept(REXCALIBUR);
			output.accept(ECLIPSE);
		})
		.build();

	public static void init(){
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CUSTOM_CREATIVE_TAB_KEY, CUSTOM_CREATIVE_TAB);
	}
}
