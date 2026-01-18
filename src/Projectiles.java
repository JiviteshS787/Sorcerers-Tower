
public class Projectiles {
	private double speedX, speedY;
	private double xPos, yPos;
	private boolean collide;
	private int damage;
	private int type;
	
	// Description: Creates the projectile object and initializes the variables.
	// Parameter(s): int update
	// Return(s): void
	public Projectiles(double bulletX1, double bulletY1, double speedX1, double speedY1, boolean collide1, int strength, int type1) {
		speedX = speedX1;
		speedY = speedY1;
		xPos = bulletX1;
		yPos = bulletY1;	
		collide = collide1;
		damage = strength;
		type = type1;
	}
	
	// Getters and Setters
	public double getX() {
		return this.xPos;
	}
	
	public double getY() {
		return this.yPos;
	}
	
	public int getType() {
		return this.type;
	}
	
	public double getSpeedX() {
		return this.speedX;
	}
	
	public double getSpeedY() {
		return this.speedY;
	}
	
	public boolean getCollide() {
		return this.collide;
	}
	
	public int getAttack() {
		return this.damage;
	}
	
	public void setDamage(int strength) {
		damage = strength;
	}
	
	public void setX(int update) {
		xPos += update;
	}
	
	public void setY(int update) {
		yPos += update;
	}
	
	// Description: Updates position
	// Parameter(s): None
	// Return(s): void
	public void updatePosition() {
		xPos += speedX;
		yPos += speedY;
	}
}
