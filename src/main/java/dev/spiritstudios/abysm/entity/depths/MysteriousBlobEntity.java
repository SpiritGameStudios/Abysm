package dev.spiritstudios.abysm.entity.depths;

import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.MoveIntoWaterGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimAroundGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static dev.spiritstudios.abysm.entity.AbstractSchoolingFishEntity.ANIM_CONTROLLER_STRING;

@SuppressWarnings("unused")
public class MysteriousBlobEntity extends WaterCreatureEntity implements GeoEntity {

	protected final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected final ServerBossBar bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.NOTCHED_20);

	protected int inverseHurtTime = 0;
	protected float scaleXZ = 1f;
	protected float prevScaleXZ = 1f;
	protected float targetScaleXZ = 1f;
	protected float targetScaleY = 1f;
	protected float scaleY = 1f;
	protected float prevScaleY = 1f;

	public MysteriousBlobEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		this.moveControl = new ManOWarEntity.GarbageMoveControl(this);
	}

	public static DefaultAttributeContainer.Builder createVaseAttributes() {
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.ATTACK_DAMAGE, Long.MAX_VALUE)
			.add(EntityAttributes.MAX_HEALTH, 1000000)
			.add(EntityAttributes.ARMOR, 10)
			.add(EntityAttributes.ARMOR_TOUGHNESS, 100)
			.add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.5)
			.add(EntityAttributes.LUCK, 1024)
			.add(EntityAttributes.FOLLOW_RANGE, 48)
			.add(EntityAttributes.WATER_MOVEMENT_EFFICIENCY, 1)
			.add(EntityAttributes.MOVEMENT_SPEED, 0.9);
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
	protected EntityNavigation createNavigation(World world) {
		return new SwimNavigation(this, world);
	}

	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new MoveIntoWaterGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.5F, true));
		this.goalSelector.add(2, new SwimAroundGoal(this, 1.0, 10));
		this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(4, new LookAroundGoal(this));
		this.targetSelector.add(1, new RevengeGoal(this, MysteriousBlobEntity.class, Leviathan.class));
	}

	@Override
	public void travel(Vec3d movementInput) {
		if (this.isTouchingWater()) {
			this.updateVelocity(this.getMovementSpeed() * 0.02F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9));
			if (this.getTarget() == null) {
				this.setVelocity(this.getVelocity().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public void tick() {
		super.tick();

		World world = this.getWorld();

		this.inverseHurtTime = this.maxHurtTime - this.hurtTime;

		float sin;
		if (this.inverseHurtTime < this.maxHurtTime) {
			sin = MathHelper.sin(this.inverseHurtTime * MathHelper.TAU * 0.05F) * 0.8F;
		} else if (this.isHappy()) {
			sin = MathHelper.sin(world.getTime() * MathHelper.TAU * 0.005F) * 0.05F;
		} else {
			sin = MathHelper.sin(world.getTime() * MathHelper.TAU * 0.005F) * 0.15F;
		}

		this.targetScaleY = -sin + 1;
		this.targetScaleXZ = sin + 1;

		if (this.scaleXZ != this.targetScaleXZ) {
			this.scaleXZ = MathHelper.lerp(0.15f, this.scaleXZ, this.targetScaleXZ);
		}
		if (this.scaleY != this.targetScaleY) {
			this.scaleY = MathHelper.lerp(0.15f, this.scaleY, this.targetScaleY);
		}

		boolean scaleDirty = false;
		if (this.prevScaleXZ != this.scaleXZ) {
			scaleDirty = true;
			this.prevScaleXZ = this.scaleXZ;
		}
		if (this.prevScaleY != this.scaleY) {
			scaleDirty = true;
			this.prevScaleY = this.scaleY;
		}
		if (scaleDirty) {
			this.calculateDimensions();
		}
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

	public float getPressure() {
		return PressureFinder.getPressure(this.getWorld(), this.getBlockPos());
	}

	public boolean isHappy() {
		return this.getPressure() >= 54f;
	}

	public float getScaleXZ() {
		return this.scaleXZ;
	}

	public float getScaleY() {
		return this.scaleY;
	}

	public float lerpScaleXZ(float delta) {
		return MathHelper.lerp(delta, this.prevScaleXZ, this.scaleXZ);
	}

	public float lerpScaleY(float delta) {
		return MathHelper.lerp(delta, this.prevScaleY, this.scaleY);
	}

	public void setTargetScaleXZ(float scale) {
		this.targetScaleXZ = scale;
	}

	public void setTargetScaleY(float scale) {
		this.targetScaleY = scale;
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
		AnimationController<MysteriousBlobEntity> animController = new AnimationController<>(ANIM_CONTROLLER_STRING, 5, event -> PlayState.STOP);

		registrar.add(animController);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}
}
