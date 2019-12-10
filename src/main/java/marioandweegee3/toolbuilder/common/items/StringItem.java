package marioandweegee3.toolbuilder.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class StringItem extends Item{
    private String name;

    public StringItem(String name) {
        super(new Item.Settings().group(ItemGroup.MISC));
        this.name = name;
    }

    @Override
    public String getTranslationKey() {
        return "string.toolbuilder."+name;
    }

}