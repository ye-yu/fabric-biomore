package fp.yeyu.denseoremod.feature.decorator;

import com.mojang.datafixers.Dynamic;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CountChanceSurface extends CountChanceHeight {
    private static final Logger LOGGER = LogManager.getLogger(CountChanceSurface.class);
    public CountChanceSurface(Function<Dynamic<?>, ? extends CountChanceConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public Stream<BlockPos> getPositions(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, CountChanceConfig config, BlockPos pos) {
        DoubleStream ds = IntStream.range(0, config.count).mapToDouble(i -> random.nextDouble()).filter(i -> i < config.chance);
        return ds.mapToObj(i -> randomBlockPos(world, config, pos, random));
    }

    public static BlockPos randomBlockPos(IWorld world, CountChanceConfig config, BlockPos blockPos, Random random) {
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
        return testBp;
    }
}
