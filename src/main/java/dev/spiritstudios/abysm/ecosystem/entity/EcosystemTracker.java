package dev.spiritstudios.abysm.ecosystem.entity;

import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemArea;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaManager;
import dev.spiritstudios.abysm.ecosystem.chunk.EcosystemAreaPos;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

// Handles most of the EcosystemChunk communication from the EcosystemLogic - entering, leaving, spawning, and dying
// And then there's Chunky... ... ... ... ... ... ... ... ... he's doing fine actually just enjoying some coffee
public class EcosystemTracker {
	public final EcosystemLogic logic;
	public final World world;
	public final MobEntity entity;

	public ChunkPos currentPos = ChunkPos.ORIGIN;
	public ChunkPos prevPos = ChunkPos.ORIGIN;
	public EcosystemAreaPos ecosystemPos = new EcosystemAreaPos(ChunkPos.ORIGIN);

	public EcosystemTracker(EcosystemLogic logic) {
		this.logic = logic;
		this.world = logic.world;
		this.entity = logic.entity;
	}

	public void tick() {
		// Possible performance increase by only checking this every 5 or 10 ticks?
		this.prevPos = this.currentPos;
		this.currentPos = this.entity.getChunkPos();
		if (this.currentPos != this.prevPos) {
			// Not creating EcosystemAreaPos every tick to save (probably only fractions of) performance
			EcosystemAreaPos newAreaPos = new EcosystemAreaPos(this.currentPos);
			if (!newAreaPos.equals(this.ecosystemPos)) {
				this.onEcosystemAreaEnter(newAreaPos);
				this.onEcosystemAreaLeave(this.ecosystemPos);
				this.ecosystemPos = newAreaPos;
			}
		}
	}

	public void onEcosystemAreaEnter(EcosystemAreaPos pos) {
		if (this.world.isClient()) return;
		this.getEcosystemArea(pos).addEntity(this.entity);
	}

	public void onEcosystemAreaLeave(EcosystemAreaPos pos) {
		if (this.world.isClient()) return;
		this.getEcosystemArea(pos).removeEntity(this.entity);
	}

	public EcosystemArea getEcosystemArea(EcosystemAreaPos pos) {
		if (this.world.isClient()) return null;
		EcosystemAreaManager ecosystemAreaManager = EcosystemAreaManager.getEcosystemAreaManagerForWorld((ServerWorld) this.world);
		return ecosystemAreaManager.getEcosystemArea(pos, true);
	}
}
