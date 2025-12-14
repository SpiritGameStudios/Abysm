package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import dev.spiritstudios.abysm.world.item.AbysmPotions;
import dev.spiritstudios.spectre.api.reflect.ReflectionHelper;
import dev.spiritstudios.spectre.api.world.item.CreativeModeTabsProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AbysmCreativeModeTabProvider extends CreativeModeTabsProvider {
	public AbysmCreativeModeTabProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider);
	}

	@Override
	protected void addCreativeModeTabs(HolderLookup.Provider provider) {
		var builder = builder(Abysm.id("abysm"));

		builder
			.title(Component.translatable("creative_mode_tab.abysm.abysm"))
			.icon(new ItemStack(AbysmBlocks.FLOROPUMICE));

		ReflectionHelper.getStaticFields(
			AbysmBlocks.class,
			Block.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value().asItem());
			if (!stack.isEmpty()) {
				if (stack.is(AbysmItems.LAPIS_BULB)) return; // avoid registering this item twice.
				builder.add(stack);
			}
		});

		ReflectionHelper.getStaticFields(
			AbysmItems.class,
			Item.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value());
			if (!stack.isEmpty()) {
				builder.add(stack);
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
				builder.add(PotionContents.createItemStack(item, potionEntry));
			}
		}
	}

	@Override
	public @NotNull String getName() {
		return "Creative Mode Tabs";
	}
}
