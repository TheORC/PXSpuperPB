package me.joxboyz.PXSuperPB.ParticleEffects;

import net.minecraft.server.v1_8_R1.EnumParticle;
import net.minecraft.server.v1_8_R1.PacketPlayOutWorldParticles;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
 
public enum ParticleEffects {
 
	EXPLOSION_NORMAL("EXPLOSION_NORMAL", 0, 17),
	EXPLOSION_LARGE("EXPLOSION_LARGE", 1, 1),
	EXPLOSION_HUGE("EXPLOSION_HUGE", 2, 0),
	FIREWORKS_SPARK("FIREWORKS_SPARK", 3, 2),
	WATER_BUBBLE("WATER_BUBBLE", 4, 3),
	WATER_SPLASH("WATER_SPLASH", 5, 21),
	WATER_WAKE("WATER_WAKE", 6, -1),
	SUSPENDED("SUSPENDED", 7, 4),
	SUSPENDED_DEPTH("SUSPENDED_DEPTH", 8, 5),
	CRIT("CRIT", 9, 7),
	CRIT_MAGIC("CRIT_MAGIC", 10, 8),
	SMOKE_NORMAL("SMOKE_NORMAL", 11, -1),
	SMOKE_LARGE("SMOKE_LARGE", 12, 22),
	SPELL("SPELL", 13, 11),
	SPELL_INSTANT("SPELL_INSTANT", 14, 12),
	SPELL_MOB("SPELL_MOB", 15, 9),
	SPELL_MOB_AMBIENT("SPELL_MOB_AMBIENT", 16, 10),
	SPELL_WITCH("SPELL_WITCH", 17, 13),
	DRIP_WATER("DRIP_WATER", 18, 27),
	DRIP_LAVA("DRIP_LAVA", 19, 28),
	VILLAGER_ANGRY("VILLAGER_ANGRY", 20, 31),
	VILLAGER_HAPPY("VILLAGER_HAPPY", 21, 32),
	TOWN_AURA("TOWN_AURA", 22, 6),
	NOTE("NOTE", 23, 24),
	PORTAL("PORTAL", 24, 15),
	ENCHANTMENT_TABLE("ENCHANTMENT_TABLE", 25, 16),
	FLAME("FLAME", 26, 18),
	LAVA("LAVA", 27, 19),
	FOOTSTEP("FOOTSTEP", 28, 20),
	CLOUD("CLOUD", 29, 23),
	REDSTONE("REDSTONE", 30, 24),
	SNOWBALL("SNOWBALL", 31, 25),
	SNOW_SHOVEL("SNOW_SHOVEL", 32, 28),
	SLIME("SLIME", 33, 29),
	HEART("HEART", 34, 30),
	BARRIER("BARRIER", 35, -1),
	ITEM_CRACK("ITEM_CRACK", 36, 33),
	BLOCK_CRACK("BLOCK_CRACK", 37, 34),
	BLOCK_DUST("BLOCK_DUST", 38, -1),
	WATER_DROP("WATER_DROP", 39, -1),
	ITEM_TAKE("ITEM_TAKE", 40, -1),
	MOB_APPEARANCE("MOB_APPEARANCE", 41, -1);

	private String name;
	private int id;
	private int legacyId;

	ParticleEffects(String name, int id, int legacyId){
		this.name = name;
		this.id = id;
		this.legacyId = legacyId;
	}

	String getName(){
		return name;
	}

	int getId(){
		return id;
	}

	int getLegacyId(){
		return legacyId;
	}
 
    public void sendToPlayer(Player p, Location whereToPlayer, float offsetX, float offsetY, float offsetZ, float speed, int count){
    	//Declare floats for X, Y, Z.
    	   float x = (float) whereToPlayer.getX();
    	   float y = (float) whereToPlayer.getY();
    	   float z = (float) whereToPlayer.getZ();
    	   //Declare the packet
    	   PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.valueOf(this.getName()), true, x, y, z, offsetX, offsetY, offsetZ, speed, count, null);
    	   ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

    }
   

}