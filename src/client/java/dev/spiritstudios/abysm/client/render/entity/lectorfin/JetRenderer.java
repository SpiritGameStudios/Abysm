package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;

public class JetRenderer extends FishEnchantmentRenderer {
	public static final JetRenderer INSTANCE = new JetRenderer();

	protected JetRenderer() {
		super(new ExtraModel(AbysmFishEnchantments.JET.getValue()));
	}
}
