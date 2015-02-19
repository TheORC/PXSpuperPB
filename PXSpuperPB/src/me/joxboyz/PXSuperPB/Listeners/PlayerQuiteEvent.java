package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuiteEvent implements Listener{

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) !=null){
			Arena arena = ArenaManager.getManager().getWhatArenaPlayerIsIn(player);
			ArenaManager.getManager().removePlayer(player, arena.getName());
		}
	}
}
