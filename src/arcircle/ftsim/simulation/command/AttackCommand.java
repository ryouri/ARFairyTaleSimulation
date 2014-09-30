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
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.Weapon;
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

	//x, y
	public static int[][] nearAttackRange = {
		{  0, -1},
		{  1,  0},
		{  0,  1},
		{ -1,  0},
	};
	
	//x, y
	public static int[][] farAttackRange = {
		{  0, -2},
		{  1, -1},
		{  2,  0},
		{  1,  1},
		{  0,  2},
		{ -1,  1},
		{ -2,  0},
		{ -1, -1},
	};
	
	@Override
	public void pushed(Field field, Chara chara) {
		this.chara = chara;
		this.field = field;		
		cursorFirstX = field.getCursor().x;
		cursorFirstY = field.getCursor().y;
		
		this.attackRange = new boolean[field.row][field.col];
		
		calculateAttackRange();
		calculateJudgeAttack();

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

	private void calculateAttackRange() {
		boolean nearAttack = false;
		boolean farAttack = false;
		
		int charaX = chara.x;
		int charaY = chara.y;
		
		for (Item item : chara.getItemList()) {
			if (!(item instanceof Weapon)) {
				continue;
			}
			
			Weapon weapon = (Weapon) item;
			
			if (weapon.rangeType == Weapon.RANGE_NEAR) {
				nearAttack = true;				
			} else if (weapon.rangeType == Weapon.RANGE_FAR) {
				farAttack = true;
			} else if (weapon.rangeType == Weapon.RANGE_NEAR_FAR) {
				nearAttack = true;
				farAttack = true;
			}
		}
		
		if (nearAttack) {
			for (int[] range : nearAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.col
						|| charaY + range[1] >= field.row) {
					continue;
				}
				attackRange[charaY + range[1]][charaX + range[0]] = true;
			}
		}
		if (farAttack) {
			for (int[] range : farAttackRange) {
				if (charaX + range[0] < 0 || charaY + range[1] < 0
						|| charaX + range[0] >= field.col
						|| charaY + range[1] >= field.row) {
					continue;
				}
				attackRange[charaY + range[1]][charaX + range[0]] = true;
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
