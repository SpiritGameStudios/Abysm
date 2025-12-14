package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.data.fishenchantment.FishEnchantment;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class LectorfinRenderState extends LivingEntityRenderState {
	public final Query query = new Query();
	public FishEnchantment enchantment;
}
