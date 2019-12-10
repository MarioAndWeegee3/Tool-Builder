package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import marioandweegee3.toolbuilder.api.BuiltTool;
import marioandweegee3.toolbuilder.common.effect.Effects;
import marioandweegee3.toolbuilder.util.SetBypassArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

@Mixin(EntityDamageSource.class)
public abstract class BypassArmorMixin extends DamageSource implements SetBypassArmor{

    protected BypassArmorMixin(String string_1) {
        super(string_1);
    }

    @Override
    public DamageSource set() {
        return this.setBypassesArmor();
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    private void setBypassArmor(String name, Entity source, CallbackInfo ci){
        if(source != null){
            if(source instanceof LivingEntity){
                
                if(((LivingEntity)source).getStackInHand(Hand.MAIN_HAND).getItem() instanceof BuiltTool){
                    ItemStack stack = ((LivingEntity)source).getStackInHand(Hand.MAIN_HAND);
                    BuiltTool tool = (BuiltTool)stack.getItem();

                    if(tool.getEffects(stack).contains(Effects.MAGICAL)){
                        ((SetBypassArmor)this).set();
                        this.setUsesMagic();
                    }
                }
            }
        }
    }

}