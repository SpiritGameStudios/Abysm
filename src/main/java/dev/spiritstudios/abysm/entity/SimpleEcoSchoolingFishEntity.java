package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.ecosystem.entity.EcologicalEntity;
import dev.spiritstudios.abysm.ecosystem.entity.EcosystemLogic;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.FleePredatorsGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.HuntPreyGoal;
import dev.spiritstudios.abysm.entity.ai.goal.ecosystem.RepopulateGoal;
import dev.spiritstudios.abysm.mixin.ecosystem.goal.AbstractFishAccessor;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class SimpleEcoSchoolingFishEntity extends AbstractSchoolingFish implements GeoEntity, EcologicalEntity {
	public static final String ANIM_CONTROLLER_STRING = "default";
	protected EcosystemLogic ecosystemLogic;
	public final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

	public SimpleEcoSchoolingFishEntity(EntityType<? extends AbstractSchoolingFish> entityType, Level world) {
		super(entityType, world);
		this.ecosystemLogic = createEcosystemLogic(this);
	}

	@Override
	public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData entityData) {
		this.alertEcosystemOfSpawn();
		return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
	}

	@Override
	public void tick() {
		super.tick();
		this.tickEcosystemLogic();
	}

	@Override
	public void onRemoval(RemovalReason reason) {
		this.alertEcosystemOfDeath();
		super.onRemoval(reason);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
		this.goalSelector.addGoal(0, new PanicGoal(this, 1.25));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6, 1.4, EntitySelector.NO_SPECTATORS::test));
		this.goalSelector.addGoal(1, new FleePredatorsGoal(this, 10.0F, 1.1, 1.2));
		this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0, false));
		this.targetSelector.addGoal(1, new HuntPreyGoal(this, false));
		this.goalSelector.addGoal(2, new RepopulateGoal(this, 1.25));
	}

	public static AttributeSupplier.Builder createPredatoryFishAttributes() {
		return createAttributes().add(Attributes.ATTACK_DAMAGE);
	}

	@Override
	public void travel(Vec3 movementInput) {
		if (this.isInWater()) {
			this.moveRelative(this.getSpeed() * this.mvmSpdMul(), movementInput);
			this.move(MoverType.SELF, this.getDeltaMovement());
			this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
			if (this.getTarget() == null) {
				this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
			}
		} else {
			super.travel(movementInput);
		}
	}

	public float mvmSpdMul() {
		return 0.02F;
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.geoCache;
	}

	@Override
	public EcosystemLogic getEcosystemLogic() {
		return ecosystemLogic;
	}

	public static class SwimToRandomPlaceGoal extends RandomSwimmingGoal {
		private final AbstractFish fish;

		public SwimToRandomPlaceGoal(AbstractFish fish, float speed) {
			super(fish,  speed, 40);
			this.fish = fish;
		}

		@Override
		public boolean canUse() {
			return ((AbstractFishAccessor)fish).invokeCanRandomSwim() && super.canUse();
		}
	}
}
