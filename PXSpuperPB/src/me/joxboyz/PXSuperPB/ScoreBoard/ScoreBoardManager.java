package me.joxboyz.PXSuperPB.ScoreBoard;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreBoardManager {
	
	private Main plugin = Main.getInstance();
	
	private static ScoreBoardManager sb = new ScoreBoardManager();
	
	public static ScoreBoardManager getScoreBoardManager(){
		return sb;
	}
	
	public void removePlayerScoreBoard(Player player){
		ScoreboardManager sbm = plugin.getServer().getScoreboardManager();
		Scoreboard sb = sbm.getNewScoreboard();
		player.setScoreboard(sb);
	}
	
	public void updateArenaScoreBoard(String arenaName){
		Arena arena = ArenaManager.getManager().getArena(arenaName);
		for(String players: arena.getPlayers()){
			@SuppressWarnings("deprecation")
			Player player = Bukkit.getPlayer(players);
			removePlayerScoreBoard(player);
			
			ScoreboardManager sbm = plugin.getServer().getScoreboardManager();
			Scoreboard sb = sbm.getNewScoreboard();
			Objective obj = sb.registerNewObjective("Scoreboard", "dummy");
			
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			obj.setDisplayName(ChatColor.RED + "Paint Ball");
			
			Score time = obj.getScore(ChatColor.GREEN + "Time Left");
			time.setScore(15);
			
			Score remainingTime = obj.getScore(ChatColor.DARK_PURPLE + "" +  arena.getCountDown() + "  ");
			remainingTime.setScore(14);
			
			Score break1 = obj.getScore(ChatColor.GOLD + "---- ");
			break1.setScore(13);
			
			Score blueTeam = obj.getScore(ChatColor.GREEN + "Blue Score");
			blueTeam.setScore(12);
			
			Score blueScore = obj.getScore(ChatColor.DARK_PURPLE + "" + arena.getBlueTeamScore() + " ");
			blueScore.setScore(11);
			
			Score break2 = obj.getScore(ChatColor.GOLD + "----");
			break2.setScore(10);
			
			Score redTeam = obj.getScore(ChatColor.GREEN + "Red Score");
			redTeam.setScore(9);
			
			Score redScore = obj.getScore(ChatColor.DARK_PURPLE + "" + arena.getRedTeamScore());
			redScore.setScore(8);
			
			player.setScoreboard(sb);
			
		}
	}
	
	//call this to schedule the task
	public void gameCountDown(final String arenaName){
		final Arena arena = ArenaManager.getManager().getArena(arenaName);
		FileConfiguration fc = plugin.getConfig("Arenas", "");
		
		arena.setCountDown(fc.getInt("arenas." + arenaName + ".gameTime"));
		
		final int tid = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
			public void run(){
				if(arena.getCountDown() <= 0){
					endTask(arena);
					ArenaManager.getManager().endArena(arenaName);
				}else{
					int countDown = arena.getCountDown();
					countDown--;
					arena.setCountDown(countDown);
					ScoreBoardManager.getScoreBoardManager().updateArenaScoreBoard(arenaName);
				}
			}
		},0L, 20L);
		
		arena.setCountDownID(tid);
	} 
			
	//call this to end the task
	public void endTask(Arena arena){
		Bukkit.getScheduler().cancelTask(arena.getCountDownID()); //cancel the task
	}
	
}
