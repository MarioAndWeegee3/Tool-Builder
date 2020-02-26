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
    TranslatableText getTranslationName();
    String getName();
    Identifier getID();
    int getMaxLevel();

    float getAdditionalAttackDamage(ItemStack stack, EntityGroup group, int level);

    List<ItemStack> modifyBlockDrops(BlockState state, BuiltTool tool, ItemStack toolStack, LootContext.Builder builder, List<ItemStack> drops, int level);

    boolean onEntityLandOnBlock(BlockView view, BuiltArmorItem armor, LivingEntity entity, int level);

    boolean bypassesArmor(String name, LivingEntity source, int level);

    int enchantmentLevelIncrement(Enchantment enchantment, ItemStack stack, int effectLevel);

    float modifyFallDamage(float fallDamage, BuiltArmorItem armor, ItemStack stack, int level);

    float modifyDamageReceived(float baseDamage, DamageSource source, BuiltArmorItem armor, ItemStack stack, int level);

    int modifyFireDuration(int fireDuration, LivingEntity entity, BuiltArmorItem armor, ItemStack stack, int level);

    void onInventoryTick(ItemStack stack, LivingEntity holder, int level);

    void onArmorInventoryTick(ItemStack stack, LivingEntity holder, int level);

    void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker, int level);

    void postMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner, XpDropCheck dropCheck, int level);

    int arrowFireTimeModifier(ItemStack stack, Bow bow, int level);

    int modifyDurability(int durability);

    float modifyArrowVelocity(float velocity, int level);

    float getAttackSpeedModifier(int level);
}