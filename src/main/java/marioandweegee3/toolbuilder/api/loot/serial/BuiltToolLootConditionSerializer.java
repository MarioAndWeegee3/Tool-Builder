package marioandweegee3.toolbuilder.api.loot.serial;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.api.loot.BuiltToolLootCondition;
import net.minecraft.loot.condition.LootCondition;

public class BuiltToolLootConditionSerializer extends LootCondition.Factory<BuiltToolLootCondition> {

    public BuiltToolLootConditionSerializer() {
        super(ToolBuilder.makeID("built_tool"), BuiltToolLootCondition.class);
    }

    @Override
    public void toJson(JsonObject json, BuiltToolLootCondition condition, JsonSerializationContext context) {
        json.addProperty("tool_type", condition.getToolType());
        json.addProperty("chance_denominator", condition.getChanceDenominator());
    }

    @Override
    public BuiltToolLootCondition fromJson(JsonObject json, JsonDeserializationContext context) {
        return new BuiltToolLootCondition(json.get("tool_type").getAsString(), json.get("chance_denominator").getAsInt());
    }
}