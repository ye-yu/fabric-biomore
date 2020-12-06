package fp.yeyu.denseoremod.feature.builder;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

public class BiomOreSingleFeatureConfig implements FeatureConfig {
    public static final Codec<BiomOreSingleFeatureConfig> CODEC =
            RecordCodecBuilder.create(
                    (instance) -> instance.group(
                            Target.CODEC.fieldOf("target").forGetter(biomOreSingleFeatureConfig -> biomOreSingleFeatureConfig.target),
                            BlockState.CODEC.fieldOf("state").forGetter((emeraldOreFeatureConfig) -> emeraldOreFeatureConfig.state)
                    ).apply(instance, BiomOreSingleFeatureConfig::new));
    public final Target target;
    public final BlockState state;

    public BiomOreSingleFeatureConfig(Target target, BlockState state) {
        this.state = state;
        this.target = target;
    }
}
