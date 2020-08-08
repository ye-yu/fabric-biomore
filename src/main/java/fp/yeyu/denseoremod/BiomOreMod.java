package fp.yeyu.denseoremod;

import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeature;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeature;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.DryDiskFeature;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import fp.yeyu.denseoremod.feature.decorator.CountChanceHeight;
import fp.yeyu.denseoremod.feature.decorator.CountChanceSurface;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BiomOreMod implements ClientModInitializer {
	public static final Feature<BiomOreVeinFeatureConfig> BIOM_THICK_VEIN_ORE_FEATURE;
	public static final Feature<BiomOreVeinFeatureConfig> BIOM_FLAT_VEIN_ORE_FEATURE;
	public static final Feature<BiomOreSingleFeatureConfig> BIOM_SINGLE_ORE_FEATURE;
	public static final Feature<DiskFeatureConfig> DRY_DISK_FEATURE;
	public static final Decorator<CountChanceConfig> COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR;
	public static final Decorator<CountChanceConfig> COUNT_CHANCE_SURFACE_CONFIG_DECORATOR;
	public static final GameRules.Key<GameRules.BooleanRule> BIOMORE;
	private static final Logger LOGGER = LogManager.getLogger(BiomOreMod.class);

	static {
		BIOM_THICK_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_thick_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig.CODEC));
		BIOM_FLAT_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_flat_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig.CODEC, true));
		BIOM_SINGLE_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_single_feature", new BiomOreSingleFeature(BiomOreSingleFeatureConfig.CODEC));
		DRY_DISK_FEATURE = Registry.register(Registry.FEATURE, "dry_disk_feature", new DryDiskFeature(DiskFeatureConfig.CODEC));
		COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_height", new CountChanceHeight(CountChanceConfig.CODEC));
		COUNT_CHANCE_SURFACE_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_surface", new CountChanceSurface(CountChanceConfig.CODEC));
		BIOMORE = register("useBiomore", GameRules.Category.MISC, GameRules.BooleanRule.create(false));
	}

	private static <T extends GameRules.Rule<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
		GameRules.Key<T> key = new GameRules.Key<>(name, category);
		GameRules.Type<?> type2 = GameRules.RULE_TYPES.put(key, type);
		if (type2 != null) {
			throw new IllegalStateException("Duplicate game rule registration for " + name);
		} else {
			return key;
		}
	}

	@Override
	public void onInitializeClient() {
		LOGGER.info("Mod is loaded.");
	}

}
