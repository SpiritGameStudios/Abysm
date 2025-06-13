package dev.spiritstudios.abysm.mixin;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PersistentProjectileEntity.class)
public interface PersistentProjectileEntityAccessor {

	@Invoker("setPierceLevel")
	void abysm$invokeSetPierceLevel(byte level);

	@Accessor("piercedEntities")
	IntOpenHashSet abysm$getPiercedEntities();


	@Accessor("piercedEntities")
	void abysm$setPiercedEntities(IntOpenHashSet piercedEntities);
}
