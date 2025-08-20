package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import dev.spiritstudios.specter.api.item.DataItemGroup;
import dev.spiritstudios.specter.api.item.SpecterItemRegistryKeys;
import net.minecraft.block.Block;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public final class AbysmItemGroups {
	public static final RegistryKey<DataItemGroup> ABYSM = ofKey("abysm");

	public static void bootstrap(Registerable<DataItemGroup> registry) {
		List<ItemStack> items = new ArrayList<>();

		ReflectionHelper.getStaticFields(
			AbysmBlocks.class,
			Block.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value().asItem());
			if (!stack.isEmpty()) {
				if (stack.isOf(AbysmItems.LAPIS_BULB)) return; // avoid registering this item twice.
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

		List<RegistryEntry<Potion>> potionEntries = new ArrayList<>();
		ReflectionHelper.getStaticFields(
			AbysmPotions.class,
			RegistryEntry.class
		).forEach(pair -> {
			potionEntries.add(pair.value());
		});

		List<Item> potionItems = List.of(Items.POTION, Items.SPLASH_POTION, Items.LINGERING_POTION, Items.TIPPED_ARROW);
		for (Item item : potionItems) {
			for (RegistryEntry<Potion> potionEntry : potionEntries) {
				items.add(PotionContentsComponent.createStack(item, potionEntry));
			}
		}

		registry.register(
			ABYSM,
			new DataItemGroup(
				Text.translatable("item_group.abysm.abysm"),
				AbysmBlocks.FLOROPUMICE,
				items
			)
		);
	}

	private static RegistryKey<DataItemGroup> ofKey(String id) {
		return RegistryKey.of(SpecterItemRegistryKeys.ITEM_GROUP, Abysm.id(id));
	}
}
