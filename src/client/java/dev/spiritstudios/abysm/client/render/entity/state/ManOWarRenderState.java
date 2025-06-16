package dev.spiritstudios.abysm.client.render.entity.state;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.List;

public class ManOWarRenderState extends LivingEntityRenderState {

	public static final List<Vector3f> STARTING_OFFSETS;

	public Vec3d velocity = Vec3d.ZERO;

	static {
		ImmutableList.Builder<Vector3f> builder = ImmutableList.builder();
		for (int i = 0; i <= 6; i++) {
			int j = 2*i - 6;
			builder.add(new Vector3f(0, 0, j));
			builder.add(new Vector3f(2, 0, j));
			builder.add(new Vector3f(-2, 0, j));
			builder.add(new Vector3f(4, 0, j));
			builder.add(new Vector3f(-4, 0, j));
			builder.add(new Vector3f(6, 0, j));
			builder.add(new Vector3f(-6, 0, j));
		}
		STARTING_OFFSETS = builder.build().stream().map(vector3f -> vector3f.mul(0.05f)).toList();
	}
}
