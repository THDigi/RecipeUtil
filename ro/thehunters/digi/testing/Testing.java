package ro.thehunters.digi.testing;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import ro.thehunters.digi.recipeUtil.RecipeUtil;

public class Testing extends JavaPlugin implements Listener
{
    private ShapedRecipe myRecipe;
    
    @Override
    public void onEnable()
    {
        getServer().getPluginManager().registerEvents(this, this);
        
        myRecipe = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        myRecipe.shape("#  ", " F ", "  H");
        myRecipe.setIngredient('#', Material.IRON_INGOT);
        myRecipe.setIngredient('F', Material.YELLOW_FLOWER);
        myRecipe.setIngredient('H', Material.ARROW);
        
        getServer().addRecipe(myRecipe);
    }
    
    @EventHandler
    public void preCraft(PrepareItemCraftEvent event)
    {
        Recipe r = event.getRecipe();
        
        System.out.print("(debug) recipes equal = " + RecipeUtil.areEqual(r, myRecipe));
    }
}
