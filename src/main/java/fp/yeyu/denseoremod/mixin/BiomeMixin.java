package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.BiomOreMod;
import fp.yeyu.denseoremod.feature.GenerationUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
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

    private boolean printDebug = true;
    @Shadow
    @Final
    private Map<Integer, List<StructureFeature<?>>> structures;

    private static boolean canGenerate(WorldAccess world) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) return true;
        return BiomOreMod.BIOMORE != null && world.getLevelProperties().getGameRules().getBoolean(BiomOreMod.BIOMORE);
    }

    @Shadow
    public abstract Biome.Category getCategory();

    @Inject(method = "generateFeatureStep", at = @At("HEAD"), cancellable = true)
    public void generateFeatureStepMixinHead(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo ci) {
        Biome biome = (Biome) (Object) this;
        if (!canGenerate(((StructureAccessorWorld) structureAccessor).getWorld())) return;
        if (this.getCategory() == Biome.Category.THEEND || this.getCategory() == Biome.Category.NETHER || this.getCategory() == Biome.Category.NONE)
            return;
        GenerationUtil.generateFeatureStep(biome, structures, structureAccessor, random, region, chunkGenerator, populationSeed, pos, printDebug);
        printDebug = false;
        ci.cancel();
    }

}
