package dev.spiritstudios.abysm.client.mixin.leviathan;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
	@Shadow
	private ClientLevel level;

//	@SuppressWarnings("UnstableApiUsage")
//	@WrapOperation(method = "handleEntityPositionSync", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getEntity(I)Lnet/minecraft/world/entity/Entity;"))
//	private Entity accountForParts(ClientLevel instance, int id, Operation<Entity> original) {
//		Entity entity = original.call(instance, id);
//		if (entity != null) {
//			return entity;
//		}
//		entity = this.level.specter$getParts().get(id);
//		return entity instanceof LeviathanPart ? entity : null;
//	}
}
