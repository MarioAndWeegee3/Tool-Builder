package marioandweegee3.toolbuilder.common.blocks;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.TorchBlock;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class Torch extends TorchBlock {
    private Torch(Block.Settings settings){
        super(settings);
    }

    public static Torch create(BlockSoundGroup sound, Material material, Identifier drops){
        return new Torch(FabricBlockSettings.of(material).breakByHand(true).lightLevel(15).breakInstantly().drops(drops).noCollision().sounds(sound).build());
    }
}