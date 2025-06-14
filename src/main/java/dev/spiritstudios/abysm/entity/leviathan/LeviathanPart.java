package dev.spiritstudios.abysm.entity.leviathan;

import dev.spiritstudios.specter.api.entity.EntityPart;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;

public class LeviathanPart<T extends Leviathan<T>> extends EntityPart<T> {

	public final String name;

	public LeviathanPart(T owner, String name, float width, float height) {
		super(owner, EntityDimensions.changing(width, height));
		this.name = name;
	}

	@SuppressWarnings("SimplifiableConditionalExpression")
	@Override
	public final boolean damage(ServerWorld world, DamageSource source, float amount) {
		return this.isAlwaysInvulnerableTo(source) ? false : this.getOwner().damagePart(world, this, source, amount);
	}
}
