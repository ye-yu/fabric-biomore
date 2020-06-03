package fp.yeyu.denseoremod.feature.builder.targetfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.predicate.block.BlockPredicate;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum Target {
    NATURAL_STONE("natural_stone", (blockState) -> BlockUtils.isAmongBlock(blockState.getBlock(), Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE)),
    OVERWORLD_SURFACE_BLOCK("overworld_surface_block", (blockState) -> BlockUtils.isAmongBlock(blockState.getBlock(), Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.SAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL, Blocks.CLAY)),
    NETHERRACK("netherrack", new BlockPredicate(Blocks.NETHERRACK));

    private static final Map<String, Target> nameMap = Arrays.stream(values()).collect(Collectors.toMap(Target::getName, (target) -> target));
    private final String name;
    private final Predicate<BlockState> predicate;

    Target(String name, Predicate<BlockState> predicate) {
        this.name = name;
        this.predicate = predicate;
    }

    public static Target byName(String name) {
        return nameMap.get(name);
    }

    public String getName() {
        return this.name;
    }

    public Predicate<BlockState> getCondition() {
        return this.predicate;
    }

}