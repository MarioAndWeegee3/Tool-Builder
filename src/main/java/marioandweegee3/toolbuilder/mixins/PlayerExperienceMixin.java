package marioandweegee3.toolbuilder.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import marioandweegee3.toolbuilder.api.effect.EffectInstance;
import marioandweegee3.toolbuilder.api.item.BuiltArmorItem;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class PlayerExperienceMixin extends LivingEntity {

    protected PlayerExperienceMixin(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Shadow
    private float experienceProgress;

    @Shadow
    private int totalExperience;

    @Shadow
    private int experienceLevel;

    @Shadow
    protected abstract void addScore(int i);

    @Shadow
    protected abstract void addExperienceLevels(int int_1);

    @Shadow
    protected abstract int getNextLevelExperience();

    @Inject(at = @At("HEAD"), method = "addExperience", cancellable = true)
    private void expHandler(int amount, CallbackInfo ci){
        for(ItemStack armorStack : this.getArmorItems()){
            if(armorStack.getItem() instanceof BuiltArmorItem){
                BuiltArmorItem armor = (BuiltArmorItem) armorStack.getItem();
                if(EffectInstance.toEffectSet(armor.getEffects(armorStack)).contains(Effects.EXPERIENCE)){
                    tb_handleXp(amount + 2);
                    ci.cancel();
                    return;
                }
            }
        }
    }

    private void tb_handleXp(int int_1){
        this.addScore(int_1);
        this.experienceProgress += (float)int_1 / (float)this.getNextLevelExperience();
        this.totalExperience = MathHelper.clamp(this.totalExperience + int_1, 0, Integer.MAX_VALUE);

        while(this.experienceProgress < 0.0F) {
            float float_1 = this.experienceProgress * (float)this.getNextLevelExperience();
            if (this.experienceLevel > 0) {
                this.addExperienceLevels(-1);
                this.experienceProgress = 1.0F + float_1 / (float)this.getNextLevelExperience();
            } else {
                this.addExperienceLevels(-1);
                this.experienceProgress = 0.0F;
            }
        }

        while(this.experienceProgress >= 1.0F) {
            this.experienceProgress = (this.experienceProgress - 1.0F) * (float)this.getNextLevelExperience();
            this.addExperienceLevels(1);
            this.experienceProgress /= (float)this.getNextLevelExperience();
        }
    }

}