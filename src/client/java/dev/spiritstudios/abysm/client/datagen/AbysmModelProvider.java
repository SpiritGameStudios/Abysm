package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.client.render.HarpoonLoadedProperty;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.BlockStateVariantMap;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.ItemModels;
import net.minecraft.client.data.Model;
import net.minecraft.client.data.ModelIds;
import net.minecraft.client.data.Models;
import net.minecraft.client.data.TextureKey;
import net.minecraft.client.data.TextureMap;
import net.minecraft.client.data.TexturedModel;
import net.minecraft.client.data.VariantsBlockModelDefinitionCreator;
import net.minecraft.client.render.model.json.ModelVariantOperator;
import net.minecraft.client.render.model.json.WeightedVariant;
import net.minecraft.data.family.BlockFamily;
import net.minecraft.item.Item;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.function.BiConsumer;

import static net.minecraft.client.data.BlockStateModelGenerator.CrossType;
import static net.minecraft.client.data.BlockStateModelGenerator.NO_OP;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_X_90;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_180;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_270;
import static net.minecraft.client.data.BlockStateModelGenerator.ROTATE_Y_90;
import static net.minecraft.client.data.BlockStateModelGenerator.createSingletonBlockState;
import static net.minecraft.client.data.BlockStateModelGenerator.createWeightedVariant;

