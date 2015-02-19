package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class PlayerHungerEvent implements Listener{
	
	@EventHandler
	public void playerHungerChange(FoodLevelChangeEvent event) {
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn((Player)event.getEntity()) !=null){
			event.setFoodLevel(20);
			event.setCancelled(true);
		}
	}
}
