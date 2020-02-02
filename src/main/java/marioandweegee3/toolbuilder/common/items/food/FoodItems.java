package marioandweegee3.toolbuilder.common.items.food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;

public class FoodItems {
    public static final Map<String, Item> foodItems = new HashMap<>();

    public static final FoodItem breadSlice = makeFoodItem("bread_slice", 1, 0.6f);

    public static final FoodItem rawBeefSlice = makeFoodItem("raw_beef_slice", 1, 0.3f, true, false, false);
    public static final FoodItem cookedBeefSlice = makeFoodItem("cooked_beef_slice", 3, 0.8f, true, false, false);

    public static final FoodItem rawChickenSlice = makeFoodItem("raw_chicken_slice", 1, 0.3f, true, false, false, Pair.of(new StatusEffectInstance(StatusEffects.HUNGER, 600, 0), 0.3F));
    public static final FoodItem cookedChickenSlice = makeFoodItem("cooked_chicken_slice", 3, 0.6f, true, false, false);

    public static final FoodItem rawBacon = makeFoodItem("raw_bacon", 1, 0.3f, true, false, false);
    public static final FoodItem cookedBacon = makeFoodItem("cooked_bacon", 3, 0.8f, true, false, false);

    public static final FoodItem cheese = makeFoodItem("cheese", 4, 0.7f);
    public static final FoodItem cheeseSlice = makeFoodItem("cheese_slice", 1, 0.7f);

    public static final FoodItem beefCheeseSandwich = makeSandwichItem(breadSlice, cookedBeefSlice, cheeseSlice);
    public static final FoodItem chickenCheeseSandwich = makeSandwichItem(breadSlice, cookedChickenSlice, cheeseSlice);
    public static final FoodItem baconCheeseSandwich = makeSandwichItem(breadSlice, cookedBacon, cheeseSlice);

    public static FoodItem makeFoodItem(String name, FoodComponent foodComponent) {
        FoodItem item = new FoodItem(new Item.Settings(), foodComponent, name);
        foodItems.put(item.getFoodName(), item);
        return item;
    }

    @SafeVarargs
    public static FoodItem makeFoodItem(String name, int hunger, float saturationModifier, boolean meat, boolean alwaysEdible, boolean snack, Pair<StatusEffectInstance, Float>... statusEffects) {
        return makeFoodItem(name, makeFoodComponent(hunger, saturationModifier, meat, alwaysEdible, snack, statusEffects));
    }

    public static FoodComponent makeFoodComponent(int hunger, float saturationModifier, boolean meat, boolean alwaysEdible, boolean snack, List<Pair<StatusEffectInstance, Float>> statusEffects) {
        FoodComponent.Builder builder = new FoodComponent.Builder();
        builder = builder.hunger(hunger);
        builder = builder.saturationModifier(saturationModifier);
        if(meat) {
            builder = builder.meat();
        }
        if(alwaysEdible) {
            builder = builder.alwaysEdible();
        }
        if(snack) {
            builder = builder.snack();
        }
        for(Pair<StatusEffectInstance, Float> statusEffect : statusEffects) {
            builder = builder.statusEffect(statusEffect.getLeft(), statusEffect.getRight());
        }
        return builder.build();
    }

    @SafeVarargs
    public static FoodComponent makeFoodComponent(int hunger, float saturationModifier, boolean meat, boolean alwaysEdible, boolean snack, Pair<StatusEffectInstance, Float>... statusEffects) {
        FoodComponent.Builder builder = new FoodComponent.Builder();
        builder = builder.hunger(hunger);
        builder = builder.saturationModifier(saturationModifier);
        if(meat) {
            builder = builder.meat();
        }
        if(alwaysEdible) {
            builder = builder.alwaysEdible();
        }
        if(snack) {
            builder = builder.snack();
        }
        for(Pair<StatusEffectInstance, Float> statusEffect : statusEffects) {
            builder = builder.statusEffect(statusEffect.getLeft(), statusEffect.getRight());
        }
        return builder.build();
    }

    @SafeVarargs
    public static FoodItem makeFoodItem(String name, int hunger, float saturationModifier, Pair<StatusEffectInstance, Float>... statusEffects) {
        return makeFoodItem(name, hunger, saturationModifier, false, false, false, statusEffects);
    }

    public static FoodComponent makeSandwichComponent(Item... items) {
        int hunger = 0;
        float saturationModifier = 0;
        boolean meat = false;
        boolean alwaysEdible = false;
        boolean snack = false;
        List<Pair<StatusEffectInstance, Float>> statusEffects = new ArrayList<>();

        for(Item item : items) {
            if(item.isFood()) {
                FoodComponent component = item.getFoodComponent();

                hunger += component.getHunger();
                if(component.getSaturationModifier() > saturationModifier) {
                    saturationModifier = component.getSaturationModifier();
                }

                if(component.isMeat()){
                    meat = true;
                }

                statusEffects.addAll(component.getStatusEffects());
            }
        }

        return makeFoodComponent(hunger, saturationModifier, meat, alwaysEdible, snack, statusEffects);
    }

    public static FoodItem makeSandwichItem(FoodItem... items) {
        FoodComponent component = makeSandwichComponent(items);
        String name = "";
        for(FoodItem item : items) {
            name += item.getFoodName();
            if(item != items[items.length-1]){
                name += "_";
            }
        }
        name += "_sandwich";
        FoodItem item = new FoodItem(new Item.Settings(), component, name, true);
        foodItems.put(name, item);
        return item;
    }
}