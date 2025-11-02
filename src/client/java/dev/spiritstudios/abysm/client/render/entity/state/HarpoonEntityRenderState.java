package dev.spiritstudios.abysm.client.render.entity.state;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.world.phys.Vec3;

public class HarpoonEntityRenderState extends ArrowRenderState {
	public Vec3 handPos;
	public int startLight;
	public int endLight;
	public boolean enchanted;
}
