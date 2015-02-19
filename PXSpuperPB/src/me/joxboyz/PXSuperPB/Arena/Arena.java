package me.joxboyz.PXSuperPB.Arena;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitTask;
 
/**
*
* @Author Jake
* @Author Oliver
*/
public class Arena {
 
//A list of all the Arena Objects
public static ArrayList<Arena> arenaObjects = new ArrayList<Arena>();
 
//Some fields we want each Arena object to store:
private Location joinLocation, blueStartLocation, redStartLocation, endLocation; //Some general arena locations
 
private String name; //Arena name
private ArrayList<String> players = new ArrayList<String>(); //And arraylist of players name

//My team Stuff
private ArrayList<String> redTeam = new ArrayList<String>();
private ArrayList<String> blueTeam = new ArrayList<String>();

//Player waiting to join the other teams.
private ArrayList<String> waitingForBlue = new ArrayList<String>();
private ArrayList<String> waitingForRed = new ArrayList<String>();

//Player using different weapons.
private ArrayList<String> playerUsingPeaShooter = new ArrayList<String>();
private ArrayList<String> playerUsingShotGun = new ArrayList<String>();
private ArrayList<String> playerUsingMachineGun = new ArrayList<String>();

private ArrayList<Snowball> redSnowBalls = new ArrayList<Snowball>();
private ArrayList<Snowball> blueSnowBalls = new ArrayList<Snowball>();

//Count down
private int countDown;
private int countDownID;
private BukkitTask countDownTask;

//Score
private int blueTeamScore;
private int redTeamScore;

private int maxPlayers;
private int minPlayers;

private int blueLastScore;
private int redLastScore;

 
private boolean inGame = false; //Boolean to determine if an Arena is ingame or not, automaticly make it false
private boolean countDownStarted = false;
 
//Now for a Constructor:
public Arena (String arenaName, Location joinLocation, Location blueStartLocation, Location redStartLocation, Location endLocation, int maxPlayers, int minPlayers, int gameTime) { //So basicly: Arena myArena = new Arena("My Arena", joinLocation, startLocation, endLocation, 17)
	//Lets initalize it all:
	this.name = arenaName;
	this.joinLocation = joinLocation;
	this.blueStartLocation = blueStartLocation;
	this.redStartLocation = redStartLocation;
	this.endLocation = endLocation;
	this.maxPlayers = maxPlayers;
	this.minPlayers = minPlayers;
	this.countDown = gameTime;
 
	//Now lets add this object to the list of objects:
	arenaObjects.add(this);
 
}
 
//Now for some Getters and Setters, so with our arena object, we can use special methods:

public World getWorld(){
	return this.getRedStartLocation().getWorld();
}
public void setCountdownTask(BukkitTask countDownTask){
	this.countDownTask = countDownTask;
}

public BukkitTask getCountDownTask(){
	return this.countDownTask;
}

public ArrayList<Snowball> getRedTeamSnowBall(){
	return this.redSnowBalls;
}

public ArrayList<Snowball> getBlueTeamSnowBall(){
	return this.blueSnowBalls;
}

public int getRedLastScore(){
	return this.redLastScore;
}

public int getBlueLastScore(){
	return this.blueLastScore;
}

public void setRedLastScore(int score){
	this.redLastScore = score;
}

public void setBlueLastScore(int score){
	this.blueLastScore = score;
}

public void setRedTeamScore(int score){
	this.redTeamScore = score;
}

public void setBlueTeamScore(int score){
	this.blueTeamScore = score;
}

public int getRedTeamScore(){
	return this.redTeamScore;
}

public int getBlueTeamScore(){
	return this.blueTeamScore;
}

public String winnerTeam(){
	if(this.blueTeamScore > this.redTeamScore){
		String blue = "blue";
		return blue;
	}else if(this.redTeamScore > this.blueTeamScore){
		String red = "red";
		return red;
	}
	
	String draw = "draw";
	return draw;
}

public int getCountDown(){
	return this.countDown;
}

public int getCountDownID(){
	return this.countDownID;
}

public void setCountDown(int amount){
	this.countDown = amount;
}

public void setCountDownID(int ID){
	this.countDownID = ID;
}

public ArrayList<String> getPlayerUsingPeaShooter(){
	return this.playerUsingPeaShooter;
}
public ArrayList<String> getPlayerUsingShotGun(){
	return this.playerUsingShotGun;
}
public ArrayList<String> getPlayerUsingMachineGun(){
	return this.playerUsingMachineGun;
}

public String getFirstPlayerInRedWaitList(){
	return this.waitingForRed.get(0);
}

public String getFirstPlayerInBlueWaitList(){
	return this.waitingForBlue.get(0);
}

public ArrayList<String> getPlayersWaitingForBlue(){
	return this.waitingForBlue;
}

public ArrayList<String> getPlayersWaitingForRed(){
	return this.waitingForRed;
}

public void addPlayerBlueWaitList(String playername){
	this.waitingForBlue.add(playername);
}

public void addPlayerRedWaitList(String playername){
	this.waitingForRed.add(playername);
}

public Location getJoinLocation() {
	return this.joinLocation;
}
 
public void setJoinLocation(Location joinLocation) {
	this.joinLocation = joinLocation;
}
 
public Location getRedStartLocation() {
	return this.redStartLocation;
}

public Location getBlueStartLocation(){
	return this.blueStartLocation;
}
 
public void setRedStartLocation(Location startLocation) {
	this.redStartLocation = startLocation;
}

public void setBlueStartLocation(Location startLocation) {
	this.blueStartLocation = startLocation;
}
 
public Location getEndLocation() {
	return this.endLocation;
}
 
public void setEndLocation(Location endLocation) {
	this.endLocation = endLocation;
}
 
public String getName() {
	return this.name;
}
 
public void setName(String name) {
	this.name = name;
}
 
public int getMaxPlayers() {
	return this.maxPlayers;
}
 
public void setMaxPlayers(int maxPlayers) {
	this.maxPlayers = maxPlayers;
}

public int getMinPlayers(){
	return this.minPlayers;
}

public void setMinPlayers(int minPlayers){
	this.minPlayers = minPlayers;
}
 
public ArrayList<String> getPlayers() {
	return this.players;
}

public ArrayList<String> getRedPlayers(){
	return this.redTeam;
}

public ArrayList<String> getBluePlayers(){
	return this.blueTeam;
}
 
 
 
//And finally, some booleans:
public boolean isFull() { //Returns weather the arena is full or not
	if (players.size() >= maxPlayers) {
		return true;
	} else {
		return false;
	}
}
 
 
public boolean isInGame() {
	return inGame;
}
 
public void setInGame(boolean inGame) {
	this.inGame = inGame;
}

public boolean countDownStarted(){
	return this.countDownStarted;
}

public void setCountDown(boolean countDown){
	this.countDownStarted = countDown;
}
 
//To send each player in the arena a message
@SuppressWarnings("deprecation")
public void sendMessage(String message) {
	for (String s: players) {
		Bukkit.getPlayer(s).sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB" + ChatColor.GRAY + "] " +message);
	}
}
 
 
}