package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import marioandweegee3.toolbuilder.common.tools.tooltypes.Shears;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.TripwireBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(TripwireBlock.class)
public abstract class TripwireShearMixin extends Block {

    public TripwireShearMixin(Settings block$Settings_1) {
        super(block$Settings_1);
    }

    @Inject(method = "onBreak", at = @At("HEAD"), cancellable = true)
    private void breakHandler(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci){
        if(world.isClient || player.getMainHandStack().isEmpty()){
            return;
        }

        Item item = player.getMainHandStack().getItem();
        if(item instanceof Shears){
            world.setBlockState(pos, state.with(TripwireBlock.DISARMED, true), 4);
        }
    }

}