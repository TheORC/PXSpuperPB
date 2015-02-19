package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;
import me.joxboyz.PXSuperPB.Shop.ShopManager;
import me.joxboyz.PXSuperPB.SignManager.SignMagager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class onSignClick implements Listener{
	
	public ShopManager sm = ShopManager.getShopManager();
	public ArenaManager am = ArenaManager.getManager();

	Main plugin;
	
	public onSignClick(Main main){
		plugin = main;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (((event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && ((event.getClickedBlock().getType().equals(Material.SIGN_POST)) || (event.getClickedBlock().getType().equals(Material.WALL_SIGN)))))){
			Sign sign = (Sign) event.getClickedBlock().getState();
			if(sign.getLine(0).equalsIgnoreCase(ChatColor.GOLD + "[PaintBall]")){
				if(sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Join")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);
						ArenaManager.getManager().joinLobby(player, arenaName);
					}
				}else if(sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Leave")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);
						ArenaManager.getManager().removePlayer(player, arenaName);
					}
				}else if(sign.getLine(1).equalsIgnoreCase(ChatColor.BLUE + "Blue")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);
						if(ArenaManager.getManager().getArena(arenaName) == null){
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"This arena does not exsist!");
							return;
						}
						Arena arena = ArenaManager.getManager().getArena(arenaName);
						
						if(arena.getBluePlayers().contains(player)){
							arena.getBluePlayers().remove(player);
						}
						
						if(arena.getRedPlayers().contains(player)){
							arena.getRedPlayers().remove(player);
						}
						
						ArenaManager.getManager().addPlayerToBlue(player, arenaName);
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.BLUE + " You have joined the blue team!");
					}
				}else if(sign.getLine(1).equalsIgnoreCase(ChatColor.RED + "Red")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);	
						if(ArenaManager.getManager().getArena(arenaName) == null){
							player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED + "This arena does not exsist!");
							return;
						}
						Arena arena = ArenaManager.getManager().getArena(arenaName);
						
						if(arena.getBluePlayers().contains(player)){
							arena.getBluePlayers().remove(player);
						}
						
						if(arena.getRedPlayers().contains(player)){
							arena.getRedPlayers().remove(player);
						}
						
						ArenaManager.getManager().addPlayerToRed(player, arenaName);
						
						player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.BLUE + "You have joied the red team");
					}
				}else if(sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Pea Shooter")){
					if(sign.getLine(3) != ""){
						if(sm.hasPlayerBoughtKit(player, "peaShooter")){
							String arenaName = sign.getLine(3);	
							am.setPlayerKitForJoin(arenaName, player, "peashooter");
						}
					}
				}else if(sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Shot Gun")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);	
						if(sm.hasPlayerBoughtKit(player, "shotgun")){
							am.setPlayerKitForJoin(arenaName, player, "shotgun");	
						}else{
							if(sm.canPlayerBuyKit(player, "ShotGun")){
								sm.buyKit(player, "ShotGun");
								am.setPlayerKitForJoin(arenaName, player, "ShotGun");	
							}else{
								player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED + "You can not buy this!");
							}
						}
					}
				}
				else if(sign.getLine(1).equalsIgnoreCase(ChatColor.GREEN + "Machine Gun")){
					if(sign.getLine(3) != ""){
						String arenaName = sign.getLine(3);	
						if(sm.hasPlayerBoughtKit(player, "machinegun")){
							am.setPlayerKitForJoin(arenaName, player, "MachineGun");	
						}else{
							if(sm.canPlayerBuyKit(player, "MachineGun")){
								sm.buyKit(player, "MachineGun");
								am.setPlayerKitForJoin(arenaName, player, "MachineGun");
							}else{
								player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED + "You can not buy this!");
							}
						}
					}
				}
				
			}
		}
	}
	
	@EventHandler
    public void signCreate(SignChangeEvent event){
		Player player = event.getPlayer();
		String[] lines = event.getLines();
		if(lines[0].equalsIgnoreCase("[PaintBall]")){
			if(lines[1].equalsIgnoreCase("Join")){
				if(lines[3] != ""){
					event.setLine(0, ChatColor.GOLD + "[PaintBall]");
					event.setLine(1, ChatColor.GREEN + "Join");
					event.setLine(2, "0/" + ArenaManager.getManager().getArena(lines[3]).getMaxPlayers());
					SignMagager.getManager().setArenaJoinSign(lines[3], event.getBlock().getLocation());
				}else{
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You have to set an arena name!");
				}
			}else if(lines[1].equalsIgnoreCase("Leave")){
				if(lines[3] != ""){
					event.setLine(0, ChatColor.GOLD + "[PaintBall]");
					event.setLine(1, ChatColor.GREEN + "Leave");
				}else{
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You have to set an arena name!");
				}
			}else if(lines[1].equalsIgnoreCase("Blue")){
				if(lines[3] != ""){
					event.setLine(0, ChatColor.GOLD + "[PaintBall]");
					event.setLine(1, ChatColor.BLUE + "Blue");
				}else{
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You have to set an arena name!");
				}
			}else if(lines[1].equalsIgnoreCase("Red")){
				if(lines[3] != ""){
					event.setLine(0, ChatColor.GOLD + "[PaintBall]");
					event.setLine(1, ChatColor.RED + "Red");
				}else{
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You have to set an arena name!");
				}
			}else if(lines[1].equalsIgnoreCase("Pea Shooter")){
				event.setLine(0, ChatColor.GOLD + "[PaintBall]");
				event.setLine(1, ChatColor.GREEN + "Pea Shooter");
			}else if(lines[1].equalsIgnoreCase("Shot Gun")){
				event.setLine(0, ChatColor.GOLD + "[PaintBall]");
				event.setLine(1, ChatColor.GREEN + "Shot Gun");
			}else if(lines[1].equalsIgnoreCase("Machine Gun")){
				event.setLine(0, ChatColor.GOLD + "[PaintBall]");
				event.setLine(1, ChatColor.GREEN + "Machine Gun");
			}
		}
	}
}
