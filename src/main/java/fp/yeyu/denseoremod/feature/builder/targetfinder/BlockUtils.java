package fp.yeyu.denseoremod.feature.builder.targetfinder;

import net.minecraft.block.Block;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Objects;

public class BlockUtils {
    public static boolean isAmongBlock(Block block, Block... blocks) {
        return Objects.nonNull(block) && Arrays.asList(blocks).contains(block);
    }
}
