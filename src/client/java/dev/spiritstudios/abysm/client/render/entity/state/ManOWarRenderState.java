package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.entity.floral_reef.ManOWar;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class ManOWarRenderState extends LivingEntityRenderState {

	public List<ManOWar.TentacleData> tentacleData;
	public Vec3d velocity = Vec3d.ZERO;
}
