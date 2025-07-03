package dev.spiritstudios.abysm.entity.harpoon;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.mixin.harpoon.PersistentProjectileEntityAccessor;
import dev.spiritstudios.abysm.registry.AbysmDamageTypes;
import dev.spiritstudios.abysm.registry.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmItems;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ProjectileDeflection;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntity extends PersistentProjectileEntity {

	public static final float VELOCITY_POWER = 3.5f;
	public static final TrackedData<Boolean> RETURNING = DataTracker.registerData(HarpoonEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public static final SoundEvent CHAIN_SOUND = Blocks.CHAIN.getDefaultState().getSoundGroup().getFallSound();

	protected int slot = -1;
	protected int ticksAlive = 0;

	protected boolean haul = false;
	protected boolean blessed = false;
	protected boolean grappling = false;

	public HarpoonEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	protected HarpoonEntity(EntityType<? extends PersistentProjectileEntity> entityType, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
		super(entityType, x, y, z, world, stack, weapon);
	}

	public HarpoonEntity(World world, PlayerEntity owner, int slot, ItemStack weapon) {
		this(AbysmEntityTypes.FLYING_HARPOON, owner.getX(), owner.getEyeY() - 0.1, owner.getZ(), world, ItemStack.EMPTY, weapon);
		this.setOwner(owner);
		Vec3d velocity = owner.getRotationVec(1.0F).multiply(VELOCITY_POWER);
		this.setVelocity(velocity);
		this.setNoGravity(true);
		this.slot = slot;
		if (weapon != null) {
			this.haul = AbysmEnchantments.hasEnchantment(weapon, world, AbysmEnchantments.HAUL);
			this.grappling = AbysmEnchantments.hasEnchantment(weapon, world, AbysmEnchantments.GRAPPLING);
			this.blessed = weapon.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY).isBlessed();
		}

		double d = velocity.horizontalLength();

		this.setYaw((float)(MathHelper.atan2(velocity.x, velocity.z) * MathHelper.DEGREES_PER_RADIAN));
		this.setPitch((float)(MathHelper.atan2(velocity.y, d) * MathHelper.DEGREES_PER_RADIAN));

		this.lastYaw = this.getYaw();
		this.lastPitch = this.getPitch();
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return ItemStack.EMPTY;
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
		ticksAlive++;
		PlayerEntity owner = this.getPlayer();
		if (owner == null || !owner.isAlive()) {
			this.discard();
			return;
		}
		World world = this.getWorld();
		if (!world.isClient()) {
			if (this.inGroundTime <= 0 && this.age % 4 == 0) {
				this.playSound(CHAIN_SOUND, 10.0F, 1.05F);
			}
			try {
				ItemStack invStack = owner.getInventory().getStack(this.slot);
				if (!invStack.isOf(AbysmItems.NOOPRAH) || invStack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY).loaded()) {
					this.discard();
				}
			} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
				Abysm.LOGGER.debug("An error occurred while ticking a harpoon!", indexOutOfBoundsException);
				this.discard();
			}
			if (owner.isSneaking() || this.squaredDistanceTo(owner) > 65536) {
				this.beginReturn(true);
			} else {
				if (this.grappling) {
					if (this.inGroundTime > 200) {
						this.beginReturn(true);
					} else if (!this.isReturning() && this.isInGround()) {
						if (!this.getBoundingBox().expand(0.3).intersects(owner.getBoundingBox())) {
							owner.setVelocity(this.getPos().subtract(owner.getPos()).normalize().multiply(2, 1.2, 2));
							owner.velocityModified = true;
							owner.fallDistance = 1;
							((HarpoonDrag) owner).abysm$setDragTicks(2);
						}
					}
				} else {
					if (this.inGroundTime > 4 || (this.ticksAlive > 60 && this.inGroundTime < 1) || this.squaredDistanceTo(owner) > 65536) {
						this.beginReturn(true);
					}
				}
			}
		}
		if (this.isReturning()) {
			boolean closeEnough = this.squaredDistanceTo(owner) < 49;
			this.setVelocity(owner.getEyePos().subtract(this.getPos()).normalize().multiply(closeEnough ? 0.65 : VELOCITY_POWER));
		}
		super.tick();
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		nbt.putInt("slot", this.slot);
		nbt.putInt("ticksAlive", this.ticksAlive);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.slot = nbt.getInt("slot", -1);
		this.ticksAlive = nbt.getInt("ticksAlive", 0);
	}

	@Override
	protected float getDragInWater() {
		return 1F;
	}

	public boolean isReturning() {
		return this.dataTracker.get(RETURNING) && this.isNoClip();
	}

	@SuppressWarnings("SameParameterValue")
	protected void beginReturn(boolean playSound) {
		if (this.isReturning()) {
			return; // lol
		}
		this.setNoClip(true);
		this.dataTracker.set(RETURNING, true);
		if (playSound) {
			this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0F, 1.0F);
		}
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		float f = this.isSubmergedInWater() ? 8.0F : 3.5F;
		Entity entity2 = this.getOwner();
		World world = this.getWorld();
		ItemStack weapon = this.getWeaponStack();
		RegistryEntry<DamageType> damageType = AbysmDamageTypes.getFromWorld(world, AbysmDamageTypes.HARPOON);
		DamageSource damageSource = new DamageSource(damageType, this, entity2 == null ? this : entity2);
		if (world instanceof ServerWorld serverWorld) {
			//noinspection DataFlowIssue
			f = EnchantmentHelper.getDamage(serverWorld, weapon, entity, damageSource, f);
		}
		f = this.haul ? 0.01f : f;
		//noinspection deprecation
		if (entity.sidedDamage(damageSource, f)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (world instanceof ServerWorld serverWorld) {
				EnchantmentHelper.onTargetDamaged(serverWorld, entity, damageSource, weapon, item -> this.kill(serverWorld));
			}

			if (entity instanceof LivingEntity livingEntity) {
				this.knockback(livingEntity, damageSource);
				this.onHit(livingEntity);
				if (this.haul) {
					((HarpoonDrag) livingEntity).abysm$setStuckHarpoon(this);
					((HarpoonDrag) livingEntity).abysm$setDragTicks(2);
				}
			}
		}
		byte pierceLevel = this.getPierceLevel();
		if (pierceLevel > 0) {
			((PersistentProjectileEntityAccessor) this).abysm$invokeSetPierceLevel((byte) (pierceLevel - 1));
			IntOpenHashSet intOpenHashSet = ((PersistentProjectileEntityAccessor) this).abysm$getPiercedEntities();
			if (intOpenHashSet == null) {
				intOpenHashSet = new IntOpenHashSet(5);
				intOpenHashSet.add(entity.getId());
				((PersistentProjectileEntityAccessor) this).abysm$setPiercedEntities(intOpenHashSet);
			} else {
				intOpenHashSet.add(entity.getId());
			}
		} else {
			this.beginReturn(true);
			this.deflect(ProjectileDeflection.SIMPLE, entity, this.getOwner(), false);
			this.setVelocity(this.getVelocity().multiply(0.02, 0.2, 0.02));
		}
		this.playSound(SoundEvents.ITEM_TRIDENT_HIT, 1.0F, 1.0F);
	}

	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}

	@Override
	protected SoundEvent getHitSound() {
		return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
	}

	@Override
	public boolean shouldRender(double distance) {
		return true;
	}

	public int getSlot() {
		return this.slot;
	}

	@SuppressWarnings("unused")
	public int getTicksAlive() {
		return this.ticksAlive;
	}
}
