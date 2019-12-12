package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.material.*;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.SnowGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class Shears extends ShearsItem implements BuiltTool{
    public final BuiltToolMaterial material;

    private Shears(BuiltToolMaterial material, Settings settings) {
        super(settings);
        this.material = material;
    }

    public static Shears create(BuiltToolMaterial material){
        return new Shears(material, new Settings().maxCount(1).maxDamage(material.getDurability()));
    }

    @Override
    public String getType() {
        return "shears";
    }

    @Override
    public BuiltToolMaterial getMaterial() {
        return material;
    }

    @Override
    public String getTranslationKey() {
        return getTranslationName(material);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        addTooltips(tooltip, stack, material, ToolBuilder.toolStyle, ToolBuilder.effectStyle, ToolBuilder.modifierStyle);
    }

    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return super.getMiningSpeed(stack, state) * material.getMineSpeedModifier();
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        onHit(stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entity) {
        onMine(material, stack, state, world, pos, entity, this::shouldDropXp);
        return false;
    }

    public boolean shouldDropXp(BlockState state, ItemStack stack){
        if(getMiningSpeed(stack, state) == 15) return true;
        else return false;
    }

    @Override
    public boolean useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand) {
        if(player.world.isClient){
            return false;
        }

        if(entity instanceof SheepEntity){
            SheepEntity sheep = (SheepEntity)entity;
            if(!sheep.isSheared() && !sheep.isBaby()){
                sheep.dropItems();
                stack.damage(1, player, p->{
                    p.sendToolBreakStatus(hand);
                });
            }
        }

        if(entity instanceof SnowGolemEntity){
            SnowGolemEntity golem = (SnowGolemEntity)entity;
            if(golem.hasPumpkin()){
                golem.setHasPumpkin(false);
                stack.damage(1, player, p->{
                    p.sendToolBreakStatus(hand);
                });
            }
        }

        if(entity instanceof MooshroomEntity && ((MooshroomEntity)entity).getBreedingAge() >= 0){
            MooshroomEntity mooshroom = (MooshroomEntity)entity;
            mooshroom.dropStack(new ItemStack(mooshroom.getMooshroomType().getMushroomState().getBlock(), 5));
            mooshroom.remove();

            CowEntity cow = EntityType.COW.create(player.world);
            cow.setPositionAndAngles(mooshroom.getX(), mooshroom.getY(), mooshroom.getZ(), mooshroom.yaw, mooshroom.pitch);
            cow.setHealth(mooshroom.getHealth());
            cow.bodyYaw = mooshroom.bodyYaw;
            if (mooshroom.hasCustomName()) {
                cow.setCustomName(mooshroom.getCustomName());
                cow.setCustomNameVisible(mooshroom.isCustomNameVisible());
            }

            player.world.spawnEntity(cow);
            cow.playSound(SoundEvents.ENTITY_MOOSHROOM_SHEAR, 1.0F, 1.0F);

            stack.damage(1, player, p->{
                p.sendToolBreakStatus(hand);
            });
        }

        return false;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getPlayer().world;
        BlockState state = world.getBlockState(context.getBlockPos());
        if(!world.isClient){
            if(state.getBlock() == Blocks.PUMPKIN){
                Direction dir = getHorizontalDirection(context.getSide());
                BlockPos pos = context.getBlockPos().offset(dir);
                world.setBlockState(context.getBlockPos(), Blocks.CARVED_PUMPKIN.getDefaultState().with(HorizontalFacingBlock.FACING, dir));
                world.spawnEntity(new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, new ItemStack(Items.PUMPKIN_SEEDS, 4)));
            }
        }
        return ActionResult.PASS;
    }

    private Direction getHorizontalDirection(Direction dir){
        if(dir == Direction.UP || dir == Direction.DOWN){
            return Direction.NORTH;
        } else return dir;
    }

}