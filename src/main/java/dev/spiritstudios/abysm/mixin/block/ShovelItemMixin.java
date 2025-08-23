package dev.spiritstudios.abysm.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.item.AbysmItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {

	@Inject(method = "useOnBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/event/GameEvent$Emitter;)V"))
	private void dropBonusItems(
		ItemUsageContext context,
		CallbackInfoReturnable<ActionResult> cir,
		@Local(ordinal = 0) World world,
		@Local(ordinal = 0) BlockPos blockPos,
		@Local(ordinal = 0) BlockState oldState,
		@Nullable @Local(ordinal = 2) BlockState newState
	) {
		if (newState != null && newState.isOf(AbysmBlocks.DREGLOAM) && oldState.isOf(AbysmBlocks.OOZING_DREGLOAM)) {
			Block.dropStack(world, blockPos, AbysmItems.DREGLOAM_OOZEBALL.getDefaultStack());
		}
	}
}
