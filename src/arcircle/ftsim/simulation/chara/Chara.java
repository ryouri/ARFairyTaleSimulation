package arcircle.ftsim.simulation.chara;

import java.util.ArrayList;

import arcircle.ftsim.simulation.chara.ai.AI;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.model.Field;

/**
 * 現在，耐久値を設定できるようになっていない
 * 設定できるようにしたら，itemListへのアイテムのコピーの実装を変更する必要がある
 */
public class Chara {
	public int x;
	public int y;

	public int pX;
	public int pY;

	public boolean isMoving;

	private boolean isMoved;

	private boolean isStand;

	private int camp;

	private boolean isAttack;
	private int attackTime;
	private int attackRightLeftDirection;
	public static final int MAX_ATTACK_TIME = 50;

	private AI ai;

	private int speed;
	public static final int SPEED = 8;

	public Status status;

	public GrowRateStatus growRateStatus;

	public Chara(String name) {
		this.status = new Status();
		this.growRateStatus = new GrowRateStatus();
		this.status.setItemList(new ArrayList<Item>());
		this.status.name = name;
		this.isStand = false;
		this.isMoved = false;
		this.speed = 8;
	}

	public AI getAI() {
		return ai;
	}
	public void setAI(AI ai) {
		this.ai = ai;
	}

	public boolean isAttack() {
		return isAttack;
	}

	public void setAttack(boolean isAttack) {
		this.isAttack = isAttack;
	}

	public int getAttackTime() {
		return attackTime;
	}

	public void setAttackTime(int attackTime) {
		this.attackTime = attackTime;
	}

	public int getCamp() {
		return camp;
	}

	public void setCamp(int camp) {
		this.camp = camp;
	}

	//味方
	public static final int  CAMP_FRIEND = 0;
	//敵
	public static final int  CAMP_ENEMY = 1;
	//友軍
	public static final int  CAMP_ALLIES = 2;

	public boolean isStand() {
		return isStand;
	}

	public void setStand(boolean isStand) {
		this.isStand = isStand;
		if (isStand) {
			resetState();
		}
	}

	public void resetState() {
		this.direction = Chara.DOWN;
		this.isMoving = false;
		this.isAttack = false;
		this.isMoved = false;
		this.isSelect = false;
		this.attackTime = 0;
	}

	public boolean isMoved() {
		return isMoved;
	}

	public void setMoved(boolean isMoved) {
		this.isMoved = isMoved;
	}


	public int direction;
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;

	public boolean isSelect;

	public ArrayList<Item> getItemList() {
		return status.getItemList();
	}

	public void setItemList(ArrayList<Item> itemList) {
		status.setItemList(new ArrayList<Item>(itemList));
	}

	public int getAttackRightLeftDirection() {
		return attackRightLeftDirection;
	}

	public void setAttackRightLeftDirection(int attackRightLeftDirection) {
		this.attackRightLeftDirection = attackRightLeftDirection;
	}

	public void setMoving(boolean moving) {
		this.isMoving = moving;
	}

	public void move() {
		if (!isMoving) {
			return;
		}

		if (direction == UP) {
			pY -= speed;
			if (y * Field.MAP_CHIP_SIZE - pY >= Field.MAP_CHIP_SIZE) {
				y--;
				pY = y * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (direction == RIGHT) {
			pX += speed;
			if (pX - x * Field.MAP_CHIP_SIZE >= Field.MAP_CHIP_SIZE) {
				x++;
				pX = x * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (direction == DOWN) {
			pY += speed;
			if (pY - y * Field.MAP_CHIP_SIZE >= Field.MAP_CHIP_SIZE) {
				y++;
				pY = y * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
		if (direction == LEFT) {
			pX -= speed;
			if (x * Field.MAP_CHIP_SIZE - pX >= Field.MAP_CHIP_SIZE) {
				x--;
				pX = x * Field.MAP_CHIP_SIZE;
				isMoving = false;
			}
		}
	}
}