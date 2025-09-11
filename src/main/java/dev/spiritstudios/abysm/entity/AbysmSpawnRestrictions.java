package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;

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

	private static <T extends WaterCreatureEntity> void registerStandardEcoWC(EntityType<T> type) {
		registerWC(type, and(AbysmSpawnRestrictions::canSpawn, EcologicalEntity::canSpawnInEcosystem));
	}

	private static <T extends WaterCreatureEntity> void registerStandardWC(EntityType<T> type) {
		registerWC(type, AbysmSpawnRestrictions::canSpawn);
	}

	private static <T extends WaterCreatureEntity> void registerWC(EntityType<T> type, SpawnRestriction.SpawnPredicate<T> predicate) {
		register(type, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, predicate);
	}

	private static <T extends MobEntity> void register(
		EntityType<T> type, SpawnLocation location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate
	) {
		SpawnRestriction.register(type, location, heightmapType, predicate);
	}

	private static <T extends Entity> SpawnRestriction.SpawnPredicate<T> and(SpawnRestriction.SpawnPredicate<T> one, SpawnRestriction.SpawnPredicate<T> two) {
		return (type, world, spawnReason,
				pos, random) ->
			one.test(type, world, spawnReason, pos, random) &&
				two.test(type, world, spawnReason, pos, random);
	}

	public static boolean canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
		int i = world.getSeaLevel();
		return pos.getY() <= i && world.getFluidState(pos.down()).isIn(FluidTags.WATER) && world.getBlockState(pos.up()).isOf(Blocks.WATER);
	}
}
