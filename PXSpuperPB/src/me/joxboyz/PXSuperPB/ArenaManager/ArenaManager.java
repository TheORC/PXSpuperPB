package me.joxboyz.PXSuperPB.ArenaManager;

import me.joxboyz.PXSuperPB.Arena.Arena;
import me.joxboyz.PXSuperPB.ArenaSetUpVar.SetUpVar;
import me.joxboyz.PXSuperPB.Main.Main;
import me.joxboyz.PXSuperPB.ScoreBoard.ScoreBoardManager;
import me.joxboyz.PXSuperPB.SignManager.SignMagager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

/**
 * 
 * @Author Jake
 */
public class ArenaManager {

	private static Main main = null;

	private static ArenaManager am = new ArenaManager();

	// Usefull for getting the ArenaManager, like so: ArenaManager.getManager()
	public static ArenaManager getManager() {
		return am;
	}

	// Update the scoreBoard when a player is painted!
	public void playerPaintAnother(String shootersTeam, String shootersName,
			String paintPlayersName, Arena arena) {
		// See what team the shooter was in.
		if (shootersTeam.equalsIgnoreCase("blue")) {
			// If the shooter is in the blue team run this.
			if (shootersTeam.equalsIgnoreCase("red")) {
				Bukkit.getConsoleSender().sendMessage(
						"The player " + shootersName + " is in both team!");
				return;
			}
			// Tell the players in the arena!
			arena.sendMessage(ChatColor.BLUE + shootersName + ChatColor.YELLOW
					+ " has painted " + ChatColor.RED + paintPlayersName);
			// Get the current score and add to it.
			int score = arena.getBlueTeamScore();
			score++;
			// Not update the score on the board.
			arena.setBlueTeamScore(score);

		} else if (shootersTeam.equalsIgnoreCase("red")) {
			// If the shooter is in the red team run this.
			if (shootersTeam.equalsIgnoreCase("blue")) {
				Bukkit.getConsoleSender().sendMessage(
						"The player " + shootersName + " is in both team!");
				return;
			}
			// Tell the players in the arena!
			arena.sendMessage(ChatColor.RED + shootersName + ChatColor.YELLOW
					+ " has painted " + ChatColor.BLUE + paintPlayersName);
			// Get the current score and add to it.
			int score = arena.getRedTeamScore();
			score++;
			// Not update the score on the board.
			arena.setRedTeamScore(score);
		} else {
			// If the shooter is in no teams, then run this because there is a
			// problem.
			arena.sendMessage(ChatColor.RED
					+ "Well it looks like some one is hacking, becuase someone not in any team is painting every one!");
		}

		// Up date the score board.
		ScoreBoardManager.getScoreBoardManager().updateArenaScoreBoard(
				arena.getName());
	}

	// A method for getting one of the Arenas out of the list by name:
	public Arena getArena(String name) {
		for (Arena a : Arena.arenaObjects) { // For all of the arenas in the
												// list of objects
			if (a.getName().equals(name)) { // If the name of an arena object in
											// the list is equal to the one in
											// the parameter...
				return a; // Return that object
			}
		}
		return null; // No objects were found, return null
	}

	// A method for adding players to the red team
	public void addPlayerToRed(Player player, String arenaName) {
		if (getArena(arenaName) != null) { // If the arena exsists
			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method
			arena.getRedPlayers().add(player.getName());
			
			if(arena.getBluePlayers().contains(player.getName())){
				arena.getBluePlayers().remove(player.getName());
			}
		} else { // The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ "The arena you are looking for could not be found!");
		}
	}

	// A method for adding players to the blue team
	public void addPlayerToBlue(Player player, String arenaName) {
		if (getArena(arenaName) != null) { // If the arena exsists
			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method
			arena.getBluePlayers().add(player.getName());
			if(arena.getRedPlayers().contains(player.getName())){
				arena.getRedPlayers().remove(player.getName());
			}
		} else { // The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ "The arena you are looking for could not be found!");
		}
	}

