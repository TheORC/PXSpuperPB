package me.joxboyz.PXSuperPB.Main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Commands.Commands;
import me.joxboyz.PXSuperPB.CountDownManager.CountDowns;
import me.joxboyz.PXSuperPB.Guns.PeaShooterListener;
import me.joxboyz.PXSuperPB.Listeners.PaintBallHitEvent;
import me.joxboyz.PXSuperPB.Listeners.PlayerCommandEvent;
import me.joxboyz.PXSuperPB.Listeners.PlayerHungerEvent;
import me.joxboyz.PXSuperPB.Listeners.PlayerHurtEvent;
import me.joxboyz.PXSuperPB.Listeners.PlayerQuiteEvent;
import me.joxboyz.PXSuperPB.Listeners.blockBreakPlaceEvent;
import me.joxboyz.PXSuperPB.Listeners.onSignClick;
import me.joxboyz.PXSuperPB.Permissions.Permissions;
import me.joxboyz.PXSuperPB.Shop.ShopManager;
import me.joxboyz.PXSuperPB.SignManager.SignMagager;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener{
	
	public final Logger logger = this.getLogger();
	
	public final Commands command = new Commands(this);
	public final onSignClick signClick = new onSignClick(this);
	public final PaintBallHitEvent painhit = new PaintBallHitEvent(this);
	public final PeaShooterListener shootClass = new PeaShooterListener(this);
	public final PlayerQuiteEvent playerq = new PlayerQuiteEvent();
	public final Permissions permissions = new Permissions();
	public final CountDowns countDowns = new CountDowns(this);
	public final blockBreakPlaceEvent bbp = new blockBreakPlaceEvent();
	public final PlayerHurtEvent playerhurtevent = new PlayerHurtEvent();
	public final PlayerHungerEvent phe = new PlayerHungerEvent();
	public final PlayerCommandEvent pce = new PlayerCommandEvent();
	public Economy econ = null;
	
	private static Main instance;
	 
    public Main () {
        instance = this;
    }
    public static Main getInstance() {
         return instance;
    }
	
	public void onEnable(){
		createConfig("Arenas", "");
		createConfig("Signs", "");
		createConfig("Shop", "");
		FileConfiguration file1 = getConfig("Arenas", "");
		FileConfiguration file2 = getConfig("Signs", "");
		FileConfiguration file3 = getConfig("Shop", "");
		saveConfig(file1, "Arenas", "");
		saveConfig(file2, "Signs", "");
		saveConfig(file3, "Shop", "");
		
		saveDefaultConfig();
		
		getCommand("pxpb").setExecutor(command);
		
		ArenaManager.getManager().loadArenas();
		SignMagager.getManager().loadSign();
		ShopManager.getShopManager().loadShop();
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this.signClick, this);
		pm.registerEvents(this.painhit, this);
		pm.registerEvents(this.shootClass, this);
		pm.registerEvents(this.playerq, this);
		pm.registerEvents(this.bbp, this);
		pm.registerEvents(this.playerhurtevent, this);
		pm.registerEvents(this.phe, this);
		pm.registerEvents(this.pce, this);
		//pm.registerEvents(this, this);
		
		if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
	}
	
	public void onDisable(){
		
	}
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
	}
	
	
	public FileConfiguration createConfig(String name, String subdirectory){
		File dir = new File(this.getDataFolder()+File.separator+subdirectory);
		//this is optional. Only needed if you want to put it into a subdirectory. If not, and you just wanna put your file in the plugin data folder, no need to add "+File.separator+subdirectory" to the constructor parameter. You can remove the second parameter even. If you wanna get fancy, check for null, or overload the method.
		if(!dir.isDirectory()){
			dir.mkdirs(); //creates the directory folders if they dont exist.
		}
		File file = new File(dir,name+".yml"); //initializes the file object
		if(!file.exists()){
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		  return YamlConfiguration.loadConfiguration(file);//returns the newly created configuration object.
	}
	
	@EventHandler
	public void onPlayerInteracte(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(event.getClickedBlock().getType().equals(Material.GRAVEL)){
				econ.depositPlayer(player, 1000);
				player.sendMessage("You Now Have " + econ.getBalance(player));
			}
		}
	}
	
	public void saveConfig(FileConfiguration config, String name, String subdirectory){
		File dir = new File(this.getDataFolder()+File.separator+subdirectory);
		if(!dir.isDirectory()){
		    dir.mkdirs();
		}
		
		File file = new File(dir,name+".yml");
		  
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getConfig(String name, String subdirectory){
		 File dir = new File(this.getDataFolder()+File.separator+subdirectory);
		 
		 if(!dir.isDirectory()){
			 return null; //File directory doesnt exist, file cant exist. Return null.
		 }
		 
		 File file = new File(dir,name+".yml");
		 
		 if(!file.exists()){
		    return null; //File doesnt exist, Return null.
		 }
		 
		 return YamlConfiguration.loadConfiguration(file); //file found, load into config and return it.
	}
}
