package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

public class AbysmSpawnRestrictions {

	public static void init() {
		registerStandardEcoWC(AbysmEntityTypes.SMALL_FLORAL_FISH);
		registerStandardEcoWC(AbysmEntityTypes.BIG_FLORAL_FISH);
		registerStandardEcoWC(AbysmEntityTypes.PADDLEFISH);
		registerStandardEcoWC(AbysmEntityTypes.SNAPPER);
		registerStandardEcoWC(AbysmEntityTypes.GUP_GUP);
		registerStandardEcoWC(AbysmEntityTypes.AROWANA_MAGICII);
		registerStandardEcoWC(AbysmEntityTypes.SYNTHETHIC_ORNIOTHOPE);
		registerStandardEcoWC(AbysmEntityTypes.BLOOMRAY);
		registerStandardEcoWC(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY);
		registerStandardWC(AbysmEntityTypes.MAN_O_WAR);
		registerStandardEcoWC(AbysmEntityTypes.LECTORFIN);
		registerStandardEcoWC(AbysmEntityTypes.RETICULATED_FLIPRAY);
		registerStandardEcoWC(AbysmEntityTypes.SKELETON_SHARK);
	}

	private static <T extends WaterAnimal> void registerStandardEcoWC(EntityType<T> type) {
		registerWC(type, and(AbysmSpawnRestrictions::canSpawn, EcologicalEntity::canSpawnInEcosystem));
	}

	private static <T extends WaterAnimal> void registerStandardWC(EntityType<T> type) {
		registerWC(type, AbysmSpawnRestrictions::canSpawn);
	}

	private static <T extends WaterAnimal> void registerWC(EntityType<T> type, SpawnPlacements.SpawnPredicate<T> predicate) {
		register(type, SpawnPlacementTypes.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, predicate);
	}

	private static <T extends Mob> void register(
		EntityType<T> type, SpawnPlacementType location, Heightmap.Types heightmapType, SpawnPlacements.SpawnPredicate<T> predicate
	) {
		SpawnPlacements.register(type, location, heightmapType, predicate);
	}

	private static <T extends Entity> SpawnPlacements.SpawnPredicate<T> and(SpawnPlacements.SpawnPredicate<T> one, SpawnPlacements.SpawnPredicate<T> two) {
		return (type, world, spawnReason,
				pos, random) ->
			one.test(type, world, spawnReason, pos, random) &&
				two.test(type, world, spawnReason, pos, random);
	}

	public static boolean canSpawn(EntityType<? extends WaterAnimal> type, LevelAccessor world, EntitySpawnReason reason, BlockPos pos, RandomSource random) {
		int i = world.getSeaLevel();
		return pos.getY() <= i && world.getFluidState(pos.below()).is(FluidTags.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER);
	}
}
