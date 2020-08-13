package fp.yeyu.denseoremod.feature.builder;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DiskFeature;
import net.minecraft.world.gen.feature.DiskFeatureConfig;

import java.util.Objects;
import java.util.Random;

public class DryDiskFeature extends DiskFeature {
	public DryDiskFeature(Codec<DiskFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DiskFeatureConfig diskFeatureConfig) {
		if (Objects.isNull(blockPos)) return false;
		int i = 0;
		int j = diskFeatureConfig.radius.getValue(random);

		for (int k = blockPos.getX() - j; k <= blockPos.getX() + j; ++k) {
			for (int l = blockPos.getZ() - j; l <= blockPos.getZ() + j; ++l) {
				int m = k - blockPos.getX();
				int n = l - blockPos.getZ();
				if (m * m + n * n <= j * j) {
					for (int o = blockPos.getY() - diskFeatureConfig.ySize; o <= blockPos.getY() + diskFeatureConfig.ySize; ++o) {
						BlockPos blockPos2 = new BlockPos(k, o, l);
						BlockState blockState = structureWorldAccess.getBlockState(blockPos2);

						for (BlockState blockState2 : diskFeatureConfig.targets) {
							if (blockState2.getBlock() == blockState.getBlock()) {
								structureWorldAccess.setBlockState(blockPos2, diskFeatureConfig.state, 2);
								++i;
								break;
							}
						}
					}
				}
			}
		}

		return i > 0;
	}

}
