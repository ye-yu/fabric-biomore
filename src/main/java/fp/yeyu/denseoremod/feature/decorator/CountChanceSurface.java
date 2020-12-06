package fp.yeyu.denseoremod.feature.decorator;

import com.mojang.serialization.Codec;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.decorator.DecoratorContext;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CountChanceSurface extends CountChanceHeight {

    public CountChanceSurface(Codec<CountChanceConfig> configCodec) {
        super(configCodec);
    }

    // todo: try using context#getTopY(Heightmap.Type, int, int)
    public static BlockPos randomBlockPos(DecoratorContext context, CountChanceConfig config, BlockPos blockPos, Random random) {
        Target target = config.target;
        int x = random.nextInt(16) + blockPos.getX();
        int z = random.nextInt(16) + blockPos.getZ();
        int y = config.range + config.bottomOffset;
        BlockPos testBp = new BlockPos(x, y, z);

        while (!target.getCondition().test(context.getBlockState(testBp))) {
            testBp = testBp.down();
            if (testBp.getY() == config.bottomOffset) {
                return null;
            }
        }
        return testBp;
    }

    @Override
    public Stream<BlockPos> getPositions(DecoratorContext context, Random random, CountChanceConfig config, BlockPos pos) {
        DoubleStream ds = IntStream.range(0, config.count).mapToDouble(i -> random.nextDouble()).filter(i -> i < config.chance);
        return ds.mapToObj(i -> CountChanceSurface.randomBlockPos(context, config, pos, random));
    }
}
