package dev.spiritstudios.abysm.world.entity.leviathan.pseudo;

import dev.spiritstudios.abysm.world.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.world.entity.leviathan.LeviathanPart;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class SkeletonSharkPart extends LeviathanPart {
	public final float originalDistance;
	public final EntityDimensions originalDimensions;

	public SkeletonSharkPart(Leviathan owner, String name, float width, float height, float originalDistance) {
		super(owner, name, width, height);
		this.originalDistance = originalDistance;
		setRelativePos(Vec3.ZERO);
		this.originalDimensions = this.dimensions;
	}

	public void setDimensions(EntityDimensions dimensions) {
		this.dimensions = dimensions;
	}

	public Vec3 getPrevRelPos() {
		return this.prevRelativePos;
	}
}