	// Join the arena lobby
	public void joinLobby(Player player, String arenaName) {
		if (getArena(arenaName) != null) { // If the arena exsists
			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method
			if (!arena.isFull()) { // If the arena is not full
				if (!arena.isInGame()) {
					if (!arena.getPlayers().contains(player.getName())) {
						// Every check is complete, arena is joinable
						player.getInventory().clear(); // Clear the players
														// inventory
						player.setHealth(player.getMaxHealth()); // Heal the
																	// player
						player.setFireTicks(0); // Heal the player even more ^ ^
												// ^
						player.setFoodLevel(20);// set the food
						player.setGameMode(GameMode.SURVIVAL);// change the
																// gameMode

						// Teleport to the arena's join location
						player.teleport(arena.getJoinLocation());

						// Add the player to the arena list
						arena.getPlayers().add(player.getName()); // Add the
																	// players
																	// name to
																	// the arena

						// update the arena sign
						SignMagager.getManager().updateSignForJoinOrLeave(
								arenaName);

						int playersLeft = arena.getMinPlayers()
								- arena.getPlayers().size(); // How many players
																// needed to
																// start

						// Send the arena's players a message
						if (playersLeft < 0) {
							arena.sendMessage(ChatColor.BLUE
									+ player.getName()
									+ " has joined the arena! We only need 0 to start the game!");
						} else {
							arena.sendMessage(ChatColor.BLUE + player.getName()
									+ " has joined the arena! We only need "
									+ playersLeft + " to start the game!");
						}

						if (playersLeft <= 0) { // IF there are 0 players needed
												// to start the game
							// make sure the arena is not in game
							if (arena.isInGame() == true) {
								Bukkit.getConsoleSender()
										.sendMessage(
												ChatColor.RED
														+ "A player has joined an Arena that is in game!!!! HELP!!!!");
								return;
							}
							// if the count down has not started run this
							if (!arena.countDownStarted()) {
								// Start the before game count down
								main.countDowns.gameStartCountdown(arena);
								// set the arena countdown to true
								arena.setCountDown(true);
							}
						}
					}else{
						player.sendMessage(ChatColor.GRAY
								+ "["
								+ ChatColor.GREEN
								+ "PXPB"
								+ ChatColor.GRAY
								+ "]"
								+ ChatColor.RED
								+ " You are already in this arena!");
					}

				} else { // Specifiend arena is in game, send the player an
							// error message
					player.sendMessage(ChatColor.GRAY
							+ "["
							+ ChatColor.GREEN
							+ "PXPB"
							+ ChatColor.GRAY
							+ "]"
							+ ChatColor.RED
							+ "The arena you are looking for is currently full!");
				}
			} else { // Specified arena is full, send the player an error
						// message
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED
						+ "The arena you are looking for is currently full!");
			}

		} else { // The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ "The arena you are looking for could not be found!");
		}
	}

	// Set the kit the player will join with
	public void setPlayerKitForJoin(String arenaName, Player player,
			String kitName) {
		Arena arena = ArenaManager.getManager().getArena(arenaName);

		if (arena == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ "This arena does not exsist!");
			return;
		}

		if (kitName.equalsIgnoreCase("peashooter")) {
			if (arena.getPlayerUsingShotGun().contains(player.getName())) {
				arena.getPlayerUsingShotGun().remove(player.getName());
			}
			if (arena.getPlayerUsingMachineGun().contains(player.getName())) {
				arena.getPlayerUsingMachineGun().remove(player.getName());
			}
			if (arena.getPlayerUsingPeaShooter().contains(player.getName())) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]"
						+ "You already have this kit selected!");
				return;
			}
			arena.getPlayerUsingPeaShooter().add(player.getName());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]"
					+ "You have selected the peas shooter");

		} else if (kitName.equalsIgnoreCase("Shotgun")) {
			if (arena.getPlayerUsingShotGun().contains(player.getName())) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]"
						+ "You already have this kit selected!");
				return;
			}
			if (arena.getPlayerUsingMachineGun().contains(player.getName())) {
				arena.getPlayerUsingMachineGun().remove(player.getName());
			}
			if (arena.getPlayerUsingPeaShooter().contains(player.getName())) {
				arena.getPlayerUsingPeaShooter().remove(player.getName());
			}

			arena.getPlayerUsingShotGun().add(player.getName());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + "You have selected the shot gun");

		} else if (kitName.equalsIgnoreCase("MachineGun")) {
			if (arena.getPlayerUsingShotGun().contains(player.getName())) {
				arena.getPlayerUsingShotGun().remove(player.getName());
			}
			if (arena.getPlayerUsingMachineGun().contains(player.getName())) {
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]"
						+ "You already have this kit selected!");
				return;
			}
			if (arena.getPlayerUsingPeaShooter().contains(player.getName())) {
				arena.getPlayerUsingPeaShooter().remove(player.getName());
			}
			arena.getPlayerUsingMachineGun().add(player.getName());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]"
					+ "You have selected the Machine gun");
		}
	}

	public void setPlayerKitBlueTeam(String arenaName, String name) {
		Arena arena = ArenaManager.getManager().getArena(arenaName);
		@SuppressWarnings("deprecation")
		Player player = Bukkit.getPlayer(name);

		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta hm = (LeatherArmorMeta) helmet.getItemMeta();
		hm.setColor(Color.BLUE);
		helmet.setItemMeta(hm);

		ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta cm = (LeatherArmorMeta) helmet.getItemMeta();
		cm.setColor(Color.BLUE);
		chestPlate.setItemMeta(cm);

		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta lm = (LeatherArmorMeta) helmet.getItemMeta();
		lm.setColor(Color.BLUE);
		leggins.setItemMeta(lm);

		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bm = (LeatherArmorMeta) helmet.getItemMeta();
		bm.setColor(Color.BLUE);
		boots.setItemMeta(bm);

		ItemStack peas = new ItemStack(Material.IRON_BARDING);
		ItemMeta peasm = peas.getItemMeta();
		peasm.setDisplayName(ChatColor.GREEN + "Pea Shooter");
		peas.setItemMeta(peasm);

		ItemStack shot = new ItemStack(Material.GOLD_BARDING);
		ItemMeta shotm = shot.getItemMeta();
		shotm.setDisplayName(ChatColor.GREEN + "Shot Gun");
		shot.setItemMeta(shotm);

		ItemStack mach = new ItemStack(Material.DIAMOND_BARDING);
		ItemMeta machm = mach.getItemMeta();
		machm.setDisplayName(ChatColor.GREEN + "Machine Gun");
		mach.setItemMeta(machm);

		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestPlate);
		player.getInventory().setLeggings(leggins);
		player.getInventory().setBoots(boots);

		if (arena.getPlayerUsingPeaShooter().contains(name)) {
			player.getInventory().addItem(peas);
		} else if (arena.getPlayerUsingShotGun().contains(name)) {
			player.getInventory().addItem(shot);
		} else if (arena.getPlayerUsingMachineGun().contains(name)) {
			player.getInventory().addItem(mach);
		} else {
			player.getInventory().addItem(peas);
			player.sendMessage(ChatColor.GRAY
					+ "["
					+ ChatColor.GREEN
					+ "PXPB"
					+ ChatColor.GRAY
					+ "]"
					+ "You did not select a kit, so I gave you the pea shooter you lucky duck :p");
		}
		player.sendMessage(ChatColor.GRAY
				+ "["
				+ ChatColor.GREEN
				+ "PXPB"
				+ ChatColor.GRAY
				+ "]"
				+ ChatColor.BLUE + "You have joined the " + ChatColor.AQUA + "blue" + ChatColor.BLUE + " team");
	}

	public void setPlayerKitRedTeam(String arenaName, String name) {
		Arena arena = ArenaManager.getManager().getArena(arenaName);
		@SuppressWarnings("deprecation")
		Player player = Bukkit.getPlayer(name);

		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta hm = (LeatherArmorMeta) helmet.getItemMeta();
		hm.setColor(Color.RED);
		helmet.setItemMeta(hm);

		ItemStack chestPlate = new ItemStack(Material.LEATHER_CHESTPLATE);
		LeatherArmorMeta cm = (LeatherArmorMeta) helmet.getItemMeta();
		cm.setColor(Color.RED);
		chestPlate.setItemMeta(cm);

		ItemStack leggins = new ItemStack(Material.LEATHER_LEGGINGS);
		LeatherArmorMeta lm = (LeatherArmorMeta) helmet.getItemMeta();
		lm.setColor(Color.RED);
		leggins.setItemMeta(lm);

		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
		LeatherArmorMeta bm = (LeatherArmorMeta) helmet.getItemMeta();
		bm.setColor(Color.RED);
		boots.setItemMeta(bm);

		ItemStack peas = new ItemStack(Material.IRON_BARDING);
		ItemMeta peasm = peas.getItemMeta();
		peasm.setDisplayName(ChatColor.GREEN + "Pea Shooter");
		peas.setItemMeta(peasm);

		ItemStack shot = new ItemStack(Material.GOLD_BARDING);
		ItemMeta shotm = shot.getItemMeta();
		shotm.setDisplayName(ChatColor.GREEN + "Shot Gun");
		shot.setItemMeta(shotm);

		ItemStack mach = new ItemStack(Material.DIAMOND_BARDING);
		ItemMeta machm = mach.getItemMeta();
		machm.setDisplayName(ChatColor.GREEN + "Machine Gun");
		mach.setItemMeta(machm);

		player.getInventory().setHelmet(helmet);
		player.getInventory().setChestplate(chestPlate);
		player.getInventory().setLeggings(leggins);
		player.getInventory().setBoots(boots);

		if (arena.getPlayerUsingPeaShooter().contains(name)) {
			player.getInventory().addItem(peas);
		} else if (arena.getPlayerUsingShotGun().contains(name)) {
			player.getInventory().addItem(shot);
		} else if (arena.getPlayerUsingMachineGun().contains(name)) {
			player.getInventory().addItem(mach);
		} else {
			player.getInventory().addItem(peas);
			player.sendMessage(ChatColor.GRAY
					+ "["
					+ ChatColor.GREEN
					+ "PXPB"
					+ ChatColor.GRAY
					+ "]"
					+ ChatColor.BLUE + "You did not select a kit, so I gave you the pea shooter you lucky duck :p");
		}
		player.sendMessage(ChatColor.GRAY
				+ "["
				+ ChatColor.GREEN
				+ "PXPB"
				+ ChatColor.GRAY
				+ "]"
				+ ChatColor.BLUE + "You have joined the " + ChatColor.RED + "red" + ChatColor.BLUE + " team");
		;
	}

	public Arena getWhatArenaPlayerIsIn(Player player) {
		for (Arena arena : Arena.arenaObjects) {
			if (arena.getPlayers().contains(player.getName())) {
				return arena;
			}
		}
		return null;
	}

	public void sendPlayerBackToSpawn(Player player, String arenaName) {
		Arena arena = this.getArena(arenaName);
		if (arena.getBluePlayers().contains(player.getName())) {
			player.teleport(arena.getBlueStartLocation());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + "You have been sent back to spawn");
		} else {
			player.teleport(arena.getRedStartLocation());
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + "You have been sent back to spawn");
		}
	}

	// A method for removing players
	public void removePlayer(Player player, String arenaName) {

		if (getArena(arenaName) != null) { // If the arena exsists

			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method

			if (arena.getPlayers().contains(player.getName())) { // If the arena
																	// has the
																	// player
																	// already

				// Every check is complete, arena is leavable
				player.getInventory().clear(); // Clear the players inventory
				player.getInventory().setBoots(null);
				player.getInventory().setHelmet(null);
				player.getInventory().setChestplate(null);
				player.getInventory().setLeggings(null);
				player.setHealth(player.getMaxHealth()); // Heal the player
				player.setFireTicks(0); // Heal the player even more ^ ^ ^

				// Teleport to the arena's join location
				player.teleport(arena.getEndLocation());

				// remove the player to the arena list
				arena.getPlayers().remove(player.getName()); // Removes the
																// players name
																// to the arena
				if (arena.getBluePlayers().contains(player)) {
					arena.getBluePlayers().remove(player);
				}

				if (arena.getRedPlayers().contains(player)) {
					arena.getRedPlayers().remove(player);
				}

				SignMagager.getManager().updateSignForJoinOrLeave(arenaName);
				ScoreBoardManager.getScoreBoardManager()
						.removePlayerScoreBoard(player);

				// Send the arena's players a message
				arena.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]" + ChatColor.BLUE
						+ player.getName() + " has left the Arena! There are "
						+ arena.getPlayers().size()
						+ " players currently left!");

				if (arena.countDownStarted()) {
					if (arena.isInGame() == false) {
						if (arena.getPlayers().size() < arena.getMinPlayers()) {
							if (arena.getCountDownTask() != null) {
								arena.getCountDownTask().cancel();
							}
							arena.setCountDown(false);
							arena.sendMessage(ChatColor.RED
									+ "Countdown stoped! Not enough players");
							Bukkit.getConsoleSender()
									.sendMessage(
											ChatColor.GRAY
													+ "["
													+ ChatColor.GREEN
													+ "PXPB"
													+ ChatColor.GRAY
													+ "]"
													+ ChatColor.RED
													+ "Countdown stoped! Not enough players");
						}
					}
				}

			} else { // Specified arena doesn't have the player, send the player
						// an error message
				player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
						+ "PXPB" + ChatColor.GRAY + "]" + ChatColor.RED
						+ "Your not in the arena your looking for!");

			}

		} else { // The arena doesn't exsist, send the player an error message
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ "The arena you are looking for could not be found!");
		}
	}

	// A method for starting an Arena:
	@SuppressWarnings("deprecation")
	public void startArena(String arenaName) {

		if (getArena(arenaName) != null) { // If the arena exsists

			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method

			// Set ingame
			arena.setInGame(true);
			arena.setCountdownTask(null);

			SignMagager.getManager().upDateGameStateSign(arenaName);
			ScoreBoardManager.getScoreBoardManager().gameCountDown(arenaName);

			for (String s : arena.getPlayers()) {// Loop through every player in
													// the arena

				if (arena.getBluePlayers().contains(s)) {
					Bukkit.getPlayer(s).teleport(arena.getBlueStartLocation());
					setPlayerKitBlueTeam(arenaName, s);
				} else if (arena.getRedPlayers().contains(s)) {
					Bukkit.getPlayer(s).teleport(arena.getRedStartLocation());
					setPlayerKitRedTeam(arenaName, s);
				} else {
					if (arena.getBluePlayers().size() >= arena.getRedPlayers()
							.size()) {
						arena.getRedPlayers().add(s);
						Bukkit.getPlayer(s).teleport(
								arena.getRedStartLocation());
						setPlayerKitRedTeam(arenaName, s);
					} else {
						arena.getBluePlayers().add(s);
						Bukkit.getPlayer(s).teleport(
								arena.getBlueStartLocation());
						setPlayerKitBlueTeam(arenaName, s);
					}
				}
			}
			Bukkit.getConsoleSender().sendMessage("Players: " + arena.getPlayers());
			Bukkit.getConsoleSender().sendMessage("Blue Players: " + arena.getBluePlayers());
			Bukkit.getConsoleSender().sendMessage("Red Players: " + arena.getRedPlayers());
		}
	}

	public void givePrize(String arenaName) {
		if (getArena(arenaName) != null) { // If the arena exsists

			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method
			for (String s : arena.getPlayers()) {
				@SuppressWarnings("deprecation")
				Player player = Bukkit.getPlayer(s);
				if (arena.winnerTeam().equalsIgnoreCase("red")) {
					if (arena.getRedPlayers().contains(s)) {
						main.econ.depositPlayer(player, 200);
						player.sendMessage(ChatColor.GRAY + "["
								+ ChatColor.GREEN + "PXPB" + ChatColor.GRAY
								+ "]" + "You Won 200 Cash!");
					} else {
						main.econ.depositPlayer(player, 50);
						player.sendMessage(ChatColor.GRAY + "["
								+ ChatColor.GREEN + "PXPB" + ChatColor.GRAY
								+ "]" + "You Won 50 Cash!");
					}
				} else if (arena.winnerTeam().equalsIgnoreCase("blue")) {
					if (arena.getBluePlayers().contains(s)) {
						main.econ.depositPlayer(player, 200);
						player.sendMessage(ChatColor.GRAY + "["
								+ ChatColor.GREEN + "PXPB" + ChatColor.GRAY
								+ "]" + "You Won 200 Cash!");
					} else {
						main.econ.depositPlayer(player, 50);
						player.sendMessage(ChatColor.GRAY + "["
								+ ChatColor.GREEN + "PXPB" + ChatColor.GRAY
								+ "]" + "You Won 50 Cash!");
					}

				} else if (arena.winnerTeam().equalsIgnoreCase("draw")) {
					main.econ.depositPlayer(player, 75);
					player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN
							+ "PXPB" + ChatColor.GRAY + "]"
							+ "You Won 75 Cash!");
				}
			}
		}
	}

	// A method for ending an Arena:
	@SuppressWarnings("deprecation")
	public void endArena(String arenaName) {

		if (getArena(arenaName) != null) { // If the arena exsists

			Arena arena = getArena(arenaName); // Create an arena for using in
												// this method

			// Send them a message
			arena.sendMessage(ChatColor.GOLD + "The arena has ended :(");

			// Set ingame
			arena.setInGame(false);

			givePrize(arenaName);

			for (String s : arena.getPlayers()) {// Loop through every player in
													// the arena

				// Teleport them:

				Player player = Bukkit.getPlayer(s); // Create a player by the
														// name
				player.teleport(arena.getEndLocation());

				player.getInventory().clear(); // Clear the players inventory
				player.setHealth(player.getMaxHealth()); // Heal the player
				player.setFireTicks(0); // Heal the player even more ^ ^ ^

				ScoreBoardManager.getScoreBoardManager().removePlayerScoreBoard(player);
			}
			Bukkit.getConsoleSender().sendMessage("Players: " + arena.getPlayers());
			Bukkit.getConsoleSender().sendMessage("Blue Players: " + arena.getBluePlayers());
			Bukkit.getConsoleSender().sendMessage("Red Players: " + arena.getRedPlayers());
			arena.getPlayers().clear();
			arena.getBluePlayers().clear();
			arena.getRedPlayers().clear();
			Bukkit.getConsoleSender().sendMessage("Players: " + arena.getPlayers());
			Bukkit.getConsoleSender().sendMessage("Blue Players: " + arena.getBluePlayers());
			Bukkit.getConsoleSender().sendMessage("Red Players: " + arena.getRedPlayers());

			arena.getBlueTeamSnowBall().clear();
			arena.getRedTeamSnowBall().clear();
			arena.setBlueTeamScore(0);
			arena.setRedTeamScore(0);
			arena.setCountdownTask(null);
			arena.setCountDown(false);
			SignMagager.getManager().upDateGameStateSign(arenaName);
		}
	}

	// And our final method, loading each arena
	// This will be resonsible for creating each arena from the config, and
	// creating an object to represent it
	// Call this method in your main class, onEnable

	public void loadArenas() {
		main = Main.getInstance();

		// I just create a quick Config Variable, obviously don't do this.
		// Use your own config file

		FileConfiguration fc = main.getConfig("Arenas", ""); // If you just use
																// this code, it
																// will
		// erorr, its null. Read the notes
		// above, USE YOUR OWN CONFIGURATION
		// FILE

		// Youll get an error here, FOR THE LOVE OF GAWD READ THE NOTES ABOVE!!!
		if (fc.getConfigurationSection("arenas") == null) {
			Bukkit.getConsoleSender()
					.sendMessage(
							ChatColor.GRAY
									+ "["
									+ ChatColor.GREEN
									+ "PXPB"
									+ ChatColor.GRAY
									+ "]"
									+ ChatColor.RED
									+ "No arenas have been loaded, because there are non!");
			return;
		}
		for (String keys : fc.getConfigurationSection("arenas").getKeys(false)) { // For
																					// each
																					// arena
																					// name
																					// in
																					// the
																					// arena
																					// file

			// Now lets get all of the values, and make an Arena object for
			// each:
			// Just to help me remember: Arena myArena = new Arena("My Arena",
			// joinLocation, startLocation, endLocation, 17)

			World world = Bukkit.getWorld(fc.getString("arenas." + keys
					+ ".world"));

			// Arena's name is keys

			double joinX = fc.getDouble("arenas." + keys + ".joinX");
			double joinY = fc.getDouble("arenas." + keys + ".joinY");
			double joinZ = fc.getDouble("arenas." + keys + ".joinZ");
			Location joinLocation = new Location(world, joinX, joinY, joinZ);

			double startBlueX = fc.getDouble("arenas." + keys + ".startBlueX");
			double startBlueY = fc.getDouble("arenas." + keys + ".startBlueY");
			double startBlueZ = fc.getDouble("arenas." + keys + ".startBlueZ");

			Location startBlueLocation = new Location(world, startBlueX,
					startBlueY, startBlueZ);

			double startRedX = fc.getDouble("arenas." + keys + ".startRedX");
			double startRedY = fc.getDouble("arenas." + keys + ".startRedY");
			double startRedZ = fc.getDouble("arenas." + keys + ".startRedZ");

			Location startRedLocation = new Location(world, startRedX,
					startRedY, startRedZ);

			double endX = fc.getDouble("arenas." + keys + ".endX");
			double endY = fc.getDouble("arenas." + keys + ".endY");
			double endZ = fc.getDouble("arenas." + keys + ".endZ");

			Location endLocation = new Location(world, endX, endY, endZ);

			int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayers");
			int minPlayers = fc.getInt("arenas." + keys + ".minPlayers");

			int gameTime = fc.getInt("arena." + keys + ".gameTime");

			// Now lets create an object to represent it:
			Arena arena = new Arena(keys, joinLocation, startBlueLocation,
					startRedLocation, endLocation, maxPlayers, minPlayers,
					gameTime);

			Arena.arenaObjects.add(arena);
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
							+ ChatColor.GRAY + "]" + ChatColor.BLUE
							+ "The arena " + ChatColor.YELLOW + arena.getName()
							+ ChatColor.BLUE
							+ " has been loaded. The min players is "
							+ ChatColor.YELLOW + arena.getMinPlayers()
							+ ChatColor.BLUE + " The max players is "
							+ ChatColor.YELLOW + arena.getMaxPlayers());
		}
		main.saveConfig(fc, "Arenas", "");

	}

	// Our final method, create arena!
	public void createArena(String arenaName, Location joinLocation,
			Location blueStartLocation, Location redStartLocation,
			Location endLocation, int maxPlayers, int minPlayers, int gameTime,
			Player player) {

		if (arenaName == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the arena name!");
			return;
		}
		if (joinLocation == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the lobby!");
			return;
		}
		if (blueStartLocation == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the blue spawn!");
			return;
		}
		if (redStartLocation == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the red spawn!");
			return;
		}
		if (endLocation == null) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the end spawn!");
			return;
		}
		if (maxPlayers == 0) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the max player!");
			return;
		}
		if (minPlayers == 0) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the min players!");
			return;
		}
		if (gameTime == 0) {
			player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
					+ ChatColor.GRAY + "]" + ChatColor.RED
					+ " You have not set the game time!");
			return;
		}

		// Now, lets create an arena object to represent it:
		Arena arena = new Arena(arenaName, joinLocation, blueStartLocation,
				redStartLocation, endLocation, maxPlayers, minPlayers, gameTime);

		// Now here is where you would save it all to a file, again, im going to
		// create a null FileConfiguration, USE YOUR OWN!!!
		FileConfiguration fc = main.getConfig("Arenas", "");

		fc.set("arenas." + arenaName, null); // Set its name
		// Now sets the other values

		String path = "arenas." + arenaName + "."; // Shortcut
		// Sets the paths
		fc.set(path + "joinX", joinLocation.getX());
		fc.set(path + "joinY", joinLocation.getY());
		fc.set(path + "joinZ", joinLocation.getZ());

		fc.set(path + "startBlueX", blueStartLocation.getX());
		fc.set(path + "startBlueY", blueStartLocation.getY());
		fc.set(path + "startBlueZ", blueStartLocation.getZ());

		fc.set(path + "startRedX", redStartLocation.getX());
		fc.set(path + "startRedY", redStartLocation.getY());
		fc.set(path + "startRedZ", redStartLocation.getZ());

		fc.set(path + "endX", endLocation.getX());
		fc.set(path + "endY", endLocation.getY());
		fc.set(path + "endZ", endLocation.getZ());

		fc.set(path + "maxPlayers", maxPlayers);
		fc.set(path + "minPlayers", minPlayers);
		fc.set(path + "gameTime", gameTime);
		fc.set(path + "world", blueStartLocation.getWorld().getName());

		// Now save it up down here
		main.saveConfig(fc, "Arenas", "");

		SetUpVar.getManager().reset();

		Arena.arenaObjects.add(arena);

		player.sendMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "PXPB"
				+ ChatColor.GRAY + "]" + ChatColor.BLUE
				+ "You Have Created A Arena!");
	}
}