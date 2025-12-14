package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.world.entity.pattern.EntityPattern;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class FloralFishRenderState extends LivingEntityRenderState {
	public final Query query = new Query();
	public EntityPattern pattern;
}
