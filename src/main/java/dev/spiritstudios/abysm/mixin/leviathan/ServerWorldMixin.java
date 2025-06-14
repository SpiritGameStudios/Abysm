package dev.spiritstudios.abysm.mixin.leviathan;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.leviathan.Leviathan;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import dev.spiritstudios.abysm.entity.leviathan.ServerWorldLeviathan;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements ServerWorldLeviathan {

	@Unique
	final Int2ObjectMap<LeviathanPart> abysm$leviathanParts = new Int2ObjectOpenHashMap<>();

	@WrapOperation(method = "getEntityOrDragonPart", at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;get(I)Ljava/lang/Object;"))
	private Object get(Int2ObjectMap<EnderDragonPart> instance, int i, Operation<Object> original) {
		LeviathanPart part = abysm$leviathanParts.getOrDefault(i, null);
		return part == null ? original.call(instance, i) : part;
	}

	@Override
	public Int2ObjectMap<LeviathanPart> abysm$getLeviathanParts() {
		return this.abysm$leviathanParts;
	}

	@Mixin(targets = "net.minecraft.server.world.ServerWorld$ServerEntityHandler")
	public static class ServerEntityHandlerMixin {

		@Shadow
		@Final
		ServerWorld field_26936; // synthetic outer class variable

		// TODO: reenable, disabled because of crash on world load
		/*
		@WrapOperation(method = "startTracking(Lnet/minecraft/entity/Entity;)V", constant = @Constant(classValue = EnderDragonEntity.class))
		private boolean startTrackingLeviathanParts(Object entity, Operation<Boolean> original) {
			if (original.call(entity)) {
				return true;
			}
			if (entity instanceof Leviathan leviathan) {
				Int2ObjectMap<LeviathanPart> parts = this.field_26936.abysm$getLeviathanParts();
				for (LeviathanPart part : leviathan.getParts()) {
					parts.put(part.getId(), part);
				}
			}
			return false;
		}
		*/

		// TODO: reenable, disabled because of crash on world load
		/*
		@WrapOperation(method = "stopTracking(Lnet/minecraft/entity/Entity;)V", constant = @Constant(classValue = EnderDragonEntity.class))
		private boolean stopTrackingLeviathanParts(Object entity, Operation<Boolean> original) {
			if (original.call(entity)) {
				return true;
			}
			if (entity instanceof Leviathan leviathan) {
				Int2ObjectMap<LeviathanPart> parts = this.field_26936.abysm$getLeviathanParts();
				for (LeviathanPart part : leviathan.getParts()) {
					parts.remove(part.getId());
				}
			}
			return false;
		}
		*/
	}
}
