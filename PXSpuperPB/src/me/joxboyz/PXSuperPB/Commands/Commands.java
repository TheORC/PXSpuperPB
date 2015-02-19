package me.joxboyz.PXSuperPB.Commands;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.ArenaSetUpVar.SetUpVar;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor{
	
	private SetUpVar variables = SetUpVar.getManager();
	
	private Main plugin;
	private String pxpbN = ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.BLUE;
	private String pxpbE = ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED;
	
	public Commands(Main main){
		this.plugin = main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(!(sender instanceof Player)){
			Bukkit.getConsoleSender().sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "Only A Player Can Run This Command");
			return true;
		}
		
		Player player = (Player)sender;
		
		if (commandLabel.equalsIgnoreCase("pxpb")) {
			if(args.length == 0){
				if(player.hasPermission(plugin.permissions.arenaSetUp) || player.isOp()){
					player.sendMessage(pxpbE + "Use </pxpb help> for a list of commands!");
				}else{
					player.sendMessage(pxpbE + "Use </pxpb help> for a list of commands!");
				}
			}else if(args.length == 1){
				if(player.hasPermission(plugin.permissions.arenaSetUp) || player.isOp()){
					if(args[0].equalsIgnoreCase("setBlueSpawn")){
						variables.setBlueSpawn(player);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.BLUE + "You have set the blue spawn");
					}else if(args[0].equalsIgnoreCase("setRedSpawn")){
						variables.setRedSpawn(player);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.BLUE +"You have set the red spawn");
					}else if(args[0].equalsIgnoreCase("setLobbySpawn")){
						variables.setLobbySpawn(player);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.BLUE +"You have set the lobby spawn");
					}else if(args[0].equalsIgnoreCase("setEndSpawn")){
						variables.setEndLocation(player);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.BLUE +"You have set the End spawn");
					}else if(args[0].equalsIgnoreCase("help")){
						player.sendMessage(pxpbN + "Paint Ball help menu!");
						player.sendMessage(pxpbN + "/pxpb setbluespawn : Use this to set the blue spawn!");
						player.sendMessage(pxpbN + "/pxpb setredspawn : Use this to set the red spawn!");
						player.sendMessage(pxpbN + "/pxpb setlobbyspawn : Use this to set the arena lobby spawn!");
						player.sendMessage(pxpbN + "/pxpb setendspawn : Use this to set the spawn players go to after the game as ended!");
						player.sendMessage(pxpbN + "/pxpb createarena <arenaName> <maxPlayer> <minPlayers> <gameTime> : Use this to create the arena!");
						player.sendMessage(pxpbN + "/pxpb join <arenaName> : Use this to join an arena!");
						player.sendMessage(pxpbN + "/pxpb leave : Use this to leave an arena!");
					}else if(args[0].equalsIgnoreCase("leave")){
						if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) ==null){
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "You are not in a arnea!");
							return true;
						}
						ArenaManager.getManager().removePlayer(player, ArenaManager.getManager().getWhatArenaPlayerIsIn(player).getName());
					}else{
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED +"Unkown command!");
					}
				}else{
					if(args[0].equalsIgnoreCase("help")){
						player.sendMessage(pxpbN + "Paint Ball help menu!");
						player.sendMessage(pxpbN + "/pxpb join <arenaName> : Use this to join an arena!");
						player.sendMessage(pxpbN + "/pxpb leave : Use this to leave an arena!");
					}else if(args[0].equalsIgnoreCase("leave")){
						if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) == null){
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "You are not in a arnea!");
							return true;
						}
						ArenaManager.getManager().removePlayer(player, ArenaManager.getManager().getWhatArenaPlayerIsIn(player).getName());
					}else{
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED +"Unkown command!");
					}}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("Join")){
					ArenaManager.getManager().joinLobby(player, args[1]);
				}else if(args[0].equals("leave")){
					ArenaManager.getManager().removePlayer(player, args[1]);
				}
			}else if(args.length == 5){
				if(player.hasPermission(plugin.permissions.arenaSetUp) || player.isOp()){
					if(args[0].equalsIgnoreCase("createArena")){
						if(ArenaManager.getManager().getArena(args[1]) == null){
							int maxPayers = Integer.parseInt(args[2]);
							int minPlayers = Integer.parseInt(args[3]);
							int gameTime = Integer.parseInt(args[4]);
							ArenaManager.getManager().createArena(args[1], variables.getLobbySpawn(), variables.getBlueSpawn(), variables.getRedSpawn(), variables.getEndLocation(), maxPayers, minPlayers, gameTime, player);
						}else{
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED +"This Arena Already Exsists!");
						}
					}
				}else{
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" +ChatColor.RED + "Unkown command!");
				}
			}
		}
		return false;
	}

}
