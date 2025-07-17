package dev.spiritstudios.abysm.client.render.entity.lectorfin;

import dev.spiritstudios.abysm.entity.ruins.AbysmFishEnchantments;

public class ShellRenderer extends FishEnchantmentRenderer {

	public static final ShellRenderer INSTANCE = new ShellRenderer();

	protected ShellRenderer() {
		super(new ExtraModel(AbysmFishEnchantments.SHELL_ID));
	}
}
