package arcircle.ftsim.simulation.model;

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

	private int directionPressedTime[];
	private static int DIRECTION_PRESSED_DURATION = 30;

	private int speed;

	public Cursor(Field field) {
		this.field = field;
		speed = 8;
		directionPressedTime = new int[4];
	}

	public void move(int direction) {
		directionPressedTime[direction] = DIRECTION_PRESSED_DURATION;

		if (isMoving) {
			return;
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
	}

	public void pressed(int direction) {
		directionPressedTime[direction]--;

		if (isMoving) {
			return;
		}

		if (direction == UP && directionPressedTime[UP] < 0) {
			if (y > 0) {
				isMoving = true;
				this.setDirection(UP);
			}
		}
		if (direction == RIGHT && directionPressedTime[RIGHT] < 0) {
			if (x < field.col - 1) {
				isMoving = true;
				this.setDirection(RIGHT);
			}
		}
		if (direction == DOWN && directionPressedTime[DOWN] < 0) {
			if (y < field.row - 1) {
				isMoving = true;
				this.setDirection(DOWN);
			}
		}
		if (direction == LEFT && directionPressedTime[LEFT] < 0) {
			if (x > 0) {
				isMoving = true;
				this.setDirection(LEFT);
			}
		}
	}

	public void update() {
		if (!isMoving) {
			return;
		}

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
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
