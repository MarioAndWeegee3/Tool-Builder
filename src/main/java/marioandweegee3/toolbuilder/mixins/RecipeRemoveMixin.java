package marioandweegee3.toolbuilder.mixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import marioandweegee3.toolbuilder.ToolBuilder;
import marioandweegee3.toolbuilder.common.config.ConfigHandler;
import net.minecraft.resource.ReloadableResourceManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.util.Identifier;

@Mixin(ReloadableResourceManagerImpl.class)
public abstract class RecipeRemoveMixin implements ReloadableResourceManager {

    @Inject(method = "findResources", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void removeRecipes(String parent, Predicate<String> loadFilter, CallbackInfoReturnable<Collection<Identifier>> ci, Set<Identifier> foundResources, List<Identifier> sortedResources){
        List<Identifier> sortedCopy = new ArrayList<>(sortedResources);
        for(Identifier id : sortedCopy){
            if (id.getPath().contains(".mcmeta") || id.getPath().contains(".png")) continue;

            if(tb_shouldRemove(id)){
                sortedResources.remove(id);
                ToolBuilder.HELPER.log("Removed "+id);
            }
        }
    }

    private boolean tb_shouldRemove(Identifier id){
        if(!ConfigHandler.getInstance().shouldRemoveVanillaToolRecipes()) return false;

        String[] vanillaHeads = {
            "wooden",
            "stone",
            "iron",
            "golden",
            "diamond"
        };
        String[] vanillaTools = {
            "pickaxe",
            "shovel",
            "axe",
            "hoe",
            "sword"
        };

        for(String head : vanillaHeads){
            for(String type: vanillaTools){
                String tool = "recipes/"+head+"_"+type+".json";
                if(id.getPath().equals(tool)){
                    return true;
                }
            }
        }
        return false;
    }
}