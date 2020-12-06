package fp.yeyu.denseoremod.feature.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BiomOreVeinFeatureConfig implements FeatureConfig {
    public static final Codec<BiomOreVeinFeatureConfig> CODEC =
            RecordCodecBuilder.create(
                    (instance) -> instance.group(
                            Target.CODEC.fieldOf("target").forGetter(biomOreSingleFeatureConfig -> biomOreSingleFeatureConfig.target),
                            BlockState.CODEC.fieldOf("state").forGetter((emeraldOreFeatureConfig) -> emeraldOreFeatureConfig.state),
                            Codec.INT.fieldOf("size").forGetter(biomOreSingleFeatureConfig -> biomOreSingleFeatureConfig.size)
                    ).apply(instance, BiomOreVeinFeatureConfig::new));

    public final Target target;
    public final int size;
    public final BlockState state;

    public BiomOreVeinFeatureConfig(Target target, BlockState state, int size) {
        this.size = size;
        this.state = state;
        this.target = target;
    }
}
