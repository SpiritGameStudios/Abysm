package dev.spiritstudios.abysm.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class AbstractSchoolingFishEntity extends SchoolingFishEntity implements GeoEntity {
	public static final String ANIM_CONTROLLER_STRING = "default";
	public final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public AbstractSchoolingFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected SoundEvent getFlopSound() {
		return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_TROPICAL_FISH_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_TROPICAL_FISH_DEATH;
	}

	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(Items.TROPICAL_FISH_BUCKET);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
