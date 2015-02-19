package me.joxboyz.PXSuperPB.ArenaSetUpVar;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetUpVar {
	
	private static SetUpVar st = new SetUpVar();
	
	public Location redSpawn = null;
	public Location blueSpawn = null;
	public Location lobbySpawn = null;
	public Location endLocation = null;
	
	public static SetUpVar getManager() {
		return st;
	}

	public Location getRedSpawn(){
		return this.redSpawn;
	}
	
	public Location getBlueSpawn(){
		return this.blueSpawn;
	}
	
	public Location getLobbySpawn(){
		return this.lobbySpawn;
	}
	
	public Location getEndLocation(){
		return this.endLocation;
	}
	
	public void setEndLocation(Player player){
		this.endLocation = player.getLocation();
	}
	
	public void setRedSpawn(Player player){
		this.redSpawn = player.getLocation();
	}
	
	public void setBlueSpawn(Player player){
		this.blueSpawn = player.getLocation();
	}
	
	public void setLobbySpawn(Player player){
		this.lobbySpawn = player.getLocation();
	}
	
	public Boolean arenaIsReadyToBeCreated(){
		
		if(this.redSpawn == null){
			return false;
		}
		if(this.blueSpawn == null){
			return false;
		}
		if(this.lobbySpawn == null){
			return false;
		}
		if(this.endLocation == null){
			return false;
		}
		
		return true;
	}
	
	public void reset(){
		this.redSpawn = null;
		this.blueSpawn = null;
		this.lobbySpawn = null;
	}
	
}
