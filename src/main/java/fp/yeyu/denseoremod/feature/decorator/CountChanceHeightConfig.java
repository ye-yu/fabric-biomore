package fp.yeyu.denseoremod.feature.decorator;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import net.minecraft.world.gen.decorator.DecoratorConfig;

public class CountChanceHeightConfig implements DecoratorConfig {

    public final float chance;
    public final int count;
    public final int bottomOffset;
    public final int range;
    public final Target target;

    public CountChanceHeightConfig(float chance, int count, int bottomOffset, int range, Target target) {
        this.chance = chance;
        this.count = count;
        this.bottomOffset = bottomOffset;
        this.range = range;
        this.target = target;
    }

    @Override
    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(
                ops,
                ops.createMap(ImmutableMap.of(
                        ops.createString("chance"), ops.createFloat(this.chance),
                        ops.createString("count"), ops.createInt(this.count),
                        ops.createString("bottom_offset"), ops.createInt(this.bottomOffset),
                        ops.createString("range"), ops.createInt(this.range),
                        ops.createString("target"), ops.createString(this.target.getName())
                )));
    }

    public static CountChanceHeightConfig deserialize(Dynamic<?> dynamic) {
        final float chance = dynamic.get("chance").asFloat(0f);
        final int count = dynamic.get("count").asInt(0);
        final int bottom_offset = dynamic.get("bottom_offset").asInt(0);
        final int range = dynamic.get("range").asInt(0);
        final Target target = Target.byName(dynamic.get("target").asString(""));
        return new CountChanceHeightConfig(chance, count, bottom_offset, range, target);
    }
}
