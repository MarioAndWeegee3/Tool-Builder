package marioandweegee3.toolbuilder.api;

import net.minecraft.util.Identifier;

public interface ToolType {
    public String getName();

    public ToolCreator<?> getBuilder();

    public String[] getRecipePattern();

    public int getHandleGripCost();

    public Identifier fabricTagToAddTo();
}