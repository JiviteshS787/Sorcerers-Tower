
public class Enemies {
	private String type;
	private int health, burnFrames, iceFrames, stunFrames;
	double xPos, yPos;
	//private double speedX, speedY;
	private int attack;
	private int coolDown;
	private boolean moving, moveLeft, moveRight, burning, freeze, stun, movingTowards;
	
	// Description: Create the enemy object and initialize variables.
	// Parameter(s): String type, int attack, int refill, int health.
	// Return(s): void
	public Enemies(String type1, int attack, int refill, int health) {
		int moving2 = 0;
		type = type1;
		this.health = health;
		xPos = Math.random()*(940-20+1)+20;
		yPos = Math.random()*(560-40+1)+40;
		this.attack = attack;
		coolDown = refill;
		moving2 = (int)(Math.random()*(2-1+1))+1;
		if(moving2 == 1) {
			moveLeft = true;
			moveRight = false;
		}else if(moving2 == 2) {
			moveLeft = false;
			moveRight = true;
		}
		moving = false;
		burning = false;
		burnFrames = 0;
		freeze = false;
		iceFrames = 0;
		stun = false;
		stunFrames = 0;
	}
	
	// Getters and Setters
	public int getHealth() {
		return this.health;
	}
	
	public double getXPos() {
		return this.xPos;
	}
	
	public double getYPos() {
		return this.yPos;
	}
	
	public int getAttack() {
		return this.attack;
	}
	
	public int getCoolDown() {
		return this.coolDown;
	}
	
	public boolean getMoving() {
		return this.moving;
	}
	
	public boolean getBurning() {
		return this.burning;
	}
	
	public int getBurnFrames() {
		return this.burnFrames;
	}
	
	public int getFreezeFrames() {
		return this.iceFrames;
	}
	
	public int getStunFrames() {
		return this.stunFrames;
	}
	
	public boolean getFrozen() {
		return this.freeze;
	}
	
	public boolean getStun() {
		return this.stun;
	}
	
	public boolean getMovingTowards() {
		return this.movingTowards;
	}
	
	public void setStunFrames(int frames) {
		this.stunFrames = frames;
	}
	
	public void setBurnFrames(int frames) {
		burnFrames = frames;
	}
	
	public void setFreezeFrames(int frames) {
		iceFrames = frames;
	}
	
	public void setStun() {
		this.stun = true;
	}
	
	public void setUnStun() {
		this.stun = false;
	}
	
	public void setFrozen() {
		this.freeze = true;
	}
	
	public void setWarming() {
		this.freeze = false;
	}
	
	public void setBurning() {
		this.burning = true;
	}
	
	public void setCool() {
		this.burning = false;
	}
	
	public void setStop() {
		moving = false;
	}
	
	public void setMoving() {
		moving = true;
	}
	
	public boolean moveLeft() {
		return this.moveLeft;
	}
	
	public boolean moveRight() {
		return this.moveRight;
	}
	
	public void setLeft() {
		moveLeft = true;
		moveRight = false;
	}
	
	public void setRight() {
		moveRight = true;
		moveLeft = false;
	}
	
	public void setMovingTowards() {
		this.movingTowards = true;
	}
	
	public void setMovingAway() {
		this.movingTowards = false;
	}
	
	// Description: Update position
	// Parameter(s): double x, double y
	// Return(s): void
	public void updatePosition(double x, double y) {
		xPos = x;
		yPos = y;
	}
	
	// Description: Update health
	// Parameter(s): int update
	// Return(s): void
	public void updateHealth(int update) {
		health -= update;
	}
}
