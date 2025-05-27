package dev.spiritstudios.abysm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.World;

public class SmallFloralFishEntity extends AbstractFloralFishEntity {
	public SmallFloralFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}
}
