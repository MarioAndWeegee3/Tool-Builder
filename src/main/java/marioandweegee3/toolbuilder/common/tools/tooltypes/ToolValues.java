package marioandweegee3.toolbuilder.common.tools.tooltypes;

public enum ToolValues {
    AXE(-3.1f, 6.0f),
    PICKAXE(-2.8f, 1.0f),
    HAMMER(-3.0f, 5.0f),
    SWORD(-2.4f, 3.0f),
    KNIFE(-2.1f, 2.0f),
    SHOVEL(-2.8f, 1.0f),
    HOE(-1.0f, 0.0f),
    GREATSWORD(-3.5f, 9),
    EXCAVATOR(-3.0f, 5.0f),
    RAPIER(-2.3f, 3.1f)
    ;
    private float speed;
    private float damage;

    private ToolValues(float speed, float damage){
        this.speed = speed;
        this.damage = damage;
    }

    public float getSpeed(){
        return speed;
    }

    public float getDamage(){
        return damage;
    }
}