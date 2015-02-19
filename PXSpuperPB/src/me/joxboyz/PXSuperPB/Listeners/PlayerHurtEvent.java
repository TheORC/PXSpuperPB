package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerHurtEvent implements Listener{
	@EventHandler
	public void onPlayerHurt(EntityDamageEvent event){
		if(event.getEntity() instanceof Player){
			Player player = (Player)event.getEntity();
			if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) != null){
				event.setCancelled(true);
			}
		}
	}
}
