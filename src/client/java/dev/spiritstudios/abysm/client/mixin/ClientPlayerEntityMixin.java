package dev.spiritstudios.abysm.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.client.duck.ClientPlayerEntityDuckInterface;
import dev.spiritstudios.abysm.worldgen.biome.AbysmBiomes;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityDuckInterface {
	private ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Unique private float underwaterAmbientSkyLight;
	@Unique private float lastUnderwaterAmbientSkyLight;

	@Inject(method = "tickMovement", at = @At("RETURN"))
	private void updateAmbientSkyLight(CallbackInfo ci) {
		RegistryEntry<Biome> biome = this.clientWorld.getBiome(this.getBlockPos());
		boolean inFloralReef = biome.matchesKey(AbysmBiomes.FLORAL_REEF);
		float targetAmbientSkyLight = inFloralReef ? 0.18F : 0.0F;

		this.lastUnderwaterAmbientSkyLight = this.underwaterAmbientSkyLight;
		if(this.isSubmergedIn(FluidTags.WATER)) {
			this.underwaterAmbientSkyLight = MathHelper.lerp(0.1F, this.underwaterAmbientSkyLight, targetAmbientSkyLight);
		} else {
			this.underwaterAmbientSkyLight = targetAmbientSkyLight;
		}
	}

	@Override
	public float abysm$getUnderwaterAmbientSkyLight(float tickDelta) {
		return this.lastUnderwaterAmbientSkyLight + tickDelta * (this.underwaterAmbientSkyLight - this.lastUnderwaterAmbientSkyLight);
	}
}
