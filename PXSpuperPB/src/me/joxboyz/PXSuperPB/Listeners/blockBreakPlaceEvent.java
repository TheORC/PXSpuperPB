package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class blockBreakPlaceEvent implements Listener{
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event){
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn(event.getPlayer()) !=null){
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn(event.getPlayer()) !=null){
			event.setCancelled(true);
		}
	}

}
