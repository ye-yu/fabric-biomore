package fp.yeyu.denseoremod;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class BiomOreMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(BiomOreMod.class);
    public static final int TYPE_EMPTY_AT;
    public static final LevelGeneratorType DENSE_ORE;

    static {
        int i = 0;
        //noinspection StatementWithEmptyBody
        while(Objects.nonNull(LevelGeneratorType.TYPES[i++]));
        i--; //reduce after increase when detecting null
        TYPE_EMPTY_AT = i;
        DENSE_ORE = (new LevelGeneratorType(TYPE_EMPTY_AT, "biomore", 2)).setVersioned();
        LOGGER.info("Type is empty at: " + i + ". Using this as the world level generation type integer.");
        LevelGeneratorType.TYPES[i] = DENSE_ORE;
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod is loaded.");
    }
}
