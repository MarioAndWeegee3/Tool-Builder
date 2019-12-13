package marioandweegee3.toolbuilder.common.blocks;

import marioandweegee3.toolbuilder.ToolBuilder;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class BlockTorches{
    public static Torch stone_torch = Torch.create(BlockSoundGroup.STONE, Material.PART, ToolBuilder.makeID("blocks/stone_torch"));
    public static WallTorch wall_stone_torch = WallTorch.create(BlockSoundGroup.STONE, Material.PART, ToolBuilder.makeID("blocks/stone_torch"));
}