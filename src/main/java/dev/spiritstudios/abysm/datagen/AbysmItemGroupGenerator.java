package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmItems;
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

import static dev.spiritstudios.abysm.Abysm.MODID;

public class AbysmItemGroupGenerator extends SpecterItemGroupProvider {

	public AbysmItemGroupGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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
			if (!stack.isEmpty()) items.add(stack);
		});
		ReflectionHelper.getStaticFields(
			AbysmItems.class,
			Item.class
		).forEach(pair -> {
			ItemStack stack = new ItemStack(pair.value());
			if (!stack.isEmpty()) items.add(stack);
		});

		provider.accept(
			Identifier.of(MODID, "abysm"),
			ItemGroupData.of(
				Identifier.of(MODID, "abysm"),
				AbysmBlocks.FLOROPUMICE,
				items
			)
		);
	}
}
