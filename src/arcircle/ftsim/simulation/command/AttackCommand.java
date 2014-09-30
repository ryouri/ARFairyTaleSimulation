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
import arcircle.ftsim.simulation.algorithm.range.CalculateMoveAttackRange;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.CharaCommandWindow;
import arcircle.ftsim.simulation.model.Characters;
import arcircle.ftsim.simulation.model.Cursor;
import arcircle.ftsim.simulation.model.Field;
import arcircle.ftsim.state.simgame.SimGameModel;

public class AttackCommand extends Command implements KeyListner, Renderer {

	private Chara chara;
	private Field field;

	Image attackRangeImage;
	Color attackColor;

	private int cursorFirstX;
	private int cursorFirstY;
	
	private boolean visible;
	
	private boolean[][] attackRange;
	
	private boolean[][] attackJudge;

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public AttackCommand(String commandName, SimGameModel sgModel,
			CharaCommandWindow charaCommandWindow) {
		super(commandName, sgModel, charaCommandWindow);
		try {
			attackRangeImage = new Image("image/commandWindow/attackRange.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		attackColor = new Color(1, 1, 1, 0.5f);
		
		
	}

	@Override
	public void pushed(Field field, Chara chara) {
		this.chara = chara;
		this.field = field;		
		cursorFirstX = field.getCursor().x;
		cursorFirstY = field.getCursor().y;
		
		this.attackRange = new boolean[field.row][field.col];
		
		int weaponType = CalculateMoveAttackRange.judgeAttackWeaponType(chara.getItemList());
		CalculateMoveAttackRange.calculateAttackRange(chara.x, chara.y, attackRange, weaponType, field);;
		attackJudge = CalculateMoveAttackRange.calculateJudgeAttack(field, attackRange, chara);

		setVisible(true);
	}

	private void calculateJudgeAttack() {
		attackJudge = new boolean[attackRange.length][attackRange[0].length];
		boolean[][] charaPut = new boolean[attackRange.length][attackRange[0].length];
		Characters characters = field.getCharacters();
		
		for (Chara putChara : characters.characterArray) {
			if (this.chara.getCamp() == Chara.CAMP_FRIEND) {
				if (putChara.getCamp() == Chara.CAMP_ENEMY) {
					charaPut[putChara.y][putChara.x] = true;
				}
			}
		}
		
		for (int y = 0; y < field.row; y++) {
			for (int x = 0; x < field.col; x++) {
				if (charaPut[y][x] == true && attackRange[y][x] == true) {
					attackJudge[y][x] = true;
				}
			}
		}
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		if (field == null || chara == null || !isVisible()) {
			return;
		}

		for (int y = field.firstTileY; y < field.lastTileY; y++) {
			for (int x = field.firstTileX; x < field.lastTileX; x++) {
				if (!attackRange[y][x]) {
					continue;
				}

				// 各マスのタイルを描画
				g.drawImage(attackRangeImage,
						field.tilesToPixels(x) + field.offsetX,
						field.tilesToPixels(y) + field.offsetY,
						attackColor);
			}
		}
	}
	
	public void setFirstPosition() {
		//カーソルをキャラの最初の位置まで戻す
		field.getCursor().x = cursorFirstX;
		field.getCursor().y = cursorFirstY;
		field.getCursor().pX = cursorFirstX * Field.MAP_CHIP_SIZE;
		field.getCursor().pY = cursorFirstY * Field.MAP_CHIP_SIZE;
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
		
		if (keyInput.isKeyDown(Input.KEY_Z)) {
			if(attackJudge[field.getCursor().y][field.getCursor().x]) {
				field.charaAttack(chara, field.getCursor().y, field.getCursor().x);
				
				//他の場所に移す
				sgModel.removeKeyInputStackByField();
				sgModel.removeRendererArrayBySubInfoWindow();
			}
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
	}
}
