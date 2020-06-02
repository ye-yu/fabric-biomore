package fp.yeyu.denseoremod.feature.builder.targetfinder;

import net.minecraft.block.Block;

public class BlockUtils {
    public static boolean isAmongBlock(Block[] blocks, Block block) {
        for (Block testBlock : blocks) if (testBlock == block) return true;
        return false;
    }

    public static boolean isAmongBlock(Block block, Block... blocks) {
        return isAmongBlock(blocks, block);
    }
}
