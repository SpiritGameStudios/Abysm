package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.world.entity.leviathan.pseudo.SkeletonSharkPart;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Map;

public class SkeletonSharkRenderState extends LivingEntityRenderState {
	public final Map<String, Part> parts = Util.make(new Object2ObjectOpenHashMap<>(), map -> {
		map.put("head", new Part(new Vector3f(), new Vector3f(), new Vector3f()));
		map.put("body", new Part(new Vector3f(), new Vector3f(), new Vector3f()));
		map.put("tail", new Part(new Vector3f(), new Vector3f(), new Vector3f()));
	});

	public boolean sans = false;
	public boolean rotateAnyway = false;

	public record Part(Vector3f absPos, Vector3f relativePos, Vector3f rotation) {

		public void extract(SkeletonSharkPart part, @Nullable Entity previous, float partialTick) {
			var abs = part.getPosition(partialTick);
			absPos.set(abs.x, abs.y, abs.z);

			var relO = part.getPrevRelPos();
			var rel = part.relativePos;
			relativePos.set(
				Mth.lerp(partialTick, relO.x, rel.x),
				Mth.lerp(partialTick, relO.y, rel.y),
				Mth.lerp(partialTick, relO.z, rel.z)
			);

			if (previous == null) return;

			var prevAbs = previous.getPosition(partialTick);

			double dx = prevAbs.x - abs.x;
			double dy = prevAbs.y - abs.y;
			double dz = prevAbs.z - abs.z;

			float pitch = (float) Math.asin(-dy);
			float yaw = (float) Mth.atan2(dx, dz);


			rotation.set(0, -yaw, 0);
		}
	}
}
