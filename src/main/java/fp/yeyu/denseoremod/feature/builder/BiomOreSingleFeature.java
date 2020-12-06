package fp.yeyu.denseoremod.feature.builder;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Objects;
import java.util.Random;

public class BiomOreSingleFeature extends Feature<BiomOreSingleFeatureConfig> {

    public BiomOreSingleFeature(Codec<BiomOreSingleFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos pos, BiomOreSingleFeatureConfig config) {
        if (Objects.isNull(pos) || !config.target.getCondition().test(world.getBlockState(pos))) return false;
        world.setBlockState(pos, config.state, 2);
        return true;
    }
}
