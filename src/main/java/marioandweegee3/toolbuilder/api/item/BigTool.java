package marioandweegee3.toolbuilder.api.item;

import java.util.Random;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.XpDropCheck;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.RayTraceContext.FluidHandling;

public interface BigTool extends BuiltTool {
    public static final Random random = new Random();

    @Override
    default int getNumModifiers(ItemStack stack) {
        return BuiltTool.super.getNumModifiers(stack) + 1;
    }

    public default void attemptBreakNeighbors(World world, BlockPos pos, PlayerEntity player, boolean checkHarvestLevel){
        world.setBlockState(pos, Blocks.GLASS.getDefaultState());
        HitResult result = calcHitResult(world, player, FluidHandling.ANY);
        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        if(result.getType() == HitResult.Type.BLOCK){
            BlockHitResult blockHitResult = (BlockHitResult)result;
            Direction dir = blockHitResult.getSide();
            ItemStack stack = player.getMainHandStack();

            int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, stack);
            int silkLevel = EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack);

            for(int a = -1; a <= 1; a++){
                for(int b = -1; b <= 1; b++){
                    if(a == 0 && b == 0) continue;

                    BlockPos target = null;

                    if (dir == Direction.UP    || dir == Direction.DOWN)  target = pos.add(a, 0, b);
                    if (dir == Direction.NORTH || dir == Direction.SOUTH) target = pos.add(a, b, 0);
                    if (dir == Direction.EAST  || dir == Direction.WEST)  target = pos.add(0, a, b);

                    attemptBreak(world, target, player, fortuneLevel, silkLevel, checkHarvestLevel);
                }
            }
        }
    }

    public default void attemptBreak(World world, BlockPos pos, PlayerEntity player, int fortuneLevel, int silkLevel, boolean checkHarvestLevel){
        BlockState state = world.getBlockState(pos);
        
        boolean validHarvest = !checkHarvestLevel || player.getMainHandStack().isEffectiveOn(state);
        boolean isEffective = isEffectiveOn(state);
        boolean witherImmune = BlockTags.WITHER_IMMUNE.contains(state.getBlock());

        if(validHarvest && isEffective && !witherImmune){
            world.breakBlock(pos, false);

            BlockEntity be = world.getBlockEntity(pos);

            Block.dropStacks(state, world, pos, be, player, player.getMainHandStack());

            player.getMainHandStack().damage(1, player, (p) -> {
                p.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
            });
            
            this.onMine(this.getMaterial(), player.getMainHandStack(), state, world, pos, player, this.getDropCheck());
        }
    }

    public XpDropCheck getDropCheck();

    public boolean isEffectiveOn(BlockState state);

    public static HitResult calcHitResult(World world, PlayerEntity player, FluidHandling fluidHandling) {
        float playerPitch = player.pitch;
        float playerYaw = player.yaw;
        Vec3d playerCamera = player.getCameraPosVec(1.0F);
        float f3 = MathHelper.cos(-playerYaw * 0.017453292F - 3.1415927F);
        float f4 = MathHelper.sin(-playerYaw * 0.017453292F - 3.1415927F);
        float f5 = -MathHelper.cos(-playerPitch * 0.017453292F);
        float f6 = MathHelper.sin(-playerPitch * 0.017453292F);
        float f7 = f4 * f5;
        float f9 = f3 * f5;
        Vec3d result = playerCamera.add((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f9 * 5.0D);
        return world.rayTrace(
                new RayTraceContext(playerCamera, result, RayTraceContext.ShapeType.OUTLINE, fluidHandling, player));
    }
}