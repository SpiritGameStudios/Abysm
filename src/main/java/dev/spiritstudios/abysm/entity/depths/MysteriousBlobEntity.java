package dev.spiritstudios.abysm.entity.depths;

import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class MysteriousBlobEntity extends WaterCreatureEntity {

	protected final ServerBossBar bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.NOTCHED_6);

	protected float scaleXZ = 1f;
	protected float prevScaleXZ = 1f;
	protected float targetScaleXZ = 1f;
	protected float targetScaleY = 1f;
	protected float scaleY = 1f;
	protected float prevScaleY = 1f;

	public MysteriousBlobEntity(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
	}

	public static DefaultAttributeContainer.Builder createVaseAttributes() {
		return MobEntity.createMobAttributes()
			.add(EntityAttributes.ATTACK_DAMAGE, Long.MAX_VALUE)
			.add(EntityAttributes.MAX_HEALTH, 1000000)
			.add(EntityAttributes.ARMOR, 10)
			.add(EntityAttributes.ARMOR_TOUGHNESS, 100)
			.add(EntityAttributes.KNOCKBACK_RESISTANCE, 0.5)
			.add(EntityAttributes.LUCK, 10)
			.add(EntityAttributes.FOLLOW_RANGE, 32)
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
	public void tick() {
		super.tick();

		World world = this.getWorld();

		if (this.isHappy()) {
			this.targetScaleY = 1;
			this.targetScaleXZ = 1;
		} else {
			float woah = world.getTime() * 0.1f;
			this.targetScaleY = MathHelper.sin(woah);
			this.targetScaleXZ = MathHelper.cos(woah);
		}

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
}
