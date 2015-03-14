package arcircle.ftsim.simulation.model;

import arcircle.ftsim.simulation.sound.SoundManager;

public class Cursor {
	public int x;
	public int y;

	public int pX;
	public int pY;

	public boolean isMoving;

	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	private Field field;

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

	public Cursor(Field field) {
		this.field = field;
		speed = SPEED;
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
			if (x < field.col - 1) {
				isMoving = true;
				this.setDirection(RIGHT);
			}
		}
		if (direction == DOWN) {
			if (y < field.row - 1) {
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

		if (isMoving == true) {
			field.getSoundManager().playSound(SoundManager.SOUND_CURSOR);
		}

		return isMoving;
	}

	/**
	 * 移動を開始したら trueを返す
	 * @param direction
	 */
	public boolean pressed(int direction) {
		directionPressedTime--;

		if (isMoving) {
			return false;
		}

		if (direction == UP && directionPressedTime < 0) {
			if (y > 0) {
				isMoving = true;
				this.setDirection(UP);
			}
		}
		if (direction == RIGHT && directionPressedTime < 0) {
			if (x < field.col - 1) {
				isMoving = true;
				this.setDirection(RIGHT);
			}
		}
		if (direction == DOWN && directionPressedTime < 0) {
			if (y < field.row - 1) {
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

		if (isMoving == true) {
			field.getSoundManager().playSound(SoundManager.SOUND_CURSOR);
		}

		return isMoving;
	}

	public void update() {
		if (!isMoving) {
			stopTime++;
			return;
		}

		stopTime = 0;

		if (getDirection() == UP) {
			pY -= speed;
			if (y * Field.MAP_CHIP_SIZE - pY >= Field.MAP_CHIP_SIZE) {
				y--;
				pY = y * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == RIGHT) {
			pX += speed;
			if (pX - x * Field.MAP_CHIP_SIZE >= Field.MAP_CHIP_SIZE) {
				x++;
				pX = x * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == DOWN) {
			pY += speed;
			if (pY - y * Field.MAP_CHIP_SIZE >= Field.MAP_CHIP_SIZE) {
				y++;
				pY = y * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (getDirection() == LEFT) {
			pX -= speed;
			if (x * Field.MAP_CHIP_SIZE - pX >= Field.MAP_CHIP_SIZE) {
				x--;
				pX = x * Field.MAP_CHIP_SIZE;
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
		if (pY > (field.row - 1) * Field.MAP_CHIP_SIZE) {
			pY = (field.row - 1) * Field.MAP_CHIP_SIZE;
			isMoving = false;
		}
		if (pX > (field.col - 1) * Field.MAP_CHIP_SIZE) {
			pX = (field.col - 1) * Field.MAP_CHIP_SIZE;
			isMoving = false;
		}
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
