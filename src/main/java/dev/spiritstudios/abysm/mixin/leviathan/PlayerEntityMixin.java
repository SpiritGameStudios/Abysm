package dev.spiritstudios.abysm.mixin.leviathan;

import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;

@Debug(export = true)
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@SuppressWarnings("ModifyVariableMayBeArgsOnly")
	@ModifyVariable(method = "attack", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;onAttacking(Lnet/minecraft/entity/Entity;)V")), at = @At("STORE"), index = 1)
	private Entity accountForLeviathanPartsToo(Entity original) {
		return original instanceof LeviathanPart leviathanPart ? leviathanPart.owner : original;
	}
}
