package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

@Mixin(ProtectionEnchantment.class)
public abstract class FireTimeMixin {

    @Inject(method = "transformFireDuration", at = @At("RETURN"), cancellable = true)
    private static void handleFire(LivingEntity entity, int duration, CallbackInfoReturnable<Integer> ci){
        int fireTime = ci.getReturnValueI();
        boolean wearingFlammable = false;
        for(ItemStack stack : entity.getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                if (armor.getEffects(stack).contains(Effects.FLAMMABLE)) {
                    wearingFlammable = true;
                    break;
                }
            }
        }

        if(wearingFlammable){
            fireTime *= ConfigHandler.INSTANCE.getFlammableTimeMultiplier();
        }

        ci.setReturnValue(fireTime);
    }
}