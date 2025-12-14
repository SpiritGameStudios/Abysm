package dev.spiritstudios.abysm.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.world.level.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.client.gui.screen.ingame.DensityBlobBlockScreen;
import dev.spiritstudios.abysm.duck.DensityPlayerEntityDuck;
import dev.spiritstudios.abysm.world.entity.effect.BlueEffect;
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
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements DensityPlayerEntityDuck {
	@Shadow
	@Final
	protected Minecraft minecraft;

	private LocalPlayerMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
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
}
