package arcircle.ftsim.state;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;

abstract public class KeyInputState extends BasicGameState {

	/**
	 * 利用するフォントファイルを格納する
	 */
	protected UnicodeFont font;

	public UnicodeFont getFont() {
		return font;
	}

	/**
	 * Stateを管理しているクラスを持っていたほうが何かと便利
	 */
	protected StateBasedGame stateGame;

	/**
	 * キー入力を受け取り，キーの状態を保持する
	 */
	protected KeyInput keyInput;

	/**
	 * キーの入力を渡すKeyListnerのスタック
	 * このスタックの「一番上に入っているデータのみ」にKeyInputが渡される
	 * 追加する場合は，deque.addFirst(a);
	 * 先頭を削除する場合は，deque.removeFirst();
	 */
	protected Deque<KeyListner> keyInputStack;

	/**
	 * 描画処理を実行するRendererのList
	 * このListの「先頭から」描画が行われる
	 */
	protected ArrayList<Renderer> rendererArray;

	/**
	 * 自インスタンスの番号を保持しておく
	 */
	private int state;

	public KeyInputState(int state) {
		super();
		this.state = state;
		this.keyInputStack = new ArrayDeque<KeyListner>();
		this.rendererArray = new ArrayList<Renderer>();
		this.keyInput = new KeyInput();
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		this.stateGame = game;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		for (Renderer renderer : rendererArray) {
			renderer.render(container, game, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		keyInputStack.getFirst().keyInput(keyInput);;
	}

	@Override
	public int getID() {
		return this.state;
	}

	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		keyInput.keyPressed(key, c);
	}

	@Override
	public void keyReleased(int key, char c) {
		super.keyReleased(key, c);
		keyInput.keyReleased(key, c);
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		this.font = FTSimulationGame.font;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		//keyInputを初期化する
		this.keyInput = new KeyInput();
		super.leave(container, game);
	}
}
