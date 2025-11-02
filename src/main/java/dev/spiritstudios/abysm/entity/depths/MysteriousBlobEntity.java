package dev.spiritstudios.abysm.entity.depths;

import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import static dev.spiritstudios.abysm.entity.SimpleEcoSchoolingFishEntity.ANIM_CONTROLLER_STRING;

@SuppressWarnings("unused")
public class MysteriousBlobEntity extends WaterAnimal implements GeoEntity {

	protected final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
	protected final ServerBossEvent bossBar = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_20);

	protected int inverseHurtTime = 0;
	protected float scaleXZ = 1f;
	protected float prevScaleXZ = 1f;
	protected float targetScaleXZ = 1f;
	protected float targetScaleY = 1f;
	protected float scaleY = 1f;
	protected float prevScaleY = 1f;

	public MysteriousBlobEntity(EntityType<? extends WaterAnimal> entityType, Level world) {
		super(entityType, world);
		this.moveControl = new ManOWarEntity.GarbageMoveControl(this);
	}

	public static AttributeSupplier.Builder createVaseAttributes() {
		return Mob.createMobAttributes()
			.add(Attributes.ATTACK_DAMAGE, Long.MAX_VALUE)
			.add(Attributes.MAX_HEALTH, 1000000)
			.add(Attributes.ARMOR, 10)
			.add(Attributes.ARMOR_TOUGHNESS, 100)
			.add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
			.add(Attributes.LUCK, 1024)
			.add(Attributes.FOLLOW_RANGE, 48)
			.add(Attributes.WATER_MOVEMENT_EFFICIENCY, 1)
			.add(Attributes.MOVEMENT_SPEED, 0.9);
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
	protected PathNavigation createNavigation(Level world) {
		return new WaterBoundPathNavigation(this, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5F, true));
		this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0, 10));
		this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new HurtByTargetGoal(this, MysteriousBlobEntity.class, Leviathan.class));
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (this.isInWater()) {
			this.moveRelative(this.getSpeed() * 0.02F, movementInput);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	@Override
	public void tick() {
		super.tick();

		Level world = this.level();

		this.inverseHurtTime = this.hurtDuration - this.hurtTime;

		float sin;
		if (this.inverseHurtTime < this.hurtDuration) {
			sin = Mth.sin(this.inverseHurtTime * Mth.TWO_PI * 0.05F) * 0.8F;
		} else if (this.isHappy()) {
			sin = Mth.sin(world.getGameTime() * Mth.TWO_PI * 0.005F) * 0.05F;
		} else {
			sin = Mth.sin(world.getGameTime() * Mth.TWO_PI * 0.005F) * 0.15F;
		}

		this.targetScaleY = -sin + 1;
		this.targetScaleXZ = sin + 1;

		if (this.scaleXZ != this.targetScaleXZ) {
			this.scaleXZ = Mth.lerp(0.15f, this.scaleXZ, this.targetScaleXZ);
		}
		if (this.scaleY != this.targetScaleY) {
			this.scaleY = Mth.lerp(0.15f, this.scaleY, this.targetScaleY);
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
			this.refreshDimensions();
		}
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

	public float getPressure() {
		return PressureFinder.getPressure(this.level(), this.blockPosition());
	}

	public boolean isHappy() {
		return this.getPressure() >= 0.25f;
	}

	public float getScaleXZ() {
		return this.scaleXZ;
	}

	public float getScaleY() {
		return this.scaleY;
	}

	public float lerpScaleXZ(float delta) {
		return Mth.lerp(delta, this.prevScaleXZ, this.scaleXZ);
	}

	public float lerpScaleY(float delta) {
		return Mth.lerp(delta, this.prevScaleY, this.scaleY);
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
