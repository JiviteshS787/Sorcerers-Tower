
public class Fahad {
	private int health, attack, speed, coolDown;
	private double speedX, speedY, xPos, yPos;
	private int burnFrames, iceFrames, stunFrames;
	private boolean burning, freeze, stun, movingTowards;
	
	// Description: Creates the object and initialized the variables
	// Parameter(s): int, int, int
	// Return(s): None
	public Fahad(int health, int refill, int attack) {
		this.health = health;
		xPos = 428;
		yPos = 45;
		this.attack = attack;
		coolDown = refill;
	}
	
	// Getter and Setter methods
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
	
	public void setXPos(int update) {
		this.xPos = update;
	}
	
	public void setHealth(int input) {
		this.health = input;
	}
	
	public void setYPos(int update) {
		this.yPos = update;
	}
	
	public void setSpeedX(double update) {
		this.speedX = update;
	}
	
	public void setSpeedY(double update) {
		this.speedY = update;
	}
	
	// Description: Updates the position
	// Parameter(s): None
	// Return(s): void
	public void updatePosition() {
		// If not stunned, update position.
		if(!stun) {
			this.xPos += speedX;
			this.yPos += speedY;
		}
	}
	
	// Description: Update health
	// Parameter(s): int health
	// Return(s): void
	public void updateHealth(int update) {
		this.health -= update;
	}
	
}
