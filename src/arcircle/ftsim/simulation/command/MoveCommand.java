package arcircle.ftsim.simulation.command;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.model.Cursor;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.simulation.sound.SoundManager;
import arcircle.ftsim.state.simgame.SimGameModel;

public class MoveCommand extends Command implements KeyListner, Renderer {
	CalculateMoveAttackRange cmRange;
	protected Chara chara;
	protected Field field;

	private int cursorFirstX;
	private int cursorFirstY;

	private boolean visible;

	private boolean[][] attackRange;

	private static final Color attackColor = new Color(1.0f, 0.5f, 0.5f, 0.2f);
	private static final Color moveColor = new Color(0.4f, 0.4f, 1.0f, 0.2f);

	public MoveCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
	}

	public void init(Field field, Chara chara) {
		this.chara = chara;
		this.field = field;
		cmRange = new CalculateMoveAttackRange(field, chara);
		cmRange.calculateRange();

		attackRange = CalculateMoveAttackRange.createJudgeAttackArray(field, chara, cmRange.moveRange);

		cursorFirstX = field.getCursor().x;
		cursorFirstY = field.getCursor().y;
		field.getCursor().setDirection(Cursor.DOWN);

		setVisible(true);
	}

	@Override
	public int pushed(Field field, Chara chara) {
		init(field, chara);

		field.getSgModel().pushKeyInputStack(this);
		field.getSgModel().addRendererArray(this);

		return Command.PUSHED_NOT_VISIBLE;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (cmRange == null || field == null || chara == null || !isVisible()) {
			return;
		}

		for (int y = field.firstTileY; y < field.lastTileY; y++) {
			for (int x = field.firstTileX; x < field.lastTileX; x++) {
				if (cmRange.moveRange[y][x]) {
					g.setColor(moveColor);
					g.fillRect(Field.tilesToPixels(x) + field.offsetX,
							   Field.tilesToPixels(y) + field.offsetY,
							   LoadField.MAP_CHIP_SIZE, LoadField.MAP_CHIP_SIZE);
				} else if (attackRange[y][x]) {
					g.setColor(attackColor);
					g.fillRect(Field.tilesToPixels(x) + field.offsetX,
							   Field.tilesToPixels(y) + field.offsetY,
							   LoadField.MAP_CHIP_SIZE, LoadField.MAP_CHIP_SIZE);
				}
			}
		}
		g.setColor(Color.white);
	}

	public void setFirstPosition() {
		//カーソルをキャラの最初の位置まで戻す
		field.getCursor().x = cursorFirstX;
		field.getCursor().y = cursorFirstY;
		field.getCursor().pX = cursorFirstX * LoadField.MAP_CHIP_SIZE;
		field.getCursor().pY = cursorFirstY * LoadField.MAP_CHIP_SIZE;

		//キャラを最初の位置まで戻す
		chara.isMoving = false;
		chara.direction = Chara.DOWN;
		chara.x = field.getCursor().x;
		chara.y = field.getCursor().y;
		chara.pX = field.getCursor().pX;
		chara.pY = field.getCursor().pY;
	}

	@Override
	public void keyInput(KeyInput keyInput) {
		//キャンセルキーが押されたとき
		if (keyInput.isKeyDown(Input.KEY_X)) {
			field.getSoundManager().playSound(SoundManager.SOUND_CANCEL);
			sgModel.removeKeyInputStackFirst();
			sgModel.removeRendererArrayEnd();

			setFirstPosition();

			charaCommandWindow.setVisible(true);

			return;
		}

		if (keyInput.isKeyDown(Input.KEY_UP)) {
			field.getCursor().startMove(Cursor.UP);
		}
		if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			field.getCursor().startMove(Cursor.RIGHT);
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			field.getCursor().startMove(Cursor.DOWN);
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			field.getCursor().startMove(Cursor.LEFT);
		}

		if (keyInput.isKeyPressed(Input.KEY_UP)) {
			field.getCursor().pressed(Cursor.UP);
		}
		if (keyInput.isKeyPressed(Input.KEY_RIGHT)) {
			field.getCursor().pressed(Cursor.RIGHT);
		}
		if (keyInput.isKeyPressed(Input.KEY_DOWN)) {
			field.getCursor().pressed(Cursor.DOWN);
		}
		if (keyInput.isKeyPressed(Input.KEY_LEFT)) {
			field.getCursor().pressed(Cursor.LEFT);
		}

		//カーソルの座標情報と，キャラの座標情報を一致させる
		//ただし，キャラが移動できる範囲内の場合のみ
		chara.isMoving = true;
		if (cmRange.moveRange[field.getCursor().y][field.getCursor().x]) {
			chara.direction = field.getCursor().getDirection();
			chara.x = field.getCursor().x;
			chara.y = field.getCursor().y;
			chara.pX = field.getCursor().pX;
			chara.pY = field.getCursor().pY;
		} else {
			chara.pX = chara.x * LoadField.MAP_CHIP_SIZE;
			chara.pY = chara.y * LoadField.MAP_CHIP_SIZE;
		}

		//決定キーが押されたとき
		/*
		 * 決定キーが押されたら，
		 *  このクラスをスタックとリストから削除
		 *  MovedCommandWindowクラスを生成し，スタックとリストに追加
		 *  キャラの移動命令をfield.Charactersに出す
		 *  キャラの移動が終了したら，MovedCommandWindowがWindowを表示する
		 *   Window表示後にキャンセルされた時，MoveCommandをもう一度表示する．，
		 *  その後はまたあとで考える
		 *
		 */
		if (keyInput.isKeyDown(Input.KEY_Z) && !field.getCursor().isMoving) {
			field.getSoundManager().playSound(SoundManager.SOUND_DECISION);

			//TODO;キャラクターが動いた時の処理を書く
			chara.setMoved(true);
			MovedCommandWindow mcWindow = new MovedCommandWindow(sgModel, field,
					chara, this);
			sgModel.pushKeyInputStack(mcWindow);
			sgModel.addRendererArray(mcWindow);

			this.setVisible(false);

			return;
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
