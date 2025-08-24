package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.data.variant.SnapperEntityVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.SimpleFishEntity;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class SnapperEntity extends SimpleFishEntity implements Variantable<SnapperEntityVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.snapper.idle");

	public static final TrackedData<RegistryEntry<SnapperEntityVariant>> VARIANT = DataTracker.registerData(SnapperEntity.class, AbysmTrackedDataHandlers.SNAPPER_VARIANT);


	public SnapperEntity(EntityType<SnapperEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(4, new SwimAroundGoal(
			this,
			1.0F,
			40
		));
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(VARIANT, SnapperEntityVariant.getDefaultEntry(this.getRegistryManager()));
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SNAPPER;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);

		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		nbt.put("variant", SnapperEntityVariant.ENTRY_CODEC, ops, this.dataTracker.get(VARIANT));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		this.setVariant(
			nbt.get("variant", SnapperEntityVariant.ENTRY_CODEC, ops)
				.orElse(SnapperEntityVariant.getDefaultEntry(this.getRegistryManager()))
		);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.getRegistryManager().getOrThrow(AbysmRegistryKeys.SNAPPER_ENTITY_VARIANT)
			.getRandom(this.random)
			.ifPresentOrElse(
				this::setVariant,
				() -> setVariant(SnapperEntityVariant.getDefaultEntry(world.getRegistryManager()))
			);
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public SnapperEntityVariant getVariant() {
		return this.dataTracker.get(VARIANT).value();
	}

	@Override
	public void setVariant(RegistryEntry<SnapperEntityVariant> variant) {
		this.dataTracker.set(VARIANT, variant);
	}

	// TODO: SoundEvents
	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_PADDLEFISH_FLOP;
	}

	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}
}
