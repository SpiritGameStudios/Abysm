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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class MysteriousBlobEntity extends WaterCreatureEntity {

	protected final ServerBossBar bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.NOTCHED_6);

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
		return this.getPressure() >= 0.61f;
	}
}
