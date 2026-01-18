// Jivitesh Singh and James Kitaura
// 2024/06/15
// In this game, Sorcerer's Tower, You are a wizard, you have to climb a tower and get to the top to be named the greatest wizard ever. 
// On your way, you will face different enemies on each floor, each floor more difficult than the last, and be able to gain power-ups to help 
// you advance. If you lose at any point you will reset to the start screen where you can access the store and use the 
// currency earned while playing to aid your character in their quest.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javax.sound.sampled.*;

public class Driver extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Runnable{

	// Initialize Variables
	
	// "Character" variables
	static String username = "";
	boolean up = false, down = false, left = false, right = false, bulletLeft = false, bulletRight = false, bulletUp = false, bulletDown = false;
	int speed = 4;
	int screenWidth = 1000;
	int screenHeight = 600;
	Thread thread;
	int FPS = 60;
	boolean dead = false;
	boolean hit = false, mageHit = false, bossHit = false;
	boolean prevDirection = false;
	static int playerHeight = 43;
	static int playerWidth = 43;
	static int staffWidth = 15;
	int spellIndex = 0;
	int numOfSpells = 1;
	int coins = 0;
	int[] spellCards = new int [2];
	static ArrayList <String> availableCards = new ArrayList<> ();
	boolean selectLeft = false, selectRight = false;
	int floorNum = 1;
	double totalMana = 100.0;
	int usernameX = 500;
	int burnTime = 5, burnDamage = 2, stunTime = 60, manaIncrease = 2, fireDamage = 15, voidDamage = 10, goldDamage = 15, iceDamage = 10, freezeTime = 300;
	double maxHp = 100.0;
	boolean canHeal = false;
	String time = "";
	Character player = new Character((int)totalMana, speed, 0);
	int bossFrames = 0;
	List <String> topFive = new ArrayList<>();
	
	// For projectiles
	int posX;
	int posY;
	double projectileX;
	double projectileY;
	double speedX;
	double speedY;
	double desiredSpeed = 2.0;
	ArrayList <Projectiles> projectiles = new ArrayList<>();
	
	// Images
	public static BufferedImage[] characterImg = new BufferedImage[2];
	public static BufferedImage[] projectileImg = new BufferedImage[4];
	public static BufferedImage[] enemyprojectileImg = new BufferedImage[1];
	public static BufferedImage[] staffs = new BufferedImage[4];
	public static BufferedImage[] spellImg = new BufferedImage[5];
	public static BufferedImage[] arrows = new BufferedImage[4];
	public static BufferedImage[] cardUpgrade = new BufferedImage[12];
	public static BufferedImage boss;
	public static BufferedImage cursor;
	public static BufferedImage confirm;
	public static BufferedImage basicEnemy;
	public static BufferedImage mageEnemy;
	public static BufferedImage background;
	public static BufferedImage titleScreen;
	public static BufferedImage settingsScreen;
	public static BufferedImage usernameScreen;
	public static BufferedImage offButton;
	public static BufferedImage onButton;
	public static BufferedImage upgrades;
	public static BufferedImage leaderboard;
	public static BufferedImage about;
	public static BufferedImage instructions;
	
	// Enemies
	static ArrayList <Enemies> enemies = new ArrayList<>();
	static ArrayList <Enemies> mageEnemies = new ArrayList<>();
	static ArrayList <Projectiles> mageProjectiles = new ArrayList<>();
	static double enemySetSpeed = 1.0;
	static double enemyprojectilespeed = 5.0;
	static double enemySpeedX;
	static double enemySpeedY;
	static double enemyX;
	static double enemyY;
	static double mageSetSpeed = 2.0;
	static int enemyHeight = 33;
	static int enemyWidth = 30;
	static boolean spawnMages;
	
	// General
	static int frames = 0;
	int prevFrames = 0;
	int levelFrames;
	int winFrames;
	int gameState = 1;
	int mageFrames;
	
	// Settings
	int upKeyCode = 87, downKeyCode = 83, leftKeyCode = 65, rightKeyCode  = 68, cycleLeftCode = 81, cycleRightCode = 69;
	boolean setUp = false, setDown = false, setLeft = false, setRight = false, setCycleLeft = false, setCycleRight = false;
	String upKey = "W", downKey = "S", leftKey = "A", rightKey = "D", cycleLeftKey = "Q", cycleRightKey = "E";
	boolean gameSounds = true, music = true;
	boolean damage = false, health = false, refillTime = false, buy = false;
	int damagePurchase = 0, healthPurchase = 0, refillPurchase = 0, damageCost = 15, healthCost = 20, refillCost = 25;
	static Map <String, String> leaderBoard = new HashMap<>();
	// Title Screen
	static boolean startHover = false, upgradeHover = false, settingsHover = false, aboutHover = false;	
	
	// To make the cursor work properly
	static int cursorappear = 0;
	static int cursortime = 0;
	static int cursorX = 500;
	static boolean draw = false;
	
	// Levels
	ArrayList <Levels> levels = new ArrayList<>();
	boolean levelsCreated = false;
	
	//Boss
	double bossHealth = 10.0;
	Fahad Ishaq = new Fahad((int)bossHealth, 10, 30);
	boolean hitWall = true;
	boolean readPosition = false;
	static double bossSpeed = 5.0;
	static ArrayList <Projectiles> bossProjectiles = new ArrayList<>();
	double bossprojectilespeed = 6.0;
	boolean addMoreEnemies = false;
	boolean bossDead = false;
	boolean spawnBoss = false;
	boolean disregard = false;
	
	// Refill bar
	double fireTime = 45.0, iceTime = 15.0, goldTime = 90.0, basicTime = 30.0;
	
	// Username
	boolean contains = false;
	
	Clip backGroundMusicClip = AudioSystem.getClip();
	Clip hurtClip = AudioSystem.getClip();
	Clip attackClip = AudioSystem.getClip();
	Clip upgradeClip = AudioSystem.getClip();				
	Clip buttonClip = AudioSystem.getClip();
	Clip bossClip = AudioSystem.getClip();
	
	
	// Description: Set sizes and add motionListener, thread, etc.. and audio clips.
	// Parameters(s): none
	// Return(s): none
	public Driver()throws LineUnavailableException{
		// Setting the screen size and adding the MousListener, KeyLisytener, Thread, etc...
		setPreferredSize(new Dimension(screenWidth,screenHeight));
		addMouseListener(this);
		addMouseMotionListener(this);
		Thread thread = new Thread(this);
		thread.start();
		addKeyListener(this);
		this.setFocusable(true);
		
		// Add audio clips
		try {
			AudioInputStream backGroundMusic = AudioSystem.getAudioInputStream(new File("backGroundMusic.wav"));
			AudioInputStream hurt = AudioSystem.getAudioInputStream(new File("hurt.wav"));
			AudioInputStream attack = AudioSystem.getAudioInputStream(new File("shoot.wav"));
			AudioInputStream upgrade = AudioSystem.getAudioInputStream(new File("upgrade.wav"));
			AudioInputStream button = AudioSystem.getAudioInputStream(new File("button.wav"));
			AudioInputStream boss = AudioSystem.getAudioInputStream(new File("boss.wav"));
			backGroundMusicClip.open(backGroundMusic);
			hurtClip.open(hurt);
			attackClip.open(attack);
			upgradeClip.open(upgrade);
			buttonClip.open(button);
			bossClip.open(boss);
		} catch (UnsupportedAudioFileException e) {
		} catch (IOException e) {
		}

	}

