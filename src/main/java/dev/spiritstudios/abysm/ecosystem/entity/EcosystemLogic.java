package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemArea;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaManager;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaPos;
import dev.spiritstudios.abysm.ecosystem.registry.EcosystemType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

// The actual implementations for handling & signaling most Entity-related Ecosystem code & calls,
// such as finding food, repopulating, or signaling the Entity's goals/brain to avoid or actively flee from a predator
public class EcosystemLogic {
	public final Mob entity;
	public final EcosystemType<?> type;
	public final Level world;

	public ChunkPos currentChunkPos = ChunkPos.ZERO;
	public ChunkPos prevChunkPos = ChunkPos.ZERO;
	public EcosystemAreaPos currentEcosystemAreaPos = new EcosystemAreaPos(ChunkPos.ZERO);

	public boolean shouldHunt = false;
	public boolean shouldScavenge = false;
	public boolean shouldRepopulate = false;

	public boolean isHunting = false;
	public boolean isBeingHunted = false;
	public int huntTicks = 0;
	public boolean isFavoredInHunt = false;

	public int breedTicks = 0;

	public EcosystemLogic(Mob entity, EcosystemType<?> type) {
		this.entity = entity;
		this.type = type;
		this.world = entity.level();
	}

	public void onSpawn() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.chunkPosition());
		this.onEcosystemAreaEnter(ecosystemAreaPos);
	}

	public void tick() {
		// Handle moving into new EcosystemArea
		this.prevChunkPos = this.currentChunkPos;
		this.currentChunkPos = this.entity.chunkPosition();
		if (this.currentChunkPos != this.prevChunkPos) {
			// Not creating EcosystemAreaPos every tick to save (probably only fractions of) performance
			EcosystemAreaPos newEcosystemAreaPos = new EcosystemAreaPos(this.currentChunkPos);
			if (!newEcosystemAreaPos.equals(this.currentEcosystemAreaPos)) {
				this.onEcosystemAreaEnter(newEcosystemAreaPos);
				this.onEcosystemAreaLeave(this.currentEcosystemAreaPos);
				this.currentEcosystemAreaPos = newEcosystemAreaPos;
			}
		}

		// Handle other tick stuff - updating numbers
		this.breedTicks++;

		if (this.isHunting) {
			if (this.huntTicks >= 0) {
				this.huntTicks--;
			} else {
				this.huntTicks = 0;
			}
		}

		// I do think it would be fun if there were some particles of some sort to display being who is favored in a hunt
//		if(this.isHunting || this.isBeingHunted) {
//			if(this.isFavoredInHunt()) {
//				((ServerWorld) this.world).spawnParticles(ParticleTypes.WAX_ON, this.entity.getX(), this.entity.getY() + 1, this.entity.getZ(), 1, 0, 0, 0, 0);
//			} else {
//				((ServerWorld) this.world).spawnParticles(ParticleTypes.WAX_OFF, this.entity.getX(), this.entity.getY() + 1, this.entity.getZ(), 1, 0, 0, 0, 0);
//			}
//		}
	}

	public void onDeath() {
		EcosystemAreaPos ecosystemAreaPos = new EcosystemAreaPos(this.entity.chunkPosition());
		this.onEcosystemAreaLeave(ecosystemAreaPos);
	}

	public void onEcosystemAreaEnter(EcosystemAreaPos pos) {
		if (this.world.isClientSide()) return;
		this.getEcosystemArea(pos).addEntity(this.entity);
	}

	public void onEcosystemAreaLeave(EcosystemAreaPos pos) {
		if (this.world.isClientSide()) return;
		this.getEcosystemArea(pos).removeEntity(this.entity);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos) {
		if (this.world.isClientSide()) return null;
		EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld((ServerLevel) this.world);
		return ecosystemAreaManager.getEcosystemArea(pos, true);
	}

	//region Getters and Setters

	public boolean shouldHunt() {
		return this.shouldHunt;
	}

	public void setShouldHunt(boolean shouldHunt) {
		this.shouldHunt = shouldHunt;
	}

	public boolean shouldRepopulate() {
		return this.shouldRepopulate;
	}

	public void setShouldRepopulate(boolean shouldRepopulate) {
		this.shouldRepopulate = shouldRepopulate;
	}

	public boolean shouldScavenge() {
		return this.shouldScavenge;
	}

	public void setShouldScavenge(boolean shouldScavenge) {
		this.shouldScavenge = shouldScavenge;
	}

	public boolean isHunting() {
		return this.isHunting;
	}

	public void setHunting(boolean hunting) {
		this.isHunting = hunting;
	}

	public boolean isBeingHunted() {
		return this.isBeingHunted;
	}

	public void setBeingHunted(boolean beingHunted) {
		this.isBeingHunted = beingHunted;
	}

	public int getHuntTicks() {
		return this.huntTicks;
	}

	public void setHuntTicks(int huntTicks) {
		this.huntTicks = huntTicks;
	}

	public boolean isFavoredInHunt() {
		return isFavoredInHunt;
	}

	public void setFavoredInHunt(boolean favoredInHunt) {
		isFavoredInHunt = favoredInHunt;
	}

	public int getBreedTicks() {
		return this.breedTicks;
	}

	public void setBreedTicks(int breedTicks) {
		this.breedTicks = breedTicks;
	}

	//endregion
}
