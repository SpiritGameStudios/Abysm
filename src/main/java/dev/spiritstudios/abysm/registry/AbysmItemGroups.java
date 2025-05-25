package dev.spiritstudios.abysm.registry;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.impl.itemgroup.FabricItemGroupBuilderImpl;
import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class AbysmItemGroups {
	public static final ItemGroup ABYSM = register(
		"abysm",
		new FabricItemGroupBuilderImpl()
			.icon(() -> AbysmBlocks.FLOROPUMICE.asItem().getDefaultStack())
			.displayName(Text.translatable("item_group.abysm.abysm"))
			.build()
	);

	public static <T extends ItemGroup> T register(RegistryKey<ItemGroup> key, T itemGroup) {
		return Registry.register(Registries.ITEM_GROUP, key, itemGroup);
	}

	private static <T extends ItemGroup> T register(String id, T itemGroup) {
		return register(keyOf(id), itemGroup);
	}

	private static RegistryKey<ItemGroup> keyOf(String id) {
		return RegistryKey.of(RegistryKeys.ITEM_GROUP, Abysm.id(id));
	}

	public static void init() {
		List<ItemStack> items = new ArrayList<>();
		ReflectionHelper.getStaticFields(
			AbysmBlocks.class,
			Block.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value().asItem());
			if (!stack.isEmpty()) items.add(stack);
		});
		/*
		ReflectionHelper.getStaticFields(
			AbysmItems.class,
			Item.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value());
			if (!stack.isEmpty()) items.add(stack);
		});
		*/

		ItemGroupEvents.modifyEntriesEvent(keyOf("abysm")).register(entries -> {
			entries.addAll(items);
		});
	}
}
