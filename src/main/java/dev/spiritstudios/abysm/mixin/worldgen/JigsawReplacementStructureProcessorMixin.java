package dev.spiritstudios.abysm.mixin.worldgen;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.JigsawReplacementStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JigsawReplacementStructureProcessor.class)
public class JigsawReplacementStructureProcessorMixin {

	@Inject(method = "process", at = @At("HEAD"), cancellable = true)
	private void handleDensityBlobBlocks(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
		BlockState blockState = currentBlockInfo.state();
		if (blockState.isOf(AbysmBlocks.DENSITY_BLOB_BLOCK)) {
			// TODO add a field for the blockstate in the density blob block entity
			cir.setReturnValue(new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), Blocks.WATER.getDefaultState(), null));
		}
	}
}
