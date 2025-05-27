package dev.spiritstudios.abysm.datagen;

import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.specter.api.item.ItemMetatags;
import dev.spiritstudios.specter.api.registry.metatag.datagen.MetatagProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataOutput;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class AbysmItemMetatagProvider extends MetatagProvider<Item> {
	public AbysmItemMetatagProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
		super(dataOutput, RegistryKeys.ITEM, registriesFuture, DataOutput.OutputType.DATA_PACK);
	}

	@Override
	protected void configure(Consumer<MetatagBuilder<Item, ?>> consumer, RegistryWrapper.WrapperLookup wrapperLookup) {
		MetatagBuilder<Item, Float> builder = create(ItemMetatags.COMPOSTING_CHANCE);

		builder.put(AbysmBlocks.ROSY_SPRIGS.asItem(), 0.65F);
		builder.put(AbysmBlocks.ROSY_BLOOMSHROOM.asItem(), 0.65F);
		builder.put(AbysmBlocks.ROSY_BLOOMSHROOM_CAP.asItem(), 0.85F);
		builder.put(AbysmBlocks.ROSY_BLOOMSHROOM_STEM.asItem(), 0.85F);

		builder.put(AbysmBlocks.WHITE_SCABIOSA.asItem(), 0.65F);
		builder.put(AbysmBlocks.ORANGE_SCABIOSA.asItem(), 0.65F);
		builder.put(AbysmBlocks.PINK_SCABIOSA.asItem(), 0.65F);
		builder.put(AbysmBlocks.PURPLE_SCABIOSA.asItem(), 0.65F);

		consumer.accept(builder);
	}
}
