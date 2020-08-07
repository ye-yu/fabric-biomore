package fp.yeyu.denseoremod.feature.decorator;

import com.mojang.serialization.Codec;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CountChanceHeight extends Decorator<CountChanceConfig> {

	public CountChanceHeight(Codec<CountChanceConfig> configCodec) {
		super(configCodec);
	}

	public static BlockPos randomBlockPos(WorldAccess world, CountChanceConfig config, BlockPos blockPos, Random random) {
		Target target = config.target;
		int x = random.nextInt(16) + blockPos.getX();
		int z = random.nextInt(16) + blockPos.getZ();
		int y = config.range + config.bottomOffset;
		BlockPos testBp = new BlockPos(x, y, z);
		while (!target.getCondition().test(world.getBlockState(testBp))) {
			testBp = testBp.down();
			if (testBp.getY() == config.bottomOffset) {
				return null;
			}
		}
		y = random.nextInt(testBp.getY() - config.bottomOffset) + config.bottomOffset;
		return new BlockPos(x, y, z);
	}

	@Override
	public Stream<BlockPos> getPositions(WorldAccess world, ChunkGenerator generator, Random random, CountChanceConfig config, BlockPos pos) {
		DoubleStream ds = IntStream.range(0, config.count).mapToDouble(i -> random.nextDouble()).filter(i -> i < config.chance);
		return ds.mapToObj(i -> randomBlockPos(world, config, pos, random));
	}
}
