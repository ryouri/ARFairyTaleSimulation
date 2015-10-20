package arcircle.ftsim.simulation.model;

import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.sound.SoundManager;

public class Cursor {
	public int x;
	public int y;

	public int pX;
	public int pY;

	public boolean isMoving;

	private boolean isVisible;

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	private Field field;
	private LoadField loadField;

	private int direction;
	public int getDirection() {
		return direction;
	}

	private int directionPressedTime;
	private static int DIRECTION_PRESSED_DURATION = 30;

	private int stopTime;
	private static int STOP_TIME_DURATION = 10;

	private int speed;
	public static final int SPEED = 8;

	public Cursor(Field field, LoadField loadField) {
		this.field = field;
		this.loadField = loadField;
		speed = SPEED;
		directionPressedTime = DIRECTION_PRESSED_DURATION;
		stopTime = 0;
		this.isVisible = true;
	}

	public void stop() {
		pX = x * LoadField.MAP_CHIP_SIZE;
		pY = y * LoadField.MAP_CHIP_SIZE;
		isMoving = false;
		directionPressedTime = DIRECTION_PRESSED_DURATION;
		stopTime = 0;
	}

	/**
	 * 移動を開始したら trueを返す
	 * @param direction
	 */
	public boolean startMove(int direction) {
		//すでに移動中なら呼び出さない
		if (isMoving) {
			return false;
		}

		//ある程度の時間止まったら，長押し判定時間をリセットする
		if (stopTime > STOP_TIME_DURATION){
			directionPressedTime = DIRECTION_PRESSED_DURATION;
		}

		if (direction == UP) {
			if (y > 0) {
				isMoving = true;
				this.setDirection(UP);
			}
		}
		if (direction == RIGHT) {
			if (x < loadField.getCol() - 1) {
				isMoving = true;
				this.setDirection(RIGHT);
			}
		}
		if (direction == DOWN) {
			if (y < loadField.getRow() - 1) {
				isMoving = true;
				this.setDirection(DOWN);
			}
		}
		if (direction == LEFT) {
			if (x > 0) {
				isMoving = true;
				this.setDirection(LEFT);
			}
		}

		if (isMoving && isVisible) {
			field.getSoundManager().playSound(SoundManager.SOUND_CURSOR);
		}

		return isMoving;
	}

	/**
	 * 移動を開始したら trueを返す
	 * @param direction
	 */
	public boolean pressed(int direction) {
		//ボタンが押されている間は長押し判定時間をデクリメント
		directionPressedTime--;

		if (isMoving) {
			return false;
		}

		//長押し判定時間を超すほどボタンが押されていれば長押し移動開始！
		if (direction == UP && directionPressedTime < 0) {
			if (y > 0) {
				isMoving = true;
				this.setDirection(UP);
			}
		}
		if (direction == RIGHT && directionPressedTime < 0) {
			if (x < loadField.getCol() - 1) {
				isMoving = true;
				this.setDirection(RIGHT);
			}
		}
		if (direction == DOWN && directionPressedTime < 0) {
			if (y < loadField.getRow() - 1) {
				isMoving = true;
				this.setDirection(DOWN);
			}
		}
		if (direction == LEFT && directionPressedTime < 0) {
			if (x > 0) {
				isMoving = true;
				this.setDirection(LEFT);
			}
		}

		if (isMoving && isVisible) {
			field.getSoundManager().playSound(SoundManager.SOUND_CURSOR);
		}

		return isMoving;
	}

	public void update() {
		if (field.getNowTurn() == field.TURN_ENEMY) {
			isVisible = false;
		}

		if (!isMoving) {
			//動いてない時は停止時間を増やしておく
			stopTime++;
			return;
		}

		//動いたら提示時間はリセット
		stopTime = 0;

		if (getDirection() == UP) {
			pY -= speed;
			if (y * LoadField.MAP_CHIP_SIZE - pY >= LoadField.MAP_CHIP_SIZE) {
				y--;
				pY = y * LoadField.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == RIGHT) {
			pX += speed;
			if (pX - x * LoadField.MAP_CHIP_SIZE >= LoadField.MAP_CHIP_SIZE) {
				x++;
				pX = x * LoadField.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == DOWN) {
			pY += speed;
			if (pY - y * LoadField.MAP_CHIP_SIZE >= LoadField.MAP_CHIP_SIZE) {
				y++;
				pY = y * LoadField.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == LEFT) {
			pX -= speed;
			if (x * LoadField.MAP_CHIP_SIZE - pX >= LoadField.MAP_CHIP_SIZE) {
				x--;
				pX = x * LoadField.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}

		if (pY < 0) {
			pY = 0;
			isMoving = false;
		}
		if (pX < 0) {
			pX = 0;
			isMoving = false;
		}
		if (pY > (loadField.getRow() - 1) * LoadField.MAP_CHIP_SIZE) {
			pY = (loadField.getRow() - 1) * LoadField.MAP_CHIP_SIZE;
			isMoving = false;
		}
		if (pX > (loadField.getCol() - 1) * LoadField.MAP_CHIP_SIZE) {
			pX = (loadField.getCol() - 1) * LoadField.MAP_CHIP_SIZE;
			isMoving = false;
		}
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean settingVisible) {
		if (settingVisible == true && field.getNowTurn() == field.TURN_ENEMY) {
			return;
		}
		this.isVisible = settingVisible;
	}
}
