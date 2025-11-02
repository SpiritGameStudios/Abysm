package dev.spiritstudios.abysm.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class OozetrickleLanternBlock extends LanternBlock {
	public static final MapCodec<LanternBlock> CODEC = simpleCodec(OozetrickleLanternBlock::new);

	public static final IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);

	private static final int[] COLORS = Util.make(new int[16], colors -> {
		for (int i = 0; i <= 15; i++) {
			float light = i / 15.0F;
			float l = light * 0.6F + (light > 0.0F ? 0.4F : 0.3F);
			colors[i] = ARGB.colorFromFloat(1.0F, l, l, l);
		}
	});

	@Override
	public MapCodec<LanternBlock> codec() {
		return CODEC;
	}

	public OozetrickleLanternBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(
			this.stateDefinition.any()
				.setValue(HANGING, false)
				.setValue(WATERLOGGED, false)
				.setValue(LIGHT, 13)
		);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(LIGHT);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		// randomize light level
		if (random.nextBoolean()) {
			int light = state.getValue(LIGHT);
			int newLight = light + random.nextIntBetweenInclusive(-4, 4);
			newLight = Mth.clamp(newLight, 0, 15);
			world.setBlockAndUpdate(pos, state.setValue(LIGHT, newLight));
		}
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
		return state.getValue(LIGHT);
	}

	public static int getColor(int powerLevel) {
		return COLORS[powerLevel];
	}
}
