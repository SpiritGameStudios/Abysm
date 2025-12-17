package dev.spiritstudios.abysm.world.entity.leviathan.pseudo;

import com.google.common.collect.ImmutableList;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.core.registries.AbysmMetatags;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.world.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.world.ecosystem.registry.EcosystemData;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.world.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.world.entity.leviathan.ChainLeviathan;
import dev.spiritstudios.abysm.world.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.world.entity.leviathan.LeviathanPart;
import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@SuppressWarnings({"UnstableApiUsage", "deprecation"})
public class SkeletonSharkEntity extends ChainLeviathan implements EcologicalEntity {

	public static final AttachmentType<Boolean> HUNTING = AttachmentRegistry.<Boolean>builder()
		.initializer(() -> false)
		.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all())
		.buildAndRegister(Abysm.id("hunting_sync"));

	public final List<SkeletonSharkPart> parts;
	public final List<SkeletonSharkPart> nonFins;
	public final EcosystemLogic ecosystemLogic = this.createEcosystemLogic(this);

	public final SkeletonSharkPart rfin;
	public final SkeletonSharkPart lfin;

	protected float prevScale = 1;

	public SkeletonSharkEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
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
		this.refreshDimensions();
	}

	@Override
	protected @NotNull ServerBossEvent createBossBar(EntityType<? extends WaterAnimal> entityType, Level world) {
		ServerBossEvent bar = super.createBossBar(entityType, world);
		bar.setColor(BossEvent.BossBarColor.WHITE);
		return bar;
	}

	@Override
	public List<SkeletonSharkPart> getSubEntities() {
		return this.parts;
	}

	@Override
	public List<? extends LeviathanPart> getTailParts() {
		return this.nonFins;
	}

	@Override
	public float getDistanceToMainBody(EntityPart<Leviathan> leviathanPart) {
		return leviathanPart instanceof SkeletonSharkPart part ? part.originalDistance : super.getDistanceToMainBody(leviathanPart);
	}

	@Override
	protected void movePart(EntityPart<Leviathan> part, double dx, double dy, double dz) {
		super.movePart(part, dx, dy, dz);

		if (part instanceof SkeletonSharkPart skelesharkPart && Objects.equals(skelesharkPart.name, "body")) {
			Vec3 vec3d = new Vec3(dz, 0 , -dx).normalize().scale(0.7);
			double finY = dy - 0.3;

			final double rX = this.rfin.getX();
			final double rY = this.rfin.getY();
			final double rZ = this.rfin.getZ();

			final double lX = this.lfin.getX();
			final double lY = this.lfin.getY();
			final double lZ = this.lfin.getZ();

			this.movePart(this.rfin, dx + vec3d.x, finY, dz + vec3d.z);
			this.movePart(this.lfin, dx - vec3d.x, finY, dz - vec3d.z);

			this.updatePartLastPos(this.rfin, rX, rY, rZ);
			this.updatePartLastPos(this.lfin, lX, lY, lZ);
		}
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && getEcosystemData().prey().contains(living.getType());
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
		if (living instanceof Mob mob) {
			this.theHuntIsOn(this, mob);
		}
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return this.ecosystemLogic;
	}

	@Override
	public void onRemoval(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemoval(reason);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
		float scale = this.getScale();
		if (scale != this.prevScale) {
			this.prevScale = scale;
			this.parts.forEach(part -> {
				EntityDimensions dimensions = part.originalDimensions.scale(scale);
				part.setDimensions(dimensions);
				part.refreshDimensions();
			});
		}
	}

	@Override
	public void tickEcosystemLogic() {
		EcologicalEntity.super.tickEcosystemLogic();
		if (this.level() instanceof ServerLevel) {
			if (this.shouldFailHunt()) {
				Optional<LivingEntity> living = this.getBrain().getMemoryInternal(MemoryModuleType.ATTACK_TARGET);
				if (living.isEmpty()) {
					return;
				}
				if (living.get() instanceof Player player && this.isValidTarget(player)) {
					return;
				}
				this.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE);
				this.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
			}
		}
	}

	public static AttributeSupplier.Builder createSansAttributes() {
		return Leviathan.createLeviathanAttributes()
			.add(Attributes.MAX_HEALTH, 2000)
			.add(Attributes.ATTACK_DAMAGE, 8)
			.add(Attributes.SCALE, 2.25)
			.add(Attributes.MOVEMENT_SPEED, 1.1);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.level() instanceof ServerLevel) {
			boolean hunting = this.getBrain().getMemoryInternal(MemoryModuleType.ATTACK_TARGET).isPresent();
			if (this.getAttachedOrCreate(HUNTING) != hunting) {
				this.setAttached(HUNTING, hunting);
			}
		}
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));
	}

	@Override
	public float damageResist() {
		return 100F;
	}

	@Override
	public int getHeadRotSpeed() {
		return 10;
	}

	@Override
	public int getMaxHeadXRot() {
		return 1;
	}

	@Override
	public int getMaxHeadYRot() {
		return 1;
	}

	@Override
	public EcosystemData getEcosystemData() {
		return getType().getDataOrThrow(AbysmMetatags.ECOSYSTEM_DATA);
	}
}