public class AbysmModelProvider extends FabricModelProvider {
	// Note - by default, the spore blossom model(used for scabiosa) is upside, which is why this is flipped
	private static final BlockStateVariantMap<ModelVariantOperator> UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.FACING)
		.register(Direction.DOWN, NO_OP)
		.register(Direction.UP, ROTATE_X_180)
		.register(Direction.NORTH, ROTATE_X_90.then(ROTATE_Y_180))
		.register(Direction.SOUTH, ROTATE_X_90)
		.register(Direction.WEST, ROTATE_X_90.then(ROTATE_Y_90))
		.register(Direction.EAST, ROTATE_X_90.then(ROTATE_Y_270));

	private static final BlockStateVariantMap<ModelVariantOperator> UP_DEFAULT_ROTATION_OPERATIONS = BlockStateVariantMap.operations(Properties.FACING)
		.register(Direction.DOWN, ROTATE_X_180)
		.register(Direction.UP, NO_OP)
		.register(Direction.NORTH, ROTATE_X_90)
		.register(Direction.SOUTH, ROTATE_X_90.then(ROTATE_Y_180))
		.register(Direction.WEST, ROTATE_X_90.then(ROTATE_Y_270))
		.register(Direction.EAST, ROTATE_X_90.then(ROTATE_Y_90));

	public AbysmModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockStateModelGenerator generator) {
		// region block families
		AbysmBlockFamilies
			.getAllAbysmBlockFamilies()
			.filter(BlockFamily::shouldGenerateModels)
			.forEach(blockFamily -> generator.registerCubeAllModelTexturePool(blockFamily.getBaseBlock()).family(blockFamily));
		// endregion

		// region floropumice
		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_FLOROPUMICE);
		generator.registerSimpleCubeAll(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		generator.registerAxisRotated(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
		// endregion

		// region bloom-ish blocks
		registerGrassLike(generator, AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.SUNBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

		registerSprigsWithPot(generator, AbysmBlocks.ROSY_SPRIGS, AbysmBlocks.POTTED_ROSY_SPRIGS);
		registerSprigsWithPot(generator, AbysmBlocks.SUNNY_SPRIGS, AbysmBlocks.POTTED_SUNNY_SPRIGS);
		registerSprigsWithPot(generator, AbysmBlocks.MAUVE_SPRIGS, AbysmBlocks.POTTED_MAUVE_SPRIGS);

		generator.registerLeafLitter(AbysmBlocks.ROSEBLOOM_PETALS);
		generator.registerLeafLitter(AbysmBlocks.SUNBLOOM_PETALS);
		generator.registerLeafLitter(AbysmBlocks.MALLOWBLOOM_PETALS);
		// endregion

		// region bloomshrooms
		generator.registerFlowerPotPlantAndItem(AbysmBlocks.ROSY_BLOOMSHROOM, AbysmBlocks.POTTED_ROSY_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);
		generator.registerFlowerPotPlantAndItem(AbysmBlocks.SUNNY_BLOOMSHROOM, AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);
		generator.registerFlowerPotPlantAndItem(AbysmBlocks.MAUVE_BLOOMSHROOM, AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM, BlockStateModelGenerator.CrossType.NOT_TINTED);

		generator.createLogTexturePool(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).stem(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).wood(AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE);
		generator.createLogTexturePool(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).stem(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).wood(AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE);
		generator.createLogTexturePool(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).stem(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).wood(AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE);

		doForMany(BlockStateModelGenerator::registerSimpleCubeAll, generator,
			AbysmBlocks.ROSY_BLOOMSHROOM_CAP,
			AbysmBlocks.SUNNY_BLOOMSHROOM_CAP,
			AbysmBlocks.MAUVE_BLOOMSHROOM_CAP,

			AbysmBlocks.ROSEBLOOM_PETALEAVES,
			AbysmBlocks.SUNBLOOM_PETALEAVES,
			AbysmBlocks.MALLOWBLOOM_PETALEAVES,

			AbysmBlocks.SWEET_NECTARSAP,
			AbysmBlocks.SOUR_NECTARSAP,
			AbysmBlocks.BITTER_NECTARSAP
		);

		registerBloomingCrowns(generator,
			AbysmBlocks.BLOOMING_SODALITE_CROWN,
			AbysmBlocks.BLOOMING_ANYOLITE_CROWN,
			AbysmBlocks.BLOOMING_MELILITE_CROWN
		);
		// endregion

		// region scabiosas
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
		// endregion

		// region misc plants
		generator.registerRoots(AbysmBlocks.ANTENNAE_PLANT, AbysmBlocks.POTTED_ANTENNAE_PLANT);
		// endregion

		// region dregloam
		generator.registerRandomHorizontalRotations(TexturedModel.CUBE_ALL, AbysmBlocks.DREGLOAM);

		doForMany(BlockStateModelGenerator::registerSimpleCubeAll, generator,
			AbysmBlocks.DREGLOAM_OOZE,
			AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE
		);
		registerGrassLike(generator, AbysmBlocks.OOZING_DREGLOAM, AbysmBlocks.DREGLOAM);
		// endregion

		// region dregloam plants
		generator.registerPlantPart(AbysmBlocks.GOLDEN_LAZULI_OREFURL, AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT, BlockStateModelGenerator.CrossType.NOT_TINTED);

		generator.registerRoots(AbysmBlocks.OOZETRICKLE_FILAMENTS, AbysmBlocks.POTTED_OOZETRICKLE_FILAMENTS);
		generator.registerDoubleBlockAndItem(AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS, CrossType.NOT_TINTED);
		// endregion

		// region oozetrickle deco
		generator.registerItemModel(AbysmBlocks.OOZETRICKLE_CORD.asItem());
		generator.registerAxisRotated(AbysmBlocks.OOZETRICKLE_CORD, createWeightedVariant(ModelIds.getBlockModelId(AbysmBlocks.OOZETRICKLE_CORD)));
		this.registerOozetrickleLantern(generator, AbysmBlocks.OOZETRICKLE_LANTERN);
		// endregion

		// region silt
		generator.registerSimpleCubeAll(AbysmBlocks.SILT);
		generator.registerSimpleCubeAll(AbysmBlocks.CHISELED_SILT);
		generator.registerSimpleCubeAll(AbysmBlocks.CUT_SILT);
		// endregion

		// region technical blocks
		generator.registerSimpleCubeAll(AbysmBlocks.DENSITY_BLOB_BLOCK);
		// endregion
	}

	private void doForMany(BiConsumer<BlockStateModelGenerator, Block> consumer, BlockStateModelGenerator generator, Block... blocks) {
		for (Block block : blocks) {
			consumer.accept(generator, block);
		}
	}

	private void registerScabiosas(BlockStateModelGenerator generator, Block... blocks) {
		doForMany(this::registerScabiosa, generator, blocks);
	}

	private void registerScabiosa(BlockStateModelGenerator generator, Block block) {
		WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(AbysmTexturedModels.BLOSSOM.upload(block, generator.modelCollector));
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, weightedVariant).coordinate(UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS));
	}

	private void registerBloomingCrowns(BlockStateModelGenerator generator, Block... blocks) {
		doForMany(this::registerBloomCrown, generator, blocks);
	}

	private void registerBloomCrown(BlockStateModelGenerator generator, Block block) {
		WeightedVariant weightedVariant = BlockStateModelGenerator.createWeightedVariant(AbysmTexturedModels.BLOOMING_CROWN.upload(block, generator.modelCollector));
		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block, weightedVariant).coordinate(UP_DEFAULT_ROTATION_OPERATIONS));
	}

	private void registerGrassLike(BlockStateModelGenerator generator, Block block, Block baseBlock) {
		TextureMap textureMapping = new TextureMap()
			.put(TextureKey.BOTTOM, TextureMap.getId(baseBlock))
			.put(TextureKey.TOP, TextureMap.getId(block))
			.put(TextureKey.SIDE, TextureMap.getSubId(block, "_side"));
		Identifier model = Models.CUBE_BOTTOM_TOP.upload(block, textureMapping, generator.modelCollector);
		generator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, BlockStateModelGenerator.modelWithYRotation(BlockStateModelGenerator.createModelVariant(model))));
	}

	private void registerSprigsWithPot(BlockStateModelGenerator generator, Block sprigs, Block pottedSprigs) {
		registerSprigs(generator, sprigs);

		TextureMap textureMap = TextureMap.plant(TextureMap.getSubId(sprigs, "_pot"));
		WeightedVariant weightedVariant = createWeightedVariant(
			BlockStateModelGenerator.CrossType.NOT_TINTED.getFlowerPotCrossModel().upload(pottedSprigs, textureMap, generator.modelCollector)
		);
		generator.blockStateCollector.accept(createSingletonBlockState(pottedSprigs, weightedVariant));
	}

	private void registerSprigs(BlockStateModelGenerator generator, Block block) {
		generator.registerItemModel(block.asItem(), CrossType.NOT_TINTED.registerItemModel(generator, block));
		registerSprigsBlockState(generator, block);
	}

	private void registerSprigsBlockState(BlockStateModelGenerator generator, Block block) {
		TextureMap textureMap = TextureMap.cross(block);
		TextureMap swayingTextureMap = TextureMap.of(TextureKey.CROSS, TextureMap.getSubId(block, "_swaying"));

		Model crossModel = CrossType.NOT_TINTED.getCrossModel();

		WeightedVariant surfaceVariant = createWeightedVariant(crossModel.upload(block, textureMap, generator.modelCollector));
		WeightedVariant waterloggedVariant = createWeightedVariant(crossModel.upload(block, "_swaying", swayingTextureMap, generator.modelCollector));

		generator.blockStateCollector.accept(VariantsBlockModelDefinitionCreator.of(block)
			.with(
				BlockStateVariantMap.models(Properties.WATERLOGGED)
					.register(false, surfaceVariant)
					.register(true, waterloggedVariant)
			)
		);
	}

	public final void registerOozetrickleLantern(BlockStateModelGenerator BMG, Block lantern) {
		WeightedVariant weightedVariant = createWeightedVariant(AbysmTexturedModels.TEMPLATE_OOZETRICKLE_LANTERN.upload(lantern, BMG.modelCollector));
		WeightedVariant weightedVariant2 = createWeightedVariant(AbysmTexturedModels.TEMPLATE_OOZETRICKLE_HANGING_LANTERN.upload(lantern, BMG.modelCollector));
		BMG.registerItemModel(lantern.asItem());
		BMG.blockStateCollector
			.accept(VariantsBlockModelDefinitionCreator.of(lantern).with(BlockStateModelGenerator.createBooleanModelMap(Properties.HANGING, weightedVariant2, weightedVariant)));
	}

	@Override
	public void generateItemModels(ItemModelGenerator generator) {
		registerGenerated(generator,
			AbysmItems.LAPIS_BULB,
			AbysmItems.GOLD_LEAF,
			AbysmItems.DREGLOAM_OOZEBALL,

			AbysmItems.FLIPPERS,

			AbysmItems.SMALL_FLORAL_FISH,
			AbysmItems.BIG_FLORAL_FISH,
			AbysmItems.SMALL_FLORAL_FISH_BUCKET,
			AbysmItems.BIG_FLORAL_FISH_BUCKET,
			AbysmItems.PADDLEFISH_BUCKET,

			AbysmItems.SMALL_FLORAL_FISH_SPAWN_EGG,
			AbysmItems.BIG_FLORAL_FISH_SPAWN_EGG,
			AbysmItems.PADDLEFISH_SPAWN_EGG,
			AbysmItems.SNAPPER_SPAWN_EGG,
			AbysmItems.GUP_GUP_SPAWN_EGG,
			AbysmItems.AROWANA_MAGICII_SPAWN_EGG,
			AbysmItems.BLOOMRAY_SPAWN_EGG,
			AbysmItems.ELECTRIC_OOGLY_BOOGLY_SPAWN_EGG,
			AbysmItems.MAN_O_WAR_SPAWN_EGG,
			AbysmItems.LECTORFIN_SPAWN_EGG,

			AbysmItems.MUSIC_DISC_RENAISSANCE
		);

		generator.registerCondition(
			AbysmItems.HARPOON,
			new HarpoonLoadedProperty(),
			ItemModels.basic(ModelIds.getItemSubModelId(AbysmItems.HARPOON, "_loaded")),
			ItemModels.basic(ModelIds.getItemModelId(AbysmItems.HARPOON))
		);
	}

	private void registerGenerated(ItemModelGenerator generator, Item... items) {
		for (Item item : items) {
			registerGenerated(generator, item);
		}
	}

	private void registerGenerated(ItemModelGenerator generator, Item item) {
		generator.register(item, Models.GENERATED);
	}
}
