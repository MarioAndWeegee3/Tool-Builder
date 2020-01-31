package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@Mixin(LivingEntity.class)
public abstract class EntityDamageMixin extends Entity {

    public EntityDamageMixin(EntityType<?> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Inject(method = "computeFallDamage", at = @At("RETURN"), cancellable = true)
    private void modifyFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> ci) {
        Float damage = ci.getReturnValue().floatValue();

        for(ItemStack stack : getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                
                for(EffectInstance instance : armor.getEffects(stack)){
                    damage = instance.getEffect().modifyFallDamage(damage, armor, stack, instance.getLevel());
                }
            }
        }

        ci.setReturnValue(damage.intValue());
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