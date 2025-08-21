package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class OozetrickleLanternBlock extends LanternBlock {
	public static final MapCodec<LanternBlock> CODEC = createCodec(OozetrickleLanternBlock::new);

	public static final IntProperty LIGHT = IntProperty.of("light", 0, 15);

	private static final int[] COLORS = Util.make(new int[16], colors -> {
		for (int i = 0; i <= 15; i++) {
			float light = i / 15.0F;
			float l = light * 0.8F + (light > 0.0F ? 0.2F : 0.1F);
			colors[i] = ColorHelper.fromFloats(1.0F, l, l, l);
		}
	});

	@Override
	public MapCodec<LanternBlock> getCodec() {
		return CODEC;
	}

	public OozetrickleLanternBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this.stateManager.getDefaultState()
				.with(HANGING, false)
				.with(WATERLOGGED, false)
				.with(LIGHT, 13)
		);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(LIGHT);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		// randomize light level
		if (random.nextBoolean()) {
			int light = state.get(LIGHT);
			int newLight = light + random.nextBetween(-4, 4);
			newLight = MathHelper.clamp(newLight, 0, 15);
			world.setBlockState(pos, state.with(LIGHT, newLight));
		}
	}

	@Override
	protected boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(LIGHT);
	}

	public static int getColor(int powerLevel) {
		return COLORS[powerLevel];
	}
}
