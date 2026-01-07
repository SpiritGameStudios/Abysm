package dev.spiritstudios.abysm.mixin;

import dev.spiritstudios.abysm.duck.MouseInputPlayer;
import dev.spiritstudios.abysm.network.MouseInput;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public class PlayerMixin implements MouseInputPlayer {
	@Unique
	private MouseInput mouseInput = MouseInput.EMPTY;

	@Override
	public MouseInput spectre$latestInput() {
		return mouseInput;
	}

	@Override
	public void spectre$latestInput(MouseInput input) {
		this.mouseInput = input;
	}
}
