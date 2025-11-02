package dev.spiritstudios.abysm.entity.harpoon;

import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.component.HarpoonComponent;
import dev.spiritstudios.abysm.duck.HarpoonOwner;
import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.entity.AbysmEntityTypes;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import dev.spiritstudios.abysm.item.AbysmItems;
import dev.spiritstudios.abysm.mixin.harpoon.AbstractArrowAccessor;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import dev.spiritstudios.abysm.registry.AbysmRegistryKeys;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntity extends AbstractArrow {
	public static final float VELOCITY_POWER = 5f;
	public static final int MAX_RANGE = 128;

	// Range in which to slow down so the harpoon doesn't clip through the player
	public static final int SLOWDOWN_RANGE = 8;

	public static final EntityDataAccessor<Boolean> RETURNING = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> IN_BLOCK = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Boolean> GRAPPLING = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BOOLEAN);
	public static final EntityDataAccessor<Float> LENGTH = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.FLOAT);
	private static final EntityDataAccessor<Boolean> ENCHANTED = SynchedEntityData.defineId(HarpoonEntity.class, EntityDataSerializers.BOOLEAN);

	protected int slot = -1;
	protected int ticksAlive = 0;

	protected boolean haul = false;
	protected boolean blessed = false;

	public HarpoonEntity(EntityType<? extends AbstractArrow> entityType, Level world) {
		super(entityType, world);
	}

	protected HarpoonEntity(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level world, ItemStack stack, @Nullable ItemStack weapon) {
		super(entityType, x, y, z, world, stack, weapon);
	}

	public HarpoonEntity(Level world, Player owner, int slot, ItemStack weapon) {
		this(
			AbysmEntityTypes.FLYING_HARPOON,
			owner.getX(), owner.getEyeY() - 0.1, owner.getZ(),
			world, ItemStack.EMPTY, weapon
		);

		this.setOwner(owner);

		Vec3 velocity = owner.getViewVector(1.0F).scale(VELOCITY_POWER);
		this.setDeltaMovement(velocity);
		this.setNoGravity(true);
		this.slot = slot;

		if (weapon != null) {
			updateWeaponFlags(weapon);
		}

		double d = velocity.horizontalDistance();

		//noinspection SuspiciousNameCombination
		this.setYRot((float) (Mth.atan2(velocity.x, velocity.z) * Mth.RAD_TO_DEG));
		this.setXRot((float) (Mth.atan2(velocity.y, d) * Mth.RAD_TO_DEG));

		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
	}

	private void updateWeaponFlags(ItemStack weapon) {
		this.haul = AbysmEnchantments.hasEnchantment(weapon, level(), AbysmEnchantments.HAUL);
		setGrappling(AbysmEnchantments.hasEnchantment(weapon, level(), AbysmEnchantments.GRAPPLING));
		this.blessed = weapon.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY).isBlessed();
		this.entityData.set(ENCHANTED, weapon.hasFoil());
	}

	@Override
	protected ItemStack getDefaultPickupItem() {
		return ItemStack.EMPTY;
	}

	@Nullable
	public Player getPlayer() {
		return this.getOwner() instanceof Player player ? player : null;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);

		builder.define(RETURNING, false);
		builder.define(GRAPPLING, false);
		builder.define(IN_BLOCK, false);
		builder.define(LENGTH, 0.0F);
		builder.define(ENCHANTED, false);
	}

	@Override
	protected boolean tryPickup(Player player) {
		if (pickup != Pickup.ALLOWED) return super.tryPickup(player);

		ItemStack invStack = player.getInventory().getItem(this.getSlot());
		if (!invStack.is(AbysmItems.HARPOON)) return true;

		HarpoonComponent component = invStack.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY);

		if (component.loaded()) return true;

		invStack.set(AbysmDataComponentTypes.HARPOON, component.builder().loaded(true).build());
		return true;
	}

	@Override
	protected void onHitBlock(BlockHitResult blockHitResult) {
		super.onHitBlock(blockHitResult);
		this.setDeltaMovement(Vec3.ZERO);
		this.setInBlock(true);
		Player playerEntity = this.getPlayer();
		if (playerEntity != null) {
			double d = playerEntity.getEyePosition().subtract(blockHitResult.getLocation()).length();
			this.setLength(Math.max((float) d * 0.5F - 3.0F, 1.5F));
		}
	}

	@Override
	public void tick() {
		ticksAlive++;

		Player owner = this.getPlayer();
		if (owner == null || !owner.isAlive()) {
			this.discard();
			return;
		}

		Level world = this.level();

		if (!world.isClientSide()) {
			if (this.inGroundTime <= 0 && this.tickCount % 2 == 0) {
				this.level().playSound(
					null,
					owner.getX(), owner.getY(), owner.getZ(),
					AbysmSoundEvents.ITEM_HARPOON_IN_AIR,
					this.getSoundSource(),
					1.0F, 1.0F
				);
			}

			try {
				ItemStack invStack = owner.getInventory().getItem(this.slot);
				if (!invStack.is(AbysmItems.HARPOON) || invStack.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY).loaded()) {
					this.discard();
				}
			} catch (IndexOutOfBoundsException indexOutOfBoundsException) {
				Abysm.LOGGER.debug("An error occurred while ticking a harpoon!", indexOutOfBoundsException);
				this.discard();
			}

			if (owner.isShiftKeyDown() || this.distanceToSqr(owner) > (MAX_RANGE * MAX_RANGE)) {
				this.beginReturn(true);
			} else if ((this.inGroundTime > 4 || this.ticksAlive > 60 && this.inGroundTime < 1) && !isGrappling()) {
				this.beginReturn(true);
			}
		}

		if (this.isReturning()) {
			boolean closeEnough = this.distanceToSqr(owner) < (SLOWDOWN_RANGE * SLOWDOWN_RANGE);
			this.setDeltaMovement(owner.getEyePosition().subtract(this.position()).normalize()
				.scale(closeEnough ? 0.65 : VELOCITY_POWER));
		}

		super.tick();
	}

	@Override
	protected void onDeflection(@Nullable Entity deflector, boolean fromAttack) {
		super.onDeflection(deflector, fromAttack);
		this.beginReturn(true);
	}

	@Override
	public void addAdditionalSaveData(ValueOutput view) {
		super.addAdditionalSaveData(view);
		view.putInt("slot", this.slot);
		view.putInt("ticksAlive", this.ticksAlive);
		view.putBoolean("inBlock", this.isInBlock());
		view.putFloat("length", this.getLength());
	}

	@Override
	public void readAdditionalSaveData(ValueInput view) {
		super.readAdditionalSaveData(view);
		this.slot = view.getIntOr("slot", -1);
		this.ticksAlive = view.getIntOr("ticksAlive", 0);
		this.setInBlock(view.getBooleanOr("inBlock", false));
		this.setLength(view.getFloatOr("length", 0.0F));

		updateWeaponFlags(this.getPickupItemStackOrigin());
	}

	@Override
	protected float getWaterInertia() {
		return 1F;
	}

	public boolean isReturning() {
		return this.entityData.get(RETURNING) && this.isNoPhysics();
	}

	@SuppressWarnings("SameParameterValue")
	protected void beginReturn(boolean playSound) {
		if (this.isReturning()) {
			return; // lol
		}

		this.setNoPhysics(true);
		this.entityData.set(RETURNING, true);
		if (playSound) {
			this.playSound(AbysmSoundEvents.ITEM_HARPOON_RETURN, 10.0F, 1.0F);
		}
	}

	@Override
	protected void onHitEntity(EntityHitResult hitResult) {
		Level world = this.level();

		if (!(world instanceof ServerLevel serverWorld)) return;

		Entity entity = hitResult.getEntity();
		float damage = this.isUnderWater() ? 12.0F : 4.5F;

		Entity owner = this.getOwner();

		ItemStack weapon = this.getWeaponItem();

		Holder<DamageType> damageType = AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.HARPOON);
		DamageSource damageSource = new DamageSource(damageType, this, owner == null ? this : owner);

		if (weapon != null) {
			damage = EnchantmentHelper.modifyDamage(serverWorld, weapon, entity, damageSource, damage);
		}

		damage = this.haul ? 0.01f : damage;
		if (this.blessed && entity instanceof LectorfinEntity lectorfin) {
			damage = 0.0001f;
			world.registryAccess().lookupOrThrow(AbysmRegistryKeys.FISH_ENCHANTMENT)
				.getRandom(this.random).ifPresent(enchantment ->
					lectorfin.setEnchantment(enchantment, lectorfin.getEnchantmentLevel())
				);
		}

		if (entity.hurtServer(serverWorld, damageSource, damage)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			EnchantmentHelper.doPostAttackEffectsWithItemSourceOnBreak(serverWorld, entity, damageSource, weapon, item -> this.kill(serverWorld));

			if (entity instanceof LivingEntity livingEntity) {
				this.doKnockback(livingEntity, damageSource);
				this.doPostHurtEffects(livingEntity);
				if (this.haul) {
					((HarpoonDrag) livingEntity).abysm$setStuckHarpoon(this);
					((HarpoonDrag) livingEntity).abysm$setDragTicks(2);
				}
			}
		}

		byte pierceLevel = this.getPierceLevel();

		if (pierceLevel > 0) {
			((AbstractArrowAccessor) this).invokeSetPierceLevel((byte) (pierceLevel - 1));

			IntOpenHashSet pierced = ((AbstractArrowAccessor) this).getPiercingIgnoreEntityIds();
			if (pierced == null) {
				pierced = new IntOpenHashSet(pierceLevel);
				((AbstractArrowAccessor) this).setPiercingIgnoreEntityIds(pierced);
			}

			pierced.add(entity.getId());
		} else {
			this.beginReturn(true);
			this.deflect(ProjectileDeflection.REVERSE, entity, this.getOwner(), false);
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.02, 0.2, 0.02));
		}

		this.playSound(AbysmSoundEvents.ITEM_HARPOON_HIT, 1.0F, 1.0F);
	}

	@Override
	public boolean canUsePortal(boolean allowVehicles) {
		return false;
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return AbysmSoundEvents.ITEM_HARPOON_HIT_GROUND;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return true;
	}

	public int getSlot() {
		return this.slot;
	}

	private void setInBlock(boolean inBlock) {
		this.getEntityData().set(IN_BLOCK, inBlock);
	}

	public boolean isInBlock() {
		return this.getEntityData().get(IN_BLOCK);
	}


	private void setGrappling(boolean grappling) {
		this.getEntityData().set(GRAPPLING, grappling);
	}

	public boolean isGrappling() {
		return this.getEntityData().get(GRAPPLING);
	}


	public boolean isEnchanted() {
		return this.entityData.get(ENCHANTED);
	}


	private void setLength(float length) {
		this.getEntityData().set(LENGTH, length);
	}

	public float getLength() {
		return this.getEntityData().get(LENGTH);
	}

	@Override
	public void setOwner(@Nullable Entity entity) {
		super.setOwner(entity);
		Player playerEntity = this.getPlayer();
		if (playerEntity != null) {
			((HarpoonOwner) playerEntity).abysm$setHarpoon(this);
		}
	}

	@Override
	public void remove(RemovalReason reason) {
		Player playerEntity = this.getPlayer();
		if (playerEntity != null) {
			((HarpoonOwner) playerEntity).abysm$setHarpoon(null);
		}
		super.remove(reason);
	}

	@Override
	public void onClientRemoval() {
		Player playerEntity = this.getPlayer();
		if (playerEntity != null) {
			((HarpoonOwner) playerEntity).abysm$setHarpoon(null);
		}
	}
}
