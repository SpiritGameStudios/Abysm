package dev.spiritstudios.abysm.client.render.entity.state;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntityRenderState extends ProjectileEntityRenderState {

	@Nullable
	public PlayerEntity player;
	public double prevX;
	public double prevY;
	public double prevZ;
}
