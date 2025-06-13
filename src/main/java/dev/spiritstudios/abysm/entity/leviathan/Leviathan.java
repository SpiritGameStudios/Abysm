package dev.spiritstudios.abysm.entity.leviathan;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public abstract class Leviathan extends MobEntity implements Monster {

	protected Leviathan(EntityType<? extends MobEntity> entityType, World world) {
		super(entityType, world);
	}

	@SuppressWarnings("unused")
	public boolean damagePart(ServerWorld world, LeviathanPart part, DamageSource source, float amount) {
		return this.damage(world, source, amount);
	}

	@Override
	public boolean damage(ServerWorld world, DamageSource source, float amount) {
		return super.damage(world, source, source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) ? amount : Math.min(1f, amount));
	}

	public static DefaultAttributeContainer.Builder createLeviathanAttributes() {
		return MobEntity.createMobAttributes().add(EntityAttributes.MAX_HEALTH, 32768)
			.add(EntityAttributes.ARMOR, 100)
			.add(EntityAttributes.ARMOR_TOUGHNESS, 100);
	}

	@Override
	public void heal(float amount) {
		super.heal(Math.max(0, amount));
	}

	public abstract List<LeviathanPart> getParts();

	@Override
	protected boolean canStartRiding(Entity entity) {
		return false;
	}

	@Override
	public boolean canUsePortals(boolean allowVehicles) {
		return false;
	}

	@Override
	public void onSpawnPacket(EntitySpawnS2CPacket packet) {
		super.onSpawnPacket(packet);
		List<LeviathanPart> parts = this.getParts();

		for(int i = 0; i < parts.size(); ++i) {
			parts.get(i).setId(i + packet.getEntityId() + 1);
		}
	}
}
