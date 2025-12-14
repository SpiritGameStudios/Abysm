package dev.spiritstudios.abysm.client.mixin.render;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.client.duck.LocalPlayerDuckInterface;
import dev.spiritstudios.abysm.world.level.levelgen.biome.AbysmBiomes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer implements LocalPlayerDuckInterface {
	private LocalPlayerMixin(ClientLevel world, GameProfile profile) {
		super(world, profile);
	}

	@Unique
	private float abysm$underwaterAmbientSkyLight;
	@Unique
	private float abysm$lastUnderwaterAmbientSkyLight;

	@Inject(method = "aiStep", at = @At("RETURN"))
	private void updateAmbientSkyLight(CallbackInfo ci) {
		Holder<Biome> biome = level().getBiome(this.blockPosition());
		boolean inFloralReef = biome.is(AbysmBiomes.FLORAL_REEF);
		float targetAmbientSkyLight = inFloralReef ? 0.18F : 0.0F;

		this.abysm$lastUnderwaterAmbientSkyLight = this.abysm$underwaterAmbientSkyLight;
		if (this.isEyeInFluid(FluidTags.WATER)) {
			this.abysm$underwaterAmbientSkyLight = Mth.lerp(0.1F, this.abysm$underwaterAmbientSkyLight, targetAmbientSkyLight);
		} else {
			this.abysm$underwaterAmbientSkyLight = targetAmbientSkyLight;
		}
	}

	@Override
	public float abysm$getUnderwaterAmbientSkyLight(float tickDelta) {
		return this.abysm$lastUnderwaterAmbientSkyLight + tickDelta * (this.abysm$underwaterAmbientSkyLight - this.abysm$lastUnderwaterAmbientSkyLight);
	}
}
