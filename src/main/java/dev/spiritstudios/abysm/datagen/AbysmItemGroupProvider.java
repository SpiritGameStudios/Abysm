package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.specter.api.core.reflect.ReflectionHelper;
import dev.spiritstudios.specter.api.item.datagen.SpecterItemGroupProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AbysmItemGroupProvider extends SpecterItemGroupProvider {
	public AbysmItemGroupProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, registriesFuture);
	}

	@Override
	protected void generate(BiConsumer<Identifier, ItemGroupData> provider, RegistryWrapper.WrapperLookup lookup) {
		List<ItemStack> items = new ArrayList<>();
		ReflectionHelper.getStaticFields(
			AbysmBlocks.class,
			Block.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value().asItem());
			if (!stack.isEmpty()) {
				if(stack.isOf(AbysmItems.LAPIS_BULB)) return; // avoid registering this item twice.
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

		provider.accept(
			Abysm.id("abysm"),
			ItemGroupData.of(
				Abysm.id("abysm"),
				AbysmBlocks.FLOROPUMICE,
				items
			)
		);
	}
}
