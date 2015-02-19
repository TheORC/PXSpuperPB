package me.joxboyz.PXSuperPB.Shop;

import java.util.ArrayList;
import java.util.List;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ShopManager {
	
	private static Main main = null;

	public ArrayList<String> playerWithShotGun = new ArrayList<String>();
	public ArrayList<String> playerWithMachineGun = new ArrayList<String>();
	
	private static ShopManager sm = new ShopManager();
	
	public static ShopManager getShopManager(){
		return sm;
	}
	
	public boolean hasPlayerBoughtKit(Player player, String kitName){
		if(kitName.equalsIgnoreCase("PeaShooter")){
			return true;
		}else if(kitName.equalsIgnoreCase("ShotGun")){
			if(playerWithShotGun.contains(player.getUniqueId().toString())){
				return true;
			}
		}else if(kitName.equalsIgnoreCase("MachineGun")){
			if(playerWithMachineGun.contains(player.getUniqueId().toString())){
				return true;
			}
		}else{
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You Have Named This Kit Wrong: " + kitName);
		}
		
		return false;
	}
	
	public boolean canPlayerBuyKit(Player player, String kitName){
		FileConfiguration fc = main.getConfig();
		int shotGunCost = fc.getInt(kitName + ".cost"), machineGunCost = fc.getInt(kitName + ".cost");
		if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player) == null){
			return false;
		}
		if(kitName.equalsIgnoreCase("ShotGun")){
			if(main.econ.getBalance(player) >= shotGunCost){
				return true;
			}
		}else if(kitName.equalsIgnoreCase("MachineGun")){
			if(main.econ.getBalance(player) >= machineGunCost){
				return true;
			}
		}else{
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You Have Named This Kit Wrong: " + kitName);
		}
		return false;
	}
	
	public void buyKit(Player player, String kitName){
		FileConfiguration shopConfig = main.getConfig("Shop", "");
		
		int shotGunCost = main.getConfig().getInt(kitName + ".cost"), machineGunCost = main.getConfig().getInt(kitName + ".cost");
		
		Bukkit.getConsoleSender().sendMessage("S: " + shotGunCost + " M: " + machineGunCost);
		if(kitName.equalsIgnoreCase("ShotGun")){
			List<String> peoplebought = shopConfig.getStringList("playerWhoHaveShotGun");
			peoplebought.add(player.getUniqueId().toString());
			shopConfig.set("playerWhoHaveShotGun", peoplebought);
			main.saveConfig(shopConfig, "Shop", "");
			main.econ.withdrawPlayer(player, shotGunCost);
			
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.BLUE +"You have bought the Shot Gun");
			playerWithShotGun.add(player.getUniqueId().toString());
		}else if(kitName.equalsIgnoreCase("MachineGun")){
			List<String> peoplebought = shopConfig.getStringList("playerWhoHaveMachineGun");
			peoplebought.add(player.getUniqueId().toString());
			shopConfig.set("playerWhoHaveMachineGun", peoplebought);
			main.saveConfig(shopConfig, "Shop", "");
			main.econ.withdrawPlayer(player, machineGunCost);
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.BLUE +"You have bought the Machine Gun");
			playerWithMachineGun.add(player.getUniqueId().toString());
		}else{
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED +"You Have Named This Kit Wrong: " + kitName);
		}
	
		main.saveConfig();
	}
	
	//YOU NEED TO ADD A CONFIRM BUY HERE!!!!!!!!
	public void confirmBuy(Player player){
		
	}
	
	public void loadShop(){
		main = Main.getInstance();
		List<String> shotgun = main.getConfig("Shop", "").getStringList("playerWhoHaveShotGun");
		List<String> machineGun = main.getConfig("Shop", "").getStringList("playerWhoHaveMachineGun");

		for(String keys: shotgun){
			playerWithShotGun.add(keys);
		}
		for(String keys: machineGun){
			playerWithMachineGun.add(keys);
		}
	}

}
