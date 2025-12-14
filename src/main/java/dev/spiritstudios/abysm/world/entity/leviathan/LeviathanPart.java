package dev.spiritstudios.abysm.world.entity.leviathan;

import dev.spiritstudios.spectre.api.world.entity.EntityPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;

public class LeviathanPart extends EntityPart<Leviathan> {

	public final String name;

	public LeviathanPart(Leviathan owner, String name, float width, float height) {
		super(owner, EntityDimensions.scalable(width, height));
		this.name = name;
	}


	@Override
	public final boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		return !this.isInvulnerableToBase(source) && this.getOwner().damagePart(world, this, source, amount);
	}
}
