package marioandweegee3.toolbuilder.api.material;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import marioandweegee3.toolbuilder.api.effect.Effect;
import net.minecraft.item.Item;

public class BowMaterial {
    public final HandleMaterial handle;
    public final StringMaterial string;
    public final Boolean grip;

    public BowMaterial(HandleMaterial handle, boolean grip, StringMaterial string){
        this.handle = handle;
        this.string = string;
        this.grip = grip;
    }

    public float getDrawSpeedMultiplier(){
        float speedMod = handle.getDrawSpeedMultiplier();

        if(grip) speedMod *= 0.9;

        return speedMod;
    }

    public String getName(){
        return handle.getName() + "_" + string.getName();
    }

    public ArrayList<Item> getRepairItems(){
        return handle.getRepairItems(grip);
    }

    public Set<Effect> getEffects(){
        Set<Effect> effects = new HashSet<>(0);
        effects.addAll(handle.getEffects());
        effects.addAll(string.getEffects());
        return effects;
    }
}