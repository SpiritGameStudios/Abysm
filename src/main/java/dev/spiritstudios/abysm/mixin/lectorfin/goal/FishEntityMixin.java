package dev.spiritstudios.abysm.mixin.lectorfin.goal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.FishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Predicate;

@Mixin(FishEntity.class)
public class FishEntityMixin {

	@WrapOperation(method = "initGoals", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/mob/PathAwareEntity;D)Lnet/minecraft/entity/ai/goal/EscapeDangerGoal;"))
	private EscapeDangerGoal lectorfinEscape(PathAwareEntity mob, double speed, Operation<EscapeDangerGoal> original) {
		if (mob instanceof LectorfinEntity lectorfin) {
			return new LectorfinEntity.EscapeWhenNearDeathGoal(lectorfin, speed);
		}
		return original.call(mob, speed);
	}

	@WrapOperation(method = "initGoals", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/mob/PathAwareEntity;Ljava/lang/Class;FDDLjava/util/function/Predicate;)Lnet/minecraft/entity/ai/goal/FleeEntityGoal;"))
	private <T extends LivingEntity> FleeEntityGoal<T> lectorfinFlee(PathAwareEntity fleeingEntity, Class<T> classToFleeFrom, float fleeDistance, double fleeSlowSpeed, double fleeFastSpeed, Predicate<LivingEntity> inclusionSelector, Operation<FleeEntityGoal<T>> original) {
		if (fleeingEntity instanceof LectorfinEntity lectorfin) {
			return new LectorfinEntity.FleeWhenWeakGoal<>(lectorfin, classToFleeFrom, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
		}
		return original.call(fleeingEntity, classToFleeFrom, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
	}

}
