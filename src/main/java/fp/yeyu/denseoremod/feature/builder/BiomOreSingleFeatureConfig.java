package fp.yeyu.denseoremod.feature.builder;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.FeatureConfig;

/*
 * This class is the copy of net.minecraft.world.gen.feature.OreFeatureConfig
 * with modification to adapt new Target: OVERWORLD_SURFACE_BLOCK
 * */
public class BiomOreSingleFeatureConfig implements FeatureConfig {
    public final BlockUtils.Target target;
    public final BlockState state;

    public BiomOreSingleFeatureConfig(BlockUtils.Target target, BlockState state) {
        this.state = state;
        this.target = target;
    }

    public static BiomOreSingleFeatureConfig deserialize(Dynamic<?> dynamic) {
        BlockUtils.Target target = BlockUtils.Target.byName(dynamic.get("target").asString(""));
        BlockState blockState = dynamic.get("state").map(BlockState::deserialize).orElse(Blocks.AIR.getDefaultState());
        return new BiomOreSingleFeatureConfig(target, blockState);
    }

    public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
        return new Dynamic<>(ops, ops.createMap(ImmutableMap.of(ops.createString("target"), ops.createString(this.target.getName()), ops.createString("state"), BlockState.serialize(ops, this.state).getValue())));
    }
}
