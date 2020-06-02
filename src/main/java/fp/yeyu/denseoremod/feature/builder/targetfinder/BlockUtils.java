package fp.yeyu.denseoremod.feature.builder.targetfinder;

import net.minecraft.block.Block;

import java.util.Arrays;

public class BlockUtils {
    public static boolean isAmongBlock(Block block, Block... blocks) {
        return Arrays.asList(blocks).contains(block);
    }
}
