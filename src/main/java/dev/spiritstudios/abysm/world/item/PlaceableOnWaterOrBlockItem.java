package dev.spiritstudios.abysm.world.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class PlaceableOnWaterOrBlockItem extends BlockItem {
	public PlaceableOnWaterOrBlockItem(Block block, Properties settings) {
		super(block, settings);
	}

	@Override
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		BlockHitResult raycastResult = getPlayerPOVHitResult(world, user, ClipContext.Fluid.SOURCE_ONLY);
		BlockHitResult blockAboveRaycastResult = raycastResult.withPosition(raycastResult.getBlockPos().above());
		return super.useOn(new UseOnContext(user, hand, blockAboveRaycastResult));
	}
}
