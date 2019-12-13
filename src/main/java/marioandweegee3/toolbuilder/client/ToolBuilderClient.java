package marioandweegee3.toolbuilder.client;

import com.swordglowsblue.artifice.api.Artifice;

import marioandweegee3.toolbuilder.client.models.TBModels;
import marioandweegee3.toolbuilder.common.blocks.BlockTorches;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class ToolBuilderClient implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        Artifice.registerAssets("toolbuilder:items", pack ->{
            TBModels.addModels(pack);
        });

        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), BlockTorches.stone_torch, BlockTorches.wall_stone_torch);
    }
}