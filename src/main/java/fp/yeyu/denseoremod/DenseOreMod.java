package fp.yeyu.denseoremod;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class DenseOreMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(DenseOreMod.class);

    public static LevelGeneratorType dense_ore;

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod is loaded.");
        addLevelType();
    }

    private static void addLevelType() {
        int i = 0;
        //noinspection StatementWithEmptyBody
        while(Objects.nonNull(LevelGeneratorType.TYPES[i++]));
        i--; //reduce after increase when detecting null
        LOGGER.info("Type is empty at: " + i);
        dense_ore = (new LevelGeneratorType(i, "dense_ore", 2)).setVersioned();
        LevelGeneratorType.TYPES[i] = dense_ore;
    }
}
