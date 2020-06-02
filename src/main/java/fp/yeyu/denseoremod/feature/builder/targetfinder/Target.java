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
    NATURAL_STONE("natural_stone", (blockState) -> {
        if (blockState == null) {
            return false;
        } else {
            Block block = blockState.getBlock();
            return BlockUtils.isAmongBlock(block, Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE);
        }
    }, 0),
    OVERWORLD_SURFACE_BLOCK("overworld_surface_block", (blockState) -> {
        if (blockState == null) {
            return false;
        } else {
            Block block = blockState.getBlock();
            return BlockUtils.isAmongBlock(block, Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.SAND, Blocks.GRASS_BLOCK, Blocks.GRAVEL);
        }
    }, 1),
    NETHERRACK("netherrack", new BlockPredicate(Blocks.NETHERRACK), 2);

    private static final Map<String, Target> nameMap = Arrays.stream(values()).collect(Collectors.toMap(Target::getName, (target) -> target));
    private final String name;
    private final Predicate<BlockState> predicate;
    private final int id;

    Target(String name, Predicate<BlockState> predicate, int id) {
        this.name = name;
        this.predicate = predicate;
        this.id = id;
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

    public int getId() {
        return this.id;
    }

}