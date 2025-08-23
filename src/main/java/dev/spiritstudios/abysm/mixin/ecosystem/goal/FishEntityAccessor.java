package dev.spiritstudios.abysm.mixin.ecosystem.goal;

import net.minecraft.entity.passive.FishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FishEntity.class)
public interface FishEntityAccessor {
	@Invoker("hasSelfControl")
	boolean abysm$invokeHasSelfControl();
}
