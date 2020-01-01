package marioandweegee3.toolbuilder.client.lang;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.swordglowsblue.artifice.api.ArtificeResourcePack.ClientResourcePackBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import marioandweegee3.toolbuilder.ToolBuilder.BowBuilder;
import marioandweegee3.toolbuilder.ToolBuilder.ToolItemBuilder;
import marioandweegee3.toolbuilder.api.material.BuiltToolMaterial;
import marioandweegee3.toolbuilder.api.material.HandleMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class TBLang {
    public static Map<String, String> entriesUS = new HashMap<>();

    public static void add(ClientResourcePackBuilder pack){
        addGeneric();
        pack.addTranslations(new Identifier("en_us"), lang -> {
            for(String key : entriesUS.keySet()){
                String translation = entriesUS.get(key);
                lang.entry(key, translation);
            }
        });
    }

    public static void addToolLang(ToolItemBuilder builder){
        BuiltToolMaterial material = builder.getMaterial();
        entriesUS.put(builder.getType().getName()+".toolbuilder."+material.head.getName(), material.head.getUSTranslation() + " "+ StringUtils.capitalize(builder.getType().getName()));
    }

    @SuppressWarnings("serial")
    private static void addGeneric(){
        entriesUS.putAll(new HashMap<String, String>(){
            {
                String text = "text.toolbuilder.";

                Map<String, String> effects = Stream.of(new String[][]{
                    {"xp","Extra Experience"},
                    {"poison","Poisonous"},
                    {"holy","Holy"},
                    {"growing","Growing"},
                    {"flaming","Flaming"},
                    {"light","Light"},
                    {"flammable","Flammable"},
                    {"durable","Durable"},
                    {"extra_modifiers","Extra Modifiers"},
                    {"magical","Magical"},
                    {"bouncy","Bouncy"},
                    {"resilient","Resilient"},
                    {"magnetic","Magnetic"},
                    {"glimmering","Glimmering"},
                    {"royal","Royal"},
                    {"aquatic","Aquatic"},
                    {"ender","Ender"}
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

                for(Map.Entry<String, String> entry : effects.entrySet()){
                    put(text+"effects."+entry.getKey(), entry.getValue());
                }

                put(text+"modifiers", "Modifiers: ");
                put(text+"max_modifiers", "Max Modifiers: ");

                Map<String, String> commands = Stream.of(new String[][]{
                    {"effect.get.effectList", "Effect List: "},
                    {"effect.set.invalid", "Invalid Effect: "},
                    {"effect.set.alreadyApplied", "Already applied Effect "},
                    {"effect.set.applied", "Applied Effect "},
                    {"effect.clear.removed", "Removed Modifiers from stack."},
                    {"effect.clear.invalid", "Invalid Stack: "}
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

                for(Map.Entry<String, String> entry : commands.entrySet()){
                    put(text+"commands."+entry.getKey(), entry.getValue());
                }

                Map<String, String> modifiers = Stream.of(new String[][]{
                    {"poison","Poison Tip"},
                    {"holy","Holy Water"},
                    {"growing","Moss"},
                    {"flaming","Blazing Stone"},
                    {"durable","Heavy Plate"},
                    {"magnetic","Magnet"}
                }).collect(Collectors.toMap(data -> data[0], data -> data[1]));

                for(Map.Entry<String, String> entry : modifiers.entrySet()){
                    put("modifier.toolbuilder."+entry.getKey(), entry.getValue());
                }

                putAll(Stream.of(new String[][]{
                    {text+"grip","Gripped"},
                    //Stats
                    {text+"durability","Durability: "},
                    {text+"mining_speed","Mining Speed: "},
                    {text+"draw_time","Draw Time: "},
                    {text+"extra_durability","Extra Durability: "},
                    {text+"durability_modifier","Durability Modifier: "},
                    {text+"enchantability","Enchantability: "},
                    {text+"mining_level","Mining Level: "}
                }).collect(Collectors.toMap(data -> data[0], data -> data[1])));
            }
        });
    }

    public static void addBlock(Block block){
        String key = block.getTranslationKey();
        if(key.startsWith("block.toolbuilder.")){
            entriesUS.put(key, WordUtils.capitalize(key.replaceFirst("block.toolbuilder.", "").replace("_", " ")));
        }
    }

    public static void addItem(Item item){
        String key = item.getTranslationKey();
        if(key.startsWith("item.toolbuilder.")){
            entriesUS.put(key, WordUtils.capitalize(key.replaceFirst("item.toolbuilder.", "").replace("_", " ")));
        }
    }

    public static void addHandle(HandleMaterial handle){
        String key = handle.getTranslationKey();
        entriesUS.put(key, getHandleTranslated(handle)+" Handle");
    }

    private static String getHandleTranslated(HandleMaterial handle){
        if(handle.getName().equals("wood") || handle.getName().equals("gold")){
            return StringUtils.capitalize(handle.getName() + "en");
        } else {
            return WordUtils.capitalize(handle.getName());
        }
    }

    public static void addBow(BowBuilder builder){
        String key = "bow.toolbuilder."+builder.getMaterial().handle.getName();
        entriesUS.put(key, getHandleTranslated(builder.getMaterial().handle) + " Bow");
    }

    public static void addItemGroup(String group){
        String key = "itemGroup.toolbuilder."+group;
        entriesUS.put(key, "Tool Builder "+WordUtils.capitalize(group.replace("_", " ")));
    }
}