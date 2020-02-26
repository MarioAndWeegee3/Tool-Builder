package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentMixin {
    
    @Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
    private static void handleLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> ci){
        int level = ci.getReturnValueI();

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            for(EffectInstance effect : tool.getEffects(stack)) {
                level += effect.getEffect().enchantmentLevelIncrement(enchantment, stack, effect.getLevel());
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            for(EffectInstance effect : armor.getEffects(stack)) {
                level += effect.getEffect().enchantmentLevelIncrement(enchantment, stack, effect.getLevel());
            }
        }

        ci.setReturnValue(level);
    }

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private static void getDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> ci){
        float baseDamage = ci.getReturnValue();

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool)stack.getItem();

            for(EffectInstance effect : tool.getEffects(stack)){
                baseDamage += effect.getEffect().getAdditionalAttackDamage(stack, group, effect.getLevel());
            }
        }

        ci.setReturnValue(baseDamage);
    }
}