package fp.yeyu.denseoremod;

import fp.yeyu.denseoremod.feature.builder.*;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import fp.yeyu.denseoremod.feature.decorator.CountChanceHeight;
import fp.yeyu.denseoremod.feature.decorator.CountChanceSurface;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DiskFeature;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.level.LevelGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

public class BiomOreMod implements ClientModInitializer {
    public static final int TYPE_EMPTY_AT;
    public static final LevelGeneratorType DENSE_ORE;
    public static final Feature<BiomOreVeinFeatureConfig> BIOM_THICK_VEIN_ORE_FEATURE;
    public static final Feature<BiomOreVeinFeatureConfig> BIOM_FLAT_VEIN_ORE_FEATURE;
    public static final Feature<BiomOreSingleFeatureConfig> BIOM_SINGLE_ORE_FEATURE;
    public static final Feature<DiskFeatureConfig> DRY_DISK_FEATURE;
    public static final Decorator<CountChanceConfig> COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR;
    public static final Decorator<CountChanceConfig> COUNT_CHANCE_SURFACE_CONFIG_DECORATOR;
    private static final Logger LOGGER = LogManager.getLogger(BiomOreMod.class);

    static {
        int i = 0;
        //noinspection StatementWithEmptyBody
        while (Objects.nonNull(LevelGeneratorType.TYPES[i++])) ;
        i--; //reduce after increase when detecting null
        TYPE_EMPTY_AT = i;
        DENSE_ORE = (new LevelGeneratorType(TYPE_EMPTY_AT, "biomore", 2)).setVersioned();
        LOGGER.info("Type is empty at: " + i + ". Using this as the world level generation type integer.");
        LevelGeneratorType.TYPES[i] = DENSE_ORE;

        BIOM_THICK_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_thick_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig::deserialize));
        BIOM_FLAT_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_flat_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig::deserialize, true));
        BIOM_SINGLE_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_single_feature", new BiomOreSingleFeature(BiomOreSingleFeatureConfig::deserialize));
        DRY_DISK_FEATURE = Registry.register(Registry.FEATURE, "dry_disk_feature", new DryDiskFeature(DiskFeatureConfig::deserialize));
        COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_height", new CountChanceHeight(CountChanceConfig::deserialize));
        COUNT_CHANCE_SURFACE_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_surface", new CountChanceSurface(CountChanceConfig::deserialize));
    }

    @Override
    public void onInitializeClient() {
        LOGGER.info("Mod is loaded.");
    }
}
