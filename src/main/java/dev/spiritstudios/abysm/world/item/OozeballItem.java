package dev.spiritstudios.abysm.world.item;

import dev.spiritstudios.abysm.world.level.block.AbysmBlocks;
import dev.spiritstudios.abysm.world.level.block.OozingDregloamBlock;
import dev.spiritstudios.abysm.core.registries.AbysmSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class OozeballItem extends Item {

	public OozeballItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Level level = context.getLevel();
		Player player = context.getPlayer();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		ItemStack stack = context.getItemInHand();

		BlockState newState = AbysmBlocks.OOZING_DREGLOAM.defaultBlockState();
		if (context.getClickedFace() != Direction.DOWN && state.is(AbysmBlocks.DREGLOAM) && OozingDregloamBlock.stayAlive(newState, level, pos)) {
			stack.shrink(1);
			if (player != null) {
				player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
			}
			level.setBlockAndUpdate(pos, newState);

			level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
			level.playSound(null, pos, AbysmSoundEvents.ITEM_OOZEBALL_APPLY, SoundSource.BLOCKS, 1.0F, 1.0F);

			return InteractionResult.SUCCESS;
		} else {
			return InteractionResult.PASS;
		}
	}
}
