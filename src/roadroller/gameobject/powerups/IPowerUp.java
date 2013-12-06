package roadroller.gameobject.powerups;

import roadroller.gameobject.GameObject;

/**
 * Interface for Powerup objects in RoadRoller
 * 
 * all powerups need to implement this inteface.
 * 
 * @author Felix
 *
 */
public interface IPowerUp extends GameObject{

	/**
	 * call this to apply the powerups effects.
	 */
	public void applyPowers();
	
	/**
	 * returns the remaining active time.
	 * 
	 * @return remaining time
	 */
	public int timeLeftToLive();
	
	/**
	 * contrary to applyPowers().
	 */
	public void undoPowers();
	
	/**
	 * tells is the powerup is currently set active.
	 * 
	 * @return status
	 */
	public boolean isActive();
}
