package marioandweegee3.toolbuilder.api.effect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class EffectInstance implements Comparable<EffectInstance> {
    private Effect effect;
    private int level;

    public EffectInstance(Effect effect, int level) {
        this.effect = effect;
        this.level = level;
        capLevel();
    }

    public void addLevel(int level){
        this.level += level;
        capLevel();
    }

    public int getLevel() {
        return level;
    }

    private void capLevel(){
        this.level = MathHelper.clamp(level, 1, effect.getMaxLevel());
    }

    public Effect getEffect() {
        return effect;
    }

    public Text getTooltip(){
        return effect.getTranslationName().append(" "+level);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj instanceof EffectInstance){
            EffectInstance other = (EffectInstance) obj;
            if(other.effect == this.effect && other.level == this.level){
                return true;
            }
        }
        return false;
    }

    public static EffectInstance fromTag(CompoundTag effectTag) throws Exception {
        Effect effect = TBRegistries.EFFECTS.get(new Identifier(effectTag.getString("id")));
        int level = effectTag.getInt("lvl");
        if (effect != null && level > 0) {
            return new EffectInstance(effect, level);
        } else {
            throw new Exception("Invalid nbt");
        }
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", effect.getID().toString());
        tag.putInt("lvl", level);
        return tag;
    }

    public static Set<EffectInstance> mergeSets(Set<EffectInstance> set1, Set<EffectInstance> set2) {
        Set<EffectInstance> effects = new HashSet<>(0);
        effects.addAll(set1);
        Set<Effect> addedEffects = new HashSet<>();
        for(EffectInstance instance : effects){
            addedEffects.add(instance.getEffect());
        }
        for(EffectInstance effectInstance : set2) {
            if(!addedEffects.contains(effectInstance.getEffect())){
                addedEffects.add(effectInstance.getEffect());
                effects.add(effectInstance);
                continue;
            }
            for(EffectInstance instance : effects) {
                if(instance.getEffect() == effectInstance.getEffect()){
                    instance.addLevel(effectInstance.getLevel());
                }
            }
        }
        return effects;
    }

    public static Set<EffectInstance> fromEffects(Collection<Effect> effects){ 
        Set<EffectInstance> instances = new HashSet<>();
        for(Effect effect : effects){
            instances.add(new EffectInstance(effect, 1));
        }
        return instances;
    }

    public static Set<Effect> toEffectSet(Collection<EffectInstance> instances) {
        Set<Effect> effects = new HashSet<>();
        for(EffectInstance instance : instances) {
            effects.add(instance.effect);
        }
        return effects;
    }

    @Override
    public int compareTo(EffectInstance o) {
        return this.effect.getName().compareTo(o.effect.getName());
    }
}