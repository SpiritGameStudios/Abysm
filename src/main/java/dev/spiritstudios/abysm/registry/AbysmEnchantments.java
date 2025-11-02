package dev.spiritstudios.abysm.registry;

import com.google.common.collect.ImmutableMap;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.tags.AbysmItemTags;
import java.util.Map;
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

public class AbysmEnchantments {
	public static final ResourceKey<Enchantment> HAUL = of("haul");
	public static final ResourceKey<Enchantment> GRAPPLING = of("grappling");

	private static ResourceKey<Enchantment> of(String path) {
		return ResourceKey.create(Registries.ENCHANTMENT, Abysm.id(path));
	}

	public static boolean hasEnchantment(ItemStack stack, Level world, ResourceKey<Enchantment> enchantment) {
		return getLevel(stack, world, enchantment) > 0;
	}

	public static int getLevel(ItemStack stack, Level world, ResourceKey<Enchantment> enchantment) {
		return EnchantmentHelper.getItemEnchantmentLevel(getFromWorld(world, enchantment), stack);
	}

	public static Holder.Reference<Enchantment> getFromWorld(Level world, ResourceKey<Enchantment> enchantment) {
		return getFromDRM(world.registryAccess(), enchantment);
	}

	public static Holder.Reference<Enchantment> getFromDRM(RegistryAccess drm, ResourceKey<Enchantment> enchantment) {
		return drm.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(enchantment);
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
			.build(HAUL.location()));
		enchantments.put(GRAPPLING, Enchantment.enchantment(Enchantment.definition(
				item.getOrThrow(AbysmItemTags.HARPOON_ENCHANTABLE),
				1,
				1,
				Enchantment.constantCost(1),
				Enchantment.constantCost(100),
				1,
				EquipmentSlotGroup.MAINHAND))
			.build(GRAPPLING.location()));
		return enchantments.build();
	}

	public static void bootstrap(BootstrapContext<Enchantment> registerable) {
		for (Map.Entry<ResourceKey<Enchantment>, Enchantment> entry : asEnchantments(registerable).entrySet()) {
			registerable.register(entry.getKey(), entry.getValue());
		}
	}
}
