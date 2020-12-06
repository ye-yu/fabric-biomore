package fp.yeyu.denseoremod.mixin;

import fp.yeyu.denseoremod.mixinutil.BlockConfigurationProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.EmeraldOreFeatureConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EmeraldOreFeatureConfig.class)
public class EmeraldOreFeatureConfigMixin implements BlockConfigurationProvider {

    @Shadow
    @Final
    public BlockState state;

    @Override
    public Block getGenerationBlock() {
        return state.getBlock();
    }
}
