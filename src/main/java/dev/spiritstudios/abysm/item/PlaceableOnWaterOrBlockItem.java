package dev.spiritstudios.abysm.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class PlaceableOnWaterOrBlockItem extends BlockItem {
	public PlaceableOnWaterOrBlockItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		BlockHitResult raycastResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
		BlockHitResult blockAboveRaycastResult = raycastResult.withBlockPos(raycastResult.getBlockPos().up());
		return super.useOnBlock(new ItemUsageContext(user, hand, blockAboveRaycastResult));
	}
}
