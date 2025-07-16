package dev.spiritstudios.abysm.client.mixin.leviathan;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

	@Shadow
	private ClientWorld world;

	@SuppressWarnings("UnstableApiUsage")
	@WrapOperation(method = "onEntityPositionSync", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getEntityById(I)Lnet/minecraft/entity/Entity;"))
	private Entity accountForParts(ClientWorld instance, int id, Operation<Entity> original) {
		Entity entity = original.call(instance, id);
		if (entity != null) {
			return entity;
		}
		entity = this.world.specter$getParts().get(id);
		return entity instanceof LeviathanPart ? entity : null;
	}
}
