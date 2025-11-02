package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import dev.spiritstudios.specter.api.item.DataItemGroup;
import dev.spiritstudios.specter.api.item.SpecterItemRegistryKeys;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;

public final class AbysmItemGroups {
	public static final ResourceKey<DataItemGroup> ABYSM = ofKey("abysm");

	public static void bootstrap(BootstrapContext<DataItemGroup> registry) {
		List<ItemStack> items = new ArrayList<>();

		ReflectionHelper.getStaticFields(
			AbysmBlocks.class,
			Block.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value().asItem());
			if (!stack.isEmpty()) {
				if (stack.is(AbysmItems.LAPIS_BULB)) return; // avoid registering this item twice.
				items.add(stack);
			}
		});

		ReflectionHelper.getStaticFields(
			AbysmItems.class,
			Item.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value());
			if (!stack.isEmpty()) {
				items.add(stack);
			}
		});

		List<Holder<Potion>> potionEntries = new ArrayList<>();
		ReflectionHelper.getStaticFields(
			AbysmPotions.class,
			Holder.class
		).forEach(pair -> {
			potionEntries.add(pair.value());
		});

		List<Item> potionItems = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW);
		for (Item item : potionItems) {
			for (Holder<Potion> potionEntry : potionEntries) {
				items.add(PotionContents.createItemStack(item, potionEntry));
			}
		}

		registry.register(
			ABYSM,
			new DataItemGroup(
				Component.translatable("item_group.abysm.abysm"),
				AbysmBlocks.FLOROPUMICE,
				items
			)
		);
	}

	private static ResourceKey<DataItemGroup> ofKey(String id) {
		return ResourceKey.create(SpecterItemRegistryKeys.ITEM_GROUP, Abysm.id(id));
	}
}
