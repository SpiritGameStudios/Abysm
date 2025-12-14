package dev.spiritstudios.abysm.client.render.entity.state;

import dev.spiritstudios.abysm.data.variant.ElectricOoglyBooglyVariant;
import dev.spiritstudios.spectre.api.core.math.Query;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

public class ElectricOoglyBooglyRenderState extends VariantState<ElectricOoglyBooglyVariant> {
	public final Query query = new Query();

	public final AnimationState bonk = new AnimationState();

	public float blowingUpWithMindTicks;
	public boolean isBlowingUpWithMind;

	public Vec3 velocity;
}
