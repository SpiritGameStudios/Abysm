package dev.spiritstudios.abysm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class BloomrayEntity extends AbstractSchoolingFishEntity implements GeoEntity {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.bloomray.idle");

	// TODO - Custom swimming AI to make it glide through the water, reducing the up/down movement amount
	// TODO - Custom AI for hiding in Bloomshroom crowns when scared(player nearby? Bigger fish/TBD enemy nearby?)
	// TODO - (Data-driven?) entity variants for different bloomshrooms
	public BloomrayEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		AnimationController<AbstractFloralFishEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> {
			// Slower idle animation speed whilst not moving until I make a swimming animation
			if (!event.isMoving()) event.setControllerSpeed(0.75f);
			else event.setControllerSpeed(1f);

			return event.setAndContinue(IDLE_ANIM);
		});

		controllerRegistrar.add(animController);
	}
}
