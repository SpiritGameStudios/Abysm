package dev.spiritstudios.abysm.entity.leviathan.pseudo;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.ecosystem.AbysmEcosystemTypes;
import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.entity.leviathan.GeoChainLeviathan;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.mixin.skeleshark.EntityPartAccessor;
import dev.spiritstudios.abysm.networking.NowHuntingS2CPayload;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class SkeletonSharkEntity extends GeoChainLeviathan implements EcologicalEntity {

	public static final AttachmentType<Boolean> HUNTING = AttachmentRegistry.<Boolean>builder()
		.initializer(() -> false)
		.syncWith(PacketCodecs.BOOLEAN, AttachmentSyncPredicate.all())
		.buildAndRegister(Abysm.id("hunting_sync"));

	public final List<SkeletonSharkPart> parts;
	public final List<SkeletonSharkPart> nonFins;
	public final EcosystemLogic ecosystemLogic = this.createEcosystemLogic(this);

	public final SkeletonSharkPart rfin;
	public final SkeletonSharkPart lfin;

	protected float prevScale = 1;

	public SkeletonSharkEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		ImmutableList.Builder<SkeletonSharkPart> builder = ImmutableList.builder();
		builder.add(new SkeletonSharkPart(this, "body", 2F, 1F, -0.1F));
		this.rfin = new SkeletonSharkPart(this, "rfin", 0.5F, 0.5F, 0F);
		this.lfin = new SkeletonSharkPart(this, "lfin", 0.5F, 0.5F, 0F);
		builder.add(new SkeletonSharkPart(this, "tail", 2F, 1F, 1F));
		builder.add(this.rfin);
		builder.add(this.lfin);
		this.parts = builder.build();
		this.nonFins = this.parts.stream().filter(part -> !part.name.contains("fin")).toList();
		this.calculateDimensions();
	}

	@Override
	protected @NotNull ServerBossBar createBossBar(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		ServerBossBar bar = super.createBossBar(entityType, world);
		bar.setColor(BossBar.Color.WHITE);
		return bar;
	}

	@Override
	public List<SkeletonSharkPart> getSpecterEntityParts() {
		return this.parts;
	}

	@Override
	public List<? extends LeviathanPart> getTailParts() {
		return this.nonFins;
	}

	@Override
	public float getDistanceToMainBody(LeviathanPart leviathanPart) {
		return leviathanPart instanceof SkeletonSharkPart part ? part.originalDistance : super.getDistanceToMainBody(leviathanPart);
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && this.getEcosystemType().prey().contains(living.getType());
	}

	@Override
	protected Optional<LivingEntity> findNearestTarget(EntityType<? extends LivingEntity> entityType) {
		return entityType == EntityType.PLAYER ? super.findNearestTarget(living -> living.getType() == EntityType.PLAYER) : super.findNearestTarget(entityType);
	}

	@Override
	protected Optional<LivingEntity> findNearestTarget(Predicate<LivingEntity> targetPredicate) {
		if (!this.shouldHunt()) {
			return Optional.empty();
		}
		return super.findNearestTarget(targetPredicate);
	}

	@Override
	protected void onTargetFound(LivingEntity living) {
		super.onTargetFound(living);
		if (living instanceof MobEntity mob) {
			this.theHuntIsOn(this.getWorld(), mob);
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<SkeletonSharkEntity> animController = new AnimationController<>(0, event -> PlayState.STOP);

		registrar.add(animController);
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
		this.alertEcosystemOfSpawn();
		return super.initialize(world, difficulty, spawnReason, entityData);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public EcosystemType<?> getEcosystemType() {
		return AbysmEcosystemTypes.SKELETON_SHARK;
	}

	@Override
	public void onRemove(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemove(reason);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
		float scale = this.getScale();
		if (scale != this.prevScale) {
			this.prevScale = scale;
			this.parts.forEach(part -> {
				EntityDimensions dimensions = part.originalDimensions.scaled(scale);
				((EntityPartAccessor) part).abysm$setDimensions(dimensions);
				part.calculateDimensions();
			});
		}
	}

	@Override
	public void tickEcosystemLogic() {
		EcologicalEntity.super.tickEcosystemLogic();
		if (this.getWorld() instanceof ServerWorld) {
			if (this.shouldFailHunt()) {
				Optional<LivingEntity> living = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET);
				if (living.isEmpty()) {
					return;
				}
				if (living.get() instanceof PlayerEntity player && this.isValidTarget(player)) {
					return;
				}
				this.getBrain().forget(MemoryModuleType.NEAREST_ATTACKABLE);
				this.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
			}
		}
	}

	public static DefaultAttributeContainer.Builder createSansAttributes() {
		return Leviathan.createLeviathanAttributes()
				.add(EntityAttributes.MAX_HEALTH, 2000)
				.add(EntityAttributes.ATTACK_DAMAGE, 8)
				.add(EntityAttributes.SCALE, 3);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		if (this.getWorld() instanceof ServerWorld) {
			boolean hunting = this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isPresent();
			if (this.getAttachedOrCreate(HUNTING) != hunting) {
				this.setAttached(HUNTING, hunting);
			}
		}
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.add(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.add(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.add(4, new SwimAroundGoal(this, 1.0, 10));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new HuntPreyGoal(this, false));
	}

	@Override
	public float damageResist() {
		return 100F;
	}

	@Override
	public int getMaxLookYawChange() {
		return 2;
	}
}
