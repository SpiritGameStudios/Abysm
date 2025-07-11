package dev.spiritstudios.abysm.entity.leviathan;

import dev.spiritstudios.specter.api.entity.EntityPart;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.EntityPositionSyncS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public class LeviathanPart extends EntityPart<Leviathan> {

	public final String name;

	public LeviathanPart(Leviathan owner, String name, float width, float height) {
		super(owner, EntityDimensions.changing(width, height));
		this.name = name;
	}

	@Override
	public void setRelativePos(Vec3d relativePos) {
		super.setRelativePos(relativePos);
		this.setPosition(this.owner.getPos().add(relativePos));
		if (!(this.getWorld() instanceof ServerWorld)) {
			return;
		}
		PlayerLookup.tracking(this.owner).forEach(serverPlayerEntity -> {
			serverPlayerEntity.networkHandler.sendPacket(EntityPositionSyncS2CPacket.create(this));
		});
	}

	@SuppressWarnings("SimplifiableConditionalExpression")
	@Override
	public final boolean damage(ServerWorld world, DamageSource source, float amount) {
		return this.isAlwaysInvulnerableTo(source) ? false : this.getOwner().damagePart(world, this, source, amount);
	}
}
