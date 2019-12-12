package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

@Mixin(EnchantmentHelper.class)
public abstract class EnchantmentMixin {
    
    @Inject(method = "getLevel", at = @At("RETURN"), cancellable = true)
    private static void handleLevel(Enchantment enchantment, ItemStack stack, CallbackInfoReturnable<Integer> ci){
        int level = ci.getReturnValueI();

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool) stack.getItem();
            if(enchantment == Enchantments.UNBREAKING && tool.getEffects(stack).contains(Effects.RESILIENT)){
                level++;
            }
            if(enchantment == Enchantments.EFFICIENCY && tool.getEffects(stack).contains(Effects.GLIMMERING)){
                CompoundTag tag = stack.getOrCreateTag();
                int glimmers = 0;
                if(tag.contains(Effects.glimmerNBTtag)){
                    glimmers = tag.getInt(Effects.glimmerNBTtag);
                }

                if(glimmers == 1){
                    level++;
                }
            }
            if(enchantment == Enchantments.FORTUNE && tool.getEffects(stack).contains(Effects.ROYAL)){
                level++;
            }
        }

        if(stack.getItem() instanceof BuiltArmorItem){
            BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
            if(enchantment == Enchantments.UNBREAKING && armor.getEffects(stack).contains(Effects.RESILIENT)){
                level++;
            }
        }

        ci.setReturnValue(level);
    }

    @Inject(method = "getAttackDamage", at = @At("RETURN"), cancellable = true)
    private static void getDamage(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> ci){
        float baseDamage = ci.getReturnValue();

        if(stack.getItem() instanceof BuiltTool){
            BuiltTool tool = (BuiltTool)stack.getItem();

            for(Effect effect : tool.getEffects(stack)){
                baseDamage = effect.getAttackDamage(stack, group, baseDamage);
            }
        }

        ci.setReturnValue(baseDamage);
    }
}