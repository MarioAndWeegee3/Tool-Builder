package marioandweegee3.toolbuilder.api;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import marioandweegee3.toolbuilder.common.effect.MagneticEffect;
import marioandweegee3.toolbuilder.common.effect.Effects;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface BuiltTool {
    public default String getTranslationName(BuiltToolMaterial material){
        return getType()+"."+getModName()+"."+material.head.getName();
    }
    public String getType();
    public BuiltToolMaterial getMaterial();
    public default String getModName(){
        return "toolbuilder";
    }

    public default Set<Effect> getEffects(ItemStack stack){
        Set<Effect> effects = new HashSet<>(0);
        effects.addAll(getMaterial().getEffects());
        effects.addAll(getModifierEffects(stack));
        return effects;
    }

    public default Set<Effect> getModifierEffects(ItemStack stack){
        Set<Effect> effects = new HashSet<>(0);
        CompoundTag tag = stack.getOrCreateTag();
        ListTag effectsTag = tag.getList(Effects.effectNBTtag, 8);
        for(Tag tag2 : effectsTag){
            if(!(tag2 instanceof StringTag)) continue;
            
            StringTag effectTag = (StringTag)tag2;
            Effect effect = TBRegistries.EFFECTS.get(new Identifier(effectTag.asString()));
            if(effect != null){
                effects.add(effect);
            }
        }
        return effects;
    }

    public default void removeEffects(ItemStack stack, Set<Effect> effects){
        CompoundTag tag = stack.getOrCreateTag();
        ListTag effectsTag = tag.getList(Effects.effectNBTtag, 8);

        int numMods = tag.getInt("num_modifiers");
        
        for(Effect effect : effects){
            effectsTag.removeIf(t -> {
                if(!(t instanceof StringTag)) return false;

                StringTag effectTag = (StringTag) t;

                Effect e = TBRegistries.EFFECTS.get(new Identifier(effectTag.asString()));
                if(effect.equals(e)){
                    return true;
                }

                return false;
            });

            numMods--;
        }
        tag.put(Effects.effectNBTtag, effectsTag);
        tag.putInt("num_modifiers", numMods);
        stack.setTag(tag);
    }

    public default int getNumModifiers(ItemStack stack){
        return (getEffects(stack).contains(Effects.EXTRA_MODS))?3:2;
    }

    public static float getAttackSpeed(BuiltToolMaterial material, float speed){
        float newSpeed = speed;

        if(material.isGripped){
            newSpeed += 0.1f;
        }
        if(material.getEffects().contains(Effects.LIGHT)){
            newSpeed += 0.05f;
        }

        return newSpeed;
    }

    public static ItemStack increaseCountForFortune(ItemStack tool, ItemStack drop){
        int fortuneLevel = EnchantmentHelper.getLevel(Enchantments.FORTUNE, tool);
        ItemStack newDrop = drop.copy();

        for(int i = 0; i < fortuneLevel; i++){
            boolean shouldIncreaseCount = (new Random().nextInt(3) == 0);
            if(shouldIncreaseCount){
                newDrop.setCount(newDrop.getCount()+1);
            }
        }

        return newDrop;
    }

    public default void onHit(ItemStack stack, LivingEntity target, LivingEntity attacker){
        if(getEffects(stack).contains(Effects.POISONOUS) && !target.world.isClient){
            target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, ConfigHandler.INSTANCE.getPoisonTime()));
        }
        if(getEffects(stack).contains(Effects.FLAMING) && !target.world.isClient){
            target.setOnFireFor(ConfigHandler.INSTANCE.getFlamingTime());
        }
        if(getEffects(stack).contains(Effects.MAGNETIC) && !target.world.isClient){
            MagneticEffect.run(attacker, target.world);
        }
    }

    public default void addTooltips(List<Text> tooltip, ItemStack stack, BuiltToolMaterial material, Style mainStyle, Style effectStyle, Style modifierStyle){
        tooltip.add(new TranslatableText(material.handle.getTranslationKey()).setStyle(mainStyle));

        tooltip.add(new TranslatableText("text.toolbuilder.durability").append(Integer.toString(stack.getMaxDamage())).setStyle(mainStyle));
        DecimalFormat dec = new DecimalFormat("#.##");
        dec.setRoundingMode(RoundingMode.HALF_UP);
        tooltip.add(new TranslatableText("text.toolbuilder.mining_speed").append(dec.format(material.getMiningSpeed())).setStyle(mainStyle));

        if(material.isGripped){
            tooltip.add(new TranslatableText("text.toolbuilder.grip").setStyle(mainStyle));
        }

        for(Effect effect : material.getEffects()){
            tooltip.add(effect.getTranslationName().setStyle(effectStyle));
        }

        Integer numMods = 0;

        for(Effect effect : getModifierEffects(stack)){
            tooltip.add(effect.getTranslationName().setStyle(modifierStyle));
            numMods++;
        }

        tooltip.add(new TranslatableText("text.toolbuilder.modifiers").append(numMods.toString()).setStyle(modifierStyle));
        tooltip.add(new TranslatableText("text.toolbuilder.max_modifiers").append(Integer.toString(getNumModifiers(stack))).setStyle(modifierStyle));
    }

    public default void onMine(BuiltToolMaterial material, ItemStack stack, BlockState state, World world, BlockPos pos, LivingEntity miner, XpDropCheck dropCheck){
        if(getEffects(stack).contains(Effects.EXPERIENCE) && !world.isClient && dropCheck.check(state, stack) && ((world.random.nextInt(3) == 0))){
            world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), ConfigHandler.INSTANCE.getExtraXp()));
        }
        if(getEffects(stack).contains(Effects.MAGNETIC) && !world.isClient){
            MagneticEffect.run(miner, world);
        }
        if(getEffects(stack).contains(Effects.GLIMMERING) && !world.isClient){
            CompoundTag tag = stack.getOrCreateTag();
            int glimmers = 0;
            if(tag.contains(Effects.glimmerNBTtag)){
                glimmers = tag.getInt(Effects.glimmerNBTtag);
            }

            boolean glimmerChance = world.random.nextInt(3) == 0;

            if(glimmerChance){
                glimmers = 1;
            } else {
                glimmers = 0;
            }

            tag.putInt(Effects.glimmerNBTtag, glimmers);
        }
    }
}