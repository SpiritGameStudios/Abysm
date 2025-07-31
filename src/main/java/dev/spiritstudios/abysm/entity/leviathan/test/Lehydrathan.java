package dev.spiritstudios.abysm.entity.leviathan.test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import dev.spiritstudios.abysm.entity.ai.AbysmSensorTypes;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import dev.spiritstudios.specter.api.entity.EntityPart;
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
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
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
import java.util.Optional;

public class Lehydrathan extends Leviathan implements GeoEntity {

	protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Lehydrathan>>> SENSORS = ImmutableList.of(
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

	public final List<EntityPart<Leviathan>> parts;
	public final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	@Override
	public List<EntityPart<Leviathan>> getSpecterEntityParts() {
		return this.parts;
	}

	public Lehydrathan(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		ImmutableList.Builder<EntityPart<Leviathan>> builder = ImmutableList.builder();
		float width = entityType.getWidth();
		float height = entityType.getHeight();
		for (int i = 0; i < 4; i++) {
			LeviathanPart part = new LeviathanPart(this, "body" + i, width, height);
			part.setRelativePos(new Vec3d(0, 0, i + 1));
			builder.add(part);
		}
		this.parts = builder.build();
	}

	@Override
	protected Brain.Profile<Lehydrathan> createBrainProfile() {
		return Brain.createProfile(MEMORY_MODULES, SENSORS);
	}

	@Override
	protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
		return LehydrathanBrain.create(this.createBrainProfile().deserialize(dynamic));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Brain<Lehydrathan> getBrain() {
		return (Brain<Lehydrathan>) super.getBrain();
	}

	@Override
	protected void sendAiDebugData() {
		super.sendAiDebugData();
		DebugInfoSender.sendBrainDebugData(this);
	}

	@Override
	protected void tickPartUpdates() {

		var parts = this.getSpecterEntityParts();

		Vec3d delta;
		Vec3d pos = this.getPos();
		Entity previousPart = this;
		EntityPart<Leviathan> currentPart;
		for (EntityPart<Leviathan> part : parts) {
			currentPart = part;

			final double posX = currentPart.getX();
			final double posY = currentPart.getY();
			final double posZ = currentPart.getZ();

			if (!previousPart.getBoundingBox().intersects(currentPart.getBoundingBox())) {
				// TODO: FIX ME, CREATE MORE NATURAL MOVEMENT
				Vec3d last = new Vec3d(previousPart.lastX, previousPart.lastY, previousPart.lastZ);
				delta = currentPart.getPos().lerp(last, 0.1F).subtract(pos);
				movePart(currentPart, delta.x, delta.y, delta.z);
			}

			currentPart.lastX = posX;
			currentPart.lastY = posY;
			currentPart.lastZ = posZ;
			currentPart.lastRenderX = posX;
			currentPart.lastRenderY = posY;
			currentPart.lastRenderZ = posZ;

			previousPart = currentPart;
		}
	}

	@Override
	protected void mobTick(ServerWorld serverWorld) {
		super.mobTick(serverWorld);
		Profiler profiler = Profilers.get();
		profiler.push("lehydrathanBrain");
		this.getBrain().tick(serverWorld, this);
		profiler.pop();
		profiler.push("lehdrathanActivityUpdate");
		LehydrathanBrain.updateActivities(this);
		profiler.pop();
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public boolean isValidNonPlayerTarget(LivingEntity living) {
		return super.isValidNonPlayerTarget(living) && living.getType().isIn(AbysmEntityTypeTags.LEHYDRATHAN_HUNT_TARGETS);
	}

	public static class LehydrathanBrain {

		protected static Brain<Lehydrathan> create(Brain<Lehydrathan> brain) {
			addCoreActivities(brain);
			addIdleActivities(brain);
			addFightActivities(brain);
			brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
			brain.setDefaultActivity(Activity.IDLE);
			brain.resetPossibleActivities();
			return brain;
		}

		private static void addFightActivities(Brain<Lehydrathan> brain) {
			brain.setTaskList(
				Activity.FIGHT,
				0,
				ImmutableList.of(
					RangedApproachTask.create(LehydrathanBrain::getTargetApproachingSpeed),
					MeleeAttackTask.create(20)
				),
				MemoryModuleType.ATTACK_TARGET
			);
		}

		private static void addCoreActivities(Brain<Lehydrathan> brain) {
			brain.setTaskList(
				Activity.CORE,
				0,
				ImmutableList.of(
					new UpdateLookControlTask(45, 90), new MoveToTargetTask()
				)
			);
		}

		@SuppressWarnings("deprecation")
		private static void addIdleActivities(Brain<Lehydrathan> brain) {
			brain.setTaskList(
				Activity.IDLE,
				ImmutableList.of(
					Pair.of(0, LookAtMobWithIntervalTask.follow(EntityType.PLAYER, 6.0F, UniformIntProvider.create(30, 60))),
					Pair.of(1, UpdateAttackTargetTask.create(LehydrathanBrain::getAttackTarget)),
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
								Pair.of(GoToLookTargetTask.create(LehydrathanBrain::canGoToLookTarget, LehydrathanBrain::returnOneHalf, 3), 3),
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

		private static float returnOneHalf(LivingEntity living) {
			return 0.5F;
		}

		public static void updateActivities(Lehydrathan lehydrathan) {
			Brain<Lehydrathan> brain = lehydrathan.getBrain();
			Activity activity = brain.getFirstPossibleNonCoreActivity().orElse(null);
			brain.resetPossibleActivities(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
			if (activity == Activity.FIGHT && brain.getFirstPossibleNonCoreActivity().orElse(null) != Activity.FIGHT) {
				brain.remember(MemoryModuleType.HAS_HUNTING_COOLDOWN, true, 2400L);
			}
		}

		private static float getTargetApproachingSpeed(LivingEntity entity) {
			return entity.isTouchingWater() ? 0.6F : 0.15F;
		}

		private static Optional<? extends LivingEntity> getAttackTarget(ServerWorld serverWorld, Lehydrathan lehydrathan) {
			return lehydrathan.getBrain().getOptionalRegisteredMemory(MemoryModuleType.NEAREST_ATTACKABLE);
		}
	}
}
