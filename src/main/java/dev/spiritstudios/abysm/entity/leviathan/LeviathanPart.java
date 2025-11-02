package dev.spiritstudios.abysm.entity.leviathan;

import dev.spiritstudios.specter.api.entity.EntityPart;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.phys.Vec3;

public class LeviathanPart extends EntityPart<Leviathan> {

	public final String name;

	public LeviathanPart(Leviathan owner, String name, float width, float height) {
		super(owner, EntityDimensions.scalable(width, height));
		this.name = name;
	}

	@Override
	public void setRelativePos(Vec3 relativePos) {
		super.setRelativePos(relativePos);
		this.setPos(this.owner.position().add(relativePos));
	}

	@SuppressWarnings("SimplifiableConditionalExpression")
	@Override
	public final boolean hurtServer(ServerLevel world, DamageSource source, float amount) {
		return this.isInvulnerableToBase(source) ? false : this.getOwner().damagePart(world, this, source, amount);
	}
}
