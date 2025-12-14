package dev.spiritstudios.abysm.mixin.ecosystem.goal;

import net.minecraft.world.entity.animal.fish.AbstractFish;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractFish.class)
public interface AbstractFishAccessor {
	@Invoker
	boolean invokeCanRandomSwim();
}
