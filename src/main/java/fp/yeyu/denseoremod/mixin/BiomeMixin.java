package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreFeatures;
import fp.yeyu.denseoremod.BiomOreMod;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.decorator.ChanceRangeDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(Biome.class)
public abstract class BiomeMixin {
    @Shadow @Final protected Biome.Category category;
    @Shadow @Final protected Map<GenerationStep.Feature, List<ConfiguredFeature<?, ?>>> features;

    @Shadow public abstract void addFeature(GenerationStep.Feature step, ConfiguredFeature<?, ?> configuredFeature);
    @Shadow public abstract Biome.Category getCategory();

    @Shadow public abstract Text getName();

    @Shadow @Final public static Logger LOGGER;

    @Inject(method="generateFeatureStep", at=@At("HEAD"), cancellable = true)
    public void generateFeatureStepMixin(GenerationStep.Feature step, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, IWorld world, long seed, ChunkRandom random, BlockPos pos, CallbackInfo ci) {
        if (world.getLevelProperties().getGeneratorType() != BiomOreMod.DENSE_ORE) return;
        this.features.get(GenerationStep.Feature.UNDERGROUND_ORES).clear();
        BiomOreFeatures.methods.getOrDefault(this.getCategory().getName(), BiomOreFeatures::none).accept(this);
        LOGGER.info("BiomOre generator for biome: " + this.getName().asString() + " category: " + this.getCategory().getName());

        // from DefaultBiomeFeatures.addMineables
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIRT.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 256))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GRAVEL.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(8, 0, 0, 256))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GRANITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIORITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.ANDESITE.getDefaultState(), 33)).createDecoratedFeature(Decorator.COUNT_RANGE.configure(new RangeDecoratorConfig(10, 0, 0, 80))));

        final int amp = 5;
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.COAL_ORE.getDefaultState(), 17 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.1f, 0, 0, 128))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.IRON_ORE.getDefaultState(), 9 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.05f, 0, 0, 64))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.GOLD_ORE.getDefaultState(), 9 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.05f, 0, 0, 32))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.REDSTONE_ORE.getDefaultState(), 8 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.08f, 0, 0, 16))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.DIAMOND_ORE.getDefaultState(), 8 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.01f, 0, 0, 16))));
        this.addFeature(GenerationStep.Feature.UNDERGROUND_ORES, net.minecraft.world.gen.feature.Feature.ORE.configure(new OreFeatureConfig(OreFeatureConfig.Target.NATURAL_STONE, Blocks.LAPIS_ORE.getDefaultState(), 7 * amp)).createDecoratedFeature(Decorator.CHANCE_RANGE.configure(new ChanceRangeDecoratorConfig(0.1f,0, 0, 16))));
    }

}
