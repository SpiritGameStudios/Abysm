package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.data.variant.BloomrayEntityVariant;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.registry.AbysmEntityVariants;
import dev.spiritstudios.abysm.registry.AbysmRegistries;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class BloomrayEntity extends AbstractSchoolingFishEntity implements GeoEntity, Variantable<BloomrayEntityVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.bloomray.idle");
	public static final TrackedData<Integer> VARIANT_ID = DataTracker.registerData(BloomrayEntity.class, TrackedDataHandlerRegistry.INTEGER);

	// TODO - Adjust stats for bigger entity now
	// TODO - Mauve variant texture
	// TODO - Programmatically animate the swimming animation instead of manual animation because that's the only animation that'll probably be needed
	// TODO - Custom swimming AI to make it glide through the water, reducing the up/down movement amount
	// TODO - Custom AI for hiding in Bloomshroom crowns when scared(player nearby? Bigger fish/TBD enemy nearby?)
	public BloomrayEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(VARIANT_ID, BloomrayEntityVariant.getDefaultIntId(this.getRegistryManager()));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("variantId", this.getVariantIntId());
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.setVariantIntId(nbt.getInt("variantId").orElse(BloomrayEntityVariant.getDefaultIntId(this.getRegistryManager())));
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		if(this.random.nextBoolean()) {
			this.setVariant(BloomrayEntityVariant.DEFAULT);
		} else {
			this.setVariant(this.getRegistryManager().getOrThrow(AbysmRegistries.BLOOMRAY_ENTITY_VARIANT).get(AbysmEntityVariants.SUNNY_BLOOMRAY));
		}
		return super.initialize(world, difficulty, spawnReason, entityData);
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

	@Override
	public BloomrayEntityVariant getVariant() {
		return BloomrayEntityVariant.fromIntId(this.getRegistryManager(), this.getVariantIntId());
	}

	@Override
	public void setVariant(BloomrayEntityVariant variant) {
		this.setVariantIntId(BloomrayEntityVariant.toIntId(this.getRegistryManager(), variant));
	}

	@Override
	public int getVariantIntId() {
		return this.dataTracker.get(VARIANT_ID);
	}

	@Override
	public void setVariantIntId(int variantId) {
		this.dataTracker.set(VARIANT_ID, variantId);
	}
}
