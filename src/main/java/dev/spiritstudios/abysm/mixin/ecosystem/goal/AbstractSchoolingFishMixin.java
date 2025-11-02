package dev.spiritstudios.abysm.mixin.ecosystem.goal;

import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractSchoolingFish.class)
public interface AbstractSchoolingFishMixin {
	@Accessor
	AbstractSchoolingFish getLeader();
}
