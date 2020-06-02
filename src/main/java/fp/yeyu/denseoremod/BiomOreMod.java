package fp.yeyu.denseoremod;

import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeature;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeature;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.decorator.CountChanceHeight;
import fp.yeyu.denseoremod.feature.decorator.CountChanceHeightConfig;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class BiomOreMod implements ClientModInitializer {
    private static final Logger LOGGER = LogManager.getLogger(BiomOreMod.class);
    public static final int TYPE_EMPTY_AT;
    public static final LevelGeneratorType DENSE_ORE;
    public static final Feature<BiomOreVeinFeatureConfig> BIOM_VEIN_ORE_FEATURE;
    public static final Feature<BiomOreSingleFeatureConfig> BIOM_SINGLE_ORE_FEATURE;
    public static final Decorator<CountChanceHeightConfig> CHANCE_HEIGHT_CONFIG_DECORATOR;

    static {
        int i = 0;
        //noinspection StatementWithEmptyBody
        while (Objects.nonNull(LevelGeneratorType.TYPES[i++])) ;
        i--; //reduce after increase when detecting null
        TYPE_EMPTY_AT = i;
        DENSE_ORE = (new LevelGeneratorType(TYPE_EMPTY_AT, "biomore", 2)).setVersioned();
        LOGGER.info("Type is empty at: " + i + ". Using this as the world level generation type integer.");
        LevelGeneratorType.TYPES[i] = DENSE_ORE;

        BIOM_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig::deserialize));
        BIOM_SINGLE_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_single_feature", new BiomOreSingleFeature(BiomOreSingleFeatureConfig::deserialize));
        CHANCE_HEIGHT_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_height", new CountChanceHeight(CountChanceHeightConfig::deserialize));
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod is loaded.");
    }
}
