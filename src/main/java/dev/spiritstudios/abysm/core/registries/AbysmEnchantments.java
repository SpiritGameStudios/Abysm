package dev.spiritstudios.abysm.core.registries;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.core.registries.tags.AbysmItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;

public final class AbysmEnchantments {
	public static final ResourceKey<Enchantment> HAUL = of("haul");
	public static final ResourceKey<Enchantment> GRAPPLING = of("grappling");

	private static ResourceKey<Enchantment> of(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, Abysm.id(path));
	}

	public static boolean hasEnchantment(ItemStack stack, Level level, ResourceKey<Enchantment> enchantment) {
		return getLevel(stack, level, enchantment) > 0;
	}

	public static int getLevel(ItemStack stack, Level level, ResourceKey<Enchantment> enchantment) {
		return EnchantmentHelper.getItemEnchantmentLevel(getFromLevel(level, enchantment), stack);
	}

	public static Holder.Reference<Enchantment> getFromLevel(Level level, ResourceKey<Enchantment> enchantment) {
		return getFromRegistries(level.registryAccess(), enchantment);
	}

	public static Holder.Reference<Enchantment> getFromRegistries(RegistryAccess registries, ResourceKey<Enchantment> enchantment) {
		return registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
	}

	public static ImmutableMap<ResourceKey<Enchantment>, Enchantment> asEnchantments(BootstrapContext<Enchantment> registerable) {
		HolderGetter<Item> item = registerable.lookup(Registries.ITEM);
		ImmutableMap.Builder<ResourceKey<Enchantment>, Enchantment> enchantments = ImmutableMap.builder();
		enchantments.put(HAUL, Enchantment.enchantment(Enchantment.definition(
				item.getOrThrow(AbysmItemTags.HARPOON_ENCHANTABLE),
				1,
				1,
				Enchantment.constantCost(1),
				Enchantment.constantCost(100),
				1,
				EquipmentSlotGroup.MAINHAND))
			.build(HAUL.identifier()));
		enchantments.put(GRAPPLING, Enchantment.enchantment(Enchantment.definition(
				item.getOrThrow(AbysmItemTags.HARPOON_ENCHANTABLE),
				1,
				1,
				Enchantment.constantCost(1),
				Enchantment.constantCost(100),
				1,
				EquipmentSlotGroup.MAINHAND))
			.build(GRAPPLING.identifier()));
		return enchantments.build();
	}

	public static void bootstrap(BootstrapContext<Enchantment> registerable) {
		for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : asEnchantments(registerable).entrySet()) {
			registerable.register(entry.getKey(), entry.getValue());
		}
	}
}
