//Chi Ngo
//cngongoc

package hw3;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class HangmanRound extends GameRound {
	private IntegerProperty hitCount = new SimpleIntegerProperty();
	private IntegerProperty missCount = new SimpleIntegerProperty();
	
	public int getHitCount() {
		return hitCount.get(); //return the integer value of hitCount
	}
	
	public void setHitCount(int hitCount) {
		this.hitCount.set(hitCount); //set the integer value of hitCount
	}
	
	public IntegerProperty hitCountProperty() {
		return hitCount; //return the property of hitCount
	}
	
	public int getMissCount() {
		return missCount.get(); //return the integer value of missCount
	}
	
	public void setMissCount(int missCount) {
		this.missCount.set(missCount); //set the integer value of missCount
	}
	
	public IntegerProperty missCountProperty() {
		return missCount; //return the property of missCount
	}
}
