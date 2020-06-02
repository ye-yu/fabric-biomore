package fp.yeyu.denseoremod;

import com.google.common.collect.Maps;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.function.Consumer;

public class BiomOreFeatures {
    public static final HashMap<String, Consumer<Object>> methods = Maps.newHashMap();
    public static final Logger LOGGER = LogManager.getLogger(BiomOreFeatures.class);

    static {
        methods.put("ocean", BiomOreFeatures::ocean);
        methods.put("plains", BiomOreFeatures::plains);
        methods.put("desert", BiomOreFeatures::desert);
        methods.put("extreme_hills", BiomOreFeatures::extremeHills);
        methods.put("forest", BiomOreFeatures::forest);
        methods.put("taiga", BiomOreFeatures::taiga);
        methods.put("swamp", BiomOreFeatures::swamp);
        methods.put("river", BiomOreFeatures::river);
        methods.put("nether", BiomOreFeatures::nether);
        methods.put("theend", BiomOreFeatures::theend);
        methods.put("icy", BiomOreFeatures::icy);
        methods.put("mushroom", BiomOreFeatures::mushroom);
        methods.put("beach", BiomOreFeatures::beach);
        methods.put("jungle", BiomOreFeatures::jungle);
        methods.put("none", BiomOreFeatures::none);
        methods.put("savanna", BiomOreFeatures::savanna);
        methods.put("mesa", BiomOreFeatures::mesa);
    }

    public static void ocean(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void plains(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        LOGGER.info("Redefined features for plain biome.");
    }

    public static void desert(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void extremeHills(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void forest(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void taiga(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void swamp(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void river(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void nether(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void theend(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void icy(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void mushroom(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void beach(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void jungle(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void none(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void savanna(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }

    public static void mesa(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
    }
}
