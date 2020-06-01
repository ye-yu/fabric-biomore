package fp.yeyu.denseoremod;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DenseOreMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(DenseOreMod.class);
    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod is loaded.");
    }
}
