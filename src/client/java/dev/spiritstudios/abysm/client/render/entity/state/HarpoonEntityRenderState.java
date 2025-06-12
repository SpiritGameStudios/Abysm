package dev.spiritstudios.abysm.client.render.entity.state;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

public class HarpoonEntityRenderState extends ProjectileEntityRenderState {

	@Nullable
	public PlayerEntity player;
	public Vec3d handPos;
	public double prevX;
	public double prevY;
	public double prevZ;
}
