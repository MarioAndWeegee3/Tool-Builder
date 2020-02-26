package marioandweegee3.toolbuilder.api;

import net.minecraft.util.Identifier;

public interface ToolType {
    String getName();

    ToolCreator<?> getBuilder();

    String[] getRecipePattern();

    int getHandleGripCost();
    
    Identifier fabricTagToAddTo();
}