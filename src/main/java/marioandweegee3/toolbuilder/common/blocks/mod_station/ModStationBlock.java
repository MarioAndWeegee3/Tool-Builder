package marioandweegee3.toolbuilder.common.blocks.mod_station;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.item.Modifier;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class ModStationBlock extends Block implements BlockEntityProvider {
    public static final ModStationBlock BLOCK = new ModStationBlock();

    public ModStationBlock() {
        super(FabricBlockSettings.copy(Blocks.SMITHING_TABLE).build());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new ModStationEntity();
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState state2, boolean moved) {
        ModStationEntity entity = (ModStationEntity) world.getBlockEntity(pos);
        entity.dropItems();
        super.onBlockRemoved(state, world, pos, state2, moved);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        ItemStack stack = player.getStackInHand(hand);
        if(!world.isClient){
            ModStationEntity entity = (ModStationEntity)world.getBlockEntity(pos);
            if(!player.isSneaking()){
                if(stack.getItem() instanceof BuiltTool){
                    if(entity.addTool(stack)){
                        stack.setCount(0);
                    }
                    
                } else if(stack.getItem() instanceof Modifier){
                    int amount = entity.addModifier(stack);
                    stack.setCount(amount);
                }
            } else {
                entity.giveInventory(player);
            }
        }
        return ActionResult.SUCCESS;
    }

}