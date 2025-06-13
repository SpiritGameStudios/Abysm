package dev.spiritstudios.abysm.mixin;

import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {

	@Invoker("setPierceLevel")
	void abysm$invokeSetPierceLevel(byte level);
}
