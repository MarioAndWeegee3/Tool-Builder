package marioandweegee3.toolbuilder.common.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class WallTorch extends WallTorchBlock {

    private WallTorch(Block.Settings settings) {
        super(settings);
    }
    
    public static WallTorch create(BlockSoundGroup sound, Material material, Identifier drops){
        return new WallTorch(FabricBlockSettings.of(material).breakByHand(true).lightLevel(15).breakInstantly().noCollision().drops(drops).sounds(sound).build());
    }
}