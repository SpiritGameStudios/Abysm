package dev.spiritstudios.abysm.mixin.bloomray;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.floralreef.BloomrayEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.passive.FishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FishEntity.class)
public abstract class FishEntityMixin {
	@WrapOperation(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V", ordinal = 2))
	private void useCustomBloomrayGoalInstead(GoalSelector instance, int priority, Goal goal, Operation<Void> original) {
		if ((FishEntity) (Object) this instanceof BloomrayEntity bloomray) {
			goal = bloomray.createWanderGoal();
		}
		original.call(instance, priority, goal);
	}
}
