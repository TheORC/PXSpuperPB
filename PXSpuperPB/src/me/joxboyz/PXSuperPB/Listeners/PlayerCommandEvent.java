package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandEvent implements Listener{
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		Player player = event.getPlayer();
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) != null){
			String[] message = event.getMessage().split(" ");
			if(!message[0].equalsIgnoreCase("/pxpb")){
				event.setCancelled(true);
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "You can not use this commands while in game!");
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "Use </pxpb leave> to leave the arena!");
			}
		}
	}
}
