package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Mixin(ProtectionEnchantment.class)
public abstract class FireTimeMixin {

    @Inject(method = "transformFireDuration", at = @At("RETURN"), cancellable = true)
    private static void handleFire(LivingEntity entity, int duration, CallbackInfoReturnable<Integer> ci){
        int fireTime = ci.getReturnValueI();
        for(ItemStack stack : entity.getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                for(EffectInstance instance : armor.getEffects(stack)) {
                    fireTime = instance.getEffect().modifyFireDuration(fireTime, entity, armor, stack, instance.getLevel());
                }
            }
        }

        ci.setReturnValue(fireTime);
    }
}