package dev.spiritstudios.abysm.mixin.worldgen;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.spiritstudios.abysm.Abysm;
import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.JigsawReplacementProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JigsawReplacementProcessor.class)
public abstract class JigsawReplacementProcessorMixin {

	@Inject(method = "processBlock", at = @At("HEAD"), cancellable = true)
	private void handleDensityBlobBlocks(LevelReader world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlaceSettings data, CallbackInfoReturnable<StructureTemplate.StructureBlockInfo> cir) {
		BlockState blockState = currentBlockInfo.state();
		if (blockState.is(AbysmBlocks.DENSITY_BLOB_BLOCK)) {
			if (currentBlockInfo.nbt() == null) {
				Abysm.LOGGER.warn("Density blob block at {} is missing nbt, will not replace", pos);
				cir.setReturnValue(currentBlockInfo);
			} else {
				String string = currentBlockInfo.nbt().getStringOr("final_state", "minecraft:air");

				BlockState newState;
				try {
					BlockStateParser.BlockResult blockResult = BlockStateParser.parseForBlock(world.holderLookup(Registries.BLOCK), string, true);
					newState = blockResult.blockState();
				} catch (CommandSyntaxException exception) {
					Abysm.LOGGER.error("Failed to parse density blob replacement state '{}' at {}: {}", string, pos, exception.getMessage());
					cir.setReturnValue(null);
					return;
				}

				cir.setReturnValue(newState.is(Blocks.STRUCTURE_VOID) ? null : new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), newState, null));
			}
		}
	}
}
