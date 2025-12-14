package dev.spiritstudios.abysm.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.item.AbysmItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {

	@Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;gameEvent(Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V"))
	private void dropBonusItems(
		UseOnContext context,
		CallbackInfoReturnable<InteractionResult> cir,
		@Local(ordinal = 0) Level world,
		@Local(ordinal = 0) BlockPos blockPos,
		@Local(ordinal = 0) BlockState oldState,
		@Nullable @Local(ordinal = 2) BlockState newState
	) {
		if (newState != null && newState.is(AbysmBlocks.DREGLOAM) && oldState.is(AbysmBlocks.OOZING_DREGLOAM)) {
			Block.popResource(world, blockPos, AbysmItems.DREGLOAM_OOZEBALL.getDefaultInstance());
		}
	}
}
