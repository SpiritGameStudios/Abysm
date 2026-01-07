package dev.spiritstudios.abysm.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.duck.MouseInputPlayer;
import dev.spiritstudios.abysm.network.MouseInput;
import dev.spiritstudios.abysm.network.MouseInputC2SPayload;
import dev.spiritstudios.abysm.world.level.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.client.gui.screen.ingame.DensityBlobBlockScreen;
import dev.spiritstudios.abysm.duck.DensityPlayerEntityDuck;
import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements DensityPlayerEntityDuck, MouseInputPlayer {
	@Shadow
	@Final
	protected Minecraft minecraft;

	private LocalPlayerMixin(ClientLevel level, GameProfile profile) {
		super(level, profile);
	}

	@Override
	public void abysm$openDensityBlobScreen(DensityBlobBlockEntity densityBlobBlockEntity) {
		this.minecraft.setScreen(new DensityBlobBlockScreen(densityBlobBlockEntity));
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;onUpdateAbilities()V"))
	private void blockFlightWhenBlue(CallbackInfo ci) {
		if (!this.isSpectator()) {
			// this is needed in addition to the update in BlueEffect to avoid super short periods of flight
			if (BlueEffect.hasBlueEffect(this)) {
				this.getAbilities().flying = false;
			}
		}
	}

	@Inject(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/ClientInput;tick()V"))
	private void updateMouseInput(CallbackInfo ci) {
		MouseInput prevInput = spectre$latestInput();
		MouseInput newInput = new MouseInput(
			minecraft.options.keyAttack.isDown(),
			minecraft.options.keyUse.isDown()
		);

		if (!newInput.equals(prevInput)) {
			spectre$latestInput(newInput);
			ClientPlayNetworking.send(new MouseInputC2SPayload(newInput));
		}
	}
}
