package dev.spiritstudios.abysm.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.spiritstudios.abysm.block.entity.DensityBlobBlockEntity;
import dev.spiritstudios.abysm.client.gui.screen.ingame.DensityBlobBlockScreen;
import dev.spiritstudios.abysm.duck.DensityPlayerEntityDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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
}
