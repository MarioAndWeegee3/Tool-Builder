package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class EntityDamageMixin extends Entity {

    public EntityDamageMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    protected ItemStack activeItemStack;

    @Shadow
    protected abstract SoundEvent getFallSound(int i);

    @Shadow
    protected abstract void method_23328();

    @Shadow
    public abstract StatusEffectInstance getStatusEffect(StatusEffect s);

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void fallDamage(float f1, float f2, CallbackInfoReturnable<Boolean> ci){
        boolean cancelled = false;
        for(ItemStack stack : getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                cancelled = true;
                break;
            }
        }

        if(!cancelled) return;
        else ci.cancel();


        boolean b1 = super.handleFallDamage(f1, f2);
        StatusEffectInstance statusEffectInstance_1 = this.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f3 = statusEffectInstance_1 == null ? 0.0F : (float)(statusEffectInstance_1.getAmplifier() + 1);
        Float damage = (float) MathHelper.ceil((f1 - 3.0F - f3) * f2); //This is changed from an int to a float in order for the damage multiplier to actually work.

        //This is the only new part; everything else is the same.
        for(ItemStack stack : getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                
                for(EffectInstance instance : armor.getEffects(stack)){
                    damage = instance.getEffect().modifyFallDamage(damage, armor, stack, instance.getLevel());
                }

                if(damage <= 0){
                    b1 = false;
                }
            }
        }

        if (damage > 0) {
            this.playSound(this.getFallSound(damage.intValue()), 1.0F, 1.0F);
            this.method_23328();
            this.damage(DamageSource.FALL, damage);
            ci.setReturnValue(true);
        }
        ci.setReturnValue(b1);
    }

    @Inject(method = "applyEnchantmentsToDamage", at = @At("RETURN"), cancellable = true)
    private void handleDamage(DamageSource source, float f, CallbackInfoReturnable<Float> ci){
        float damage = ci.getReturnValueF();
        Entity entity = source.getAttacker();
        if(entity instanceof LivingEntity){
            for(ItemStack stack : getArmorItems()){
                if(stack.getItem() instanceof BuiltArmorItem){
                    BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                    
                    for(EffectInstance instance : armor.getEffects(stack)) {
                        damage = instance.getEffect().modifyDamageRecieved(damage, source, armor, stack, instance.getLevel());
                    }
                }
            }
        }

        if(damage < 0){
            damage = 0;
        }

        ci.setReturnValue(damage);
    }
}