	// Description: Set the thread to 60FPS and run methods
	// Parameters(s): None
	// Return(s): void
	public void run() {
		//initialize();
		while(true) {
			if(dead) {
				backGroundMusicClip.stop();
			}
			while(gameState == 1) {
				repaint();
			}
			while (gameState == 2) {
				//main game loop
				// Run update method.
				update();
				repaint();
				try {
					Thread.sleep(1000/FPS);
					// Only count frames in gameState 2.
					frames++;
				} catch(Exception e) {
					e.printStackTrace();
				}
				// Play the background music.
				if (music) {   
                    backGroundMusicClip.start();
                    backGroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY);
				}else {
					backGroundMusicClip.stop();
				}
				if (mageEnemies.size() == 0 && enemies.size() == 0 && floorNum != 5) {
					coins += 20;
					// If floorNum is not five, give upgrades.
					if (floorNum == 1 || floorNum == 3) {
						// Let player add a new spell.
						getSpell();
						gameState = 3;
						
					}
					else {
						// If floorNum 2 or 4, upgrade player or current spell stats.
						getCards();
						gameState = 3;
					}
				}
				else if(dead) {	
					// If dead take to leaderboard screen.
					gameState = 7;
					floorNum = 1;
				}
			}
			while (gameState == 3) {
				repaint();
				try {
					Thread.sleep(1000/FPS);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			while(gameState == 5 || gameState == 4 || gameState == 10 || gameState == 7 || gameState == 8) {
				repaint();
				try {
					Thread.sleep(1000/FPS);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Description: Run all movement related methods, shooting enemy and player, moving enemy and player, keeping in bounds, collision
	// 				detection methods.
	// Parameters(s): None
	// Return(s): void
	public void update() {
		// If player is not dead
		if(!dead) {
			// Run all necessary methods.
			// Move player.
			move();
			// Update mana and health
			updateMana();
			// Move player projectiles and keep in bound.
			moveProjectile();
			projectileInBound();
			// Check for projectile collisions
			for(int i = 0; i < enemies.size(); i ++) {
				projectileCollision(enemies.get(i), false);
			}
			// For fire spell.
			tickerDamage();
			for(int i = 0; i < mageEnemies.size(); i ++) {
				projectileCollision(mageEnemies.get(i), true);
			}
			
			// Make enemies shoot every 45 frames.
			if(frames % 45 == 0) {
				enemyShoot(false);
			}
			// Move enemy projectiles.
			moveEnemyProjectile(false);
			enemyProjectileCollision(false);
			// Move the enemies.
			moveEnemies();
			keepInBound();
			
			if(floorNum == 5 && spawnBoss) {
				// Execute boss methods, moving, shooting, projectile collision, adding more enemies.
				if(!bossDead) {
					moveBoss();
					projectileCollision();
					
					if(frames % 30 == 0) {
						enemyShoot(true);
					}
					moveEnemyProjectile(true);
					enemyProjectileCollision(true);
					
					// If the health of the boss is less than or equal to half and more enemies have not been added
					// spawn more enemies to help the boss. 1 Mage enemy and 5 Basic enemies.
					if(Ishaq.getHealth() <= bossHealth/2 && !addMoreEnemies) {
						addMoreEnemies = true;
						addEnemies();
					}
					if((bossHit && frames-bossFrames == 60) || !bossHit) {
						checkCollision();
					}
					
				}
			}
			// If boss is defeated and no addition enemies
			if(Ishaq.getHealth() <= 0) {
				bossDead = true;
				if(mageEnemies.size() == 0 && enemies.size() == 0) {
					coins += 30;
					gameState = 7;
					// Calculate times.
					int seconds = frames/60;
					int minutes = 00;
					if(seconds > 60) {
						seconds = seconds%60;
						minutes = seconds/60;
					}
					time = String.format("%02d:%02d", minutes, seconds);
					highscoreUpdate();
				}
			}
			// Check normal collision, not projectile. 60 second coolDown if hit.
			if(((hit || mageHit) && frames-prevFrames == 60) || (!hit && !mageHit)) {
				checkCollision(false);
				checkCollision(true);
			}
		}
	}
	
	// Description: Helps to draw cursor.
	// Parameters(s): None
	// Return(s): void
	public void cursor()
	{
		if(gameState == 10)
		{
			if(!draw)
			{
				cursorappear++;	
			}		
		}
	}
	
	// Description: Add to leaderBoard, sorting and then finding topFive sublist.
	// Parameters(s): None
	// Return(s): void
	public void highscoreUpdate()
	{	
		// Add username and user time to the leaderBoard map.
		leaderBoard.put(username, username + " " + time);
		// Get a list of the values.
		List <String> leaders = new ArrayList<>(leaderBoard.values());
		// Sort the new list by time.
		Collections.sort(leaders, new SortByTime());
		// Take subList of topFive.
		if(leaders.size() > 5) {
			topFive = leaders.subList(0, 5);
		}else {
			topFive = leaders.subList(0, leaders.size());
		}
		
			
		// Overwrite HighScore file with new top 5.
	    try {
	      	PrintWriter outputFile = new PrintWriter(new FileWriter("HighScore.txt"));
	      	for(int i = 0; i < 5; i ++) {
	       		outputFile.println(topFive.get(i));
	       	}
        	outputFile.close();
        	for(int i = 0; i < topFive.size(); i ++) {
        		System.out.println(topFive.get(i));
        	}
        }catch(Exception j) {
	       	System.out.println("File not found");
	    }
	}
	
	// Description: Load the current top five into the leaderboard map.
	// Parameters(s): None
	// Return(s): void
	public void loadLeaderBoard() {
		// Clear leaderboard.
		leaderBoard.clear();
		try {
			// Copy all elements from highscore.txt into leaderboard map.
			Scanner inputFile = new Scanner(new File("HighScore.txt"));
			while(inputFile.hasNextLine()) {
				String input = inputFile.nextLine();
				String name = input.substring(0, input.lastIndexOf(" "));
				String time1 = input.substring(input.lastIndexOf(" ")+1);
				leaderBoard.put(name, name + " " + time1);	
			}
			// Take list of values, sort, and take top 5 sublist.
			List <String> leaders = new ArrayList<>(leaderBoard.values());
			Collections.sort(leaders, new SortByTime());
			if(leaders.size() > 5) {
				topFive = leaders.subList(0, 5);
			}else {
				topFive = leaders.subList(0, leaders.size());
			}
			
		}catch(Exception i) {
			System.out.println("File not found");
		}
	}
	
	// Description: Paint the screen.
	// Parameters(s): Graphics object
	// Return(s): void
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(background,0,0,null);
		// Create the levels.
		if(!levelsCreated) {
			levels.add(new Levels(1,5,1));
			levels.add(new Levels(2,5,2));
			levels.add(new Levels(3,10,2));
			levels.add(new Levels(4,15,4));
			levels.add(new Levels(5,15,8));
			levelsCreated = true;
		}
		
		if(gameState == 1) {
			// Start screen, display coins.
			g2.setColor(Color.yellow);
			g2.setFont(new Font("Courier New", Font.BOLD, 20));
			g2.drawString("Coins: " + coins, 14, 25);
			
			g2.drawImage(titleScreen, 0,0,null);
			g2.setColor(Color.yellow);
			g2.setFont(new Font("Courier New", Font.BOLD, 20));
			g2.drawString("Coins: " + coins, 14, 25);
			// If hovering on button, illuminate.
			if(startHover) {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.white);
				g2.drawRect(202, 315, 279, 99);
			}else if(upgradeHover) {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.white);
				g2.drawRect(519, 315, 279, 99);
			}else if(settingsHover) {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.white);
				g2.drawRect(202, 447, 279, 99);
			}else if(aboutHover) {
				g2.setStroke(new BasicStroke(3));
				g2.setColor(Color.white);
				g2.drawRect(519, 447, 279, 99);
			}	
		}else if(gameState == 2){
			
			g2.setColor(Color.yellow);
			g2.setFont(new Font("Courier New", Font.BOLD, 20));
			g2.drawString("Coins: " + coins, 75, 25);
			
			if(!dead) {
				// If player not dead, depending on direction player is facing, draw the correct sprite.
				if(left || prevDirection) {
					g2.drawImage(characterImg[0], player.getXPos(), player.getYPos(), null);
					g2.drawImage(staffs[player.spells[spellIndex]], player.getXPos()-15, player.getYPos(), null);

				} if(right || !prevDirection) {
					g2.drawImage(characterImg[1], player.getXPos(), player.getYPos(), null);
					g2.drawImage(staffs[player.spells[spellIndex]], player.getXPos()+43, player.getYPos(), null);
				}
				// Health bar
				g2.setColor(Color.BLACK);
				g2.fillRect(928, 18, 60, 7);
				g2.setColor(Color.GREEN);
				g2.fillRect(929, 19, (int)((player.getHealth()/maxHp)*58), 5);
				
				// Mana bar
				g2.setColor(Color.BLACK);
				g2.fillRect(928, 10, 60, 6);
				g2.setColor(Color.BLUE);
				g2.fillRect(929, 11, (int)((player.getMana()/totalMana)*58), 4);
				
				// Basic spell refill bar
				if(frames - player.getCastTimeBasic() <= basicTime) {
					g2.setColor(Color.BLACK);
					g2.fillRect(8, 3, 50, 7);
					g2.setColor(new Color(162, 21, 237));
					g2.fillRect(8, 4, (int)(((frames-player.getCastTimeBasic())/basicTime)*48), 5);
				}else {
					g2.setColor(Color.BLACK);
					g2.fillRect(8, 3, 50, 7);
					g2.setColor(new Color(162, 21, 237));
					g2.fillRect(8, 4, 48, 5);
				}
				
				if(numOfSpells == 3) {
					if(player.spells[1] == 1) {
						//Fire spell refill bar
						if(frames - player.getCastTimeFire() <= fireTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(228, 146, 45));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeFire())/fireTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(183, 76, 12));
							g2.fillRect(12, 14, 48, 5);
						}
					}else if(player.spells[1] == 2) {
						//Ice spell refill bar
						if(frames - player.getCastTimeIce() <= iceTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeIce())/iceTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(12, 14, 48, 5);
						}
					}else if(player.spells[1] == 3) {
						//Rock spell refill bar
						if(frames - player.getCastTimeRock() <= goldTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeRock())/goldTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(12, 14, 48, 5);
						}
					}
					
