package dev.spiritstudios.abysm.mixin.salination;

import com.llamalad7.mixinextras.sugar.Local;
import dev.spiritstudios.abysm.entity.effect.SalinationEffect;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.entity.projectile.thrown.SplashPotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author axialeaa
 */
@Mixin(SplashPotionEntity.class)
public abstract class SplashPotionEntityMixin extends PotionEntity {

    public SplashPotionEntityMixin(EntityType<? extends PotionEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "spawnAreaEffectCloud", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;applyInstantEffect(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/LivingEntity;ID)V"))
    private void grantHeroBrineCriterion(ServerWorld world, ItemStack stack, HitResult hitResult, CallbackInfo ci, @Local LivingEntity living, @Local RegistryEntry<StatusEffect> registryEntry) {
        if (this.getOwner() instanceof ServerPlayerEntity player)
            SalinationEffect.tryGrantHeroBrineCriterion(player, living, new StatusEffectInstance(registryEntry));
    }

}
