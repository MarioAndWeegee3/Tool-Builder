package marioandweegee3.toolbuilder.common.effect;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.effect.Effect;
import marioandweegee3.toolbuilder.api.registry.TBRegistries;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import net.minecraft.entity.EntityGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public enum Effects implements Effect {
    EXPERIENCE("xp", new EffectResults(){}), 
    POISONOUS("poison", new EffectResults(){}), 
    HOLY("holy", new EffectResults(){
        @Override
        public float getAttackDamage(ItemStack stack, EntityGroup group, float baseDamage){
            if(group == EntityGroup.UNDEAD){
                return (float) (baseDamage + ConfigHandler.INSTANCE.getHolyDamage());
            }
            return baseDamage;
        }
    }), 
    GROWING("growing", new EffectResults(){}), 
    FLAMING("flaming", new EffectResults(){}), 
    LIGHT("light", new EffectResults(){}),
    FLAMMABLE("flammable", new EffectResults(){}), 
    DURABLE("durable", new EffectResults(){}), 
    EXTRA_MODS("extra_modifiers", new EffectResults(){}), 
    MAGICAL("magical", new EffectResults(){}),
    BOUNCY("bouncy", new EffectResults(){}),
    RESILIENT("resilient", new EffectResults(){}),
    MAGNETIC("magnetic", new EffectResults(){}),
    GLIMMERING("glimmering", new EffectResults(){}),
    ROYAL("royal", new EffectResults(){});

    private Effects(String name, EffectResults results) {
        this.name = name;
        this.results = results;
    }

    public final String name;

    private final EffectResults results;

    public static final String effectNBTtag = "toolbuilder_effects";
    public static final String glimmerNBTtag = "toolbuilder_glimmers";
    public static final String magneticTickNBTtag = "toolbuilder_magnetic_tick";

    @Override
    public TranslatableText getTranslationName() {
        return new TranslatableText("text.toolbuilder.effects." + name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Identifier getID() {
        return new Identifier(ToolBuilder.modID, name);
    }

    @Override
    public float getAttackDamage(ItemStack stack, EntityGroup group, float baseDamage) {
        return results.getAttackDamage(stack, group, baseDamage);
    }

    public static SuggestionProvider<ServerCommandSource> effectSuggestions(){
        return (ctx, builder) -> getSuggestionBuilder(builder);
    }

    private static CompletableFuture<Suggestions> getSuggestionBuilder(SuggestionsBuilder builder){
        String remaining = builder.getRemaining().toLowerCase(Locale.ROOT);

        for(Identifier effect : TBRegistries.EFFECTS.keySet()){
            String name = effect.toString();
            name = "\""+name+"\"";
            if(name.toLowerCase(Locale.ROOT).startsWith(remaining)){
                builder.suggest(name);
            }
        }

        return builder.buildFuture();
    }

    public static abstract class EffectResults {
        public float getAttackDamage(ItemStack stack, EntityGroup group, float baseDamage){
            return baseDamage;
        }
    }
}