					if(player.spells[2] == 1) {
						//Fire spell refill bar
						if(frames - player.getCastTimeFire() <= fireTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(228, 146, 45));
							g2.fillRect(16, 24, (int)(((frames-player.getCastTimeFire())/fireTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(183, 76, 12));
							g2.fillRect(16, 24, 48, 5);
						}
					}else if(player.spells[2] == 2) {
						//Ice spell refill bar
						if(frames - player.getCastTimeIce() <= iceTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(16, 24, (int)(((frames-player.getCastTimeIce())/iceTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(16, 24, 48, 5);
						}
					}else if(player.spells[2] == 3) {
						//Rock spell refill bar
						if(frames - player.getCastTimeRock() <= goldTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(16, 24, (int)(((frames-player.getCastTimeRock())/goldTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(16, 23, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(16, 24, 48, 5);
						}
					}
				}else if(numOfSpells == 2) {
					if(player.spells[1] == 1) {
						//Fire spell refill bar
						if(frames - player.getCastTimeFire() <= fireTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(228, 146, 45));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeFire())/fireTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(183, 76, 12));
							g2.fillRect(12, 14, 48, 5);
						}
					}else if(player.spells[1] == 2) {
						//Ice spell refill bar
						if(frames - player.getCastTimeIce() <= iceTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeIce())/iceTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(77, 128, 204));
							g2.fillRect(12, 14, 48, 5);
						}
					}else if(player.spells[1] == 3) {
						//Rock spell refill bar
						if(frames - player.getCastTimeRock() <= goldTime) {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(12, 14, (int)(((frames-player.getCastTimeRock())/goldTime)*48), 5);
						}else {
							g2.setColor(Color.BLACK);
							g2.fillRect(12, 13, 50, 7);
							g2.setColor(new Color(237, 189, 26));
							g2.fillRect(12, 14, 48, 5);
						}
					}
				}
			}
			if(enemies.size() != 0) {
				// Painting the enemies
				Iterator<Enemies> iterator2 = enemies.iterator();
				while (iterator2.hasNext()) {	
					Enemies enemy = iterator2.next();
					// Enemy health bar
					g2.drawImage(basicEnemy, (int)enemy.getXPos(), (int)enemy.getYPos(), null);
					g2.setColor(Color.BLACK);
					g2.fillRect((int)enemy.getXPos()-1, (int)enemy.getYPos()-9, 33, 7);
					g2.setColor(Color.GREEN);
					g2.fillRect((int)enemy.getXPos(), (int)enemy.getYPos()-8, (int)((enemy.getHealth()/50.0)*enemyWidth), 5);
				}
			}
			
			if(mageEnemies.size() != 0) {
				// Painting the mages
				Iterator<Enemies> iterator = mageEnemies.iterator();
				while (iterator.hasNext()) {
				    Enemies mage = iterator.next();
				    // Mage health bar.
				    g2.drawImage(mageEnemy, (int)mage.getXPos(), (int)mage.getYPos(), null);
				    g2.setColor(Color.BLACK);
					g2.fillRect((int)mage.getXPos()-1, (int)mage.getYPos()-9, 43, 7);
					g2.setColor(Color.GREEN);
					g2.fillRect((int)mage.getXPos(), (int)mage.getYPos()-8, (int)((mage.getHealth()/70.0)*43), 5);
				}
			}
			// Painting enemy projectiles
			Iterator<Projectiles> iterator3 = mageProjectiles.iterator();
			g2.setColor(Color.ORANGE);
			while (iterator3.hasNext()) {	
				Projectiles projectile = iterator3.next();
				g2.drawImage(enemyprojectileImg[0], (int)projectile.getX(), (int)projectile.getY(), null);
			}
			// Painting player projectiles
			Iterator<Projectiles> iterator5 = projectiles.iterator();
			while (iterator5.hasNext()) {
			    Projectiles projectile = iterator5.next();
			    g2.drawImage(projectileImg[projectile.getType()], (int)projectile.getX(), (int)projectile.getY(), null);
			}
			
			
			if(floorNum == 5) {
				if(mageEnemies.size() == 0 && enemies.size() == 0 && !disregard) {
					spawnBoss = true;
					disregard = true;
				}
				if(spawnBoss || addMoreEnemies) {
					if(!bossDead) {
						//Draw the boss.
						g2.drawImage(boss, (int)Ishaq.getXPos(), (int)Ishaq.getYPos(), null);
						// Draw boss health bar.
						g2.setColor(Color.BLACK);
						g2.fillRect(428, 18, 144, 7);
						g2.setColor(Color.GREEN);
						g2.fillRect(428, 19, (int)((Ishaq.getHealth()/bossHealth)*144), 5);
						
						// Painting boss projectiles
						Iterator <Projectiles> iterator4 = bossProjectiles.iterator();
						g2.setColor(Color.ORANGE);
						while (iterator4.hasNext()) {	
							Projectiles projectile = iterator4.next();
							g2.drawImage(enemyprojectileImg[0], (int)projectile.getX(), (int)projectile.getY(), null);
						}
					}	
				}
			}
			
		}else if(gameState == 3) {
			g2.setColor(Color.YELLOW);
			// If left or right option selected, illuminate
			if (selectLeft) {
				g2.fillRect(95, 95, 310, 410);
			}
			else if (selectRight) {
				g2.fillRect(595, 95, 310, 410);
			}
			if (floorNum == 1 || floorNum == 3) {
				// Draw the images of the spells to be selected.
				g2.drawImage(spellImg[spellCards[0]-1], 100, 100, null);
				g2.drawImage(spellImg[spellCards[1]-1], 600, 100, null);
			}
			
			else {
				// Draw the images of the upgrades to be selected.
				g2.drawImage(cardUpgrade[Integer.parseInt(availableCards.get(spellCards[0]))], 100, 100, null);
				g2.drawImage(cardUpgrade[Integer.parseInt(availableCards.get(spellCards[1]))], 600, 100, null);
			}
			
			// Draw the confirm button.
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawImage(confirm, 425, 515, null);
			
		}else if(gameState == 4){
			//Upgrades
			g2.setStroke(new BasicStroke(4));
			g2.setColor(Color.yellow);
			g2.drawImage(upgrades, 0,0,null);
			// Highlight the upgrade selected.
			if(health) {
				g2.drawRect(320, 130, 361, 58);
			}else if(damage) {
				g2.drawRect(320, 210, 361, 58);
			}else if(refillTime) {
				g2.drawRect(320, 290, 361, 58);
			}
			// Show coins.
			g2.setColor(Color.yellow);
			g2.setFont(new Font("Courier New", Font.BOLD, 20));
			g2.drawString("Coins: " + coins, 14, 25);
			
			g2.setFont(new Font("Courier New", Font.BOLD, 40));
			
			// Show the cost of the upgrades.
			if(health) {
				g2.drawString(healthCost + "", 480, 485);
			}else if(damage) {
				g2.drawString(damageCost + "", 480, 485);
			}else if(refillTime) {
				g2.drawString(refillCost + "", 480, 485);
			}
			
		}else if(gameState == 5) {
			// Settings
			g2.drawImage(settingsScreen, 0, 0, null);
			g2.setStroke(new BasicStroke(4));
			g2.setColor(Color.yellow);
			// Highlight the button selected.
			if(setLeft) {
				g2.drawRect(302, 99, 179, 49);
			}else if(setRight) {
				g2.drawRect(302, 169, 179, 49);
			}else if(setUp) {
				g2.drawRect(302, 239, 179, 49);
			}else if(setDown) {
				g2.drawRect(302, 309, 179, 49);
			}else if(setCycleLeft) {
				g2.drawRect(302, 379, 179, 49);
			}else if(setCycleRight) {
				g2.drawRect(302, 449, 179, 49);
			}
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Courier New", Font.BOLD, 40));
			// Draw the new key selected for each movement.
			if(leftKeyCode >= 65) {
				g2.drawString(leftKey, 538,135);
			}else {
				g2.drawImage(arrows[arrowImg(leftKeyCode)], 520, 98, null);
			}
			if(rightKeyCode >= 65) {
				g2.drawString(rightKey, 538, 206);
			}else {
				g2.drawImage(arrows[arrowImg(rightKeyCode)], 520, 168, null);
			}
			if(upKeyCode >= 65) {
				g2.drawString(upKey, 538, 275);
			}else {
				g2.drawImage(arrows[arrowImg(upKeyCode)], 520, 238, null);
			}
			if(downKeyCode >= 65) {
				g2.drawString(downKey, 538, 346);
			}else {
				g2.drawImage(arrows[arrowImg(downKeyCode)], 520, 308, null);
			}
			if(cycleLeftCode >= 65) {
				g2.drawString(cycleLeftKey, 538, 413);
			}else {
				g2.drawImage(arrows[arrowImg(cycleLeftCode)], 520, 378, null);
			}
			if(cycleRightCode >= 65) {
				g2.drawString(cycleRightKey, 538, 485);
			}else {
				g2.drawImage(arrows[arrowImg(cycleRightCode)], 520, 448, null);
			}
			
			// Draw music and gameSounds switch, on or off.
			if(music) {
				g2.drawImage(onButton, 747, 159, null);
			}else {
				g2.drawImage(offButton, 747, 159, null);
				backGroundMusicClip.stop();
			}
			
			if(gameSounds) {
				g2.drawImage(onButton, 747, 347, null);
			}else {
				g2.drawImage(offButton, 747, 347, null);
			}
		}else if(gameState == 6) {
			// About screen.
			g.drawImage(about, 0, 0, null);
		}else if(gameState == 7) {
			// Draw the leaderboard.
			loadLeaderBoard();
			g2.drawImage(leaderboard,0,0,null);
			g2.setFont(new Font("Courier New", Font.BOLD, 50));
			g2.setColor(Color.WHITE);
			int yCushion = 0;
			for(int i = 0; i < topFive.size(); i ++) {
				String input = topFive.get(i);
				String name = input.substring(0, input.indexOf(" "));
				String score = input.substring(input.lastIndexOf(" ") + 1);
				String padding = "";
				if(name.length() < 12) {
					for(int j = 0; j < 12-name.length(); j ++) {
						padding += " ";
					}
				}
				name = padding + name;
				g2.drawString(name + " : " + score, 96, 190+yCushion);
				yCushion += 70;
			}
		}else if(gameState == 8) {
			g.drawImage(instructions, 0, 0, null);
		}else if(gameState == 10) {
			cursor();
			// Draw the cursor and the username.
			g2.drawImage(usernameScreen, 0, 0, null);
			g2.setColor(Color.WHITE);
			if(cursorappear % 35 == 0)
			{	
				cursortime++;
				if(cursortime <= 17)
				{
					draw = true;
					g.drawImage(cursor, cursorX, 273, null);
				}
				else
				{
					cursortime = 0;
					cursorappear ++;
					draw = false;
				}
			}
			g2.setFont(new Font("Courier New", Font.BOLD, 40));
			g2.drawString(username, usernameX, 310);
			
			if(contains) {
				g2.drawString("Invalid Input", 350, 420);
			}
		}
	}
		
	// Description: Updates the player Mana and health over time.
	// Parameters(s): None
	// Return(s): void
	public void updateMana() {
		// Every 12 frames update player mana, if it is less than the max possible.
		if(frames % 12 == 0 && player.getMana() < totalMana) {
			player.updateMana(manaIncrease);
		}
		// Rarely it can exceed the max so bring it back down.
		if(player.getMana() > totalMana) {
			player.updateMana((int)(totalMana - player.getMana()));
		}
		// If canHeal upgrade is available, update the player health
		if(canHeal && frames % 12 == 0 && player.getHealth() < maxHp) {
			player.updateHealth(-1);
		}
		// Rarely it can exceed the max so bring it back down.
		if(player.getHealth() > maxHp) {
			player.updateHealth((int)(maxHp - player.getHealth()));
		}
	}
	
	// Description: Return the index of the arrow selected, to access the correct image.
	// Parameters(s): int keyCode
	// Return(s): int the index of the image to access
	public int arrowImg(int keyCode) {
		if(keyCode == 37) {
			return 0;
			//left
		}else if(keyCode == 38) {
			return 1;
			//up
		}else if(keyCode == 39) {
			return 2;
			//right
		}else if(keyCode == 40) {
			return 3;
			//down
		}
		return keyCode;
	}
	
	// Description: Randomly generates the upgrade cards to be chosen from.
	// Parameters(s): None
	// Return(s): void
	public void getCards() {
		int cardType1 = 0, cardType2 = 0;
		String cards = "";
		// Add all existing upgrades to a string.
		for (int i = 0; i < 3; i++) {
			cards += "," + player.charUpgrades[i] + ",";
		}
		// Check if upgrade is already taken, if not add to spellCards.
		do {
			cardType1 = (int) (Math.random() * (availableCards.size()));
		} while (cards.indexOf("," + cardType1 + ",") != -1);
		do {
			cardType2 = (int) (Math.random() * (availableCards.size()));
		} while (cards.indexOf("," + cardType2 + ",") != -1 || cardType1 == cardType2);
		spellCards [0] = cardType1;
		spellCards [1] = cardType2;
	}

	// Description: Randomly generates the spell cards to be chosen from.
	// Parameters(s): None
	// Return(s): void
	public void getSpell() {
		if (numOfSpells == 1) {
			int spellType1 = 0, spellType2 = 0;
			String spells = "";
			// Add all existing spells to a string.
			for (int i = 0; i < 3; i++) {
				spells += "," + player.spells[i] + ",";
			}
			// Check if spell is already taken, if not add to spellCards.
			do {
				spellType1 = (int) (Math.random() * 3)+1;
			} while (spells.indexOf("," + spellType1 + ",") != -1);
			do {
				spellType2 = (int) (Math.random() * 3)+1;
			} while (spells.indexOf("," + spellType2 + ",") != -1 || spellType1 == spellType2);
			spellCards [0] = spellType1;
			spellCards [1] = spellType2;
		} else {
			// If 2 spells already taken, set spellCards to the other 2 available.
			if (player.spells[1] == 1) {
				spellCards [0] = 2;
				spellCards [1] = 3;
			} else if (player.spells[1] == 2) {
				spellCards [0] = 1;
				spellCards [1] = 3;
			} else {
				spellCards [0] = 1;
				spellCards [1] = 2;
			}
		}
	}

	// Description: Burns the enemies after getting hit by a fire spell
	// Parameters(s): None
	// Return(s): void
	public void tickerDamage() {
		int currentFrames = frames;
		for(int i = 0; i < enemies.size(); i ++) {
			// Check which enemies are burning.
			int burnFrames = enemies.get(i).getBurnFrames();
			if(enemies.get(i).getBurning()) {
				// Reduce enemy health every 48 frames, do it as many times as burnTime.
				if((currentFrames - burnFrames)%48 == 0 && currentFrames - burnFrames != 0) {
					if((currentFrames - burnFrames)/48 <= burnTime) {
						if(enemies.get(i).getHealth() <= 0) {
							enemies.remove(i);
							i--;
							break;
						}else {
							enemies.get(i).updateHealth(burnDamage);
							if(enemies.get(i).getHealth() <= 0) {
								enemies.remove(i);
								i--;
								break;
							}
						}
						
					}
					// If greater than burntime, set enemy to burning to false.
					else {
						enemies.get(i).setCool();
					}
				}
			}
		}
		for(int i = 0; i < mageEnemies.size(); i ++) {
			int burnFrames = mageEnemies.get(i).getBurnFrames();
			if(mageEnemies.get(i).getBurning()) {
				if((currentFrames - burnFrames)%48 == 0 && currentFrames - burnFrames != 0) {
					if((currentFrames - burnFrames)/48 <= burnTime) {
						mageEnemies.get(i).updateHealth(burnDamage);
						if(mageEnemies.get(i).getHealth() <= 0) {
							mageEnemies.remove(i);
							i--;
						}
					}
					else {
						mageEnemies.get(i).setCool();
					}
				}
			}
		}
		if(Ishaq.getBurning()) {
			int burnFrames = Ishaq.getBurnFrames();
			if((currentFrames - burnFrames)%48 == 0 && currentFrames - burnFrames != 0) {
				if((currentFrames - burnFrames)/48 <= burnTime) {
					Ishaq.updateHealth(burnDamage);
					if(Ishaq.getHealth() <= 0) {
						bossDead = true;
					}
				}
				else {
					Ishaq.setCool();
				}
			}
		}
		
		
	}
	
	// Description: Keeps the player in bounds
	// Parameters(s): None
	// Return(s): void
	public void keepInBound() {
		// Keep the character in bounds
		if(player.getXPos() < 22 && !left && !right)
			player.setXPos(22);
		else if(player.getXPos()-15 < 22 && left)
			player.setXPos(37);
		else if(player.getXPos() >= screenWidth - playerWidth - staffWidth - 17)
			player.setXPos(screenWidth - playerWidth - staffWidth - 17);
			
		if(player.getYPos() < 40)
			player.setYPos(40);
		else if(player.getYPos() > screenHeight - playerHeight)
			player.setYPos(screenHeight - playerHeight);
	}
	
	// Description: Keeps the projectiles in bound
	// Parameters(s): None
	// Return(s): void
	public void projectileInBound() {
		// If out of bouds, remove projectile.
		for(int i = 0; i < projectiles.size(); i ++) {
			double left1 = projectiles.get(i).getX();
			double right1 = projectiles.get(i).getX() + 15;
			double top1 = projectiles.get(i).getY();
			double bottom1 = projectiles.get(i).getY() + 15;
			if(right1 > screenWidth - 18 || left1 < 22 || bottom1 > screenHeight || top1 < 40) {
				projectiles.remove(i);
				i--;
			}
		}
	}	
	
	// Description: Makes the mages and boss shoot.
	// Parameters(s): Boolean isBoss
	// Return(s): void
	public void enemyShoot(boolean isBoss) {
		if(!isBoss) {
			// If mage speed and starting point of projectiles varies, shoot one each time.
			for(int i = 0; i < mageEnemies.size(); i ++) {
				projectileX = mageEnemies.get(i).getXPos()+33;
				projectileY = mageEnemies.get(i).getYPos()+7;

				double distanceX = (player.getXPos()+21) - projectileX;
			    double distanceY = (player.getYPos()+21) - projectileY;
				        
			    // Calculate the magnitude of the distance vector (Pythagorean theorem)
			    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
				        
			    // Calculate the speed components to maintain consistent speed
			    double speedFactor = enemyprojectilespeed / distance;
			    speedX = distanceX * speedFactor;
			    speedY = distanceY * speedFactor;
				    
			    // Add to list.
			    mageProjectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, 10, -1));
			}
		}else {
			// If boss, shoot 2 projectiles, one from each eye.Speed also varies.
			projectileX = Ishaq.getXPos()+61;
			projectileY = Ishaq.getYPos()+96;

			double distanceX = (player.getXPos()+21) - projectileX;
		    double distanceY = (player.getYPos()+21) - projectileY;
			        
		    // Calculate the magnitude of the distance vector (Pythagorean theorem)
		    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
			        
		    // Calculate the speed components to maintain consistent speed
		    double speedFactor = bossprojectilespeed / distance;
		    speedX = distanceX * speedFactor;
		    speedY = distanceY * speedFactor;
			    
		    // Add to list.
		    bossProjectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, 15, -1));
		    
		    projectileX = Ishaq.getXPos()+80;
			projectileY = Ishaq.getYPos()+96;

			distanceX = (player.getXPos()+21) - projectileX;
		    distanceY = (player.getYPos()+21) - projectileY;
			        
		    // Calculate the magnitude of the distance vector (Pythagorean theorem)
		    distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
			        
		    // Calculate the speed components to maintain consistent speed
		    speedFactor = bossprojectilespeed / distance;
		    speedX = distanceX * speedFactor;
		    speedY = distanceY * speedFactor;
			    
		    // Add to list.
		    bossProjectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, 15, -1));
		    
		}
		
	}
	
	// Description: Checks if projectiles fired by the enemies collide with the player.
	// Parameters(s): Boolean isBoss
	// Return(s): void
	public void enemyProjectileCollision(boolean isBoss) {
		// Check if bullet is in bounds and for collisions.
		if(!isBoss) {
			for(int i = 0; i < mageProjectiles.size(); i ++) {
				double left1 = mageProjectiles.get(i).getX();
				double right1 = mageProjectiles.get(i).getX() + 15;
				double top1 = mageProjectiles.get(i).getY();
				double bottom1 = mageProjectiles.get(i).getY() + 15;
				double left2 = player.getXPos();
				double right2 = player.getXPos() + 43;
				double top2 = player.getYPos();
				double bottom2 = player.getYPos() + 43;
				// If projectiles collide, from any side, update the player health by the projectile strength,
				// play the hurt sound and remove the projectile.
				if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
				   	player.updateHealth(mageProjectiles.get(i).getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	mageProjectiles.remove(i);
			    	i--;
				}
			    else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
			    	player.updateHealth(mageProjectiles.get(i).getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	mageProjectiles.remove(i);
			    	i--;
			    }
			    else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
			    	player.updateHealth(mageProjectiles.get(i).getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	mageProjectiles.remove(i);
			    	i--;
				}
			    else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
			    	player.updateHealth(mageProjectiles.get(i).getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	mageProjectiles.remove(i);
			    	i--;
				}
				// If projectile out of bounds.
			    else if(right1 > screenWidth - 18  || left1 < 22 || bottom1 > screenHeight || top1 < 40) {
			    	mageProjectiles.remove(i);
					i--;
			    }
			}		
		}else {
			for(int i = 0; i < bossProjectiles.size(); i ++) {
				double left1 = bossProjectiles.get(i).getX();
				double right1 = bossProjectiles.get(i).getX() + 15;
				double top1 = bossProjectiles.get(i).getY();
				double bottom1 = bossProjectiles.get(i).getY() + 15;
				double left2 = player.getXPos();
				double right2 = player.getXPos() + 43;
				double top2 = player.getYPos();
				double bottom2 = player.getYPos() + 43;
				
				// If projectiles collide, from any side, update the player health by the projectile strength,
				// play the hurt sound and remove the projectile.
				if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
				   	player.updateHealth(bossProjectiles.get(i).getAttack() + Ishaq.getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	bossProjectiles.remove(i);
			    	i--;
				}
			    else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
			    	player.updateHealth(bossProjectiles.get(i).getAttack() + Ishaq.getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	bossProjectiles.remove(i);
			    	i--;
			    }
			    else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
			    	player.updateHealth(bossProjectiles.get(i).getAttack() + Ishaq.getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	bossProjectiles.remove(i);
			    	i--;
				}
			    else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
			    	player.updateHealth(bossProjectiles.get(i).getAttack() + Ishaq.getAttack());
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
			    	bossProjectiles.remove(i);
			    	i--;
				}
				// If projectile out of bounds.
			    else if(right1 > screenWidth - 18  || left1 < 22 || bottom1 > screenHeight || top1 < 40) {
			    	bossProjectiles.remove(i);
					i--;
			    }
			}		
		}
	}

	// Description: Check if projectiles fired by the player have collided with the boss.
	// Parameters(s): None
	// Return(s): void
	public void projectileCollision(Enemies enemy, boolean isMage) {
		// Check if bullet is in bounds and for collisions.
		for(int i = 0; i < projectiles.size(); i ++) {
			int currentFrames = frames;
			double left1 = projectiles.get(i).getX();
			double right1 = projectiles.get(i).getX() + 15;
			double top1 = projectiles.get(i).getY();
			double bottom1 = projectiles.get(i).getY() + 15;
			double left2 = 0;
			double right2 = 0;
			double top2 = 0;
			double bottom2 = 0;
			if(isMage) {
				left2 = enemy.getXPos();
				right2 = enemy.getXPos() + 43;
				top2 = enemy.getYPos();
				bottom2 = enemy.getYPos() + 44;
			}
			else {
				left2 = enemy.getXPos();
				right2 = enemy.getXPos() + 30;
				top2 = enemy.getYPos();
				bottom2 = enemy.getYPos() + 33;
			}
			// If collided, update enemy Health and set to burning, stun or freeze depending on type of spell used
			// and remove the projectile and update number of coins. Remove enemy is no health.
			if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
		    	enemy.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		enemy.setBurning();
		    		enemy.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		enemy.setFrozen();
		    		enemy.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		enemy.setStun();
		    		enemy.setStunFrames(currentFrames);
		    	}
		    	
		    	if(enemy.getHealth() <= 0) {
			    	if(isMage) {
			    		mageEnemies.remove(enemy);
			    	}else {
			    		enemies.remove(enemy);
			    	}
		    	}
		    	projectiles.remove(i);
		    	i--;
		    }
		    else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
		    	enemy.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		enemy.setBurning();
		    		enemy.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		enemy.setFrozen();
		    		enemy.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		enemy.setStun();
		    		enemy.setStunFrames(currentFrames);
		    	}
		    	
		    	if(enemy.getHealth() <= 0) {
			    	if(isMage) {
			    		mageEnemies.remove(enemy);
			    	}else {
			    		enemies.remove(enemy);
			    	}
		    	}
		    	projectiles.remove(i);
		    	i--;		    
		    }
		    else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
		    	enemy.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		enemy.setBurning();
		    		enemy.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		enemy.setFrozen();
		    		enemy.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		enemy.setStun();
		    		enemy.setStunFrames(currentFrames);
		    	}
		    	
		    	if(enemy.getHealth() <= 0) {
			    	if(isMage) {
			    		mageEnemies.remove(enemy);
			    	}else {
			    		enemies.remove(enemy);
			    	}
		    	}
		    	projectiles.remove(i);
		    	i--;	    
		    }
		    else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
		    	enemy.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		enemy.setBurning();
		    		enemy.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		enemy.setFrozen();
		    		enemy.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		enemy.setStun();
		    		enemy.setStunFrames(currentFrames);
		    	}
		    	
		    	if(enemy.getHealth() <= 0) {
			    	if(isMage) {
			    		mageEnemies.remove(enemy);
			    	}else {
			    		enemies.remove(enemy);
			    	}
		    	}
		    	projectiles.remove(i);
		    	i--;	    
		    }
		    if(right1 > screenWidth - 18 || left1 < 22 || bottom1 > screenHeight || top1 < 40) {
		    	projectiles.remove(i);
		    	i--;
		    }
		}
	}

	// Description: Check if projectiles fired by the player have collided with the boss.
	// Parameters(s): None
	// Return(s): void
	public void projectileCollision() {
		// Check if bullet is in bounds and for collisions.
		for(int i = 0; i < projectiles.size(); i ++) {
			int currentFrames = frames;
			double left1 = projectiles.get(i).getX();
			double right1 = projectiles.get(i).getX() + 15;
			double top1 = projectiles.get(i).getY();
			double bottom1 = projectiles.get(i).getY() + 15;
			double left2 = Ishaq.getXPos();
			double right2 = Ishaq.getXPos() + 144;
			double top2 = Ishaq.getYPos();
			double bottom2 = Ishaq.getYPos() + 144;
			
			// If collided, update boss Health and set to burning, stun or freeze depending on type of spell used.
			// Remove projectile.
			if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
		    	Ishaq.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		Ishaq.setBurning();
		    		Ishaq.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		Ishaq.setFrozen();
		    		Ishaq.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		Ishaq.setStun();
		    		Ishaq.setStunFrames(currentFrames);
		    	}
		    	
		    	if(Ishaq.getHealth() <= 0) {
			    	bossDead = true;
		    	}
		    	projectiles.remove(i);
		    	i--;
		    }
		    else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
		    	Ishaq.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		Ishaq.setBurning();
		    		Ishaq.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		Ishaq.setFrozen();
		    		Ishaq.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		Ishaq.setStun();
		    		Ishaq.setStunFrames(currentFrames);
		    	}
		    	
		    	if(Ishaq.getHealth() <= 0) {
			    	bossDead = true;
		    	}
		    	projectiles.remove(i);
		    	i--;		    
		    }
		    else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
		    	Ishaq.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		Ishaq.setBurning();
		    		Ishaq.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		Ishaq.setFrozen();
		    		Ishaq.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		Ishaq.setStun();
		    		Ishaq.setStunFrames(currentFrames);
		    	}
		    	
		    	if(Ishaq.getHealth() <= 0) {
			    	bossDead = true;
		    	}
		    	projectiles.remove(i);
		    	i--;	    
		    }
		    else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
		    	Ishaq.updateHealth(projectiles.get(i).getAttack());
		    	if(projectiles.get(i).getType() == 1) {
		    		Ishaq.setBurning();
		    		Ishaq.setBurnFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 2) {
		    		Ishaq.setFrozen();
		    		Ishaq.setFreezeFrames(currentFrames);
		    	}else if(projectiles.get(i).getType() == 3) {
		    		Ishaq.setStun();
		    		Ishaq.setStunFrames(currentFrames);
		    	}
		    	
		    	if(Ishaq.getHealth() <= 0) {
			    	bossDead = true;
		    	}
		    	projectiles.remove(i);
		    	i--;	    
		    }
		    if(right1 > screenWidth - 18 || left1 < 22 || bottom1 > screenHeight || top1 < 40) {
		    	projectiles.remove(i);
		    	i--;
		    }
		}
	}
		
	// Description: Check if enemy or mage hit the player, not projectile
	// Parameters(s): boolean isMage
	// Return(s): void
	public void checkCollision(boolean isMage) {
		// Depending on Mage or not if player is hit update player health and set hit or mageHit to true.
		// Also play the hurt clip. If player health is <= 0, set dead to true.
		if(!isMage) {
			// Check if character is in bounds and for collisions.
			for(int i = 0; i < enemies.size(); i ++) {
				double left1 = player.getXPos();
				double right1 = player.getXPos() + 43;
				double top1 = player.getYPos();
				double bottom1 = player.getYPos() + 43;
				double left2 = enemies.get(i).getXPos();
				double right2 = enemies.get(i).getXPos() + 30;
				double top2 = enemies.get(i).getYPos();
				double bottom2 = enemies.get(i).getYPos() + 33;
				
				if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
					player.updateHealth(enemies.get(i).getAttack());
					if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
					hit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
			    }
				else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
				    player.updateHealth(enemies.get(i).getAttack());
				    if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
					hit = true;					
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
			    }
				else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
				   	player.updateHealth(enemies.get(i).getAttack());
				   	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
					hit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
				}
				else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
				   	player.updateHealth(enemies.get(i).getAttack());
				   	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
					hit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	break;
			    }else {
			    	hit = false;
			    }
			}
		}else {
			// Check if character is in bounds and for collisions.
			for(int i = 0; i < mageEnemies.size(); i ++) {
				double left1 = player.getXPos();
				double right1 = player.getXPos() + 43;
				double top1 = player.getYPos();
				double bottom1 = player.getYPos() + 43;
				double left2 = mageEnemies.get(i).getXPos();
				double right2 = mageEnemies.get(i).getXPos() + 43;
				double top2 = mageEnemies.get(i).getYPos();
				double bottom2 = mageEnemies.get(i).getYPos() + 44;
						
				if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
					player.updateHealth(mageEnemies.get(i).getAttack());
					if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
					mageHit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
			    }
				else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
				    player.updateHealth(mageEnemies.get(i).getAttack());
				    if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
				    mageHit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
			    }
				else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
				   	player.updateHealth(mageEnemies.get(i).getAttack());
				   	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
				   	mageHit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}
			    	break;
				}
				else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
				   	player.updateHealth(mageEnemies.get(i).getAttack());
				   	if (gameSounds) {
						hurtClip.setMicrosecondPosition(0);
						hurtClip.start();
					}
				   	mageHit = true;
					prevFrames = frames;
			    	if(player.getHealth() <= 0) {
			    		dead = true;
			    	}	
			    	break;
			    }else {
			    	mageHit = false;
			    }
			}
		}
	}
	
	// Description: Check if boss hit the player, not projectile
	// Parameters(s): None
	// Return(s): void
	public void checkCollision() {
		// Check if character has been hit by boss, if so update the player health, if no health left, set dead to true.
		// Set bossHit to true is hit.
		double left1 = player.getXPos();
		double right1 = player.getXPos() + 43;
		double top1 = player.getYPos();
		double bottom1 = player.getYPos() + 43;
		double left2 = Ishaq.getXPos();
		double right2 = Ishaq.getXPos() + 144;
		double top2 = Ishaq.getYPos();
		double bottom2 = Ishaq.getYPos() + 144;
		
		if(right1 > left2 && left1 < left2 && right1 - left2 < bottom1 - top2 &&  right1 - left2 < bottom2 - top1){
			player.updateHealth(Ishaq.getAttack());
			bossHit = true;
			bossFrames = frames;
		   	if(player.getHealth() <= 0) {
		   		dead = true;
		   	}
		   	return;
		}
		else if(left1 < right2 && right1 > right2 && right2 - left1 < bottom1 - top2 && right2 - left1 < bottom2 - top1){
		    player.updateHealth(Ishaq.getAttack());
		    bossHit = true;
		    bossFrames = frames;
		    if(player.getHealth() <= 0) {
		   		dead = true;
		   	}
		    return;
		}
		else if(bottom1 > top2 && top1 < top2 && left1 > left2 && right1 < right2){
		   	player.updateHealth(Ishaq.getAttack());
		   	bossHit = true;
		   	bossFrames = frames;
	    	if(player.getHealth() <= 0) {
	    		dead = true;
	    	}
	    	return;
		}
		else if(top1 < bottom2 && bottom1 > bottom2 && left1 > left2 && right1 < right2){
		   	player.updateHealth(Ishaq.getAttack());
		   	bossHit = true;
			bossFrames = frames;
		   	if(player.getHealth() <= 0) {
		    	dead = true;
		   	}
		   	return;
		}else {
			bossHit = false;
			return;
	    }
	}
	
	// Description: Checks what spell has been used and updates the enemy sped accordingly.
	// Parameters(s): enemy object, x distance, y distance, total distance.
	// Return(s): void
	public void collisionSpeed(Enemies enemy, double distanceX, double distanceY, double distance, boolean isMage) {
		if(enemy.getStun()) {
			// If stuntime is over set to unstun.
	    	if(frames - enemy.getStunFrames() >= stunTime) {
	    		enemy.setUnStun();
	    	}
	    }else {
	    	// if enemy frozen
	    	if(enemy.getFrozen()) {
		    	if(frames - enemy.getFreezeFrames() <= freezeTime) {
		    		// Check enemy direction, change is needed.
		    		if(enemy.moveLeft() && !enemy.getMovingTowards()) {
		    			if(enemy.getXPos() <= 22) {
							enemy.setRight();
						}else {
							// Set speed to half.
							if(isMage) {
								enemyX -= mageSetSpeed*(1.5/2.0);
							}else {
								enemyX -= enemySetSpeed*(1.5/2.0);
							}
							enemy.updatePosition(enemyX, enemyY);
						}
		    		}else if(enemy.moveRight() && !enemy.getMovingTowards()){
		    			if((enemy.getXPos()+enemyWidth >= 982 && !isMage) || (enemy.getXPos()+44 >= 982 && isMage)) {
							enemy.setLeft();
						}else {
							// Set speed to half.
							if(isMage) {
								enemyX += mageSetSpeed*(1.5/2.0);
							}else {
								enemyX += enemySetSpeed*(1.5/2.0);
							}
							enemy.updatePosition(enemyX, enemyY);
						}
		    		} else {
		    			// Calculate the speed components to maintain consistent speed
		    			// If enemy is moving towards you, no 1.5 speed increase, just half the speed.
					    double speedFactor = (enemySetSpeed/2.0) / distance;
					    enemySpeedX = distanceX * speedFactor;
					    enemySpeedY = distanceY * speedFactor;
					    
					    enemyX += enemySpeedX;
					    enemyY += enemySpeedY;
					    
					    enemy.updatePosition(enemyX, enemyY);
					    enemy.setMoving();
		    		}	
		    	}else {
		    		// If not freeze time has been surpassed.
		    		enemy.setWarming();
		    	}
		    }else {
		    	// If not frozen.
		    	// Set the speed for both, x and y, speed is different depending on basic or mage enemy.
		    	if(isMage) {
		    		// Calculate the speed components to maintain consistent speed
				    double speedFactor = mageSetSpeed / distance;
				    enemySpeedX = distanceX * speedFactor;
				    enemySpeedY = distanceY * speedFactor;
				    
				    enemyX += enemySpeedX;
				    enemyY += enemySpeedY;
				    
				    enemy.updatePosition(enemyX, enemyY);
				    enemy.setMoving();
		    	}else {
		    		 // Calculate the speed components to maintain consistent speed
				    double speedFactor = enemySetSpeed / distance;
				    enemySpeedX = distanceX * speedFactor;
				    enemySpeedY = distanceY * speedFactor;
				    
				    enemyX += enemySpeedX;
				    enemyY += enemySpeedY;
				    
				    enemy.updatePosition(enemyX, enemyY);
				    enemy.setMoving();
		    	}
		    }
	    }
	}
	
	// Description: Based on spell used, edit boss speed.
	// Parameters(s): distanceX, distanceY, distance
	// Return(s): void
	public void collisionSpeed(double distanceX, double distanceY, double distance) {
		if(Ishaq.getStun()) {
			// If stun time over, set boss to unStun.
	    	if(frames - Ishaq.getStunFrames() >= stunTime) {
	    		Ishaq.setUnStun();
	    	}
	    }else {
	    	// If frozen half boss speed.
	    	if(Ishaq.getFrozen()) {
		    	if(frames - Ishaq.getFreezeFrames() <= freezeTime) {
		    		double speedFactor = (bossSpeed/2.0) / distance;
				    enemySpeedX = distanceX * speedFactor;
				    enemySpeedY = distanceY * speedFactor;
				    
				    Ishaq.setSpeedX(enemySpeedX);
				    Ishaq.setSpeedY(enemySpeedY); 
				    
				    Ishaq.updatePosition();
		    	}else {
		    		Ishaq.setWarming();
		    	}
		    }else {
		    	// If not frozen, calculate speed and update position.
		    	// Calculate the speed components to maintain consistent speed
			    double speedFactor = bossSpeed / distance;
			    enemySpeedX = distanceX * speedFactor;
			    enemySpeedY = distanceY * speedFactor;
			    
			    Ishaq.setSpeedX(enemySpeedX);
			    Ishaq.setSpeedY(enemySpeedY);
			    
			    Ishaq.updatePosition();
		    }
	    }
	}

	// Description: Moves player.
	// Parameters(s): None
	// Return(s): void
	public void move() {
		// Move the player.
		if(left)
			player.updatePosition(-speed, 0);
		else if(right)
			player.updatePosition(speed, 0);
		
		if(up)
			player.updatePosition(0, -speed);
		else if(down)
			player.updatePosition(0, speed);
	}
	
	// Description: Moves the enemies.
	// Parameters(s): None
	// Return(s): void
	public void moveEnemies() {
		if(!dead) {
			if(enemies.size() > 0) {
				for(int i = 0; i < enemies.size(); i ++) {
					enemyX = enemies.get(i).getXPos();
					enemyY = enemies.get(i).getYPos();
					
					// If enemies are close to the player move enemies towards the player.
					if(Math.abs(player.getXPos()-enemyX) <= 300 && Math.abs(player.getYPos()-enemyY) <= 300) {
						double distanceX = (player.getXPos()+5) - enemyX;
					    double distanceY = (player.getYPos()+5) - enemyY;
					        
					    // Calculate the magnitude of the distance vector (Pythagorean theorem)
					    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
					    
					    enemies.get(i).setMovingTowards();
					    
					    collisionSpeed(enemies.get(i), distanceX, distanceY, distance, false);
					}
					
					else {
						// Else move left and right.
						enemies.get(i).setMovingAway();
						if(enemies.get(i).moveLeft()) {
							if(enemies.get(i).getStun() || enemies.get(i).getFrozen()) {
								collisionSpeed(enemies.get(i), enemyX, enemyY, 0, false);
							}else {
								if(enemies.get(i).getXPos() <= 22) {
									enemies.get(i).setRight();
								}else {
									enemyX -= enemySetSpeed*1.5;
									enemies.get(i).updatePosition(enemyX, enemyY);
								}
							}
						}
						else if(enemies.get(i).moveRight()) {
							if(enemies.get(i).getStun() || enemies.get(i).getFrozen()) {
								collisionSpeed(enemies.get(i), enemyX, enemyY, 0, false);
							}else {
								if(enemies.get(i).getXPos()+enemyWidth >= 982) {
									enemies.get(i).setLeft();
								}else {
									enemyX += enemySetSpeed*1.5;
									enemies.get(i).updatePosition(enemyX, enemyY);
								}
							}
						}
						enemies.get(i).setStop();
					}
				}
			}
			
			if(mageEnemies.size() > 0) {
				for(int i = 0; i < mageEnemies.size(); i ++) {
					enemyX = mageEnemies.get(i).getXPos();
					enemyY = mageEnemies.get(i).getYPos();
					
					if(Math.abs(player.getXPos()-enemyX) <= 400 && Math.abs(player.getYPos()-enemyY) <= 400) {
						// If close to the player, move towards the player.
						double distanceX = (player.getXPos()+5) - enemyX;
					    double distanceY = (player.getYPos()+5) - enemyY;
					        
					    // Calculate the magnitude of the distance vector (Pythagorean theorem)
					    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
					    
					    mageEnemies.get(i).setMovingTowards();
					    
					    collisionSpeed(mageEnemies.get(i), distanceX, distanceY, distance, true);
					}
					
					else {
						// Els just move left and right.
						mageEnemies.get(i).setMovingAway();
						if(mageEnemies.get(i).moveLeft()) {
							if(mageEnemies.get(i).getStun() || mageEnemies.get(i).getFrozen()) {
								collisionSpeed(mageEnemies.get(i), enemyX, enemyY, 0, true);
							}else {
								if(mageEnemies.get(i).getXPos() <= 22) {
									mageEnemies.get(i).setRight();
								}else {
									enemyX -= mageSetSpeed*1.5;
									mageEnemies.get(i).updatePosition(enemyX, enemyY);
								}
							}
						}
						else if(mageEnemies.get(i).moveRight()) {
							if(mageEnemies.get(i).getStun() || mageEnemies.get(i).getFrozen()) {
								collisionSpeed(mageEnemies.get(i), enemyX, enemyY, 0, true);
							}else {
								if(mageEnemies.get(i).getXPos()+44 >= 982) {
									mageEnemies.get(i).setLeft();
								}else {
									enemyX += mageSetSpeed*1.5;
									mageEnemies.get(i).updatePosition(enemyX, enemyY);
								}
							}
						}
						mageEnemies.get(i).setStop();
					}
				}
			}
		}
	}
	
	// Description: Moves the boss
	// Parameters(s): None
	// Return(s): void
	public void moveBoss() {
		// Keep the boss in bounds, the second it hits the wall.
		if(Ishaq.getXPos() < 22) {
			Ishaq.setXPos(22);
			hitWall = true;
		}else if(Ishaq.getXPos() >= screenWidth - 144 - 17) {
			Ishaq.setXPos(screenWidth - 144 - 17);
			hitWall = true;
		}	
		if(Ishaq.getYPos() < 40) {
			Ishaq.setYPos(40);
			hitWall = true;
		}else if(Ishaq.getYPos() > screenHeight - 144) {
			Ishaq.setYPos(screenHeight - 144);
			hitWall = true;
		}
		// If stun time is over, set unstun.
		if(frames - Ishaq.getStunFrames() >= stunTime) {
			Ishaq.setUnStun();
		}
		// If wall is hit.
		if(hitWall) {
			enemyX = Ishaq.getXPos()+72;
			enemyY = Ishaq.getYPos()+72;
			// Move towards player if wall is hit and keep moving in a straight line.
			double distanceX = (player.getXPos()+5) - enemyX;
		    double distanceY = (player.getYPos()+5) - enemyY;
			        
		    // Calculate the magnitude of the distance vector (Pythagorean theorem)
		    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
		    
		    collisionSpeed(distanceX, distanceY, distance);
		    
		    // Play boss sound when wall is hit.
		    if (gameSounds) {
				bossClip.setMicrosecondPosition(0);
				bossClip.start();
			}
		    // Wall hit to false to stop from reading player position again.
			hitWall = false;
		}else {
			// Keep updating position.
			Ishaq.updatePosition();
		}
	}
	
	// Description: Move the player projectiles.
	// Parameters(s): None
	// Return(s): void
	public void moveProjectile() {
		for(int i = 0; i < projectiles.size(); i ++) {
			projectiles.get(i).updatePosition();
		}
	}
	
	// Description: Move the enemy projectiles
	// Parameters(s): boolean isBoss
	// Return(s): void
	public void moveEnemyProjectile(boolean isBoss) {
		if(!isBoss) {
			for(int i = 0; i < mageProjectiles.size(); i ++) {
				mageProjectiles.get(i).updatePosition();
			}
		}else {
			for(int i = 0; i < bossProjectiles.size(); i ++) {
				bossProjectiles.get(i).updatePosition();
			}
		}
	}
	
	// Description: Spawns the enemies for each level.
	// Parameters(s): None
	// Return(s): void
	public void spawnEnemies() {
		enemies.clear();
		mageEnemies.clear();
		
		boolean samePos = false;
		
		if(floorNum <= 5) {
			// Spawn the mages.
			int yCushion = 0;
			if(levels.get(floorNum-1).getMages() == 1) {
				mageEnemies.add(new Enemies("Mage", 20, 5, 70));
				mageEnemies.get(0).updatePosition(478, 40);
			}else {
				for(int i = 1; i <= levels.get(floorNum-1).getMages(); i ++) {
					if(i%2 == 0) {
						mageEnemies.add(new Enemies("Mage", 20, 5, 70));
						mageEnemies.get(i-1).updatePosition(520, 40+yCushion);
						yCushion+=54;
					}else {
						mageEnemies.add(new Enemies("Mage", 20, 5, 70));
						mageEnemies.get(i-1).updatePosition(435, 40+yCushion);
					}
				}	
			}
			for(int i = 0; i < levels.get(floorNum-1).getBasicEnemies(); i ++) {
				samePos = false;
				do {
					enemies.add(new Enemies("Basic", 15, 5, 50));
					if(Math.abs(enemies.get(i).getXPos()-player.getXPos()) <= 100 && Math.abs(enemies.get(i).getYPos()-player.getYPos()) <= 100) {
						samePos = true;
						enemies.remove(i);
					}
					else if(!samePos && mageEnemies.size() > 0 && enemies.get(i).getXPos() > 375 && enemies.get(i).getXPos() < 580 && enemies.get(i).getYPos() < mageEnemies.get(levels.get(floorNum-1).getMages()-1).getYPos() + 50) {
						samePos = true;
						enemies.remove(i);
					}
					else {
						for(int j = 0; j < i; j ++) {
							if(Math.abs(enemies.get(j).getXPos()-enemies.get(i).getXPos()) <= 50 && Math.abs(enemies.get(j).getYPos()-enemies.get(i).getYPos()) <= 50) {
								samePos = true;
								enemies.remove(i);
								break;
							}
							else {
								samePos = false;
							}
						}
					}
				} while(samePos);
			}
			
		}else {
			gameState = 1;
		}
	}
	
	// Description: Resets the player and other important variables, allows for replays without restarting the code
	// Parameters(s): None
	// Return(s): void.
	public void resetPlayer() {
		player.setXPos(473);
		player.setYPos(545);
		player.setCastTimeBasic(0);
		player.setCastTimeFire(0);
		player.setCastTimeIce(0);
		player.setCastTimeRock(0);
		prevDirection = false;
		projectiles.clear();
		mageProjectiles.clear();
		left = false;
		right = false;
		dead = false;
		up = false;
		down = false;
		mageFrames = 0;
		burnTime = 5;
		burnDamage = 2;
		stunTime = 60;
		manaIncrease = 2;
		fireDamage = 15;
		voidDamage = 10;
		goldDamage = 15;
		iceDamage = 10;
		freezeTime = 300;
		canHeal = false;
		numOfSpells = 1;
		spellIndex = 0;
		maxHp = 100 + 50 * healthPurchase;
		player.setHealth((int)maxHp);
		player.setMana((int)totalMana);
		player.updateAttack(0 + 5 * damagePurchase);
		manaIncrease = 2 + refillPurchase;
		username = "";
		usernameX = 500;
		player.spells[0] = 0;
		player.spells[1] = -1;
		player.spells[2] = -1;
		player.charUpgrades[0] = -1;
		player.charUpgrades[1] = -1;
		player.charUpgrades[2] = -1;
		player.index = 1; 
		player.indexUp = 0;
		floorNum = 5;

		availableCards.clear();
		availableCards.add("6");
		availableCards.add("7");
		availableCards.add("8");
		availableCards.add("9");
		availableCards.add("10");
		
		bossProjectiles.clear();
		Ishaq.setUnStun();
		Ishaq.setWarming();
		Ishaq.setCool();
		bossDead = false;
		spawnBoss = false;
		addMoreEnemies = false;
		disregard = false;
		hitWall = true;
		readPosition = false;
		Ishaq.setHealth((int)bossHealth);
		levelsCreated = false;
		draw = false;
		time = "";
		cursorappear = 0;
		cursorX = 500;
		cursortime = 0;
		contains = false;
		
		hit = false;
		mageHit = false;
		bossHit = false;
	}
	
	// Description: Add more enemies when boss Health is half
	// Parameters(s): None
	// Return(s): void
	public void addEnemies() {
		boolean samePos = false;
		// Add one mage.
		mageEnemies.add(new Enemies("Mage", 20, 5, 70));
		mageEnemies.get(0).updatePosition(478, 40);
			
		// Spawn 5 basic enemies.
		for(int i = 0; i < 1; i ++) {
			samePos = false;
			do {
				enemies.add(new Enemies("Basic", 15, 5, 50));
				if(Math.abs(enemies.get(i).getXPos()-player.getXPos()) <= 100 && Math.abs(enemies.get(i).getYPos()-player.getYPos()) <= 100) {
					samePos = true;
					enemies.remove(i);
				}
				else if(!samePos && enemies.get(i).getXPos() > 375 && enemies.get(i).getXPos() < 580 && enemies.get(i).getYPos() < mageEnemies.get(0).getYPos() + 50) {
					samePos = true;
					enemies.remove(i);
				}
				else {
					for(int j = 0; j < i; j ++) {
						if(Math.abs(enemies.get(j).getXPos()-enemies.get(i).getXPos()) <= 50 && Math.abs(enemies.get(j).getYPos()-enemies.get(i).getYPos()) <= 50) {
							samePos = true;
							enemies.remove(i);
							break;
						}
						else {
							samePos = false;
						}
					}
				}
			} while(samePos);
		}
	}
	
	// Description: Detects if a key has been pressed.
	// Parameters(s): KeyEvent object
	// Return(s): void
	public void keyPressed(KeyEvent e) {
		if(gameState == 2) {
			int key = e.getKeyCode();
			// For player movement.
			if(key == leftKeyCode) {
				left = true;
				right = false;
				prevDirection = true;
			}else if(key == rightKeyCode) {
				right = true;
				left = false;
				prevDirection = false;
			}else if(key == upKeyCode) {
				up = true;
				down = false;
			}else if(key == downKeyCode) {
				down = true;
				up = false;
			}
			// To switch between spells
			if(key == cycleLeftCode) {
				// Can cycle both ways infinitely, loops back
				if (numOfSpells == 3) {
					if(spellIndex >= 0) {
						if(spellIndex == 0) {
							spellIndex = 2;
						}else {
							spellIndex -= 1;
						}
					}
				}
				else if (numOfSpells == 2) {
					if(spellIndex >= 0) {
						if(spellIndex == 0) {
							spellIndex = 1;
						}else {
							spellIndex -= 1;
						}
					}
				}
			}else if(key == cycleRightCode) {
				// Can cycle both ways infinitely, loops back
				if (numOfSpells == 3) {
					if(spellIndex <= 2) {
						if(spellIndex == 2) {
							spellIndex = 0;
						}else {
							spellIndex += 1;
						}
					}
				}
				else if (numOfSpells == 2) {
					if(spellIndex <= 2) {
						if(spellIndex == 1) {
							spellIndex = 0;
						}else {
							spellIndex += 1;
						}
					}
				}

			}
		}else if(gameState == 5) {
			int key = e.getKeyCode();
			String key2 = (e.getKeyChar()+"").toUpperCase();
			// Check if the entered replacement Key is valid or not.
			if(key >= 65 & key <= 90 || key >= 37 && key <= 40) {
				// Check if the entered key is already in use for another command
				// If not allow the user to set the key for the selected command.
				if(setLeft) {
					if(key != rightKeyCode && key != upKeyCode && key != downKeyCode && key != cycleLeftCode && key != cycleRightCode) {
						leftKeyCode = key;
						leftKey = key2;
					}
				}else if(setRight) {
					if(leftKeyCode != key && key != upKeyCode && key != downKeyCode && key != cycleLeftCode && key != cycleRightCode) {
						rightKeyCode = key;
						rightKey = key2;
					}
				}else if(setUp) {
					if(key != rightKeyCode && leftKeyCode != key && key != downKeyCode && key != cycleLeftCode && key != cycleRightCode) {
						upKeyCode = key;
						upKey = key2;	
					}
				}else if(setDown) {
					if(key != rightKeyCode && key != upKeyCode && leftKeyCode != key && key != cycleLeftCode && key != cycleRightCode) {
						downKeyCode = key;
						downKey = key2;
					}
				}else if(setCycleLeft) {
					if(key != rightKeyCode && key != upKeyCode && key != downKeyCode && leftKeyCode != key && key != cycleRightCode) {
						cycleLeftCode = key;
						cycleLeftKey = key2;
					}
				}else if(setCycleRight) {
					if(key != rightKeyCode && key != upKeyCode && key != downKeyCode && key != cycleLeftCode && leftKeyCode != key) {
						cycleRightCode = key;
						cycleRightKey = key2;
					}
				}
			}
		}else if(gameState == 10) {
			// Username screen.
			if(e.getKeyCode() >= 65 && e.getKeyCode() <= 90 || e.getKeyCode() >= 48 & e.getKeyCode() <= 57)
			{			
				// This checks the character limit, no more than 12 characters are allowed.
				if(username.length() < 12)
				{
					username += e.getKeyChar();
					// The cursor starts in the middle of the box, and every character
					// is approximately 12 pixels in length, so the X position of the 
					// username String when it is being draw has to be changed to ensure
					// it stays in the middle of the given box.
					usernameX = usernameX - 12;
					cursorX = cursorX + 12;
				}
				
			}
			if(e.getKeyCode() == 8 && username.length() > 0)
			{
				// If the backpsace button is presses the username String has 
				// to remove one character each time the button is presses.
				// To do this the substring from the start of the String the one character
				// before the end has to be grabbed.
				username = username.substring(0,username.length()-1);
				// The X position of the username String has to add 12 pixels now as one
				// character is lost when the backspace is presses. This is to ensure
				// the username stays in the center of the box when it being drawn on the 
				// screen.
				usernameX = usernameX + 12;
				cursorX = cursorX - 12;
			}	
		}
	}
	
	// Description: Detects if a key has been released.
	// Parameters(s): KeyEvent object
	// Return(s): void
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if(gameState == 2) {
			// If key released, stop moving in desired directions, set variable to false.
			if(key == leftKeyCode) {
				left = false;
			}else if(key == rightKeyCode) {
				right = false;
			}else if(key == upKeyCode) {
				up = false;
			}else if(key == downKeyCode) {
				down = false;
			}
			// If key released, do not change spellIndex.
			if(key == cycleRightCode) {
				spellIndex += 0;
			}else if(key == cycleLeftCode) {
				spellIndex += 0;
			}
		}	
	}
	
	// Description: Detects if the mouse has been moved.
	// Parameters(s): MouseEvent object
	// Return(s): void
	public void mouseMoved(MouseEvent e) {
		if(gameState == 1) {
			posX = e.getX();
			posY = e.getY();
			// If the mouse is hovering above any of the buttons on the start screen, set their hover variable to true, 
			// variable used for graphics
			if(posX >= 202 && posX <= 481 && posY >= 315 && posY <= 414) {
				startHover = true;
				upgradeHover = false;
				settingsHover = false;
				aboutHover = false;
			}else if(posX >= 519 && posX <= 798 && posY >= 315 && posY <= 414) {
				startHover = false;
				upgradeHover = true;
				settingsHover = false;
				aboutHover = false;
			}else if(posX >= 202 && posX <= 481 && posY >= 447 && posY <= 546) {
				startHover = false;
				upgradeHover = false;
				settingsHover = true;
				aboutHover = false;
			}else if(posX >= 519 && posX <= 798 && posY >= 447 && posY <= 546) {
				startHover = false;
				upgradeHover = false;
				settingsHover = false;
				aboutHover = true;
			}else {
				startHover = false;
				upgradeHover = false;
				settingsHover = false;
				aboutHover = false;
			}
		}else if(gameState != 1) {
			startHover = false;
			upgradeHover = false;
			settingsHover = false;
			aboutHover = false;
		}
	}
		
	// Description: Detects if the mouse has been pressed.
	// Parameters(s): MouseEvent object
	// Return(s): void
	public void mousePressed(MouseEvent e) {
		if(gameState == 2) {
			if((player.getMana() >= 15 && player.spells[spellIndex] == 1) || (player.spells[spellIndex] == 2 && player.getMana() >= 10) || (player.spells[spellIndex] == 3 && player.getMana() >= 15) || player.spells[spellIndex] == 0) {
				// Get x and y pos.
				posX = e.getX();
				posY = e.getY();
				
				if(posX >= 21 && posX <= 982 && posY >= 40 && posY <= 600) {
					// If clicked within the bounds.
					// Set projecylice X based on direction a staff is on different sides.
					if(left || prevDirection) {
						projectileX = player.getXPos()-15;
						projectileY = player.getYPos();
					} else {
						projectileX = player.getXPos()+playerWidth;
						projectileY = player.getYPos();
					}
					
					// Find x and y distance.
					double distanceX = posX - projectileX;
				    double distanceY = posY - projectileY;
				        
				    // Calculate the magnitude of the distance vector (Pythagorean theorem)
				    double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
				        
				    // Calculate the speed components to maintain consistent speed.
				    double speedFactor = desiredSpeed / distance;
				    speedX = distanceX * speedFactor;
				    speedY = distanceY * speedFactor;
	
				    // Based on the type of spell used, check if spell cast time has been fulfilled, play the attack clip, set the cast time to
				    // the current frames and add the projectile to the projectiles list.
					if(player.spells[spellIndex] == 0 && (player.getCastTimeBasic() == 0 || (frames-player.getCastTimeBasic())/basicTime >= 1)) {
						if (gameSounds) {
							attackClip.setMicrosecondPosition(0);
							attackClip.start();
						}
						player.setCastTimeBasic(frames);
					    projectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, voidDamage + player.getAttack(), player.spells[spellIndex]));
					}else if(player.spells[spellIndex] == 1 && (player.getCastTimeFire() == 0 || (frames-player.getCastTimeFire())/45 >= 1)) {
						if (gameSounds) {
							attackClip.setMicrosecondPosition(0);
							attackClip.start();
						}
						player.setCastTimeFire(frames);
						player.updateMana(-15);
						projectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, fireDamage + player.getAttack(), player.spells[spellIndex]));
					}else if(player.spells[spellIndex] == 2 && (player.getCastTimeIce() == 0 || (frames-player.getCastTimeIce())/15 >= 1)) {
						if (gameSounds) {
							attackClip.setMicrosecondPosition(0);
							attackClip.start();
						}
						player.setCastTimeIce(frames);
						player.updateMana(-10);
						projectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, iceDamage + player.getAttack(), player.spells[spellIndex]));
					}else if(player.spells[spellIndex] == 3 && (player.getCastTimeRock() == 0 || (frames-player.getCastTimeRock())/90 >= 1)) {
						if (gameSounds) {
							attackClip.setMicrosecondPosition(0);
							attackClip.start();
						}
						player.setCastTimeRock(frames);
						player.updateMana(-15);
						projectiles.add(new Projectiles(projectileX, projectileY, speedX, speedY, false, goldDamage + player.getAttack(), player.spells[spellIndex]));
					}
				}
			}
		} else if(gameState == 1) {
			posX = e.getX();
			posY = e.getY();
			if(posX >= 202 && posX <= 481 && posY >= 315 && posY <= 414) {
				// If start clicked, play button click sound. Call resetPlayer(), spawnEnemies(), set frames to 0, levelFrames to 0
				// and load the leaderboard, change gamestate to 10 --> username screen.
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
				resetPlayer();
				spawnEnemies();
				frames = 0;
				levelFrames = 0;
				loadLeaderBoard();
				gameState = 10;
			}else if(posX >= 519 && posX <= 798 && posY >= 315 && posY <= 414) {
				// Play button click sound, change to gs 4 --> upgrades.
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
				gameState = 4;
			}else if(posX >= 202 && posX <= 481 && posY >= 447 && posY <= 546) {
				// Play button click sound, change to gs 5 --> settings.
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
				gameState = 5;
			}else if(posX >= 519 && posX <= 798 && posY >= 447 && posY <= 546) {
				// Play button click sound, change to gs 6 --> about
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
				gameState = 6;
			}	
		}else if (gameState == 3) {
			int x = e.getX();
			int y = e.getY();
			// In game upgrades screen.
			if (x > 425 && x < 575 && y > 515 && y < 575 && (selectLeft || selectRight)) {
				if (floorNum == 1 || floorNum == 3) {
					if (selectLeft) {
						// depending on the spell chosen by the player, add the respective spell upgrade cards
						// to availableCards.
						player.addSpell(spellCards[0]);
						if (spellCards[0] == 1) {
							availableCards.add("0");
							availableCards.add("1");
						} else if (spellCards[0] == 2) {
							availableCards.add("2");
							availableCards.add("3");
						} else {
							availableCards.add("4");
							availableCards.add("5");
						}
					}
					else if (selectRight) {
						player.addSpell(spellCards[1]);
						// depending on the spell chosen by the player, add the respective spell upgrade cards
						// to availableCards.
						if (spellCards[1] == 1) {
							availableCards.add("0");
							availableCards.add("1");
						} else if (spellCards[1] == 2) {
							availableCards.add("2");
							availableCards.add("3");
						} else {
							availableCards.add("4");
							availableCards.add("5");
						}
					}
					// Increment the number of spells.
					numOfSpells++;
				}
				else {
					int card = -1;
					if (selectLeft) {
						// Add the chosen upgrade to the player and remove it from availableCards so it isn't chosen again. 
						// Save card number, used for incrementing the stats.
						player.addUpgrade(Integer.parseInt(availableCards.get(spellCards[0])));
						card = Integer.parseInt(availableCards.get(spellCards[0]));
						availableCards.remove(spellCards[0]);
					}
					else if (selectRight) {
						// Add the chosen upgrade to the player and remove it from availableCards so it isn't chosen again. 
						// Save card number, used for incrementing the stats.
						player.addUpgrade(Integer.parseInt(availableCards.get(spellCards[1])));
						card = Integer.parseInt(availableCards.get(spellCards[1]));
						availableCards.remove(spellCards[1]);
					}
					// Depending on the card number chosen increment the player stats.
					if (card == 0) {
						burnTime += 2;
					} else if (card == 1) {
						burnDamage = 3;
					} else if (card == 2) {
						iceDamage = 15;
					} else if (card == 3) {
						freezeTime = 400;
					} else if (card == 4) {
						stunTime = 80;
					} else if (card == 5) {
						goldDamage = 25;
					} else if (card == 6) {
						voidDamage = 15;
					} else if (card == 7) {
						basicTime = 23;
					} else if (card == 8) {
						maxHp = maxHp * 1.5;
						canHeal = true;
					} else if (card == 9) {
						player.upgradeAttack();
					} else {
						manaIncrease += 2;
					}
				}
				selectLeft = false;
				selectRight = false;
				floorNum++;
				// Reset some variables for the next floor.
				levelFrames = frames;
				player.setXPos(473);
				player.setYPos(545);
				player.setCastTimeBasic(0);
				player.setCastTimeFire(0);
				player.setCastTimeIce(0);
				player.setCastTimeRock(0);
				prevDirection = false;
				projectiles.clear();
				mageProjectiles.clear();
				left = false;
				right = false;
				dead = false;
				up = false;
				down = false;
				spawnEnemies();
				gameState = 2;
			} else if (x > 95 && x < 405 && y > 95 && y < 505) {
				// Play the upgrade clip.
				if (gameSounds) {
					upgradeClip.stop();
					upgradeClip.setMicrosecondPosition(0);
					upgradeClip.start();
				}
				selectLeft = true;
				selectRight = false;
			} else if (x > 595 && x < 905 && y > 95 && y < 505) {
				// Play the upgrade clip
				if (gameSounds) {
					upgradeClip.stop();
					upgradeClip.setMicrosecondPosition(0);
					upgradeClip.start();
				}
				selectLeft = false;
				selectRight = true;
			}
		}else if(gameState == 4) {
			posX = e.getX();
			posY = e.getY();
			if(posX >= 49 && posX <= 209 && posY >= 538 && posY <= 587) {
				// Return to gs1.
				gameState = 1;
				// Play the button clip.
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
			}
			// Based on which upgrade is selected, turn the respective variable to true.
			if(posX >= 320 && posX <= 681 && posY >= 129 && posY <= 187) {
				damage = false;
				health = true;
				refillTime = false;
				buy = false;
			}else if(posX >= 320 && posX <= 681 && posY >= 209 && posY <= 267) {
				damage = true;
				health = false;
				refillTime = false;
				buy = false;
			}else if(posX >= 320 && posX <= 681 && posY >= 289 && posY <= 356) {
				damage = false;
				health = false;
				refillTime = true;
				buy = false;
			}else if(posX >= 402 && posX <= 599 && posY >= 450 && posY <= 497 && (health || refillTime || damage)) {
				// If buy selected.
				buy = true;
				// If there are enough coins available for the selected upgrade, decrement the number for coins, increase the price
				// for the next time, upgrade the player stats, and play the button clip.
				if(health) {
					if(coins >= healthCost) {
						coins -= healthCost;
						player.updateHealth(-50);
						maxHp += 50;
						healthCost += 15;
						healthPurchase++;
						if (gameSounds) {
							buttonClip.stop();
							buttonClip.setMicrosecondPosition(0);
							buttonClip.start();
						}
					}
				}else if(damage) {
					if(coins >= damageCost) {
						coins -= damageCost;
						player.updateAttack(5);
						damageCost += 15;
						damagePurchase++;
						if (gameSounds) {
							buttonClip.stop();
							buttonClip.setMicrosecondPosition(0);
							buttonClip.start();
						}
					}
				}else if(refillTime) {
					if(coins >= refillCost) {
						coins -= refillCost;
						manaIncrease += 1;
						refillCost += 15;
						refillPurchase++;
						if (gameSounds) {
							buttonClip.stop();
							buttonClip.setMicrosecondPosition(0);
							buttonClip.start();
						}
					}
				}
			}
			else {
				damage = false;
				health = false;
				refillTime = false;
				buy = false;
			}
			
		}else if(gameState == 5) {
			posX = e.getX();
			posY = e.getY();
			// Based on the button clicked, set respective variables to true
			if(posX >= 301 && posX <= 481 && posY >= 98 && posY <= 147) {
				setLeft = true;
				setRight = false;
				setUp = false;
				setDown = false;
				setCycleLeft = false;
				setCycleRight = false;
			}else if(posX >= 301 && posX <= 481 && posY >= 168 && posY <= 217) {
				setLeft = false;
				setRight = true;
				setUp = false;
				setDown = false;
				setCycleLeft = false;
				setCycleRight = false;
			}else if(posX >= 301 && posX <= 481 && posY >= 238 && posY <= 287) {
				setLeft = false;
				setRight = false;
				setUp = true;
				setDown = false;
				setCycleLeft = false;
				setCycleRight = false;
			}else if(posX >= 301 && posX <= 481 && posY >= 308 && posY <= 357) {
				setLeft = false;
				setRight = false;
				setUp = false;
				setDown = true;
				setCycleLeft = false;
				setCycleRight = false;
			}else if(posX >= 301 && posX <= 481 && posY >= 378 && posY <= 427) {
				setLeft = false;
				setRight = false;
				setUp = false;
				setDown = false;
				setCycleLeft = true;
				setCycleRight = false;
			}else if(posX >= 301 && posX <= 481 && posY >= 448 && posY <= 497) {
				setLeft = false;
				setRight = false;
				setUp = false;
				setDown = false;
				setCycleLeft = false;
				setCycleRight = true;
			}else {
				setLeft = false;
				setRight = false;
				setUp = false;
				setDown = false;
				setCycleLeft = false;
				setCycleRight = false;
			}
			// If back pressed, set gs to 1, and play button clip.
			if(posX >= 49 && posX <= 209 && posY >= 538 && posY <= 587) {
				gameState = 1;
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
			}
			
			// Depending on current state to music and gameSounds variables, if button clicked set to opposite.
			if(posX >= 747 && posX <= 814 && posY >= 159 && posY <= 271) {
				if(!music) {
					music = true;
				}else {
					music = false;
				}
			}
			
			if(posX >= 747 && posX <= 814 && posY >= 347 && posY <= 459) {
				if(!gameSounds) {
					gameSounds = true;
				}else {
					gameSounds = false;
				}
			}
		}else if(gameState == 6) {
			posX = e.getX();
			posY = e.getY();
			if(posX >= 354 && posX <= 646 && posY >= 499 && posY <= 565) {
				gameState = 8;
			}			
			else if(posX >= 40 && posX <= 191 && posY >= 499 && posY <= 565) {
				gameState = 1;
			}
			if (gameSounds) {
				buttonClip.setMicrosecondPosition(0);
				buttonClip.start();
			}
		}else if(gameState == 7) {
			posX = e.getX();
			posY = e.getY();
			// If back clickes, change gs to 1, play button clip.
			if(posX >= 356 && posX <= 646 && posY >= 529 && posY <= 578) {
				gameState = 1;
			}
			if (gameSounds) {
				buttonClip.stop();
				buttonClip.setMicrosecondPosition(0);
				buttonClip.start();
			}
		}else if(gameState == 8) {
			posX = e.getX();
			posY = e.getY();
			if(posX >= 358 && posX <= 643 && posY >= 496 && posY <= 562) {
				gameState = 1;
			}
			if (gameSounds) {
				buttonClip.stop();
				buttonClip.setMicrosecondPosition(0);
				buttonClip.start();
			}
		}else if(gameState == 10) {
			posX = e.getX();
			posY = e.getY();
			
			if(posX >= 399 && posX <= 600 && posY >= 488 && posY <= 547 && username.length() > 0) {
				// If valid username, change gs to 2.
				if(!leaderBoard.containsKey(username)) {
					gameState = 2;
				}else {
					// If already exists, set contains to true.
					contains = true;
				}
				// Play button clip.
				if (gameSounds) {
					buttonClip.stop();
					buttonClip.setMicrosecondPosition(0);
					buttonClip.start();
				}
			}
		}
	}
	
	// Description: Converts the scaled imageIcons to a bufferedImage to be able to store in the bufferedImage arrays.
	// Parameters(s): ImageIcon
	// Return(s): BufferedImage
	public static BufferedImage toBufferedImage(ImageIcon icon) {
        Image img = icon.getImage();
        // Create a BufferedImage with the same width and height as the Image
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the Image onto the BufferedImage
        Graphics2D bImage = bufferedImage.createGraphics();
        bImage.drawImage(img, 0, 0, null);
        bImage.dispose();

        return bufferedImage;
    }
	
	public static void main(String[] args) throws LineUnavailableException {
		// GS1 --> Title screen
		// GS2 --> Game
		// GS3 --> Level end screen
		// GS4 --> Upgrades
		// GS5 --> Settings
		// GS6 --> About
		// GS7 --> Leaderboard
		// GS10 --> Username
		
		try{
			// Load in all the images.
			background = ImageIO.read(new File("Background.png"));
			
			for(int i = 0; i < 2; i ++) {
				ImageIcon imageIcon = new ImageIcon("Character" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(playerWidth, playerHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				characterImg[i] = toBufferedImage(imageIcon);
			}
			for(int i = 0; i < 4; i ++) {
				ImageIcon imageIcon = new ImageIcon("Ball" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(staffWidth, staffWidth,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				projectileImg[i] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 4; i ++) {
				ImageIcon imageIcon = new ImageIcon("Spell" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(staffWidth, playerHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				staffs[i] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 1; i ++) {
				ImageIcon imageIcon = new ImageIcon("EnemyBall" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(staffWidth, staffWidth,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				enemyprojectileImg[i] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 3; i ++) {
				ImageIcon imageIcon = new ImageIcon("SpellCard" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				spellImg[i] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 2; i ++) {
				ImageIcon imageIcon = new ImageIcon("FireUpgrade" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				cardUpgrade[i] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 2; i ++) {
				ImageIcon imageIcon = new ImageIcon("IceUpgrade" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				cardUpgrade[i+2] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 2; i ++) {
				ImageIcon imageIcon = new ImageIcon("GoldUpgrade" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				cardUpgrade[i+4] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 2; i ++) {
				ImageIcon imageIcon = new ImageIcon("VoidUpgrade" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				cardUpgrade[i+6] = toBufferedImage(imageIcon);
			}

			for(int i = 0; i < 3; i ++) {
				ImageIcon imageIcon = new ImageIcon("CharUpgrade" + (i+1) + ".png"); // load the image to a imageIcon
				Image image = imageIcon.getImage(); // transform it 
				Image newimg = image.getScaledInstance(300, 400,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
				imageIcon = new ImageIcon(newimg);  // transform it back
				cardUpgrade[i+8] = toBufferedImage(imageIcon);
			}
			
			for(int i = 0; i < 4; i ++) {
				arrows[i] = ImageIO.read(new File("Arrow" + (i+1) + ".png"));
			}
			
			ImageIcon imageIcon = new ImageIcon("Enemy.png"); // load the image to a imageIcon
			Image image = imageIcon.getImage(); // transform it 
			Image newimg = image.getScaledInstance(enemyWidth, enemyHeight,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
			basicEnemy = toBufferedImage(imageIcon);
			
			imageIcon = new ImageIcon("Confirm.png"); // load the image to a imageIcon
			image = imageIcon.getImage(); // transform it 
			newimg = image.getScaledInstance(150, 60,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
			confirm = toBufferedImage(imageIcon);
			
			imageIcon = new ImageIcon("onButton.png"); // load the image to a imageIcon
			image = imageIcon.getImage(); // transform it 
			newimg = image.getScaledInstance(67, 112,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
			onButton = toBufferedImage(imageIcon);
			
			imageIcon = new ImageIcon("offButton.png"); // load the image to a imageIcon
			image = imageIcon.getImage(); // transform it 
			newimg = image.getScaledInstance(67, 112,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way  
			imageIcon = new ImageIcon(newimg);  // transform it back
			offButton = toBufferedImage(imageIcon);
			
			titleScreen = ImageIO.read(new File("TitleScreen.png"));
			settingsScreen = ImageIO.read(new File("Settings.png"));
			mageEnemy = ImageIO.read(new File("MageEnemy.png"));
			upgrades = ImageIO.read(new File("Upgrades.png"));
			usernameScreen = ImageIO.read(new File("UsernameScreen.png"));
			cursor = ImageIO.read(new File("Cursor.png"));
			boss = ImageIO.read(new File("Boss.png"));
			leaderboard = ImageIO.read(new File("LeaderBoard.png"));
			about = ImageIO.read(new File("About.png"));
			instructions = ImageIO.read(new File("Instructions.png"));
			
			// Add the character upgrade cards to availableCards to start.
			availableCards.add("6");
			availableCards.add("7");
			availableCards.add("8");
			availableCards.add("9");
			availableCards.add("10");
		}
		catch(Exception e) {
			System.out.println("Please load images");
		}
		
		// Initialize frame.
		JFrame myFrame = new JFrame("Sorcerer's Tower");
		Driver myPanel = new Driver();
		myFrame.add(myPanel);
		myFrame.setVisible(true);
		myFrame.pack();
		// Makes the JFrame appear in the center of the screen
		myFrame.setLocationRelativeTo(null);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.setResizable(false);
	}
	
	// Not in use
	public void keyTyped(KeyEvent e) {
		
	}
	public void mouseClicked(MouseEvent e) {
		
	}	
	public void mouseDragged(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}
	public void mouseExited(MouseEvent e) {
		
	}
	public void mouseReleased(MouseEvent e) {
		
	}

}