import java.util.*;

public class Levels {
	private int basicEnemies;
	private int mageEnemies;
	private int levelNum;
	
	// Description: Creates level object and initializes the variables.
	// Parameter(s): int update
	// Return(s): void
	public Levels(int levelNum, int basicEnemies, int mageEnemies) {
		this.levelNum = levelNum;
		this.basicEnemies = basicEnemies;
		this.mageEnemies = mageEnemies;
	}
	
	// Getters 
	public int getLevelNum() {
		return this.levelNum;
	}
	
	public int getBasicEnemies() {
		return this.basicEnemies;
	}
	
	public int getMages() {
		return this.mageEnemies;
	}
	
}
