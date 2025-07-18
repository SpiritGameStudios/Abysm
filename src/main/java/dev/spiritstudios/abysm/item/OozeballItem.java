package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.block.AbysmBlocks;
import dev.spiritstudios.abysm.block.OozingDregloamBlock;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class OozeballItem extends Item {

	public OozeballItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		PlayerEntity player = context.getPlayer();
		BlockPos pos = context.getBlockPos();
		BlockState state = world.getBlockState(pos);
		ItemStack stack = context.getStack();

		BlockState newState = AbysmBlocks.OOZING_DREGLOAM.getDefaultState();
		if (context.getSide() != Direction.DOWN && state.isOf(AbysmBlocks.DREGLOAM) && OozingDregloamBlock.stayAlive(newState, world, pos)) {
			stack.decrement(1);
			if (player != null) {
				player.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
			}
			world.setBlockState(pos, newState);

			world.emitGameEvent(null, GameEvent.FLUID_PLACE, pos);
			world.playSound(null, pos, AbysmSoundEvents.ITEM_OOZEBALL_APPLY, SoundCategory.BLOCKS, 1.0F, 1.0F);

			return ActionResult.SUCCESS;
		} else {
			return ActionResult.PASS;
		}
	}
}
