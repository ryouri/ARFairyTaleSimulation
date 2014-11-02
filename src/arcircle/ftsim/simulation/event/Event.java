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
	public static final String TOUCH_CHARA = "TOUCH_CHARA";
	/**
	 * あるキャラがある場所に到達したら
	 */
	public static final int TYPE_ARRIVAL = 1;
	public static final String ARRIVAL = "ARRIVAL";
	/**
	 * nターン経過したら
	 */
	public static final int TYPE_TURN_PROGRESS = 2;
	public static final String TURN_PROGRESS = "TURN_PROGRESS";
	/**
	 * あるキャラが死んだら
	 */
	public static final int TYPE_CHARA_DIE = 3;
	public static final String CHARA_DIE = "CHARA_DIE";
	/**
	 * 敵がn体以下になったら
	 */
	public static final int TYPE_ENEMY_BELOW = 4;
	public static final String ENEMY_BELOW = "ENEMY_BELOW";
	/**
	 * 敵をn体倒したら
	 */
	public static final int TYPE_KILL_ENEMY = 5;
	public static final String KILL_ENEMY = "KILL_ENEMY";

	/**
	 * EVENTの種類の数
	 */
	public static final int TYPE_NUM = 6;
}
