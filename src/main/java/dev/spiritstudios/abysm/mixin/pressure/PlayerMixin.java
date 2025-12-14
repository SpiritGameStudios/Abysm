package dev.spiritstudios.abysm.mixin.pressure;

import dev.spiritstudios.abysm.world.entity.AbysmDamageTypes;
import dev.spiritstudios.abysm.world.item.AbysmArmorMaterials;
import dev.spiritstudios.abysm.util.PressureFinder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

	protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Inject(method = "aiStep", at = @At("HEAD"))
	private void pressureDamage(CallbackInfo ci) {
		if (!(this.level() instanceof ServerLevel level)) {
			return;
		}

		boolean fullDivingSuit = true;
		for (Map.Entry<EquipmentSlot, TagKey<Item>> entry :
			AbysmArmorMaterials.DIVING_SUITS_BY_SLOT.entrySet()) {
			ItemStack stack = this.getItemBySlot(entry.getKey());
			if (stack.isEmpty() || !stack.is(entry.getValue())) {
				fullDivingSuit = false;
				break;
			}
		}
		if (fullDivingSuit) {
			return;
		}

		float pressure = PressureFinder.getPressure(level, this.blockPosition());
		if (pressure >= 0.2F) {
			this.hurtServer(level,
				new DamageSource(AbysmDamageTypes.getOrThrow(level, AbysmDamageTypes.PRESSURE)),
				pressure * 5F);
		}
	}
}
