package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.component.AbysmDataComponentTypes;
import dev.spiritstudios.abysm.component.HarpoonComponent;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import dev.spiritstudios.abysm.registry.AbysmSoundEvents;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HarpoonItem extends Item {
	public static final ResourceLocation PIERCING = ResourceLocation.withDefaultNamespace("piercing");

	public HarpoonItem(Properties settings) {
		super(settings);
	}

	@Override
	public InteractionResult use(Level world, Player user, InteractionHand hand) {
		if (!world.isClientSide()) {
			ItemStack stack = user.getItemInHand(hand);
			HarpoonComponent component = stack.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY);
			if (component.loaded()) {
				int slot;
				if (hand == InteractionHand.OFF_HAND) {
					slot = Inventory.SLOT_OFFHAND;
				} else {
					slot = user.getInventory().findSlotMatchingItem(stack);
				}
				HarpoonEntity harpoon = new HarpoonEntity(world, user, slot, stack);
				world.addFreshEntity(harpoon);
				stack.set(
					AbysmDataComponentTypes.HARPOON,
					component.builder()
						.loaded(false)
						.ticksSinceShot(0)
						.build()
				);
				world.playSound(null, harpoon, AbysmSoundEvents.ITEM_HARPOON_LAUNCH, SoundSource.PLAYERS, 1.0F, 1.0F);
				if (AbysmEnchantments.hasEnchantment(stack, world, AbysmEnchantments.HAUL)) {
					user.getCooldowns().addCooldown(stack, 120);
				}

				stack.hurtWithoutBreaking(1, user);
			} else {
				user.getCooldowns().addCooldown(stack, 10);
			}
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot slot) {
		if (entity instanceof Player player) {
			Inventory inventory = player.getInventory();
			int invSlot = inventory.findSlotMatchingItem(stack);
			if (invSlot == -1) {
				super.inventoryTick(stack, world, entity, slot);
				return;
			}
			ItemStack invStack = inventory.getItem(invSlot);
			if (!ItemStack.isSameItemSameComponents(stack, invStack)) {
				super.inventoryTick(stack, world, entity, slot);
				return;
			}
		}
		HarpoonComponent component = stack.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY);
		int ticksSinceLastShot = component.ticksSinceShot();
		if (ticksSinceLastShot >= 0 && ticksSinceLastShot < 200) {
			stack.set(AbysmDataComponentTypes.HARPOON, component.builder().ticksSinceShot(ticksSinceLastShot + 1).build());
		} else if (ticksSinceLastShot >= 200 && !component.loaded()) {
			stack.set(AbysmDataComponentTypes.HARPOON, component.builder().loaded(true).build());
		}
		super.inventoryTick(stack, world, entity, slot);
	}

	@Override
	public boolean allowComponentsUpdateAnimation(Player player, InteractionHand hand, ItemStack oldStack, ItemStack newStack) {
		return !allowContinuingBlockBreaking(player, oldStack, newStack);
	}

	@Override
	public boolean allowContinuingBlockBreaking(Player player, ItemStack oldStack, ItemStack newStack) {
		if (!ItemStack.isSameItem(oldStack, newStack)) {
			return super.allowContinuingBlockBreaking(player, oldStack, newStack);
		}
		if (!oldStack.has(AbysmDataComponentTypes.HARPOON) || !newStack.has(AbysmDataComponentTypes.HARPOON)) {
			return super.allowContinuingBlockBreaking(player, oldStack, newStack);
		}
		if (!Objects.equals(oldStack.get(AbysmDataComponentTypes.HARPOON), newStack.get(AbysmDataComponentTypes.HARPOON))) {
			return true;
		}
		return super.allowContinuingBlockBreaking(player, oldStack, newStack);
	}

	@Override
	public boolean canDestroyBlock(ItemStack stack, BlockState state, Level world, BlockPos pos, LivingEntity user) {
		return !user.hasInfiniteMaterials();
	}

	@Override
	public boolean overrideStackedOnOther(ItemStack stack, Slot slot, ClickAction clickType, Player player) {
		if (clickType == ClickAction.SECONDARY) {
			ItemStack slotStack = slot.getItem();
			HarpoonComponent component = stack.getOrDefault(AbysmDataComponentTypes.HARPOON, HarpoonComponent.EMPTY);
			ItemStack componentStack = component.stack();
			if (componentStack.isEmpty() && slotStack.is(Items.HEART_OF_THE_SEA)) {
				stack.set(AbysmDataComponentTypes.HARPOON, component.builder().stack(slotStack.copyWithCount(1)).build());
				slotStack.shrink(1);
				return true;
			}
			if (slotStack.isEmpty() && !componentStack.isEmpty()) {
				slot.setByPlayer(componentStack.copy());
				stack.set(AbysmDataComponentTypes.HARPOON, component.builder().stack(ItemStack.EMPTY).build());
				return true;
			}
			final int maxCount = slotStack.getMaxStackSize();
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
		return super.overrideStackedOnOther(stack, slot, clickType, player);
	}

	@Override
	public boolean canBeEnchantedWith(ItemStack stack, Holder<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.is(PIERCING);
	}
}
