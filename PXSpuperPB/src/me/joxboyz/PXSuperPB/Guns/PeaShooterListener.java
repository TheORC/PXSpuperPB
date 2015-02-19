package me.joxboyz.PXSuperPB.Guns;

import java.util.HashMap;

import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PeaShooterListener implements Listener {

	private HashMap<String, Boolean> canPlayerShoot = new HashMap<String, Boolean>();
	private HashMap<String, Integer> machBulRemain = new HashMap<String, Integer>();
	
	Main plugin;
	
	public PeaShooterListener(Main main){
		plugin = main;
	}

	@EventHandler
	public void onPlayerInateracte(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (ArenaManager.getManager().getWhatArenaPlayerIsIn(player) == null) {
			return;
		}
		
		ItemStack itemInHand = player.getItemInHand();
		
		if (itemInHand.getType().equals(Material.IRON_BARDING)) {
			if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Pea Shooter")) {
				firePeaShooter(player);
			}
		} else if (itemInHand.getType().equals(Material.GOLD_BARDING)) {
			if(itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Shot Gun")){
				fireShotGun(player);	
			}

		} else if (itemInHand.getType().equals(Material.DIAMOND_BARDING)) {
			if(itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GREEN + "Machine Gun")){
				fireMachineGun(player);
			}

		}
	}
	
	public float getRandomFloat(float min, float max) {
        return min + (float) (Math.random() * ((1 + max) - min));
    }

	public void firePeaShooter(Player player) {
		if(canPlayerShoot.get(player.getName()) == null || canPlayerShoot.get(player.getName()) == true){
			Snowball snowball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
			snowball.setShooter(player);
			snowball.setVelocity(player.getLocation().getDirection().multiply(2));	
			canPlayerShoot.put(player.getName(), false);
			peaShooterCountDown(player.getName());
			if(ArenaManager.getManager().getWhatArenaPlayerIsIn(player).getBluePlayers().contains(player.getName())){
				ArenaManager.getManager().getWhatArenaPlayerIsIn(player).getBlueTeamSnowBall().add(snowball);
			}else{
				ArenaManager.getManager().getWhatArenaPlayerIsIn(player).getRedTeamSnowBall().add(snowball);
			}
		}
	}

	public void fireShotGun(Player player) {
		if(canPlayerShoot.get(player.getName()) == null || canPlayerShoot.get(player.getName()) == true){
			for(int i = 0; i < 6; i++){
				Snowball snowball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
				snowball.setShooter(player);
				Location location = player.getLocation();
				float randomYaw = getRandomFloat(-12, 12);
				float randomPitch = getRandomFloat(-7, 7);
				location.setYaw(location.getYaw() + randomYaw);
				location.setPitch(location.getPitch() + randomPitch);
				snowball.setVelocity(location.getDirection().multiply(2));
			}
			canPlayerShoot.put(player.getName(), false);
			ShotGunCountDown(player.getName());
		}
	}

	public void fireMachineGun(Player player) {
		if(machBulRemain.get(player.getName()) == null){
			machBulRemain.put(player.getName(), 20);
			player.setExp(20);
		}
		if(machBulRemain.get(player.getName()) >= 1){
			Snowball snowball = player.getWorld().spawn(player.getEyeLocation(), Snowball.class);
			snowball.setShooter(player);
			snowball.setVelocity(player.getLocation().getDirection().multiply(2));	
			int bulre = machBulRemain.get(player.getName());
			bulre--;
			machBulRemain.put(player.getName(), bulre);
			player.setExp(bulre);
			if(bulre <= 0){
				canPlayerShoot.put(player.getName(), false);
				MachineGunCountDown(player.getName());
				player.sendMessage("You are out of bullets for 2.5 seconds!");
			}
		}
	}

	public void peaShooterCountDown(final String playerName) {
		
		new BukkitRunnable()
		{
		  public void run()
		  {
			  canPlayerShoot.put(playerName, true);
			  cancel();
		  }
		 
		}.runTaskLater(plugin, 20L);
		
	}

	public void ShotGunCountDown(final String playerName) {
	
		new BukkitRunnable()
		{
			public void run()
			{
				canPlayerShoot.put(playerName, true);
				cancel();
			}
	 
		}.runTaskLater(plugin, 30L);
	
	}

	public void MachineGunCountDown(final String playerName) {
		
		new BukkitRunnable()
		{
		  public void run()
		  {
			  canPlayerShoot.put(playerName, true);
			  machBulRemain.put(playerName, 20);
			  cancel();
		  }
		 
		}.runTaskLater(plugin, 50L);
		
	}
}
