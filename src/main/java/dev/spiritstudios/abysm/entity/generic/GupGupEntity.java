package dev.spiritstudios.abysm.entity.generic;

import dev.spiritstudios.abysm.data.variant.GupGupEntityVariant;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.AbysmTrackedDataHandlers;
import dev.spiritstudios.abysm.entity.SimpleFishEntity;
import dev.spiritstudios.abysm.entity.ai.goal.SwimAroundBoidGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.variant.Variantable;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.VariantSelectorProvider;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.spawn.SpawnContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class GupGupEntity extends SimpleFishEntity implements Variantable<GupGupEntityVariant> {
	public static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.gup_gup.idle");

	public static final TrackedData<RegistryEntry<GupGupEntityVariant>> VARIANT = DataTracker.registerData(GupGupEntity.class, AbysmTrackedDataHandlers.GUP_GUP_VARIANT);

	public GupGupEntity(EntityType<GupGupEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected void initGoals() {
		// No super, there will be lots of these so we don't want to add goals we don't need.
		this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25));
		this.goalSelector.add(2, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6, 1.4, EntityPredicates.EXCEPT_SPECTATOR::test));
		this.goalSelector.add(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(4, new SwimAroundBoidGoal(
			this,
			2.0F,
			100 * MathHelper.RADIANS_PER_DEGREE,
			160 * MathHelper.RADIANS_PER_DEGREE,
			0.25F, 0.6F, 0.2F, 0.05F, 2F,
			0.025F, 0.05F
		));
	}

	@Override
	public int getLimitPerChunk() {
		return 50;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(VARIANT, GupGupEntityVariant.getDefaultEntry(this.getRegistryManager()));
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
		controllers.add(new AnimationController<>(0, event -> event.setAndContinue(IDLE_ANIM)));
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);

		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		nbt.put("variant", GupGupEntityVariant.ENTRY_CODEC, ops, this.dataTracker.get(VARIANT));
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);

		RegistryOps<NbtElement> ops = this.getRegistryManager().getOps(NbtOps.INSTANCE);

		this.setVariant(
			nbt.get("variant", GupGupEntityVariant.ENTRY_CODEC, ops)
				.orElse(GupGupEntityVariant.getDefaultEntry(this.getRegistryManager()))
		);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		VariantSelectorProvider.select(
			this.getRegistryManager().getOrThrow(AbysmRegistryKeys.GUP_GUP_ENTITY_VARIANT).streamEntries(),
			RegistryEntry::value, random,
			SpawnContext.of(world, this.getBlockPos())
		).ifPresent(this::setVariant);

		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public boolean shouldRender(double distance) {
		double d = this.getBoundingBox().getAverageSideLength();
		if (Double.isNaN(d)) {
			d = 1.0;
		}

		d *= 64.0 * 5; // 5x vanilla draw distance
		return distance < d * d;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.GUP_GUP;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source) {
		return AbysmSoundEvents.ENTITY_GUP_GUP_HURT;
	}

	@Override
	protected @Nullable SoundEvent getDeathSound() {
		return AbysmSoundEvents.ENTITY_GUP_GUP_DEATH;
	}

	@Override
	protected SoundEvent getFlopSound() {
		return AbysmSoundEvents.ENTITY_GUP_GUP_FLOP;
	}

	// TODO: Bucket
	@Override
	public ItemStack getBucketItem() {
		return new ItemStack(AbysmItems.PADDLEFISH_BUCKET);
	}

	@Override
	public GupGupEntityVariant getVariant() {
		return this.dataTracker.get(VARIANT).value();
	}

	@Override
	public void setVariant(RegistryEntry<GupGupEntityVariant> variant) {
		this.dataTracker.set(VARIANT, variant);
	}
}
