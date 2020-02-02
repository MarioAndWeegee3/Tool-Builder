package marioandweegee3.toolbuilder.common.items.food;

import java.text.DecimalFormat;
import java.util.List;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class FoodItem extends Item {
    private String name;
    private boolean sandwich;

    public FoodItem(Settings settings, FoodComponent foodComponent, String name) {
        this(settings, foodComponent, name, false);
    }

    public FoodItem(Settings settings, FoodComponent foodComponent, String name, boolean sandwich) {
        super(settings.food(foodComponent).group(ItemGroup.FOOD));
        this.name = name;
        this.sandwich = sandwich;
    }

    public String getFoodName(){
        return name;
    }

    @Override
    public String getTranslationKey() {
        if(sandwich){
            return "food.toolbuilder.sandwich";
        } else {
            return "food.toolbuilder."+getFoodName();
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        FoodComponent component = this.getFoodComponent();
        if(!FabricLoader.getInstance().isModLoaded("appleskin")) {
            tooltip.add(new TranslatableText("text.toolbuilder.food.hunger").append(Integer.toString(component.getHunger())));

            DecimalFormat format = new DecimalFormat("#.##");

            tooltip.add(new TranslatableText("text.toolbuilder.food.saturation").append(format.format(component.getHunger() * component.getSaturationModifier())));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

}