package dev.spiritstudios.abysm.entity;

import dev.spiritstudios.abysm.registry.AbysmEntityTypes;
import dev.spiritstudios.abysm.registry.AbysmItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntity extends PersistentProjectileEntity {
	public HarpoonEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
		this.owner = null;
	}

	private final PlayerEntity owner;

	public HarpoonEntity(PlayerEntity owner) {
		super(AbysmEntityTypes.FLYING_HARPOON, owner.getEntityWorld());
		this.owner = owner;
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return AbysmItems.NOOPRAH.getDefaultStack();
	}

	@Nullable
	public PlayerEntity getPlayer() {
		return owner;
	}
}
