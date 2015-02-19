package me.joxboyz.PXSuperPB.SignManager;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;



public class SignMagager {
	
	private static Main main = null;
	
	private static SignMagager sm = new SignMagager();
	
	// Usefull for getting the ArenaManager, like so: ArenaManager.getManager()
	public static SignMagager getManager() {
		return sm;
	}
	
	public Sign getJoinSign(String arenaName){
		FileConfiguration fc = main.getConfig("Signs", "");
		if(fc.getString("signs." + arenaName + ".world") == null){
			Bukkit.getConsoleSender().sendMessage("Sign Have Not Reset.  This may be due to that there are no sign,");
			Bukkit.getConsoleSender().sendMessage("but if you do have sign, then something has gone wrong in the config.");
			return null;
		}
		World world = Bukkit.getWorld(fc.getString("signs." + arenaName + ".world"));
		
		double x = fc.getDouble("signs." + arenaName + ".signX");
		double y = fc.getDouble("signs." + arenaName + ".signY");
		double z = fc.getDouble("signs." + arenaName + ".signZ");
		
		Location loc = new Location(world, x, y, z);
		
		Block b = world.getBlockAt(loc);
		
		if(b.getState() instanceof Sign){
			Sign s = (Sign)b.getState();
			return s;
		}
		
		return null;
		
	}
	
	public void updateSignForJoinOrLeave(String arenaName){
		
		Sign sign = getJoinSign(arenaName);
		
		if(ArenaManager.getManager() == null){
			Bukkit.getConsoleSender().sendMessage("Arena manager is null");
			return;
		}
		
		int howManyPlayer = ArenaManager.getManager().getArena(arenaName).getPlayers().size();
		int maxPlayer = ArenaManager.getManager().getArena(arenaName).getMaxPlayers();
		
		if(ArenaManager.getManager().getArena(arenaName).isInGame()){
			
		}else{
			if(sign != null){
				sign.setLine(2, howManyPlayer + "/" + maxPlayer);
				sign.update();
			}
		}
	}
	
	public void upDateGameStateSign(String arenaName){
		Sign sign = getJoinSign(arenaName);
		
		if(ArenaManager.getManager().getArena(arenaName).isInGame()){
			sign.setLine(2, ChatColor.GREEN + "In Game");
			sign.update();
		}else{
			updateSignForJoinOrLeave(arenaName);
		}
	}
	
	public void loadSign(){
		main = Main.getInstance();
		FileConfiguration fc = main.getConfig("Arenas", "");
		
		if(fc.getConfigurationSection("arenas") == null){
			Bukkit.getConsoleSender().sendMessage( ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED + "No signs have been loaded, because there are non!");
			return;
		}
		
		for (String keys : fc.getConfigurationSection("arenas").getKeys(false)) { // For
			updateSignForJoinOrLeave(keys);
		}
	}
	
	public void setArenaJoinSign(String arenaName, Location signLocation){
			FileConfiguration fc = main.getConfig("Signs", "");
			
			fc.set("signs." + arenaName, null); // Set its name
			// Now sets the other values

			String path = "signs." + arenaName + "."; // Shortcut
			// Sets the paths
			fc.set(path + "signX", signLocation.getX());
			fc.set(path + "signY", signLocation.getY());
			fc.set(path + "signZ", signLocation.getZ());
			
			fc.set(path + "world", signLocation.getWorld().getName());

			// Now save it up down here
			main.saveConfig(fc, "Signs", "");
	}
}
