package dev.spiritstudios.abysm.entity.leviathan;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import dev.spiritstudios.specter.api.entity.EntityPart;
import dev.spiritstudios.specter.api.entity.PartHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestLivingEntitiesSensor;
import net.minecraft.entity.ai.control.AquaticMoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public abstract class Leviathan extends WaterCreatureEntity implements Monster, PartHolder<Leviathan> {

	protected final ServerBossBar bossBar;

	protected Leviathan(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = this.createBossBar(entityType, world);
		this.moveControl = new AquaticMoveControl(this, 85, 10, 0.1F, 0.5F, false);
		this.lookControl = new YawAdjustingLookControl(this, 20);
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new SwimNavigation(this, world);
	}

	@SuppressWarnings("unused")
	public boolean damagePart(ServerWorld world, LeviathanPart part, DamageSource source, float amount) {
		return this.damage(world, source, amount);
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		return super.damage(world, source, source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) ? amount : Math.min(1f, amount));
	}

	public static DefaultAttributeContainer.Builder createLeviathanAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 32768)
			.add(EntityAttributes.ARMOR, 100)
			.add(EntityAttributes.ARMOR_TOUGHNESS, 100)
			.add(EntityAttributes.ATTACK_DAMAGE, 10)
			.add(EntityAttributes.MOVEMENT_SPEED, 1.1);
	}

	@Override
	public void heal(float amount) {
		if (!Float.isFinite(amount)) {
			amount = Float.MAX_VALUE;
		}
		super.heal(Math.max(0, amount));
	}

	@Override
	protected boolean canStartRiding(Entity entity) {
		return false;
	}

	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		var parts = this.getEntityParts();

		for(int i = 0; i < parts.size(); ++i) {
			parts.get(i).setId(i + packet.getEntityId() + 1);
		}
	}

	@SuppressWarnings("unused")
	protected @NotNull ServerBossBar createBossBar(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		return new ServerBossBar(this.getDisplayName(), BossBar.Color.PURPLE, BossBar.Style.PROGRESS);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		if (this.hasCustomName()) {
			this.bossBar.setName(this.getDisplayName());
		}
	}

	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		this.bossBar.setName(this.getDisplayName());
	}

	@Override
	protected void mobTick(ServerWorld world) {
		super.mobTick(world);
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		if (this.isAiDisabled()) {
			return;
		}
		this.tickPartUpdates();
	}

	protected abstract void tickPartUpdates();

	protected void movePart(EntityPart<Leviathan> entityPart, double dx, double dy, double dz) {
		entityPart.setRelativePos(new Vec3d(dx, dy, dz));
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.isTouchingWater()) {
			this.updateVelocity(this.getMovementSpeed(), movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
		} else {
			super.travel(movementInput);
		}
	}

	public boolean isValidTarget(@Nullable Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return false;
		}
		World world = this.getWorld();
		return world == living.getWorld() &&
			EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(living) &&
			!this.isTeammate(living) &&
			living.getType() != EntityType.ARMOR_STAND &&
			!(living instanceof Leviathan) &&
			!living.isInvulnerable() &&
			!living.isDead() &&
			world.getWorldBorder().contains(living.getBoundingBox());
	}


	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return living.getType() != EntityType.PLAYER;
	}

	public static class AttackablesSensor extends NearestLivingEntitiesSensor<Leviathan> {
		@Override
		public Set<MemoryModuleType<?>> getOutputMemoryModules() {
			return ImmutableSet.copyOf(Iterables.concat(super.getOutputMemoryModules(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
		}

		protected void sense(ServerWorld serverWorld, Leviathan leviathan) {
			super.sense(serverWorld, leviathan);
			findNearestTarget(leviathan, living -> living.getType() == EntityType.PLAYER)
				.or(() -> findNearestTarget(leviathan, leviathan::isValidNonPlayerTarget))
				.ifPresentOrElse(
					living -> leviathan.getBrain().remember(MemoryModuleType.NEAREST_ATTACKABLE, living),
					() -> leviathan.getBrain().forget(MemoryModuleType.NEAREST_ATTACKABLE)
				);
		}

		private static Optional<LivingEntity> findNearestTarget(Leviathan leviathan, Predicate<LivingEntity> targetPredicate) {
			return leviathan.getBrain()
				.getOptionalRegisteredMemory(MemoryModuleType.MOBS)
				.stream()
				.flatMap(Collection::stream)
				.filter(leviathan::isValidTarget)
				.filter(targetPredicate)
				.findFirst();
		}
	}
}
