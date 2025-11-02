package dev.spiritstudios.abysm.entity.leviathan;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.spiritstudios.abysm.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.entity.leviathan.pseudo.SkeletonSharkEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.GateBehavior;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MeleeAttack;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.PositionTracker;
import net.minecraft.world.entity.ai.behavior.RandomStroll;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.behavior.TryFindWater;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.DragonFlightHistory;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class GeoChainLeviathan extends Leviathan implements GeoEntity {

	protected static final ImmutableList<? extends SensorType<? extends Sensor<? super GeoChainLeviathan>>> SENSORS = ImmutableList.of(
		SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, AbysmSensorTypes.LEVIATHAN_ATTACKABLES
	);
	protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(
		MemoryModuleType.NEAREST_LIVING_ENTITIES,
		MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
		MemoryModuleType.NEAREST_VISIBLE_PLAYER,
		MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER,
		MemoryModuleType.LOOK_TARGET,
		MemoryModuleType.WALK_TARGET,
		MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
		MemoryModuleType.PATH,
		MemoryModuleType.ATTACK_TARGET,
		MemoryModuleType.ATTACK_COOLING_DOWN,
		MemoryModuleType.HURT_BY_ENTITY,
		MemoryModuleType.NEAREST_ATTACKABLE,
		MemoryModuleType.HAS_HUNTING_COOLDOWN
	);

	public final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	public final DragonFlightHistory frameTracker = new DragonFlightHistory();

	public GeoChainLeviathan(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected Brain.Provider<GeoChainLeviathan> brainProvider() {
		return Brain.provider(MEMORY_MODULES, SENSORS);
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		return LeviathanBrain.create(this.brainProvider().makeBrain(dynamic));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Brain<GeoChainLeviathan> getBrain() {
		return (Brain<GeoChainLeviathan>) super.getBrain();
	}

	@Override
	protected void sendDebugPackets() {
		super.sendDebugPackets();
		DebugPackets.sendEntityBrain(this);
	}

	@Override
	protected void tickPartUpdates() {
		List<? extends LeviathanPart> parts = this.getTailParts();
		this.frameTracker.record(this.getY(), this.getYRot());

		this.setYRot(wrapYaw(this.getYRot()));

		this.yBodyRot = this.getYRot();

		DragonFlightHistory.Sample frame = this.frameTracker.get(5);

		// I have no idea what to call this variable - Sky
		float r = (float)(this.frameTracker.get(5).y() - this.frameTracker.get(10).y()) * 10.0F * Mth.DEG_TO_RAD;
		float sinR = Mth.cos(r);
		float cosR = Mth.sin(r);
		float yawRad = this.getYRot() * Mth.DEG_TO_RAD;
		float sinYaw = Mth.sin(yawRad);
		float cosYaw = Mth.cos(yawRad);

		for (int index = 0; index < parts.size(); index++) {
			LeviathanPart leviathanPart = parts.get(index);

			final double originalPartX = leviathanPart.getX();
			final double originalPartY = leviathanPart.getY();
			final double originalPartZ = leviathanPart.getZ();

			DragonFlightHistory.Sample frame2 = this.frameTracker.get(12 + index * 2);
			float changedYaw = (
					this.getYRot() + this.wrapYaw(frame2.yRot() - frame.yRot())
				) * Mth.DEG_TO_RAD;
			float sinNewYaw = Mth.sin(changedYaw);
			float cosNewYaw = Mth.cos(changedYaw);
			float distanceToMain = this.getDistanceToMainBody(leviathanPart);
			float perPart = (index + 1) * leviathanPart.getBbWidth();

			double dx = (sinYaw * distanceToMain + sinNewYaw * perPart) * sinR;
			double dy = frame2.y() - frame.y() - (perPart + distanceToMain) * cosR;
			double dz = -(cosYaw * distanceToMain + cosNewYaw * perPart) * sinR;
			this.movePart(leviathanPart, dx, dy, dz);

			if (this instanceof SkeletonSharkEntity shark && Objects.equals(leviathanPart.name, "body")) {
				Vec3 vec3d = new Vec3(dz, 0 , -dx).normalize().scale(0.7);
				double finY = dy - 0.3;

				final double rX = shark.rfin.getX();
				final double rY = shark.rfin.getY();
				final double rZ = shark.rfin.getZ();

				final double lX = shark.lfin.getX();
				final double lY = shark.lfin.getY();
				final double lZ = shark.lfin.getZ();

				this.movePart(shark.rfin, dx + vec3d.x, finY, dz + vec3d.z);
				this.movePart(shark.lfin, dx - vec3d.x, finY, dz - vec3d.z);

				this.updatePartLastPos(shark.rfin, rX, rY, rZ);
				this.updatePartLastPos(shark.lfin, lX, lY, lZ);
			}

			this.updatePartLastPos(leviathanPart, originalPartX, originalPartY, originalPartZ);
		}
	}

	public List<? extends LeviathanPart> getTailParts() {
		return this.getSpecterEntityParts();
	}

	public float getDistanceToMainBody(LeviathanPart leviathanPart) {
		return 0.5F;
	}

	protected float wrapYaw(double yawDeg) {
		return (float) Mth.wrapDegrees(yawDeg);
	}

	@Override
	public int getHeadRotSpeed() {
		return 1;
	}

	@Override
	protected void customServerAiStep(ServerLevel serverWorld) {
		super.customServerAiStep(serverWorld);
		ProfilerFiller profiler = Profiler.get();
		profiler.push("leviathanBrain");
		this.getBrain().tick(serverWorld, this);
		profiler.pop();
		profiler.push("leviathanActivityUpdate");
		LeviathanBrain.updateActivities(this);
		profiler.pop();
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	public static class LeviathanBrain {

		protected static Brain<GeoChainLeviathan> create(Brain<GeoChainLeviathan> brain) {
			addCoreActivities(brain);
			addIdleActivities(brain);
			addFightActivities(brain);
			brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
			brain.setDefaultActivity(Activity.IDLE);
			brain.useDefaultActivity();
			return brain;
		}

		private static void addFightActivities(Brain<GeoChainLeviathan> brain) {
			brain.addActivityAndRemoveMemoryWhenStopped(
				Activity.FIGHT,
				0,
				ImmutableList.of(
					SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(LeviathanBrain::getTargetApproachingSpeed),
					MeleeAttack.create(20)
				),
				MemoryModuleType.ATTACK_TARGET
			);
		}

		private static void addCoreActivities(Brain<GeoChainLeviathan> brain) {
			brain.addActivity(
				Activity.CORE,
				0,
				ImmutableList.of(
					new LookAtTargetSink(45, 90), new MoveToTargetSink()
				)
			);
		}

		@SuppressWarnings("deprecation")
		private static void addIdleActivities(Brain<GeoChainLeviathan> brain) {
			brain.addActivity(
				Activity.IDLE,
				ImmutableList.of(
					Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
					Pair.of(1, StartAttacking.create(LeviathanBrain::getAttackTarget)),
					Pair.of(1, TryFindWater.create(6, 0.15F)),
					Pair.of(
						2,
						new GateBehavior<>(
							ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
							ImmutableSet.of(),
							GateBehavior.OrderPolicy.ORDERED,
							GateBehavior.RunningPolicy.TRY_ALL,
							ImmutableList.of(
								Pair.of(RandomStroll.swim(0.5F), 2),
								Pair.of(RandomStroll.stroll(0.15F, false), 2),
								Pair.of(SetWalkTargetFromLookTarget.create(LeviathanBrain::canGoToLookTarget, living -> 0.5F, 3), 3),
								Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), 5),
								Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), 5)
							)
						)
					)
				)
			);
		}

		private static boolean canGoToLookTarget(LivingEntity entity) {
			Level world = entity.level();
			Optional<PositionTracker> optional = entity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
			if (optional.isEmpty()) {
				return false;
			}
			BlockPos blockPos = optional.get().currentBlockPosition();
			return world.isWaterAt(blockPos) == entity.isInWater();
		}

		public static void updateActivities(GeoChainLeviathan leviathan) {
			Brain<GeoChainLeviathan> brain = leviathan.getBrain();
			Activity activity = brain.getActiveNonCoreActivity().orElse(null);
			brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
			if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse(null) != Activity.FIGHT) {
				brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
			}
		}

		private static float getTargetApproachingSpeed(LivingEntity entity) {
			return entity.isInWater() ? 0.6F : 0.15F;
		}

		private static Optional<? extends LivingEntity> getAttackTarget(ServerLevel serverWorld, GeoChainLeviathan leviathan) {
			return leviathan.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
		}
	}
}
