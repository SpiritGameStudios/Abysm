package dev.spiritstudios.abysm.registry;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

import java.util.Map;

public class AbysmEnchantments {
	public static final RegistryKey<Enchantment> HAUL = of("haul");
	public static final RegistryKey<Enchantment> GRAPPLING = of("grappling");

	private static RegistryKey<Enchantment> of(String path) {
		return RegistryKey.of(RegistryKeys.ENCHANTMENT, Abysm.id(path));
	}

	public static boolean hasEnchantment(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
		return getLevel(stack, world, enchantment) > 0;
	}

	public static int getLevel(ItemStack stack, World world, RegistryKey<Enchantment> enchantment) {
		return EnchantmentHelper.getLevel(getFromWorld(world, enchantment), stack);
	}

	public static RegistryEntry.Reference<Enchantment> getFromWorld(World world, RegistryKey<Enchantment> enchantment) {
		return getFromDRM(world.getRegistryManager(), enchantment);
	}

	public static RegistryEntry.Reference<Enchantment> getFromDRM(DynamicRegistryManager drm, RegistryKey<Enchantment> enchantment) {
		return drm.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment);
	}

	public static ImmutableMap<RegistryKey<Enchantment>, Enchantment> asEnchantments(Registerable<Enchantment> registerable) {
		RegistryEntryLookup<Item> item = registerable.getRegistryLookup(RegistryKeys.ITEM);
		ImmutableMap.Builder<RegistryKey<Enchantment>, Enchantment> enchantments = ImmutableMap.builder();
		enchantments.put(HAUL, Enchantment.builder(Enchantment.definition(
				item.getOrThrow(AbysmItemTags.HARPOON_ENCHANTABLE),
				1,
				1,
				Enchantment.constantCost(1),
				Enchantment.constantCost(100),
				1,
				AttributeModifierSlot.MAINHAND))
			.build(HAUL.getValue()));
		enchantments.put(GRAPPLING, Enchantment.builder(Enchantment.definition(
				item.getOrThrow(AbysmItemTags.HARPOON_ENCHANTABLE),
				1,
				1,
				Enchantment.constantCost(1),
				Enchantment.constantCost(100),
				1,
				AttributeModifierSlot.MAINHAND))
			.build(GRAPPLING.getValue()));
		return enchantments.build();
	}

	public static void bootstrap(Registerable<Enchantment> registerable) {
		for (Map.Entry<RegistryKey<Enchantment>, Enchantment> entry : asEnchantments(registerable).entrySet()) {
			registerable.register(entry.getKey(), entry.getValue());
		}
	}
}
