package fp.yeyu.denseoremod.feature.decorator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class CountChanceConfig implements DecoratorConfig {
    public static final Codec<CountChanceConfig> CODEC =
            RecordCodecBuilder.create(
                    (instance) -> instance.group(
                            Codec.FLOAT.fieldOf("chance").forGetter(countChanceConfig -> countChanceConfig.chance),
                            Codec.INT.fieldOf("count").forGetter(countChanceConfig -> countChanceConfig.count),
                            Codec.INT.fieldOf("bottomOffset").forGetter(countChanceConfig -> countChanceConfig.bottomOffset),
                            Codec.INT.fieldOf("range").forGetter(countChanceConfig -> countChanceConfig.range),
                            Target.CODEC.fieldOf("target").forGetter(biomOreSingleFeatureConfig -> biomOreSingleFeatureConfig.target)
                    ).apply(instance, CountChanceConfig::new));

    public final float chance;
    public final int count;
    public final int bottomOffset;
    public final int range;
    public final Target target;

    public CountChanceConfig(float chance, int count, int bottomOffset, int range, Target target) {
        this.chance = chance;
        this.count = count;
        this.bottomOffset = bottomOffset;
        this.range = range;
        this.target = target;
    }
}
