package dev.spiritstudios.abysm.mixin.worldgen;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.block.AbysmBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.registry.RegistryKeys;
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
			if (currentBlockInfo.nbt() == null) {
				Abysm.LOGGER.warn("Density blob block at {} is missing nbt, will not replace", pos);
				cir.setReturnValue(currentBlockInfo);
			} else {
				String string = currentBlockInfo.nbt().getString("final_state", "minecraft:air");

				BlockState newState;
				try {
					BlockArgumentParser.BlockResult blockResult = BlockArgumentParser.block(world.createCommandRegistryWrapper(RegistryKeys.BLOCK), string, true);
					newState = blockResult.blockState();
				} catch (CommandSyntaxException exception) {
					Abysm.LOGGER.error("Failed to parse density blob replacement state '{}' at {}: {}", string, pos, exception.getMessage());
					cir.setReturnValue(null);
					return;
				}

				cir.setReturnValue(newState.isOf(Blocks.STRUCTURE_VOID) ? null : new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), newState, null));
			}
		}
	}
}
