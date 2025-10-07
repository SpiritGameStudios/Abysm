package dev.spiritstudios.abysm.mixin.pressure;

import dev.spiritstudios.abysm.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.item.AbysmArmorMaterials;
import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void pressureDamage(CallbackInfo ci) {
		if (!(this.getWorld() instanceof ServerWorld world)) {
			return;
		}

		boolean fullDivingSuit = true;
		for (Map.Entry<EquipmentSlot, TagKey<Item>> entry :
			AbysmArmorMaterials.DIVING_SUITS_BY_SLOT.entrySet()) {
			ItemStack stack = this.getEquippedStack(entry.getKey());
			if (stack.isEmpty() || !stack.isIn(entry.getValue())) {
				fullDivingSuit = false;
				break;
			}
		}
		if (fullDivingSuit) {
			return;
		}

		float pressure = PressureFinder.getPressure(world, this.getBlockPos());
		if (pressure >= 40f) {
			this.damage(world,
				new DamageSource(AbysmDamageTypes.getOrThrow(world, AbysmDamageTypes.PRESSURE)),
				pressure * 0.2f);
		}
	}
}
