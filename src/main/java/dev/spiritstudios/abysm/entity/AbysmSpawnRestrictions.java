package dev.spiritstudios.abysm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnLocation;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.world.Heightmap;

public class AbysmSpawnRestrictions {

	public static void init() {
		registerWaterCreature(AbysmEntityTypes.SMALL_FLORAL_FISH);
		registerWaterCreature(AbysmEntityTypes.BIG_FLORAL_FISH);
		registerWaterCreature(AbysmEntityTypes.BLOOMRAY);
		registerWaterCreature(AbysmEntityTypes.ELECTRIC_OOGLY_BOOGLY);
		registerWaterCreature(AbysmEntityTypes.MAN_O_WAR);
	}

	private static <T extends WaterCreatureEntity> void registerWaterCreature(EntityType<T> type) {
		register(type, SpawnLocationTypes.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, WaterCreatureEntity::canSpawn);
	}

	private static <T extends MobEntity> void register(
		EntityType<T> type, SpawnLocation location, Heightmap.Type heightmapType, SpawnRestriction.SpawnPredicate<T> predicate
	) {
		SpawnRestriction.register(type, location, heightmapType, predicate);
	}
}
