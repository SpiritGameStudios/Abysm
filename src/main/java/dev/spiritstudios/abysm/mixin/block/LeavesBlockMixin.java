package dev.spiritstudios.abysm.mixin.block;

import dev.spiritstudios.abysm.registry.tags.AbysmBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
	@Inject(method = "getOptionalDistanceFromLog", at = @At("HEAD"), cancellable = true)
	private static void applyForNonLogLeavePreservers(BlockState state, CallbackInfoReturnable<OptionalInt> cir) {
		if (state.isIn(AbysmBlockTags.ALSO_PRESERVES_LEAVES)) {
			cir.setReturnValue(OptionalInt.of(0));
		}
	}
}
