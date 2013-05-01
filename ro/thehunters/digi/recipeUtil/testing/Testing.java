package ro.thehunters.digi.recipeUtil.testing;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
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
    
    // Quick example (message when crafting)
    @EventHandler
    public void craft(CraftItemEvent event) // event is triggered when clicking result slot
    {
        // the event has some issues with getRecipe(), it still returns the last recipe when clicking the empty result slot!
        ItemStack result = event.getCurrentItem();
        if(result == null || result.getTypeId() == 0)
        {
            return;
        }
        
        // this is the part where it's checking if the current recipe is equal to the custom recipe.
        if(RecipeUtil.areEqual(event.getRecipe(), myRecipe))
        {
            // And send a message to all players as an example.
            Bukkit.broadcastMessage(event.getWhoClicked().getName() + " crafted a custom recipe making " + event.getCurrentItem() + " !");
        }
    }
    
    // Practical example (checking permissions)
    @EventHandler
    public void preCraft(PrepareItemCraftEvent event) // event is triggered when a recipe is found after placing ingredients
    {
        // Storing the equality result because it's not cheap to compare recipes.
        // I'm doing this because I'm using it twice, if you're using it only once you don't need to store it.
        boolean equal = RecipeUtil.areEqual(event.getRecipe(), myRecipe);
        
        // confirmation message of event triggering and recipe equality
        System.out.print("(debug) recipes equal = " + equal);
        
        if(equal)
        {
            HumanEntity human = event.getView().getPlayer();
            
            // check if crafter has permission...
            if(!human.hasPermission("my.awesome.permission"))
            {
                // basically cancels the event
                event.getInventory().setResult(null);
                
                // need to check because it could be NPCs or something
                if(human instanceof Player)
                {
                    Player player = (Player)human;
                    player.sendMessage("Need permission to craft this!");
                }
            }
        }
    }
}
