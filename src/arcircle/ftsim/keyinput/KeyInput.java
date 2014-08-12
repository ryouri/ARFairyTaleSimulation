package arcircle.ftsim.keyinput;
import java.util.HashMap;

import org.newdawn.slick.Input;

/**
*キーボードの入力を管理するクラス<p>
*スペースキーで撃つ<p>
*カーソルキーで移動　と定義されている
*/
public class KeyInput
{
	HashMap<Integer, Key> keyMap;

	public KeyInput() {
		keyMap = new HashMap<Integer, Key>();
		keyMap.put(Input.KEY_UP, new Key(Input.KEY_UP));
		keyMap.put(Input.KEY_DOWN, new Key(Input.KEY_DOWN));
		keyMap.put(Input.KEY_LEFT, new Key(Input.KEY_LEFT));
		keyMap.put(Input.KEY_RIGHT, new Key(Input.KEY_RIGHT));
		keyMap.put(Input.KEY_Z, new Key(Input.KEY_Z));
		keyMap.put(Input.KEY_X, new Key(Input.KEY_X));
		keyMap.put(Input.KEY_C, new Key(Input.KEY_C));
		keyMap.put(Input.KEY_A, new Key(Input.KEY_A));
		keyMap.put(Input.KEY_S, new Key(Input.KEY_S));
		keyMap.put(Input.KEY_D, new Key(Input.KEY_D));
		keyMap.put(Input.KEY_LSHIFT, new Key(Input.KEY_LSHIFT));
	}

	/**
	 * キーが押されたときに呼ばれる処理。
	 * 変数にキー状態を保存する。
	 */
	public void keyPressed(int key, char c) {
		keyMap.get(key).keyPressed();

		//ESCAPEは強制終了
		if (key == Input.KEY_ESCAPE)
		{
			System.exit(0);
		}
	}

	/**
	 * 押されていたキーを放したときに呼ばれる処理
	 */
	public void keyReleased(int key, char c) {
		keyMap.get(key).keyReleased();
	}

	/**
	 * 押されていたキーを放したときに呼ばれる処理
	 */
	public void keyUpdate() {
		for (int keyNumber : keyMap.keySet()) {
			keyMap.get(keyNumber).keyUpdate();
		}
	}

	/**
	 * @param keyNumber Input.***でキーを取得
	 * @return keyが存在しなければ，-1を返す
	 *         keyが存在すればその状態を返す
	 */
	public int getKeyState(int keyNumber) {
		Key key = keyMap.get(keyNumber);
		if (key == null) {
			return -1;
		}

		return key.getKeyState();
	}

	/**
	 * @param keyNumber Input.***でキーを取得
	 * @return keyが存在しなければ，falseを返す
	 *         keyが存在すれば押されているかを返す
	 */
	public boolean isKeyPressed(int keyNumber) {
		Key key = keyMap.get(keyNumber);
		if (key == null) {
			return false;
		}
		return key.isPressed();
	}

	/**
	 * @param keyNumber Input.***でキーを取得
	 * @return keyが存在しなければ，falseを返す
	 *         keyが存在すれば押された瞬間かを返す
	 */
	public boolean isKeyDown(int keyNumber) {
		Key key = keyMap.get(keyNumber);
		if (key == null) {
			return false;
		}
		return key.isDown();
	}

	/**
	 * @param key Input.***でキーを取得
	 * @return keyが存在しなければ，falseを返す
	 *         keyが存在すれば離されているかを返す
	 */
	public boolean isKeyReleased(int keyNumber) {
		Key key = keyMap.get(keyNumber);
		if (key == null) {
			return false;
		}
		return key.isRelease();
	}
}
