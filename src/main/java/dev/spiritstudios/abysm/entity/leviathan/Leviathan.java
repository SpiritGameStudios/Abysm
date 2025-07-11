package dev.spiritstudios.abysm.entity.leviathan;

import dev.spiritstudios.specter.api.entity.EntityPart;
import dev.spiritstudios.specter.api.entity.PartHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Leviathan extends WaterCreatureEntity implements Monster, PartHolder<Leviathan> {

	protected final ServerBossBar bossBar;

	protected Leviathan(EntityType<? extends WaterCreatureEntity> entityType, World world) {
		super(entityType, world);
		this.bossBar = this.createBossBar(entityType, world);
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
			.add(EntityAttributes.ATTACK_DAMAGE, 10);
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

	@NotNull
	protected abstract ServerBossBar createBossBar(EntityType<? extends WaterCreatureEntity> entityType, World world);

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
		if (this.getWorld().isClient) {
			return;
		}
		if (this.isAiDisabled()) {
			return;
		}

		var parts = this.getEntityParts();

		Vec3d delta = Vec3d.ZERO;
		Vec3d pos = this.getPos();
		Entity previousPart = this;
		EntityPart<Leviathan> currentPart;
		for(int i = 0; i < parts.size(); i++) {

			currentPart = parts.get(i);

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

	protected void movePart(EntityPart<Leviathan> entityPart, double dx, double dy, double dz) {
		entityPart.setRelativePos(new Vec3d(dx, dy, dz));
	}

	public float wrapYawChange(double yawDegrees) {
		return (float) MathHelper.wrapDegrees(yawDegrees);
	}
}
