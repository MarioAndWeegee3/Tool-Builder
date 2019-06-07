package marioandweegee3.toolbuilder.common.tools.tooltypes;

public enum ToolValues{
    AXE(-3.1f, 6.0f,"axe"),
    PICKAXE(-2.8f, 1.0f, "pickaxe"),
    HAMMER(-3.0f, 5.0f, "hammer"),
    SWORD(-2.4f, 3.0f, "sword"),
    KNIFE(-2.2f, 2.0f, "knife"),
    SHOVEL(-2.8f, 1.0f, "shovel"),
    HOE(-1.0f, 0.0f, "hoe");

    private float speed;
    private float damage;
    private String name;

    private ToolValues(float speed, float damage, String name){
        this.speed = speed;
        this.damage = damage;
        this.name = name;
    }

    public float getSpeed(){
        return speed;
    }

    public float getDamage(){
        return damage;
    }

    public String getName(){
        return name;
    }
}