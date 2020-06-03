package fp.yeyu.denseoremod.feature.builder.targetfinder;

import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.Objects;

public class BlockUtils {
    public static boolean isAmongBlock(Block block, Block... blocks) {
        return Objects.nonNull(block) && Arrays.asList(blocks).contains(block);
    }
}
