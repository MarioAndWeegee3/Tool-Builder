package marioandweegee3.toolbuilder.common.blocks.gripping_station;

import marioandweegee3.toolbuilder.api.BuiltTool;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GripStationBlock extends Block implements BlockEntityProvider {
    public static final GripStationBlock BLOCK = new GripStationBlock();

    public GripStationBlock() {
        super(FabricBlockSettings.copy(Blocks.SMITHING_TABLE).build());
    }

    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new GripStationEntity();
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult result) {
        ItemStack stack = player.getStackInHand(hand);
        if(!world.isClient){
            GripStationEntity entity = (GripStationEntity)world.getBlockEntity(pos);
            if(!player.isSneaking()){
                if(stack.getItem() instanceof BuiltTool){
                    if(entity.addTool(stack)){
                        stack.setCount(0);
                    }
                    
                } else if(stack.getItem() == Items.LEATHER){
                    int amount = entity.addLeather(stack);
                    stack.setCount(amount);
                }
            } else {
                entity.giveLeather(player);
                entity.giveResultTool(player);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState state2, boolean moved) {
        GripStationEntity entity = (GripStationEntity) world.getBlockEntity(pos);
        entity.dropItems();
        super.onBlockRemoved(state, world, pos, state2, moved);
    }

    

}