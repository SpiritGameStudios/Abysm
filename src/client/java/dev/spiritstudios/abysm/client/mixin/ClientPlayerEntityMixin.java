package dev.spiritstudios.abysm.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.client.gui.screen.ingame.DensityBlobBlockScreen;
import dev.spiritstudios.abysm.duck.DensityPlayerEntityDuck;
import dev.spiritstudios.abysm.entity.effect.BlueEffect;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements DensityPlayerEntityDuck {
	@Shadow
	@Final
	protected MinecraftClient client;

	private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Override
	public void abysm$openDensityBlobScreen(DensityBlobBlockEntity densityBlobBlockEntity) {
		this.client.setScreen(new DensityBlobBlockScreen(densityBlobBlockEntity));
	}

	@Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendAbilitiesUpdate()V"))
	private void blockFlightWhenBlue(CallbackInfo ci) {
		if (!this.isSpectator()) {
			// this is needed in addition to the update in BlueEffect to avoid super short periods of flight
			if (BlueEffect.hasBlueEffect(this)) {
				this.getAbilities().flying = false;
			}
		}
	}
}
