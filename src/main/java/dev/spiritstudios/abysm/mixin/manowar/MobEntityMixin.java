package dev.spiritstudios.abysm.mixin.manowar;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public class MobEntityMixin {

	@WrapOperation(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Box;expand(DDD)Lnet/minecraft/util/math/Box;"))
	private Box useTentacleBoxToLoot(Box instance, double x, double y, double z, Operation<Box> original) {
		if ((MobEntity) (Object) this instanceof ManOWarEntity manOWar) {
			return manOWar.getTentacleBox(x, y, z);
		}
		return original.call(instance, x, y, z);
	}
}
