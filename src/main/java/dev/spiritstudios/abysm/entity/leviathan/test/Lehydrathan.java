package dev.spiritstudios.abysm.entity.leviathan.test;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.entity.leviathan.GeoChainLeviathan;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class Lehydrathan extends GeoChainLeviathan {

	public final List<LeviathanPart> parts;

	public Lehydrathan(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		ImmutableList.Builder<LeviathanPart> builder = ImmutableList.builder();
		float width = entityType.getWidth();
		float height = entityType.getHeight();
		for (int i = 0; i < 4; i++) {
			LeviathanPart part = new LeviathanPart(this, "body" + i, width, height);
			part.setRelativePos(new Vec3d(0, 0, i + 1));
			builder.add(part);
		}
		this.parts = builder.build();
	}

	@Override
	public List<LeviathanPart> getSpecterEntityParts() {
		return this.parts;
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && living.getType().isIn(AbysmEntityTypeTags.LEHYDRATHAN_HUNT_TARGETS);
	}

}
