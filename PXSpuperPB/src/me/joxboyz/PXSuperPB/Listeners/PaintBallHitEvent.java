package me.joxboyz.PXSuperPB.Listeners;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaManager.ArenaManager;
import me.joxboyz.PXSuperPB.Main.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PaintBallHitEvent implements Listener{
	
	Main plugin;
	
	public PaintBallHitEvent(Main main){
		this.plugin = main;
	}

	@EventHandler
	 public void onSnowballHit(EntityDamageByEntityEvent event) throws Exception
	    {
	        Entity entitityDamaged = event.getEntity();
	        Entity whatHurtEntity = event.getDamager();
	 
	        if(entitityDamaged instanceof Player){
		        if(whatHurtEntity instanceof Snowball)
		        {
		            Snowball snowball = (Snowball)whatHurtEntity;
		            
		            LivingEntity entityThrower = (LivingEntity)snowball.getShooter();
		            
		            if(entityThrower instanceof Player)
		            {
		                Player playerThrower = (Player)entityThrower;
		                Player playerHit = (Player)entitityDamaged;
		                
		                Arena arena = ArenaManager.getManager().getWhatArenaPlayerIsIn(playerThrower);
		                
		                if(arena != null){
		                	
		                	//for(String p: arena.getPlayers()){
		                		//ParticleEffects.PORTAL.sendToPlayer(Bukkit.getPlayer(p), playerHit.getLocation(), 0, 0, 0, 1, 50);
		                	//}
		                	
		                	if(arena.getBluePlayers().contains(playerHit.getName()) && arena.getRedPlayers().contains(playerHit.getName())){
		                		//arena.sendMessage(playerHit.getName() + " is in both teams!");
		                		return;
		                	}
		                	if(playerThrower.getName() == playerHit.getName()){
		                		event.setCancelled(true);
		                		//playerThrower.sendMessage("You hit yourself!");
		                		
		                		return;
		                	}
		                	
		                	if(arena.getBluePlayers().contains(playerThrower.getName()) && arena.getBluePlayers().contains(playerHit.getName())){
		                		//Bukkit.getConsoleSender().sendMessage("The blue team containes both players!");
		                		return;
		                	}
		                	
		                	if(arena.getRedPlayers().contains(playerThrower.getName()) && arena.getRedPlayers().contains(playerHit.getName())){
		                		//Bukkit.getConsoleSender().sendMessage("The blue team containes both players!");
		                		return;
		                	}
		                	
		                	if(arena.getBluePlayers().contains(playerThrower.getName())){
		                		ArenaManager.getManager().playerPaintAnother("blue", ((Player) entityThrower).getName(), playerHit.getName(), arena);
		                	}else{
		                		ArenaManager.getManager().playerPaintAnother("red", ((Player) entityThrower).getName(), playerHit.getName(), arena);
		                	}
		                	ArenaManager.getManager().sendPlayerBackToSpawn(playerHit, arena.getName());
		                }
		                
		            }
		        }
	        }
	    }
}
