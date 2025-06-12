package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.registry.AbysmDataComponentTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HarpoonItem extends Item {

	public HarpoonItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
		return !user.isInCreativeMode();
	}

	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT) {
			ItemStack slotStack = slot.getStack();
			ItemStack componentStack = stack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY).getStack();
			if (componentStack.isEmpty() && slotStack.isOf(Items.HEART_OF_THE_SEA)) {
				stack.set(AbysmDataComponentTypes.BLESSED, new BlessedComponent(slotStack.copyWithCount(1)));
				slotStack.decrement(1);
				return true;
			}
			if (slotStack.isEmpty() && !componentStack.isEmpty()) {
				slot.setStack(componentStack.copy());
				stack.remove(AbysmDataComponentTypes.BLESSED);
				return true;
			}
			final int maxCount = slotStack.getMaxCount();
			if (slotStack.getItem().equals(componentStack.getItem()) && slotStack.getCount() < maxCount) {
				int count = slotStack.getCount() + componentStack.getCount();
				if (count <= maxCount) {
					slotStack.setCount(count);
				} else {
					slotStack.setCount(maxCount);
					componentStack.setCount(count - maxCount);
				}
				return true;
			}
		}
		return super.onStackClicked(stack, slot, clickType, player);
	}
}
