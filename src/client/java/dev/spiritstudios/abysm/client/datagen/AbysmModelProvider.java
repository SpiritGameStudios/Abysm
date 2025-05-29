package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.registry.AbysmBlocks;
import dev.spiritstudios.abysm.registry.AbysmItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.*;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
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
		AbysmBlockFamilies
			.getAllAbysmBlockFamilies()
			.filter(BlockFamily::shouldGenerateModels)
			.forEach(blockFamily -> generator.registerCubeAllModelTexturePool(blockFamily.getBaseBlock()).family(blockFamily));

		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		generator.registerAxisRotated(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);

		generator.registerRoots(AbysmBlocks.ROSY_SPRIGS, AbysmBlocks.POTTED_ROSY_SPRIGS);
		generator.registerRoots(AbysmBlocks.SUNNY_SPRIGS, AbysmBlocks.POTTED_SUNNY_SPRIGS);
		generator.registerRoots(AbysmBlocks.MAUVE_SPRIGS, AbysmBlocks.POTTED_MAUVE_SPRIGS);

		generator.registerFlowerPotPlantAndItem(AbysmBlocks.ROSY_BLOOMSHROOM, AbysmBlocks.POTTED_ROSY_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);
		generator.registerFlowerPotPlantAndItem(AbysmBlocks.SUNNY_BLOOMSHROOM, AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);
		generator.registerFlowerPotPlantAndItem(AbysmBlocks.MAUVE_BLOOMSHROOM, AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);

		registerGrassLike(generator, AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.SUNBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

		generator.createLogTexturePool(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).stem(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).wood(AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE);
		generator.registerSimpleCubeAll(AbysmBlocks.ROSY_BLOOMSHROOM_CAP);

		generator.createLogTexturePool(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).stem(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).wood(AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE);
		generator.registerSimpleCubeAll(AbysmBlocks.SUNNY_BLOOMSHROOM_CAP);

		generator.createLogTexturePool(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).stem(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).wood(AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE);
		generator.registerSimpleCubeAll(AbysmBlocks.MAUVE_BLOOMSHROOM_CAP);

		generator.registerSimpleCubeAll(AbysmBlocks.BLOOMSHROOM_GOOP);

		registerScabiosas(generator,
			AbysmBlocks.WHITE_SCABIOSA,
			AbysmBlocks.ORANGE_SCABIOSA,
			AbysmBlocks.MAGENTA_SCABIOSA,
			AbysmBlocks.LIGHT_BLUE_SCABIOSA,
			AbysmBlocks.YELLOW_SCABIOSA,
			AbysmBlocks.LIME_SCABIOSA,
			AbysmBlocks.PINK_SCABIOSA,
			AbysmBlocks.GREY_SCABIOSA,
			AbysmBlocks.LIGHT_GREY_SCABIOSA,
			AbysmBlocks.CYAN_SCABIOSA,
			AbysmBlocks.PURPLE_SCABIOSA,
			AbysmBlocks.BLUE_SCABIOSA,
			AbysmBlocks.BROWN_SCABIOSA,
			AbysmBlocks.GREEN_SCABIOSA,
			AbysmBlocks.RED_SCABIOSA,
			AbysmBlocks.BLACK_SCABIOSA
		);
	}

	private void registerScabiosas(BlockStateModelGenerator generator, Block... blocks) {
		for(Block block : blocks) {
			registerScabiosa(generator, block);
		}
	}

	private void registerScabiosa(BlockStateModelGenerator generator, Block block) {
		WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(BLOSSOM_FACTORY.upload(block, generator.modelCollector));
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, weightedVariant).coordinate(UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS));
	}

	private static void registerGrassLike(BlockStateModelGenerator generator, Block block, Block baseBlock) {
		TextureMap textureMapping = new TextureMap()
			.put(TextureKey.BOTTOM, TextureMap.getId(baseBlock))
			.put(TextureKey.TOP, TextureMap.getId(block))
			.put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"));
		Identifier model = Models.CUBE_BOTTOM_TOP.upload(block, textureMapping, generator.modelCollector);
		generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, BlockStateModelGenerator.modelWithYRotation(BlockStateModelGenerator.createModelVariant(model))));
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator) {
		registerGenerated(generator,
			AbysmItems.FLIPPERS
		);
	}

	private static void registerGenerated(ItemModelGenerator generator, Item... items) {
		for(Item item : items) {
			registerGenerated(generator, items);
		}
	}

	private static void registerGenerated(ItemModelGenerator generator, Item item) {
		generator.register(item, Models.GENERATED);
	}
}
