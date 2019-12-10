package marioandweegee3.toolbuilder.api.effect;

import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public interface Effect {
    public TranslatableText getTranslationName();
    public String getName();
    public Identifier getID();

    public float getAttackDamage(ItemStack stack, EntityGroup group, float baseDamage);
}