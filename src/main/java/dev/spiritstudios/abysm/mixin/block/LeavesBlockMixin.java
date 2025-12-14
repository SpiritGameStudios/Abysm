package dev.spiritstudios.abysm.mixin.block;

import dev.spiritstudios.abysm.core.registries.tags.AbysmBlockTags;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalInt;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin {
	@Inject(method = "getOptionalDistanceAt", at = @At("HEAD"), cancellable = true)
	private static void applyForNonLogLeavePreservers(BlockState state, CallbackInfoReturnable<OptionalInt> cir) {
		if (state.is(AbysmBlockTags.ALSO_PRESERVES_LEAVES)) {
			cir.setReturnValue(OptionalInt.of(0));
		}
	}
}
