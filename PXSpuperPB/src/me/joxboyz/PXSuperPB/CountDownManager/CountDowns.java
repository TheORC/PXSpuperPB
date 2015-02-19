package me.joxboyz.PXSuperPB.CountDownManager;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class CountDowns {
	
	Main plugin;
	public CountDowns(Main main){
		plugin = main;
	}
	
	public void gameStartCountdown(final Arena arena){
		plugin.getServer().getWorld("world");
		BukkitTask task = new BukkitRunnable() {
			private int count = 61;
			
			public void run() {
				count--;
				 
				if(count == 60){
					arena.sendMessage(ChatColor.BLUE + "The arena will start in " + ChatColor.YELLOW + "1" + ChatColor.BLUE + " minute!");
				}
				 
				if(count == 30 || count == 20 || count < 11){
					arena.sendMessage(ChatColor.BLUE + "The arena will start in " +ChatColor.YELLOW +  count + ChatColor.BLUE + " seconds!");
					for(String p: arena.getPlayers()){
						@SuppressWarnings("deprecation")
						Player player = Bukkit.getPlayer(p);
						Bukkit.getServer().getWorld(arena.getWorld().getName()).playSound(player.getLocation(), Sound.CLICK, 2, 5);
					}
				}
				 
				if(count == 0){
					ArenaManager.getManager().startArena(arena.getName());
					cancel();
				}
				
			}
		}.runTaskTimer(plugin, 0L, 20);
		
		arena.setCountdownTask(task);
	}


}
