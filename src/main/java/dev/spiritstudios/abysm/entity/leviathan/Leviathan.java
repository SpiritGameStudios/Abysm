package dev.spiritstudios.abysm.entity.leviathan;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import dev.spiritstudios.specter.api.entity.EntityPart;
import dev.spiritstudios.specter.api.entity.PartHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.sensing.NearestLivingEntitySensor;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec3;

public abstract class Leviathan extends WaterAnimal implements Enemy, PartHolder<Leviathan> {

	protected final ServerBossEvent bossBar;

	protected Leviathan(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
		this.bossBar = this.createBossBar(entityType, world);
		this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.1F, 0.5F, false);
		this.lookControl = new SmoothSwimmingLookControl(this, 20);
	}

	@Override
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@SuppressWarnings("unused")
	public boolean damagePart(ServerLevel world, LeviathanPart part, DamageSource source, float amount) {
		return this.hurtServer(world, source, amount);
	}

	@Override
	public boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		return super.hurtServer(world, source, source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) ? amount : Math.min(this.damageResist(), amount));
	}

	public float damageResist() {
		return 1;
	}

	public static AttributeSupplier.Builder createLeviathanAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 32768)
			.add(Attributes.ARMOR, 100)
			.add(Attributes.ARMOR_TOUGHNESS, 100)
			.add(Attributes.ATTACK_DAMAGE, 10)
			.add(Attributes.MOVEMENT_SPEED, 1.1);
	}

	@Override
	public void heal(float amount) {
		if (!Float.isFinite(amount)) {
			amount = Float.MAX_VALUE;
		}
		super.heal(Math.max(0, amount));
	}

	@Override
	protected boolean canRide(Entity entity) {
		return false;
	}

	@Override
	public boolean canUsePortal(boolean allowVehicles) {
		return false;
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		var parts = this.getSpecterEntityParts();

		for (int i = 0; i < parts.size(); ++i) {
			parts.get(i).setId(i + packet.getId() + 1);
		}
	}

	@SuppressWarnings("unused")
	protected @NotNull ServerBossEvent createBossBar(EntityType<? extends WaterAnimal> entityType, Level world) {
		return new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS);
	}

	@Override
	protected void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);

		if (this.hasCustomName()) {
			this.bossBar.setName(this.getDisplayName());
		}
	}

	@Override
	public void setCustomName(@Nullable Component name) {
		super.setCustomName(name);
		this.bossBar.setName(this.getDisplayName());
	}

	@Override
	protected void customServerAiStep(ServerLevel world) {
		super.customServerAiStep(world);
		this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public void startSeenByPlayer(ServerPlayer player) {
		super.startSeenByPlayer(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void stopSeenByPlayer(ServerPlayer player) {
		super.stopSeenByPlayer(player);
		this.bossBar.removePlayer(player);
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.isNoAi()) {
			return;
		}
		this.tickPartUpdates();
	}

	@Override
	public abstract List<? extends LeviathanPart> getSpecterEntityParts();

	protected abstract void tickPartUpdates();

	protected void movePart(EntityPart<Leviathan> part, double dx, double dy, double dz) {
		part.setRelativePos(new Vec3(dx, dy, dz));
	}

	protected void updatePartLastPos(EntityPart<Leviathan> part, double x, double y, double z) {
		part.xo = x;
		part.yo = y;
		part.zo = z;
		part.xOld = x;
		part.yOld = y;
		part.zOld = z;
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (this.isInWater()) {
			this.moveRelative(this.getSpeed(), movementInput);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
		} else {
			super.travel(movementInput);
		}
	}

	public boolean isValidTarget(@Nullable Entity entity) {
		if (!(entity instanceof LivingEntity living)) {
			return false;
		}
		Level world = this.level();
		return world == living.level() &&
			EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(living) &&
			!this.isAlliedTo(living) &&
			living.getType() != EntityType.ARMOR_STAND &&
			!(living instanceof Leviathan) &&
			!living.isInvulnerable() &&
			!living.isDeadOrDying() &&
			world.getWorldBorder().isWithinBounds(living.getBoundingBox());
	}


	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return living.getType() != EntityType.PLAYER;
	}

	protected Optional<LivingEntity> findNearestTarget(Predicate<LivingEntity> targetPredicate) {
		return this.getBrain()
			.getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES)
			.stream()
			.flatMap(Collection::stream)
			.filter(this::isValidTarget)
			.filter(targetPredicate)
			.findFirst();
	}

	protected Optional<LivingEntity> findNearestTarget(EntityType<? extends LivingEntity> entityType) {
		return findNearestTarget(living -> living.getType() == entityType);
	}

	protected void onTargetFound(LivingEntity living) {
		this.getBrain().setMemory(MemoryModuleType.NEAREST_ATTACKABLE, living);
	}

	public static class AttackablesSensor extends NearestLivingEntitySensor<Leviathan> {
		@Override
		public Set<MemoryModuleType<?>> requires() {
			return ImmutableSet.copyOf(Iterables.concat(super.requires(), List.of(MemoryModuleType.NEAREST_ATTACKABLE)));
		}

		protected void sense(ServerLevel serverWorld, Leviathan leviathan) {
			super.doTick(serverWorld, leviathan);
			leviathan.findNearestTarget(EntityType.PLAYER)
				.or(() -> leviathan.findNearestTarget(leviathan::isValidNonPlayerTarget))
				.ifPresentOrElse(
					leviathan::onTargetFound,
					() -> leviathan.getBrain().eraseMemory(MemoryModuleType.NEAREST_ATTACKABLE)
				);
		}
	}
}
