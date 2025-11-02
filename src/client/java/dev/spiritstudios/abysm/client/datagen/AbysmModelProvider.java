package dev.spiritstudios.abysm.client.datagen;

import dev.spiritstudios.abysm.block.AbysmBlockFamilies;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.client.render.HarpoonLoadedProperty;
import dev.spiritstudios.abysm.item.AbysmEquipmentAssetKeys;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.BlockModelGenerators.PlantType;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.data.BlockFamily;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

import static net.minecraft.client.data.models.BlockModelGenerators.NOP;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.X_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_180;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_270;
import static net.minecraft.client.data.models.BlockModelGenerators.Y_ROT_90;
import static net.minecraft.client.data.models.BlockModelGenerators.createSimpleBlock;
import static net.minecraft.client.data.models.BlockModelGenerators.plainVariant;

public class AbysmModelProvider extends FabricModelProvider {
	// Note - by default, the spore blossom model(used for scabiosa) is upside, which is why this is flipped
	private static final PropertyDispatch<VariantMutator> UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.FACING)
		.select(Direction.DOWN, NOP)
		.select(Direction.UP, X_ROT_180)
		.select(Direction.NORTH, X_ROT_90.then(Y_ROT_180))
		.select(Direction.SOUTH, X_ROT_90)
		.select(Direction.WEST, X_ROT_90.then(Y_ROT_90))
		.select(Direction.EAST, X_ROT_90.then(Y_ROT_270));

	private static final PropertyDispatch<VariantMutator> UP_DEFAULT_ROTATION_OPERATIONS = PropertyDispatch.modify(BlockStateProperties.FACING)
		.select(Direction.DOWN, X_ROT_180)
		.select(Direction.UP, NOP)
		.select(Direction.NORTH, X_ROT_90)
		.select(Direction.SOUTH, X_ROT_90.then(Y_ROT_180))
		.select(Direction.WEST, X_ROT_90.then(Y_ROT_270))
		.select(Direction.EAST, X_ROT_90.then(Y_ROT_90));

	public AbysmModelProvider(FabricDataOutput output) {
		super(output);
	}

	@Override
	public void generateBlockStateModels(BlockModelGenerators generator) {
		// region block families
		AbysmBlockFamilies
			.getAllAbysmBlockFamilies()
			.filter(BlockFamily::shouldGenerateModel)
			.forEach(blockFamily -> generator.family(blockFamily.getBaseBlock()).generateFor(blockFamily));
		// endregion

		// region floropumice
		generator.createTrivialCube(AbysmBlocks.POLISHED_FLOROPUMICE);
		generator.createTrivialCube(AbysmBlocks.POLISHED_SMOOTH_FLOROPUMICE);
		generator.createRotatedPillarWithHorizontalVariant(AbysmBlocks.SMOOTH_FLOROPUMICE_PILLAR, TexturedModel.COLUMN_ALT, TexturedModel.COLUMN_HORIZONTAL_ALT);
		// endregion

		// region bloom-ish blocks
		registerGrassLike(generator, AbysmBlocks.ROSEBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.SUNBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);
		registerGrassLike(generator, AbysmBlocks.MALLOWBLOOMED_FLOROPUMICE, AbysmBlocks.FLOROPUMICE);

		registerSprigsWithPot(generator, AbysmBlocks.ROSY_SPRIGS, AbysmBlocks.POTTED_ROSY_SPRIGS);
		registerSprigsWithPot(generator, AbysmBlocks.SUNNY_SPRIGS, AbysmBlocks.POTTED_SUNNY_SPRIGS);
		registerSprigsWithPot(generator, AbysmBlocks.MAUVE_SPRIGS, AbysmBlocks.POTTED_MAUVE_SPRIGS);

		generator.createLeafLitter(AbysmBlocks.ROSEBLOOM_PETALS);
		generator.createLeafLitter(AbysmBlocks.SUNBLOOM_PETALS);
		generator.createLeafLitter(AbysmBlocks.MALLOWBLOOM_PETALS);
		// endregion

		// region bloomshrooms
		generator.createPlantWithDefaultItem(AbysmBlocks.ROSY_BLOOMSHROOM, AbysmBlocks.POTTED_ROSY_BLOOMSHROOM, BlockModelGenerators.PlantType.NOT_TINTED);
		generator.createPlantWithDefaultItem(AbysmBlocks.SUNNY_BLOOMSHROOM, AbysmBlocks.POTTED_SUNNY_BLOOMSHROOM, BlockModelGenerators.PlantType.NOT_TINTED);
		generator.createPlantWithDefaultItem(AbysmBlocks.MAUVE_BLOOMSHROOM, AbysmBlocks.POTTED_MAUVE_BLOOMSHROOM, BlockModelGenerators.PlantType.NOT_TINTED);

		generator.woodProvider(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).log(AbysmBlocks.ROSY_BLOOMSHROOM_STEM).wood(AbysmBlocks.ROSY_BLOOMSHROOM_HYPHAE);
		generator.woodProvider(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).log(AbysmBlocks.SUNNY_BLOOMSHROOM_STEM).wood(AbysmBlocks.SUNNY_BLOOMSHROOM_HYPHAE);
		generator.woodProvider(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).log(AbysmBlocks.MAUVE_BLOOMSHROOM_STEM).wood(AbysmBlocks.MAUVE_BLOOMSHROOM_HYPHAE);

		doForMany(BlockModelGenerators::createTrivialCube, generator,
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
		generator.createNetherRoots(AbysmBlocks.ANTENNAE_PLANT, AbysmBlocks.POTTED_ANTENNAE_PLANT);
		// endregion

		// region dregloam
		generator.createColoredBlockWithRandomRotations(TexturedModel.CUBE, AbysmBlocks.DREGLOAM);

		doForMany(BlockModelGenerators::createTrivialCube, generator,
			AbysmBlocks.DREGLOAM_OOZE,
			AbysmBlocks.DREGLOAM_GOLDEN_LAZULI_ORE
		);
		registerGrassLike(generator, AbysmBlocks.OOZING_DREGLOAM, AbysmBlocks.DREGLOAM);
		// endregion

		// region dregloam plants
		generator.createGrowingPlant(AbysmBlocks.GOLDEN_LAZULI_OREFURL, AbysmBlocks.GOLDEN_LAZULI_OREFURL_PLANT, BlockModelGenerators.PlantType.NOT_TINTED);

		generator.createNetherRoots(AbysmBlocks.OOZETRICKLE_FILAMENTS, AbysmBlocks.POTTED_OOZETRICKLE_FILAMENTS);
		generator.createDoublePlantWithDefaultItem(AbysmBlocks.TALL_OOZETRICKLE_FILAMENTS, PlantType.NOT_TINTED);
		// endregion

		// region oozetrickle deco
		generator.registerSimpleFlatItemModel(AbysmBlocks.OOZETRICKLE_CORD.asItem());
		generator.createAxisAlignedPillarBlockCustomModel(AbysmBlocks.OOZETRICKLE_CORD, plainVariant(ModelLocationUtils.getModelLocation(AbysmBlocks.OOZETRICKLE_CORD)));
		this.registerOozetrickleLantern(generator, AbysmBlocks.OOZETRICKLE_LANTERN);
		// endregion

		// region silt
		generator.createTrivialCube(AbysmBlocks.SILT);
		generator.createTrivialCube(AbysmBlocks.CHISELED_SILT);
		generator.createTrivialCube(AbysmBlocks.CUT_SILT);
		// endregion

		// region technical blocks
		generator.createTrivialCube(AbysmBlocks.DENSITY_BLOB_BLOCK);
		// endregion
	}

	private void doForMany(BiConsumer<BlockModelGenerators, Block> consumer, BlockModelGenerators generator, Block... blocks) {
		for (Block block : blocks) {
			consumer.accept(generator, block);
		}
	}

	private void registerScabiosas(BlockModelGenerators generator, Block... blocks) {
		doForMany(this::registerScabiosa, generator, blocks);
	}

	private void registerScabiosa(BlockModelGenerators generator, Block block) {
		MultiVariant weightedVariant = BlockModelGenerators.plainVariant(AbysmTexturedModels.BLOSSOM.create(block, generator.modelOutput));
		generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, weightedVariant).with(UP_FLIPPED_DEFAULT_ROTATION_OPERATIONS));
	}

	private void registerBloomingCrowns(BlockModelGenerators generator, Block... blocks) {
		doForMany(this::registerBloomCrown, generator, blocks);
	}

	private void registerBloomCrown(BlockModelGenerators generator, Block block) {
		MultiVariant weightedVariant = BlockModelGenerators.plainVariant(AbysmTexturedModels.BLOOMING_CROWN.create(block, generator.modelOutput));
		generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, weightedVariant).with(UP_DEFAULT_ROTATION_OPERATIONS));
	}

	private void registerGrassLike(BlockModelGenerators generator, Block block, Block baseBlock) {
		TextureMapping textureMapping = new TextureMapping()
			.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(baseBlock))
			.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block))
			.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, "_side"));
		ResourceLocation model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, textureMapping, generator.modelOutput);
		generator.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(block, BlockModelGenerators.createRotatedVariants(BlockModelGenerators.plainModel(model))));
	}

	private void registerSprigsWithPot(BlockModelGenerators generator, Block sprigs, Block pottedSprigs) {
		registerSprigs(generator, sprigs);

		TextureMapping textureMap = TextureMapping.plant(TextureMapping.getBlockTexture(sprigs, "_pot"));
		MultiVariant weightedVariant = plainVariant(
			BlockModelGenerators.PlantType.NOT_TINTED.getCrossPot().create(pottedSprigs, textureMap, generator.modelOutput)
		);
		generator.blockStateOutput.accept(createSimpleBlock(pottedSprigs, weightedVariant));
	}

	private void registerSprigs(BlockModelGenerators generator, Block block) {
		generator.registerSimpleItemModel(block.asItem(), PlantType.NOT_TINTED.createItemModel(generator, block));
		registerSprigsBlockState(generator, block);
	}

	private void registerSprigsBlockState(BlockModelGenerators generator, Block block) {
		TextureMapping textureMap = TextureMapping.cross(block);
		TextureMapping swayingTextureMap = TextureMapping.singleSlot(TextureSlot.CROSS, TextureMapping.getBlockTexture(block, "_swaying"));

		ModelTemplate crossModel = PlantType.NOT_TINTED.getCross();

		MultiVariant surfaceVariant = plainVariant(crossModel.create(block, textureMap, generator.modelOutput));
		MultiVariant waterloggedVariant = plainVariant(crossModel.createWithSuffix(block, "_swaying", swayingTextureMap, generator.modelOutput));

		generator.blockStateOutput.accept(MultiVariantGenerator.dispatch(block)
			.with(
				PropertyDispatch.initial(BlockStateProperties.WATERLOGGED)
					.select(false, surfaceVariant)
					.select(true, waterloggedVariant)
			)
		);
	}

	public final void registerOozetrickleLantern(BlockModelGenerators BMG, Block lantern) {
		MultiVariant weightedVariant = plainVariant(AbysmTexturedModels.TEMPLATE_OOZETRICKLE_LANTERN.create(lantern, BMG.modelOutput));
		MultiVariant weightedVariant2 = plainVariant(AbysmTexturedModels.TEMPLATE_OOZETRICKLE_HANGING_LANTERN.create(lantern, BMG.modelOutput));
		BMG.registerSimpleFlatItemModel(lantern.asItem());
		BMG.blockStateOutput
			.accept(MultiVariantGenerator.dispatch(lantern).with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.HANGING, weightedVariant2, weightedVariant)));
	}

	@Override
	public void generateItemModels(ItemModelGenerators generator) {
		generator.declareCustomModelItem(AbysmItems.DIVING_HELMET);

		registerArmorSet(
				generator,
				AbysmEquipmentAssetKeys.DIVING_SUIT,
				false,
				AbysmItems.DIVING_BOOTS,
				AbysmItems.DIVING_LEGGINGS,
				AbysmItems.DIVING_CHESTPLATE,
				null // special-cased
		);

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
			AbysmItems.SYNTHETHIC_ORNIOTHOPE_SPAWN_EGG,
			AbysmItems.BLOOMRAY_SPAWN_EGG,
			AbysmItems.ELECTRIC_OOGLY_BOOGLY_SPAWN_EGG,
			AbysmItems.MAN_O_WAR_SPAWN_EGG,
			AbysmItems.MYSTERIOUS_BLOB_SPAWN_EGG,
			AbysmItems.LECTORFIN_SPAWN_EGG,
			AbysmItems.RETICULATED_FLIPRAY_SPAWN_EGG,
			AbysmItems.SKELETON_SHARK_SPAWN_EGG,

			AbysmItems.MUSIC_DISC_RENAISSANCE
		);

		generator.generateBooleanDispatch(
			AbysmItems.HARPOON,
			new HarpoonLoadedProperty(),
			ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(AbysmItems.HARPOON, "_loaded")),
			ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(AbysmItems.HARPOON))
		);
	}

	private void registerArmorSet(
			final ItemModelGenerators generator,
			final ResourceKey<EquipmentAsset> asset,
			final boolean dyeable,
			final @Nullable Item boots,
			final @Nullable Item leggings,
			final @Nullable Item chestplate,
			final @Nullable Item helmet
	) {
		if (boots != null) {
			generator.generateTrimmableItem(boots, asset, ItemModelGenerators.TRIM_PREFIX_BOOTS, dyeable);
		}

		if (leggings != null) {
			generator.generateTrimmableItem(leggings, asset, ItemModelGenerators.TRIM_PREFIX_LEGGINGS, dyeable);
		}

		if (chestplate != null) {
			generator.generateTrimmableItem(chestplate, asset, ItemModelGenerators.TRIM_PREFIX_CHESTPLATE, dyeable);
		}

		if (helmet != null) {
			generator.generateTrimmableItem(helmet, asset, ItemModelGenerators.TRIM_PREFIX_HELMET, dyeable);
		}
	}

	private void registerGenerated(ItemModelGenerators generator, Item... items) {
		for (Item item : items) {
			registerGenerated(generator, item);
		}
	}

	private void registerGenerated(ItemModelGenerators generator, Item item) {
		generator.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
	}
}
