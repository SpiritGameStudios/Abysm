package dev.spiritstudios.abysm.item;

import dev.spiritstudios.abysm.component.BlessedComponent;
import dev.spiritstudios.abysm.entity.harpoon.HarpoonEntity;
import dev.spiritstudios.abysm.registry.AbysmEnchantments;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class HarpoonItem extends Item {

	public static final Identifier PIERCING = Identifier.ofVanilla("piercing");

	public HarpoonItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()) {
			ItemStack stack = user.getStackInHand(hand);
			BlessedComponent component = stack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY);
			if (component.loaded()) {
				int slot;
				if (hand == Hand.OFF_HAND) {
					slot = PlayerInventory.OFF_HAND_SLOT;
				} else {
					slot = user.getInventory().getSlotWithStack(stack);
				}
				HarpoonEntity harpoon = new HarpoonEntity(world, user, slot, stack);
				world.spawnEntity(harpoon);
				stack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().loaded(false).ticksSinceShot(0).build());
				world.playSoundFromEntity(null, harpoon, SoundEvents.ITEM_TRIDENT_THROW.value(), SoundCategory.PLAYERS, 1.0F, 1.0F);
				if (AbysmEnchantments.hasEnchantment(stack, world, AbysmEnchantments.HAUL)) {
					user.getItemCooldownManager().set(stack, 120);
				}
			} else {
				user.getItemCooldownManager().set(stack, 10);
			}
		}
		return ActionResult.CONSUME;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerWorld world, Entity entity, @Nullable EquipmentSlot slot) {
		if (entity instanceof PlayerEntity player) {
			PlayerInventory inventory = player.getInventory();
			int invSlot = inventory.getSlotWithStack(stack);
			if (invSlot == -1) {
				super.inventoryTick(stack, world, entity, slot);
				return;
			}
			ItemStack invStack = inventory.getStack(invSlot);
			if (!ItemStack.areItemsAndComponentsEqual(stack, invStack)) {
				super.inventoryTick(stack, world, entity, slot);
				return;
			}
		}
		BlessedComponent component = stack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY);
		int ticksSinceLastShot = component.ticksSinceShot();
		if (ticksSinceLastShot >= 0 && ticksSinceLastShot < 200) {
			stack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().ticksSinceShot(ticksSinceLastShot + 1).build());
		} else if (ticksSinceLastShot >= 200 && !component.loaded()) {
			stack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().loaded(true).build());
		}
		super.inventoryTick(stack, world, entity, slot);
	}

	@Override
	public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
		return !allowContinuingBlockBreaking(player, oldStack, newStack);
	}

	@Override
	public boolean allowContinuingBlockBreaking(PlayerEntity player, ItemStack oldStack, ItemStack newStack) {
		if (!ItemStack.areItemsEqual(oldStack, newStack)) {
			return super.allowContinuingBlockBreaking(player, oldStack, newStack);
		}
		if (!oldStack.contains(AbysmDataComponentTypes.BLESSED) || !newStack.contains(AbysmDataComponentTypes.BLESSED)) {
			return super.allowContinuingBlockBreaking(player, oldStack, newStack);
		}
		if (!Objects.equals(oldStack.get(AbysmDataComponentTypes.BLESSED), newStack.get(AbysmDataComponentTypes.BLESSED))) {
			return true;
		}
		return super.allowContinuingBlockBreaking(player, oldStack, newStack);
	}

	@Override
	public boolean canMine(ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity user) {
		return !user.isInCreativeMode();
	}

	@Override
	public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
		if (clickType == ClickType.RIGHT) {
			ItemStack slotStack = slot.getStack();
			BlessedComponent component = stack.getOrDefault(AbysmDataComponentTypes.BLESSED, BlessedComponent.EMPTY);
			ItemStack componentStack = component.stack();
			if (componentStack.isEmpty() && slotStack.isOf(Items.HEART_OF_THE_SEA)) {
				stack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().stack(slotStack.copyWithCount(1)).build());
				slotStack.decrement(1);
				return true;
			}
			if (slotStack.isEmpty() && !componentStack.isEmpty()) {
				slot.setStack(componentStack.copy());
				stack.set(AbysmDataComponentTypes.BLESSED, component.buildNew().stack(ItemStack.EMPTY).build());
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

	@Override
	public boolean canBeEnchantedWith(ItemStack stack, RegistryEntry<Enchantment> enchantment, EnchantingContext context) {
		return super.canBeEnchantedWith(stack, enchantment, context) || enchantment.matchesId(PIERCING);
	}

	public static AttributeModifiersComponent createAttributes(float attackDamage, float attackSpeed) {
		return AttributeModifiersComponent.builder()
			.add(
				EntityAttributes.ATTACK_DAMAGE,
				new EntityAttributeModifier(Item.BASE_ATTACK_DAMAGE_MODIFIER_ID, attackDamage - 1, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.add(
				EntityAttributes.ATTACK_SPEED,
				new EntityAttributeModifier(Item.BASE_ATTACK_SPEED_MODIFIER_ID, attackSpeed - 4, EntityAttributeModifier.Operation.ADD_VALUE),
				AttributeModifierSlot.MAINHAND
			)
			.build();
	}
}
