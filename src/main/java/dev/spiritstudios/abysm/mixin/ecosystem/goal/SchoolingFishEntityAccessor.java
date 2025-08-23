package dev.spiritstudios.abysm.mixin.ecosystem.goal;

import net.minecraft.entity.passive.SchoolingFishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SchoolingFishEntity.class)
public interface SchoolingFishEntityAccessor {
	@Accessor("leader")
	SchoolingFishEntity abysm$getLeader();
}
