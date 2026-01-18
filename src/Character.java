
public class Character {
	private int health;
	private int xPos;
	private int yPos;
	private int attack;
	public int mana;
	public int speed;
	public int[] spells = {0, -1, -1};
	private int castTimeBasic = 0, castTimeFire = 0, castTimeIce = 0, castTimeRock = 0;
	public int[] charUpgrades = {-1,-1,-1};
	public int index = 1, indexUp = 0;
	
	// Description: Create the character object and initialize variables.
	// Parameter(s): int mana, int speed, int attack
	// Return(s): void
	public Character(int mana, int speed, int attack) {
		health = 100;
		xPos = 473;
		yPos = 545;
		this.attack = attack;
		this.mana = mana;
		this.speed = speed;
		castTimeBasic = 0;
		castTimeFire = 0;
		castTimeRock = 0;
		castTimeIce = 0;
	}
	
	// Getters and Setters
	public int getHealth() {
		return this.health;
	}
	
	public int getXPos() {
		return this.xPos;
	}
	
	public int getYPos() {
		return this.yPos;
	}
	
	public int getAttack() {
		return this.attack;
	}
	
	public int getMana() {
		return this.mana;
	}
	
	public int getCastTimeBasic() {
		return this.castTimeBasic;
	}

	public int getCastTimeFire() {
		return this.castTimeFire;
	}
	
	public int getCastTimeIce() {
		return this.castTimeIce;
	}
	
	public int getCastTimeRock() {
		return this.castTimeRock;
	}
	
	public void setXPos(int position) {
		xPos = position;
	}
	
	public void setYPos(int position) {
		yPos = position;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void setCastTimeBasic(int frames) {
		this.castTimeBasic = frames;
	}

	public void setCastTimeFire(int frames) {
		this.castTimeFire = frames;
	}
	
	public void setCastTimeIce(int frames) {
		this.castTimeIce = frames;
	}
	
	public void setMana(int update) {
		this.mana = update;
	}
	
	public void setCastTimeRock(int frames) {
		this.castTimeRock = frames;
	}
	
	// Description: Adds spell
	// Parameter(s): int spell
	// Return(s): void
	public void addSpell(int spell) {
		spells[index] = spell;
		index++;
	}
	
	// Description: Add upgrade
	// Parameter(s): int upgrade
	// Return(s): void
	public void addUpgrade(int upgrade) {
		charUpgrades[indexUp] = upgrade;
		indexUp++;
	}
	
	// Description: Upgrade attack
	// Parameter(s): int update
	// Return(s): void
	public void upgradeAttack() {
		this.attack += 5;
	}
	
	// Description: Updates attack
	// Parameter(s): int update
	// Return(s): void
	public void updateAttack(int update) {
		this.attack += update;
	}
	
	// Description: Updates position
	// Parameter(s): int speedX, speedY
	// Return(s): void
	public void updatePosition(int speedX, int speedY) {
		this.xPos += speedX;
		this.yPos += speedY;
	}
	
	// Description: Updates health
	// Parameter(s): int update
	// Return(s): void
	public void updateHealth(int update) {
		this.health -= update;
	}
	
	// Description: Updates mana
	// Parameter(s): int update
	// Return(s): void
	public void updateMana(int update) {
		this.mana += update;
	}
}
