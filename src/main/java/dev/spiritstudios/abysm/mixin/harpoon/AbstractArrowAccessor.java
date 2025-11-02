package dev.spiritstudios.abysm.mixin.harpoon;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface AbstractArrowAccessor {
	@Invoker
	void invokeSetPierceLevel(byte level);

	@Accessor
	IntOpenHashSet getPiercingIgnoreEntityIds();

	@Accessor
	void setPiercingIgnoreEntityIds(IntOpenHashSet piercingIgnoreEntityIds);
}
