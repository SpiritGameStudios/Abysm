package dev.spiritstudios.abysm.mixin.manowar;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Mob.class)
public abstract class MobMixin {
	@WrapOperation(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/phys/AABB;inflate(DDD)Lnet/minecraft/world/phys/AABB;"))
	private AABB useTentacleBoxToLoot(AABB instance, double x, double y, double z, Operation<AABB> original) {
		if ((Mob) (Object) this instanceof ManOWarEntity manOWar) {
			return manOWar.getTentacleBox(x, y, z);
		}
		return original.call(instance, x, y, z);
	}
}
