package dev.spiritstudios.abysm.mixin.salination;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author axialeaa
 */
@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin {

    @Shadow public abstract @Nullable LivingEntity getOwner();

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;applyInstantEffect(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/LivingEntity;ID)V"))
    private void grantHeroBrineCriterion(ServerWorld world, CallbackInfo ci, @Local LivingEntity living, @Local StatusEffectInstance instance) {
        if (this.getOwner() instanceof ServerPlayerEntity player)
            SalinationEffect.tryGrantHeroBrineCriterion(player, living, instance);
    }

}
