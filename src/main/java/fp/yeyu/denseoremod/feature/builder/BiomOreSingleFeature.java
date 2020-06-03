package fp.yeyu.denseoremod.feature.builder;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class BiomOreSingleFeature extends Feature<BiomOreSingleFeatureConfig> {
    private static final Logger LOGGER = LogManager.getLogger(BiomOreSingleFeature.class);

    public BiomOreSingleFeature(Function<Dynamic<?>, ? extends BiomOreSingleFeatureConfig> configDeserializer) {
        super(configDeserializer);
    }

    @Override
    public boolean generate(IWorld world, ChunkGenerator<? extends ChunkGeneratorConfig> generator, Random random, @Nullable BlockPos pos, BiomOreSingleFeatureConfig config) {
        if (Objects.isNull(pos) || !config.target.getCondition().test(world.getBlockState(pos))) return false;
        world.setBlockState(pos, config.state, 2);
        return true;
    }
}
