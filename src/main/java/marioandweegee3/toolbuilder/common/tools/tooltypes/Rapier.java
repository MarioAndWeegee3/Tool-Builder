package marioandweegee3.toolbuilder.common.tools.tooltypes;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Rapier extends SwordItem implements BuiltTool {
    private static float speed = ToolValues.RAPIER.getSpeed();
    private static float damage = ToolValues.RAPIER.getDamage();

    private BuiltToolMaterial material;

    protected Rapier(BuiltToolMaterial material, Settings settings) {
        super(material, (int) damage, BuiltTool.getAttackSpeed(material, speed), settings);
        this.material = material;
    }

    public static Item create(BuiltToolMaterial material) {
        return new Rapier(new RapierMaterial(material), new Settings());
    }

    @Override
    public String getType() {
        return "rapier";
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
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        onHit(stack, target, attacker);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
        onMine(material, stack, state, world, pos, miner, this::shouldDropXp);

        return super.postMine(stack, world, state, pos, miner);
    }

    public boolean shouldDropXp(BlockState state, ItemStack stack){
        if(getMiningSpeed(stack, state) == 15) return true;
        else return false;
    }

    @Override
    public int getMaxUseTime(ItemStack itemStack_1) {
        return 200;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(player.isFallFlying()) return new TypedActionResult<ItemStack>(ActionResult.PASS, stack);

        player.setCurrentHand(hand);
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean isSelected) {
        if(world.isClient && entity instanceof ClientPlayerEntity){
            ClientPlayerEntity player = (ClientPlayerEntity)entity;
            ItemStack activeStack = player.getActiveItem();
            if(!activeStack.isEmpty() && activeStack.getItem() == this){
                player.horizontalSpeed *= 0.9f * 5;
                player.forwardSpeed *= 0.9f * 5;
            }
        }

        super.inventoryTick(stack, world, entity, slot, isSelected);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        int time = this.getMaxUseTime(stack) - timeLeft;
        if(time > 5){
            if(entity instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity)entity;
                player.addExhaustion(0.2f);
                player.getItemCooldownManager().set(stack.getItem(), 3);
            }
            entity.setSprinting(true);

            float vertical = (float) (0.02 * time + 0.2);
            if(vertical > 0.56f){
                vertical = 0.56f;
            }
            
            float horizontal = 0.05f * time;
            if(horizontal > 0.925f){
                horizontal = 0.925f;
            }

            if(EffectInstance.toEffectSet(getEffects(stack)).contains(Effects.LIGHT)){
                horizontal += 0.1f;
                vertical += 0.02f;
            }

            for(ItemStack armorStack : entity.getArmorItems()){
                if(armorStack.getItem() instanceof BuiltArmorItem){
                    BuiltArmorItem armor = (BuiltArmorItem) armorStack.getItem();
                    if(EffectInstance.toEffectSet(armor.getEffects(armorStack)).contains(Effects.LIGHT)){
                        horizontal += 0.1f;
                        vertical += 0.02f;
                        break;
                    }
                }
            }

            entity.setVelocity(
                -MathHelper.sin((float) (entity.yaw / 180 * Math.PI)) * MathHelper.cos((float) (entity.pitch / 180 * Math.PI)) * horizontal,
                entity.getVelocity().y + vertical, 
                MathHelper.cos((float) (entity.yaw / 180 * Math.PI)) * MathHelper.cos((float) (entity.pitch / 180 * Math.PI)) * horizontal
            );

            entity.fallDistance = 0;
        }

        super.onStoppedUsing(stack, world, entity, timeLeft);
    }

    public static class RapierMaterial extends BuiltToolMaterial {

        public RapierMaterial(BuiltToolMaterial base) {
            super(base.handle, base.head, base.getName(), base.isGripped);
        }

        @Override
        public int getDurability() {
            return (int) (super.getDurability() * 0.95f);
        }

    }
}