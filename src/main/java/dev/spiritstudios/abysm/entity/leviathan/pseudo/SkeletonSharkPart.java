package dev.spiritstudios.abysm.entity.leviathan.pseudo;

import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.mixin.skeleshark.EntityPartAccessor;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SkeletonSharkPart extends LeviathanPart implements GeoEntity {

	public final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public final float originalDistance;
	public final EntityDimensions originalDimensions;

	public SkeletonSharkPart(Leviathan owner, String name, float width, float height, float originalDistance) {
		super(owner, name, width, height);
		this.originalDistance = originalDistance;
		this.setRelativePos(Vec3d.ZERO);
		this.originalDimensions = ((EntityPartAccessor) this).abysm$getDimensions();
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}
