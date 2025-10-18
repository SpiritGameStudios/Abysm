package dev.spiritstudios.abysm.mixin.salination;

import dev.spiritstudios.abysm.entity.effect.AbysmStatusEffects;
import dev.spiritstudios.abysm.registry.tags.AbysmEntityTypeTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract boolean isMobOrPlayer();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canHaveStatusEffect", at = @At("HEAD"), cancellable = true)
    private void canHaveSalinationEffect(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (!effect.getEffectType().equals(AbysmStatusEffects.SALINATION))
            return;

        if (this.getType().isIn(AbysmEntityTypeTags.IMMUNE_TO_SALINATION) || !this.isMobOrPlayer())
            cir.setReturnValue(false);
    }

}
