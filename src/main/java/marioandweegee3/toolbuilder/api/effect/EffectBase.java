package marioandweegee3.toolbuilder.api.effect;

import java.util.List;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.XpDropCheck;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.common.tools.tooltypes.Bow;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext.Builder;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class EffectBase implements Effect {

    @Override
    public TranslatableText getTranslationName() {
        return new TranslatableText("text.toolbuilder.error");
    }

    @Override
    public String getName() {
        return "error";
    }

    @Override
    public Identifier getID() {
        return ToolBuilder.makeID("error");
    }

    @Override
    public float getAdditonalAttackDamage(ItemStack stack, EntityGroup group, int level) {
        return 0;
    }

    @Override
    public List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, Builder builder, List<ItemStack> drops, int level) {
        return drops;
    }

    @Override
    public void onEntityLandOnBlock(BlockView view, BuiltArmorItem armor, LivingEntity entity, int level) {

    }

    @Override
    public boolean bypassesArmor(String name, LivingEntity source, int level) {
        return false;
    }

    @Override
    public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel) {
        return 0;
    }

    @Override
    public float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level) {
        return fallDamage;
    }

    @Override
    public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack,
            int level) {
        return baseDamage;
    }

    @Override
    public int modifyFireDuration(int fireDuration, LivingEntity entity, BuiltArmorItem armor, ItemStack stack,
            int level) {
        return fireDuration;
    }

    @Override
    public void onInventoryTick(ItemStack stack, LivingEntity holder, int level) {

    }

    @Override
    public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level) {
        
    }

    @Override
    public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level) {

    }

    @Override
    public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos,
            LivingEntity miner, XpDropCheck dropCheck, int level) {

    }

    @Override
    public int getMaxLevel() {
        return 0;
    }

    @Override
    public int arrowFireTimeModifier(ItemStack stack, Bow bow, int level) {
        return 0;
    }

    @Override
    public int modifyDurability(int durability) {
        return durability;
    }

    @Override
    public float modifyArrowVelocity(float velocity, int level) {
        return velocity;
    }

    @Override
    public float getAttackSpeedModifier(int level) {
        return 0;
    }

}