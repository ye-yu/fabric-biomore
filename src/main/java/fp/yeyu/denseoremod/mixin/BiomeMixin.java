package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.feature.BiomOreFeatures;
import fp.yeyu.denseoremod.BiomOreMod;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
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
    @Shadow
    @Final
    protected Map<GenerationStep.Feature, List<ConfiguredFeature<?, ?>>> features;

    @Shadow
    public abstract void addFeature(GenerationStep.Feature step, ConfiguredFeature<?, ?> configuredFeature);

    @Shadow
    public abstract Biome.Category getCategory();

    @Shadow
    public abstract Text getName();

    @Shadow
    @Final
    public static Logger LOGGER;

    @Inject(method = "generateFeatureStep", at = @At("HEAD"), cancellable = true)
    public void generateFeatureStepMixin(GenerationStep.Feature step, ChunkGenerator<? extends ChunkGeneratorConfig> chunkGenerator, IWorld world, long seed, ChunkRandom random, BlockPos pos, CallbackInfo ci) {
        if (world.getLevelProperties().getGeneratorType() != BiomOreMod.DENSE_ORE) return;
        if (this.getCategory() == Biome.Category.THEEND || this.getCategory() == Biome.Category.NETHER || this.getCategory() == Biome.Category.NONE)
            return;
        this.features.get(GenerationStep.Feature.UNDERGROUND_ORES).clear();
        BiomOreFeatures.methods.getOrDefault(this.getCategory().getName(), BiomOreFeatures::none).accept(this);
    }

}
