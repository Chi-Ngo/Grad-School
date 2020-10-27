//Chi Ngo
//cngongoc

package hw1;

public class HangmanRound extends GameRound {
	private int hitCount;
	private int missCount;
	
	public int getHitCount() {
		return hitCount;	//return for the latest number of hits
	}
	
	public void setHitCount(int hitCount) {
		this.hitCount = hitCount;	//update the number of hits to a new value
	}
	
	public int getMissCount() {
		return missCount;	//return for the latest number of misses
	}
	
	public void setMissCount(int missCount) {
		this.missCount = missCount;	//update the number of misses to a new value
	}
}
