package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.item.Item;
import arcircle.ftsim.simulation.item.SupportItem;
import arcircle.ftsim.simulation.sound.SoundManager;

public class HealTask extends Task {
	private int healCount;

	private Chara healChara;
	private Chara healedChara;

	public static final int HEAL_COUNT_MAX = 50;

	public HealTask(TaskManager taskManager, Chara nealChara, Chara healedChara) {
		super(taskManager);

		healCount = 0;
		charaAttackPrepareDir(nealChara, healedChara);

		this.healChara = nealChara;
		this.healedChara = healedChara;

		taskManager.field.getSoundManager().playSound(SoundManager.SOUND_HEAL);
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		int change = healCount;
		//時間の変化によって，キャラクタの移動量を制御
		if (change <= 5 || change >= Chara.MAX_ATTACK_TIME - 5) {
			change = 0;
		} else if (change < Chara.MAX_ATTACK_TIME / 2) {
			change -= 5;
		} else if (change >= Chara.MAX_ATTACK_TIME / 2) {
			change = Chara.MAX_ATTACK_TIME - change - 5;
		}

		Animation anime = null;
		if (healChara.direction == Chara.UP) {
			anime = taskManager.characters.upAttackAnimeMap.get(healChara.getFolderName());
		} else if (healChara.direction == Chara.RIGHT) {
			anime = taskManager.characters.rightAttackAnimeMap.get(healChara.getFolderName());
		} else if (healChara.direction == Chara.LEFT) {
			anime = taskManager.characters.leftAttackAnimeMap.get(healChara.getFolderName());
		} else {//(chara.direction == Chara.DOWN) {
			anime = taskManager.characters.downAttackAnimeMap.get(healChara.getFolderName());
		}

		int changeX = 0;
		int changeY = 0;

		if (healChara.direction == Chara.UP) {
			changeY = -change;
		} else if (healChara.direction == Chara.RIGHT) {
			changeX = change;
			if (healChara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (healChara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else if (healChara.direction == Chara.LEFT) {
			changeX = -change;
			if (healChara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (healChara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else {//(chara.direction == Chara.DOWN) {
			changeY = change;
		}

		anime.draw(
				healChara.pX + offsetX + changeX,
				healChara.pY + offsetY + changeY);
	}

	@Override
	public void update(int delta) {
		healCount++;

		healedChara.setColor(new Color(0.7f, 1.0f, 0.7f, 1.0f));

		if (healCount > HEAL_COUNT_MAX) {
			int healPoint = 0;
			for (Item item : healChara.status.getItemList()) {
				if (item instanceof SupportItem) {
					healPoint = ((SupportItem)item).power;
				}
			}

			healPoint += healChara.status.magicPower;
			healedChara.status.setHp(healedChara.status.getHp() + healPoint);
			healChara.setStand(true);
			//生きている時のみ状態を戻す
			healedChara.resetState();
			taskManager.taskEnd();
		}
	}

	private void charaAttackPrepareDir(Chara chara, Chara damageChara) {
		chara.setAttack(true);
		if (damageChara.x > chara.x) {
			chara.direction = Chara.RIGHT;

			damageChara.direction = Chara.LEFT;
			damageChara.setMoving(true);

			chara.setAttackRightLeftDirection(Chara.RIGHT);
			if (damageChara.y < chara.y) {
				chara.setAttackRightLeftDirection(Chara.UP);
			} else if (damageChara.y > chara.y) {
				chara.setAttackRightLeftDirection(Chara.DOWN);
			}
		} else if (damageChara.x < chara.x) {
			chara.direction = Chara.LEFT;

			damageChara.direction = Chara.RIGHT;
			damageChara.setMoving(true);

			chara.setAttackRightLeftDirection(Chara.LEFT);
			if (damageChara.y < chara.y) {
				chara.setAttackRightLeftDirection(Chara.UP);
			} else if (damageChara.y > chara.y) {
				chara.setAttackRightLeftDirection(Chara.DOWN);
			}
		} else if (damageChara.y < chara.y) {
			chara.direction = Chara.UP;

			damageChara.direction = Chara.DOWN;
			damageChara.setMoving(true);
		} else if (damageChara.y > chara.y) {
			chara.direction = Chara.DOWN;

			damageChara.direction = Chara.UP;
			damageChara.setMoving(true);
		}
	}
}
