package dev.spiritstudios.abysm.mixin.leviathan;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.spiritstudios.abysm.entity.leviathan.LeviathanPart;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.server.world.ServerChunkLoadingManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(ServerChunkLoadingManager.class)
public class ServerChunkLoadingManagerMixin {

	@WrapOperation(method = "loadEntity", constant = @Constant(classValue = EnderDragonPart.class))
	private boolean doNotLoadLeviathanPartEither(Object entity, Operation<Boolean> original) {
		return original.call(entity) || entity instanceof LeviathanPart;
	}
}
