package dev.spiritstudios.abysm.mixin.lectorfin.goal;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.ruins.LectorfinEntity;
import net.minecraft.entity.ai.goal.FollowGroupLeaderGoal;
import net.minecraft.entity.passive.SchoolingFishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SchoolingFishEntity.class)
public class SchoolingFishEntityMixin {

	@WrapOperation(method = "initGoals", at = @At(value = "NEW", target = "(Lnet/minecraft/entity/passive/SchoolingFishEntity;)Lnet/minecraft/entity/ai/goal/FollowGroupLeaderGoal;"))
	private FollowGroupLeaderGoal lectorfinFollow(SchoolingFishEntity fish, Operation<FollowGroupLeaderGoal> original) {
		if (fish instanceof LectorfinEntity lectorfin) {
			return new LectorfinEntity.FollowGroupWhenWeakGoal(lectorfin);
		}
		return original.call(fish);
	}

}
