package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.world.Heightmap;

public class AbysmSpawnRestrictions {

	public static void init() {
		register(AbysmEntityTypes.SMALL_FLORAL_FISH, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn);
		register(AbysmEntityTypes.BIG_FLORAL_FISH, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn);
	}

	private static <T extends MobEntity> void register(
		EntityType<T> type, SpawnLocation location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate
	) {
		SpawnRestriction.register(type, location, heightmapType, predicate);
	}
}
