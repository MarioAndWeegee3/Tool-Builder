package marioandweegee3.toolbuilder.api.loot;

import java.util.Random;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.BuiltTool;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

public class BuiltToolLootCondition implements LootCondition {
    protected String toolType;
    protected boolean checkRng;

    public BuiltToolLootCondition(String toolType, boolean checkRng){
        this.toolType = toolType;
        this.checkRng = checkRng;
    }

    @Override
    public boolean test(LootContext context) {
        ToolBuilder.logger.info("loot check for BuiltToolLootCondition");
        Entity entity = context.get(LootContextParameters.KILLER_ENTITY);

        if(!(entity instanceof LivingEntity)){
            return false;
        }

        ItemStack stack = ((LivingEntity)entity).getMainHandStack();

        if(stack.isEmpty()){
            return false;
        }

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            if(tool.getType() == this.toolType){
                if(checkRng){
                    return onSuccess(stack);
                } else {
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    protected boolean onSuccess(ItemStack stack){
        Random rng = new Random();

        int chanceDenominator = 20;

        int lootingLevel = EnchantmentHelper.getLevel(Enchantments.LOOTING, stack);

        chanceDenominator -= lootingLevel * 3;

        if(rng.nextInt(chanceDenominator) == 0){
            return true;
        } else return false;
    }

}