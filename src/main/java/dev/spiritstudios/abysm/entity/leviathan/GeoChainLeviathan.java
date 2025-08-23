package dev.spiritstudios.abysm.entity.leviathan;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.spiritstudios.abysm.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.entity.leviathan.pseudo.SkeletonSharkEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.boss.dragon.EnderDragonFrameTracker;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.profiler.Profilers;
import net.minecraft.world.World;
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
		MemoryModuleType.MOBS,
		MemoryModuleType.VISIBLE_MOBS,
		MemoryModuleType.NEAREST_VISIBLE_PLAYER,
		MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
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
	public final EnderDragonFrameTracker frameTracker = new EnderDragonFrameTracker();

	public GeoChainLeviathan(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected Brain.Profile<GeoChainLeviathan> createBrainProfile() {
		return Brain.createProfile(MEMORY_MODULES, SENSORS);
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return LeviathanBrain.create(this.createBrainProfile().deserialize(dynamic));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Brain<GeoChainLeviathan> getBrain() {
		return (Brain<GeoChainLeviathan>) super.getBrain();
	}

	@Override
	protected void sendAiDebugData() {
		super.sendAiDebugData();
		DebugInfoSender.sendBrainDebugData(this);
	}

	@Override
	protected void tickPartUpdates() {
		List<? extends LeviathanPart> parts = this.getTailParts();
		this.frameTracker.tick(this.getY(), this.getYaw());

		this.setYaw(wrapYaw(this.getYaw()));

		this.bodyYaw = this.getYaw();

		EnderDragonFrameTracker.Frame frame = this.frameTracker.getFrame(5);

		// I have no idea what to call this variable - Sky
		float r = (float)(this.frameTracker.getFrame(5).y() - this.frameTracker.getFrame(10).y()) * 10.0F * MathHelper.RADIANS_PER_DEGREE;
		float sinR = MathHelper.cos(r);
		float cosR = MathHelper.sin(r);
		float yawRad = this.getYaw() * MathHelper.RADIANS_PER_DEGREE;
		float sinYaw = MathHelper.sin(yawRad);
		float cosYaw = MathHelper.cos(yawRad);

		for (int index = 0; index < parts.size(); index++) {
			LeviathanPart leviathanPart = parts.get(index);

			final double originalPartX = leviathanPart.getX();
			final double originalPartY = leviathanPart.getY();
			final double originalPartZ = leviathanPart.getZ();

			EnderDragonFrameTracker.Frame frame2 = this.frameTracker.getFrame(12 + index * 2);
			float changedYaw = (
					this.getYaw() + this.wrapYaw(frame2.yRot() - frame.yRot())
				) * MathHelper.RADIANS_PER_DEGREE;
			float sinNewYaw = MathHelper.sin(changedYaw);
			float cosNewYaw = MathHelper.cos(changedYaw);
			float distanceToMain = this.getDistanceToMainBody(leviathanPart);
			float perPart = (index + 1) * leviathanPart.getWidth();

			double dx = (sinYaw * distanceToMain + sinNewYaw * perPart) * sinR;
			double dy = frame2.y() - frame.y() - (perPart + distanceToMain) * cosR;
			double dz = -(cosYaw * distanceToMain + cosNewYaw * perPart) * sinR;
			this.movePart(leviathanPart, dx, dy, dz);

			if (this instanceof SkeletonSharkEntity shark && Objects.equals(leviathanPart.name, "body")) {
				Vec3d vec3d = new Vec3d(dz, 0 , -dx).normalize().multiply(0.7);
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
		return (float) MathHelper.wrapDegrees(yawDeg);
	}

	@Override
	public int getMaxLookYawChange() {
		return 1;
	}

	@Override
	protected void mobTick(ServerWorld serverWorld) {
		super.mobTick(serverWorld);
		Profiler profiler = Profilers.get();
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
			brain.resetPossibleActivities();
			return brain;
		}

		private static void addFightActivities(Brain<GeoChainLeviathan> brain) {
			brain.setTaskList(
				Activity.FIGHT,
				0,
				ImmutableList.of(
					RangedApproachTask.create(LeviathanBrain::getTargetApproachingSpeed),
					MeleeAttackTask.create(20)
				),
				MemoryModuleType.ATTACK_TARGET
			);
		}

		private static void addCoreActivities(Brain<GeoChainLeviathan> brain) {
			brain.setTaskList(
				Activity.CORE,
				0,
				ImmutableList.of(
					new UpdateLookControlTask(45, 90), new MoveToTargetTask()
				)
			);
		}

		@SuppressWarnings("deprecation")
		private static void addIdleActivities(Brain<GeoChainLeviathan> brain) {
			brain.setTaskList(
				Activity.IDLE,
				ImmutableList.of(
					Pair.of(0, LookAtMobWithIntervalTask.follow(EntityType.PLAYER, 6.0F, UniformIntProvider.create(30, 60))),
					Pair.of(1, UpdateAttackTargetTask.create(LeviathanBrain::getAttackTarget)),
					Pair.of(1, SeekWaterTask.create(6, 0.15F)),
					Pair.of(
						2,
						new CompositeTask<>(
							ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryModuleState.VALUE_ABSENT),
							ImmutableSet.of(),
							CompositeTask.Order.ORDERED,
							CompositeTask.RunMode.TRY_ALL,
							ImmutableList.of(
								Pair.of(StrollTask.createDynamicRadius(0.5F), 2),
								Pair.of(StrollTask.create(0.15F, false), 2),
								Pair.of(GoToLookTargetTask.create(LeviathanBrain::canGoToLookTarget, living -> 0.5F, 3), 3),
								Pair.of(TaskTriggerer.predicate(Entity::isTouchingWater), 5),
								Pair.of(TaskTriggerer.predicate(Entity::isOnGround), 5)
							)
						)
					)
				)
			);
		}

		private static boolean canGoToLookTarget(LivingEntity entity) {
			World world = entity.getWorld();
			Optional<LookTarget> optional = entity.getBrain().getOptionalRegisteredMemory(MemoryModuleType.LOOK_TARGET);
			if (optional.isEmpty()) {
				return false;
			}
			BlockPos blockPos = optional.get().getBlockPos();
			return world.isWater(blockPos) == entity.isTouchingWater();
		}

		public static void updateActivities(GeoChainLeviathan leviathan) {
			Brain<GeoChainLeviathan> brain = leviathan.getBrain();
			Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);
			brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
			if (activity == Activity.FIGHT && brain.getFirstPossibleNonCoreActivity().orElse(null) != Activity.FIGHT) {
				brain.remember(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
			}
		}

		private static float getTargetApproachingSpeed(LivingEntity entity) {
			return entity.isTouchingWater() ? 0.6F : 0.15F;
		}

		private static Optional<? extends LivingEntity> getAttackTarget(ServerWorld serverWorld, GeoChainLeviathan leviathan) {
			return leviathan.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_ATTACKABLE);
		}
	}
}
