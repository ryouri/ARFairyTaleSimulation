package arcircle.ftsim.simulation.event;

public class Event {
	public int eventType;

	public String eventFileName;

	Event(String eventFileName) {
		this.eventFileName = eventFileName;
	}

	/**
	 * あるキャラとあるキャラが接触したら
	 */
	public static final int TYPE_TOUCH_CHARA = 0;
	/**
	 * あるキャラがある場所に到達したら
	 */
	public static final int TYPE_ARRIVAL = 1;
	/**
	 * nターン経過したら
	 */
	public static final int TYPE_TURN_PROGRESS = 2;
	/**
	 * あるキャラが死んだら
	 */
	public static final int TYPE_CHARA_DIE = 3;
	/**
	 * 敵がn体以下になったら
	 */
	public static final int TYPE_ENEMY_BELOW = 4;
	/**
	 * 敵をn体倒したら
	 */
	public static final int TYPE_KILL_ENEMY = 5;

	/**
	 * EVENTの種類の数
	 */
	public static final int TYPE_NUM = 6;
}
