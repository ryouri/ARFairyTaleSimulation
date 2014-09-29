package arcircle.ftsim.simulation.command;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.algorithm.range.CalculateMoveRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Cursor;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class MoveCommand extends Command implements KeyListner, Renderer {
	CalculateMoveRange cmRange;
	private Chara chara;
	private Field field;

	Image moveRange;
	Color moveColor;

	private int cursorFirstX;
	private int cursorFirstY;

	private boolean visible;

	public MoveCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
		try {
			moveRange = new Image("image/commandWindow/moveRange.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		moveColor = new Color(1, 1, 1, 0.3f);
	}

	@Override
	public void pushed(Field field, Chara chara) {
		this.chara = chara;
		this.field = field;
		cmRange = new CalculateMoveRange(field, chara);
		cmRange.calculateRange();

		cursorFirstX = field.getCursor().x;
		cursorFirstY = field.getCursor().y;
		field.getCursor().setDirection(Cursor.DOWN);

		setVisible(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (cmRange == null || field == null || chara == null || !isVisible()) {
			return;
		}

		for (int y = field.firstTileY; y < field.lastTileY; y++) {
			for (int x = field.firstTileX; x < field.lastTileX; x++) {
				if (!cmRange.moveRange[y][x]) {
					continue;
				}

				// 各マスのタイルを描画
				g.drawImage(moveRange,
						field.tilesToPixels(x) + field.offsetX,
						field.tilesToPixels(y) + field.offsetY,
						moveColor);
			}
		}
	}

	public void setFirstPosition() {
		//カーソルをキャラの最初の位置まで戻す
		field.getCursor().x = cursorFirstX;
		field.getCursor().y = cursorFirstY;
		field.getCursor().pX = cursorFirstX * Field.MAP_CHIP_SIZE;
		field.getCursor().pY = cursorFirstY * Field.MAP_CHIP_SIZE;

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
			sgModel.removeKeyInputStackFirst();
			sgModel.removeRendererArrayEnd();

			setFirstPosition();

			charaCommandWindow.setVisible(true);

			return;
		}

		if (keyInput.isKeyDown(Input.KEY_UP)) {
			field.getCursor().move(Cursor.UP);
		}
		if (keyInput.isKeyDown(Input.KEY_RIGHT)) {
			field.getCursor().move(Cursor.RIGHT);
		}
		if (keyInput.isKeyDown(Input.KEY_DOWN)) {
			field.getCursor().move(Cursor.DOWN);
		}
		if (keyInput.isKeyDown(Input.KEY_LEFT)) {
			field.getCursor().move(Cursor.LEFT);
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
			chara.pX = chara.x * field.MAP_CHIP_SIZE;
			chara.pY = chara.y * field.MAP_CHIP_SIZE;
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
		if (keyInput.isKeyDown(Input.KEY_Z)) {
			//TODO;キャラクターが動いた時の処理を書く
			chara.setMoved(true);
			MovedCommandWindow mcWindow = new MovedCommandWindow(sgModel, field,
					chara, this);
			sgModel.keyInputStackPush(mcWindow);
			sgModel.rendererArrayAdd(mcWindow);

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
