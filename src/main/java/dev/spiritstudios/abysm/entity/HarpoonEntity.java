package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntity extends PersistentProjectileEntity {

	public static final TrackedData<Boolean> RETURNING = DataTracker.registerData(HarpoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public HarpoonEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	protected HarpoonEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world, PlayerEntity owner) {
		super(entityType, world);
		this.setOwner(owner);
		this.setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
		this.setVelocity(owner.getRotationVec(1.0F).multiply(5.0));
	}

	public HarpoonEntity(World world, PlayerEntity owner) {
		this(AbysmEntityTypes.FLYING_HARPOON, world, owner);
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return AbysmItems.NOOPRAH.getDefaultStack();
	}

	@Nullable
	public PlayerEntity getPlayer() {
		return this.getOwner() instanceof PlayerEntity player ? player : null;
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(RETURNING, false);
	}

	@Override
	public void tick() {
		PlayerEntity owner = this.getPlayer();
		if (owner == null || !owner.isAlive()) {
			this.discard();
			super.tick();
			return;
		}
		if (this.inGroundTime > 2 || this.squaredDistanceTo(owner) > 65536) {
			this.beginReturn();
		}
		if (this.isNoClip() && this.isReturning()) {
			boolean closeEnough = this.squaredDistanceTo(owner) < 10;
			this.setVelocity(owner.getEyePos().subtract(this.getPos()).normalize().multiply(closeEnough ? 1 : 5));
		}
		super.tick();
	}

	@Override
	protected float getDragInWater() {
		return 1F;
	}

	public boolean isReturning() {
		return this.dataTracker.get(RETURNING);
	}

	protected void beginReturn() {
		this.setNoClip(true);
		this.dataTracker.set(RETURNING, true);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		this.beginReturn();
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
	}

	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}

	public static float updateHarpoonRotation(float lastRot, float newRot) {
		while (newRot - lastRot < -180.0F) {
			lastRot -= 360.0F;
		}

		while (newRot - lastRot >= 180.0F) {
			lastRot += 360.0F;
		}

		return MathHelper.lerp(0.8F, lastRot, newRot);
	}
}
