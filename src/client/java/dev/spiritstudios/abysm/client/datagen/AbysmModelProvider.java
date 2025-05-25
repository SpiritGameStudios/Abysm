package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

import java.util.Optional;

public class AbysmModelProvider extends FabricModelProvider {
	// Note - by default, the spore blossom model(used for scabiosa) is upside, which is why this is flipped
	private static final BlockStateVariantMap<ModelVariantOperator> UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.FACING)
		.register(Direction.DOWN, BlockStateModelGenerator.NO_OP)
		.register(Direction.UP, BlockStateModelGenerator.ROTATE_X_180)
		.register(Direction.NORTH, BlockStateModelGenerator.ROTATE_X_90.then(BlockStateModelGenerator.ROTATE_Y_180))
		.register(Direction.SOUTH, BlockStateModelGenerator.ROTATE_X_90)
		.register(Direction.WEST, BlockStateModelGenerator.ROTATE_X_90.then(BlockStateModelGenerator.ROTATE_Y_90))
		.register(Direction.EAST, BlockStateModelGenerator.ROTATE_X_90.then(BlockStateModelGenerator.ROTATE_Y_270));

	public static final TextureKey BLOSSOM_FLOWER_KEY = TextureKey.of("flower");
	public static final TextureKey BLOSSOM_BASE_KEY = TextureKey.of("base");

	public static final TexturedModel.Factory BLOSSOM_FACTORY = TexturedModel.makeFactory(
		block -> new TextureMap()
			.put(BLOSSOM_FLOWER_KEY, TextureMap.getId(block))
			.put(BLOSSOM_BASE_KEY, TextureMap.getSubId(block, "_base")),
		new Model(
			Optional.of(Abysm.id("block/blossom")),
			Optional.empty(),
			BLOSSOM_FLOWER_KEY, BLOSSOM_BASE_KEY
		)
	);

	public AbysmModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
		generator.registerSimpleCubeAll(AbysmBlocks.FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.FLOROPUMICE_BRICKS);
		generator.registerSimpleCubeAll(AbysmBlocks.FLOROPUMICE_TILES);
		generator.registerSimpleCubeAll(AbysmBlocks.CHISLED_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.SMOOTH_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.SMOOTH_FLOROPUMICE_BRICKS);
		generator.registerSimpleCubeAll(AbysmBlocks.CUT_SMOOTH_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.CHISELED_SMOOTH_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR);

		registerScabiosa(generator, AbysmBlocks.PURPLE_SCABIOSA);
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator) {

	}

	public final void registerScabiosa(BlockStateModelGenerator generator, Block block) {
		WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(BLOSSOM_FACTORY.upload(block, generator.modelCollector));
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, weightedVariant).coordinate(UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS));
	}
}
