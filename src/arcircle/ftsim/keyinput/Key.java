package arcircle.ftsim.keyinput;

public class Key {
	/**
	 * 押されていない
	 */
	public static int KEY_RELEASE = 0;
	/**
	 * 押された瞬間
	 */
	public static int KEY_DOWN = 1;
	/**
	 * 押されているとき
	 */
	public static int KEY_PRESSED = 2;

	/**
	 * キーを押された瞬間であると認識する時間
	 * （キーが押されているときに移行するまでの時間）
	 */
	public int keyDownTimeSetting;

	/**
	 * 今キーが押されてからどれくらい経ったか
	 */
	public int keyDownTime;

	/**
	 * キーの状態を↑の３つの状態で保持
	 */
	private int keyState;

	/**
	 * キーの番号を格納
	 */
	private int keyNumber;

	public Key(int keyNumber) {
		super();
		this.keyState = KEY_RELEASE;
		this.keyNumber = keyNumber;
		this.keyDownTimeSetting = 1;
	}

	public Key(int keyNumber, int keyDownTimeSetting) {
		this(keyNumber);
		this.keyDownTimeSetting = keyDownTimeSetting;
	}

	/**
	 * このPressed()は「キーが押された瞬間のみ呼ばれる」
	 * そのため，押された瞬間と押され続けている時を区別するために，keyUpdateを実行する
	 */
	public void keyPressed() {
		//押された瞬間を表すフラグ
		keyState = KEY_DOWN;
		keyDownTime = 0;
	}

	public void keyUpdate() {
		//押された瞬間であれば，
		if (keyState == KEY_DOWN)
		{
			keyDownTime++;
		}
		if (keyDownTime >= keyDownTimeSetting) {
			keyState = KEY_PRESSED;
		}
	}

	public void keyReleased() {
		keyState = KEY_RELEASE;
		keyDownTime = 0;
	}

	public int getKeyState() {
		return keyState;
	}

	public boolean isRelease() {
		if (keyState == KEY_RELEASE) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isPressed() {
		if (keyState == KEY_PRESSED) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDown() {
		if (keyState == KEY_DOWN) {
			return true;
		} else {
			return false;
		}
	}
}
