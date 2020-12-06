package fp.yeyu.denseoremod;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import fp.yeyu.denseoremod.feature.builder.*;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import fp.yeyu.denseoremod.feature.decorator.CountChanceHeight;
import fp.yeyu.denseoremod.feature.decorator.CountChanceSurface;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.EntitySelectorOptions;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.GameRules;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.DiskFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BiomOreMod implements DedicatedServerModInitializer, ModInitializer {
    public static final Feature<BiomOreVeinFeatureConfig> BIOM_THICK_VEIN_ORE_FEATURE;
    public static final Feature<BiomOreVeinFeatureConfig> BIOM_FLAT_VEIN_ORE_FEATURE;
    public static final Feature<BiomOreSingleFeatureConfig> BIOM_SINGLE_ORE_FEATURE;
    public static final Feature<DiskFeatureConfig> DRY_DISK_FEATURE;
    public static final Decorator<CountChanceConfig> COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR;
    public static final Decorator<CountChanceConfig> COUNT_CHANCE_SURFACE_CONFIG_DECORATOR;
    private static final Logger LOGGER = LogManager.getLogger(BiomOreMod.class);
    public static volatile GameRules.Key<GameRules.BooleanRule> BIOMORE;

    static {
        BIOM_THICK_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_thick_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig.CODEC));
        BIOM_FLAT_VEIN_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_flat_vein_feature", new BiomOreVeinFeature(BiomOreVeinFeatureConfig.CODEC, true));
        BIOM_SINGLE_ORE_FEATURE = Registry.register(Registry.FEATURE, "biomore_single_feature", new BiomOreSingleFeature(BiomOreSingleFeatureConfig.CODEC));
        DRY_DISK_FEATURE = Registry.register(Registry.FEATURE, "dry_disk_feature", new DryDiskFeature(DiskFeatureConfig.CODEC));
        COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_height", new CountChanceHeight(CountChanceConfig.CODEC));
        COUNT_CHANCE_SURFACE_CONFIG_DECORATOR = Registry.register(Registry.DECORATOR, "count_chance_surface", new CountChanceSurface(CountChanceConfig.CODEC));
    }

    private static void initBiomoreRule() {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
            BIOMORE = null;
        } else {
            BIOMORE = register("useBiomore", GameRules.Category.MISC, GameRules.BooleanRule.create(false));
        }
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

    private static void registerBiomoreHereCommand(CommandDispatcher<ServerCommandSource> dispatcher, boolean isDedicated) {
        if (!isDedicated) return;
        dispatcher.register(
                CommandManager.literal("biomore").executes(BiomOreMod::biomelessBiomoreCommand)
        );
    }

    private static int biomelessBiomoreCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        final ServerPlayerEntity player = context.getSource().getPlayer();
        final Optional<RegistryKey<Biome>> optionalKey = player.getServerWorld().method_31081(player.getBlockPos());
        if (!optionalKey.isPresent()) throw EntitySelectorOptions.INAPPLICABLE_OPTION_EXCEPTION.create(null);
        final RegistryKey<Biome> biomeRegistryKey = optionalKey.get();
        context.getSource().sendFeedback(new LiteralText("Obtainable ore: ").formatted(Formatting.GOLD), false);
        final RegistryKey<Biome> fallbackBiomeKey = BiomOreFeatures.FALLBACK_BIOME.getOrDefault(player.getServerWorld().getBiome(player.getBlockPos()).getCategory(), BiomeKeys.PLAINS);
        final String fallback = BiomOreFeatures.BIOME_CONTAINS.getOrDefault(fallbackBiomeKey, "<err>");
        context.getSource().sendFeedback(new LiteralText("  " + BiomOreFeatures.BIOME_CONTAINS.getOrDefault(biomeRegistryKey, fallback)), false);
        return 1;
    }

    @Override
    public void onInitializeServer() {
        LOGGER.info("BiomOre server initialized.");
    }

    @Override
    public void onInitialize() {
        LOGGER.info("BiomOre main initialized.");
        initBiomoreRule();
        CommandRegistrationCallback.EVENT.register(BiomOreMod::registerBiomoreHereCommand);
    }
}
