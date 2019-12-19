package marioandweegee3.toolbuilder.api.effect;

import java.util.List;

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
import net.minecraft.loot.context.LootContext;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public interface Effect {
    public TranslatableText getTranslationName();
    public String getName();
    public Identifier getID();
    public int getMaxLevel();

    public float getAdditonalAttackDamage(ItemStack stack, EntityGroup group, int level);

    public List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, LootContext.Builder builder, List<ItemStack> drops, int level);

    public void onEntityLandOnBlock(BlockView view, BuiltArmorItem armor, LivingEntity entity, int level);

    public boolean bypassesArmor(String name, LivingEntity source, int level);

    public int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel);

    public float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level);

    public float modifyDamageRecieved(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack, int level);

    public int modifyFireDuration(int fireDuration, LivingEntity entity, BuiltArmorItem armor, ItemStack stack, int level);

    public void onInventoryTick(ItemStack stack, LivingEntity holder, int level);

    public void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level);

    public void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level);

    public void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner, XpDropCheck dropCheck, int level);

    public int arrowFireTimeModifier(ItemStack stack, Bow bow, int level);

    public int modifyDurability(int durability);

    public float modifyArrowVelocity(float velocity, int level);

    public float getAttackSpeedModifier(int level);
}