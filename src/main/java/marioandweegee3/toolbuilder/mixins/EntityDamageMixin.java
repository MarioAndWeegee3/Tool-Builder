package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
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
    public abstract StatusEffectInstance getStatusEffect(StatusEffect s);

    @Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
    private void fallDamage(float f1, float f2, CallbackInfo ci){
        boolean cancelled = false;
        for(ItemStack stack : getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                cancelled = true;
                break;
            }
        }

        if(!cancelled) return;
        else ci.cancel();


        super.handleFallDamage(f1, f2);
        StatusEffectInstance statusEffectInstance_1 = this.getStatusEffect(StatusEffects.JUMP_BOOST);
        float f3 = statusEffectInstance_1 == null ? 0.0F : (float)(statusEffectInstance_1.getAmplifier() + 1);
        float damage = MathHelper.ceil((f1 - 3.0F - f3) * f2); //This is changed from an int to a float in order for the damage multiplier to actually work.

        //This is the only new part; everything else is the same.
        for(ItemStack stack : getArmorItems()){
            if(stack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                if (armor.getEffects(stack).contains(Effects.LIGHT)) {
                    damage *= ConfigHandler.INSTANCE.getLightFallDamageMultiplier();
                    if (damage > 0) {
                        stack.damage(1, world.random, null);
                    }
                }
                if (armor.getEffects(stack).contains(Effects.BOUNCY) && armor.getSlotType() == EquipmentSlot.FEET) {
                    if (damage > 0 && ConfigHandler.INSTANCE.bouncyDamagesArmor()) {
                        stack.damage(1, world.random, null);
                    }
                    damage = 0;
                }
            }
        }

        if (damage > 0) {
            this.playSound(this.getFallSound((int) damage), 1.0F, 1.0F);
            this.damage(DamageSource.FALL, damage);
            int int_2 = MathHelper.floor(this.x);
            int int_3 = MathHelper.floor(this.y - 0.20000000298023224D);
            int int_4 = MathHelper.floor(this.z);
            BlockState blockState_1 = this.world.getBlockState(new BlockPos(int_2, int_3, int_4));
            if (!blockState_1.isAir()) {
                BlockSoundGroup blockSoundGroup_1 = blockState_1.getSoundGroup();
                this.playSound(blockSoundGroup_1.getFallSound(), blockSoundGroup_1.getVolume() * 0.5F, blockSoundGroup_1.getPitch() * 0.75F);
            }
        }
    }

    @Inject(method = "applyEnchantmentsToDamage", at = @At("RETURN"), cancellable = true)
    private void handleDamage(DamageSource source, float f, CallbackInfoReturnable<Float> ci){
        float damage = ci.getReturnValueF();
        Entity entity = source.getAttacker();
        if(entity instanceof LivingEntity){
            LivingEntity attacker = (LivingEntity)entity;
            for(ItemStack stack : getArmorItems()){
                if(stack.getItem() instanceof BuiltArmorItem){
                    BuiltArmorItem armor = (BuiltArmorItem) stack.getItem();
                    if (armor.getEffects(stack).contains(Effects.HOLY) && attacker.isUndead()) {
                        damage -= ConfigHandler.INSTANCE.getHolyDamage() * 0.25;
                    }

                    if (armor.getEffects(stack).contains(Effects.MAGICAL) && source.getMagic()) {
                        damage *= 0.9;
                    }

                    if (armor.getEffects(stack).contains(Effects.BOUNCY)
                            && armor.getSlotType() == EquipmentSlot.HEAD
                            && source == DamageSource.FLY_INTO_WALL) {
                        if (damage > 0 && ConfigHandler.INSTANCE.bouncyDamagesArmor()) {
                            stack.damage(1, world.random, null);
                        }
                        damage = 0;
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