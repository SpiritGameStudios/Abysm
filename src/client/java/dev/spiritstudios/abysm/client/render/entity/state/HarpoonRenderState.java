package dev.spiritstudios.abysm.client.render.entity.state;

import net.minecraft.client.renderer.entity.state.ArrowRenderState;
import net.minecraft.world.phys.Vec3;

public class HarpoonRenderState extends ArrowRenderState {
	public Vec3 handPos;
	public boolean isFoil;

	public int startBlockLight = 0;
	public int endBlockLight = 0;
	public int startSkyLight = 15;
	public int endSkyLight = 15;
}
