package dev.spiritstudios.abysm.client.render.entity.state;

import net.minecraft.client.render.entity.state.ProjectileEntityRenderState;
import net.minecraft.util.math.Vec3d;

public class HarpoonEntityRenderState extends ProjectileEntityRenderState {
	public Vec3d handPos;
	public int startLight;
	public int endLight;
	public boolean enchanted;
}
