package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.entity.floralreef.ManOWarEntity;
import java.util.List;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ManOWarRenderState extends LivingEntityRenderState {
	public List<ManOWarEntity.TentacleData> tentacleData;
	public Vec3 velocity = Vec3.ZERO;
	public AABB tentacleBox;
	public Vec3 centerBoxPos;
}
