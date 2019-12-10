package marioandweegee3.toolbuilder.api;

public interface ToolType {
    public String getName();

    public ToolCreator<?> getBuilder();

    public String[] getRecipePattern();

    public int getHandleGripCost();
}