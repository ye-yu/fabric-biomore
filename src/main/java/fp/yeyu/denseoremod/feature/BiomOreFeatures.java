package fp.yeyu.denseoremod.feature;

import com.google.common.collect.Maps;
import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.builder.BiomOreSingleFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.BiomOreVeinFeatureConfig;
import fp.yeyu.denseoremod.feature.builder.targetfinder.Target;
import fp.yeyu.denseoremod.feature.decorator.CountChanceConfig;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStep;

import java.util.HashMap;
import java.util.function.Consumer;

public class BiomOreFeatures {
    public static final HashMap<String, Consumer<Object>> methods = Maps.newHashMap();
    public static final HashMap<Block, Integer> commonVeinSize = Maps.newHashMap();
    public static final double AMP = 3;

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

        commonVeinSize.put(Blocks.COAL_ORE, 17);
        commonVeinSize.put(Blocks.IRON_ORE, 9);
        commonVeinSize.put(Blocks.GOLD_ORE, 9);
        commonVeinSize.put(Blocks.REDSTONE_ORE, 8);
        commonVeinSize.put(Blocks.DIAMOND_ORE, 8);
        commonVeinSize.put(Blocks.LAPIS_ORE, 7);
    }

    public static void ocean(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
        DefaultBiomeFeatures.addMineables(biome);
        DefaultBiomeFeatures.addDefaultDisks(biome);

        // custom ores generation
        addThickVeinOre(biome, Blocks.DIAMOND_ORE, 0.25f, 5, 0, 64);
        addSurfaceVeinOre(biome, Blocks.EMERALD_ORE, 10, Target.OVERWORLD_SURFACE_BLOCK, 0.2f, 1, 20, 0, 64);
        addSingleOre(biome, Blocks.GOLD_BLOCK, 0.35f, 5, 5, 0, 64);
    }

    public static void plains(Object biomeObj) {
        Biome biome = (Biome) biomeObj;
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

    public static void addThickVeinOre(Biome biome,
                                       Block block,
                                       int veinSize,
                                       Target target,
                                       float chance,
                                       int count,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                BiomOreMod.BIOM_THICK_VEIN_ORE_FEATURE
                        .configure(
                                new BiomOreVeinFeatureConfig(
                                        target,
                                        block.getDefaultState(),
                                        veinSize))
                        .createDecoratedFeature(
                                BiomOreMod.COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR.configure(
                                        new CountChanceConfig(chance, count, bottomOffset, top - topOffSet, target)))
        );
    }

    public static void addSurfaceVeinOre(Biome biome,
                                         Block block,
                                         int veinSize,
                                         Target target,
                                         float chance,
                                         int count,
                                         int bottomOffset,
                                         int topOffSet,
                                         int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                BiomOreMod.BIOM_FLAT_VEIN_ORE_FEATURE
                        .configure(
                                new BiomOreVeinFeatureConfig(
                                        target,
                                        block.getDefaultState(),
                                        veinSize))
                        .createDecoratedFeature(
                                BiomOreMod.COUNT_CHANCE_SURFACE_CONFIG_DECORATOR.configure(
                                        new CountChanceConfig(chance, count, bottomOffset, top - topOffSet, target)))
        );
    }

    public static void addThickVeinOre(Biome biome,
                                       Block block,
                                       float chance,
                                       int bottomOffset,
                                       int topOffSet,
                                       int top) {
        addThickVeinOre(
                biome,
                block,
                (int) Math.round(commonVeinSize.get(block) * AMP),
                Target.NATURAL_STONE,
                chance,
                1,
                bottomOffset,
                topOffSet,
                top);
    }

    public static void addSingleOre(Biome biome,
                                    Block block,
                                    float chance,
                                    int count,
                                    int bottomOffset,
                                    int topOffset,
                                    int top) {
        biome.addFeature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                BiomOreMod.BIOM_SINGLE_ORE_FEATURE
                        .configure(
                                new BiomOreSingleFeatureConfig(
                                        Target.NATURAL_STONE,
                                        block.getDefaultState()
                                )
                        ).createDecoratedFeature(
                        BiomOreMod.COUNT_CHANCE_HEIGHT_CONFIG_DECORATOR.configure(
                                new CountChanceConfig(chance, 2, count, top - topOffset, Target.NATURAL_STONE)
                        )
                )
        );
    }
}
