package dev.spiritstudios.abysm.entity.leviathan.test;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.entity.leviathan.GeoChainLeviathan;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class Lehydrathan extends GeoChainLeviathan {

	public final List<LeviathanPart> parts;

	public Lehydrathan(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
		ImmutableList.Builder<LeviathanPart> builder = ImmutableList.builder();
		float width = entityType.getWidth();
		float height = entityType.getHeight();
		for (int i = 0; i < 4; i++) {
			LeviathanPart part = new LeviathanPart(this, "body" + i, width, height);
			part.setRelativePos(new Vec3(0, 0, i + 1));
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
		return super.isValidNonPlayerTarget(living) && living.getType().is(AbysmEntityTypeTags.LEHYDRATHAN_HUNT_TARGETS);
	}

}
