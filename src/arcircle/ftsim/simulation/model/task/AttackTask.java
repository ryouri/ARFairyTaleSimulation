package arcircle.ftsim.simulation.model.task;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.chara.battle.SupportInfo;
import arcircle.ftsim.simulation.model.AttackInfo;

public class AttackTask extends Task {
	ArrayList<AttackInfo> attackInfoArray;
	int nowAttackIndex;
	boolean isAttackNow;

	public AttackTask(TaskManager taskManager, Chara attackChara, Chara damageChara) {
		super(taskManager);

		calcAttackInfo(attackChara, damageChara);

	}

	private void calcAttackInfo(Chara attackChara, Chara damageChara) {
		Random random = new Random();

		ExpectBattleInfo expectBattleInfo =
				new ExpectBattleInfo(attackChara, attackChara.getEquipedWeapon(), new SupportInfo(),
						damageChara, damageChara.getEquipedWeapon(), new SupportInfo());

		this.attackInfoArray = new ArrayList<AttackInfo>();
		attackInfoArray.add(new AttackInfo(attackChara, damageChara));
		attackInfoArray.add(new AttackInfo(damageChara, attackChara));

		if (expectBattleInfo.getFirstCharaBattleInfo().isTwiceAttack()) {
			attackInfoArray.add(new AttackInfo(attackChara, damageChara));
		} else if (expectBattleInfo.getSecondCharaBattleInfo().isTwiceAttack()) {
			attackInfoArray.add(new AttackInfo(damageChara, attackChara));
		}
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		if (attackInfoArray.size() > 0) {
			renderAttack(attackInfoArray.get(nowAttackIndex).attackChara, g, offsetX, offsetY);
		}
	}

	private void renderAttack(Chara chara, Graphics g, int offsetX, int offsetY) {
		int change = chara.getAttackTime();
		if (change <= 5 || change >= Chara.MAX_ATTACK_TIME - 5) {
			change = 0;
		} else if (change < Chara.MAX_ATTACK_TIME / 2) {
			change -= 5;
		} else if (change >= Chara.MAX_ATTACK_TIME / 2) {
			change = Chara.MAX_ATTACK_TIME - change - 5;
		}

		Animation anime = null;
		if (chara.direction == Chara.UP) {
			anime = taskManager.characters.upAttackAnimeMap.get(chara.status.name);
		} else if (chara.direction == Chara.RIGHT) {
			anime = taskManager.characters.rightAttackAnimeMap.get(chara.status.name);
		} else if (chara.direction == Chara.LEFT) {
			anime = taskManager.characters.leftAttackAnimeMap.get(chara.status.name);
		} else {//(chara.direction == Chara.DOWN) {
			anime = taskManager.characters.downAttackAnimeMap.get(chara.status.name);
		}

		int changeX = 0;
		int changeY = 0;

		if (chara.direction == Chara.UP) {
			changeY = -change;
		} else if (chara.direction == Chara.RIGHT) {
			changeX = change;
			if (chara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (chara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else if (chara.direction == Chara.LEFT) {
			changeX = -change;
			if (chara.getAttackRightLeftDirection() == Chara.UP) {
				changeY = -change;
			} else if (chara.getAttackRightLeftDirection() == Chara.DOWN) {
				changeY = change;
			}
		} else {//(chara.direction == Chara.DOWN) {
			changeY = change;
		}

		anime.draw(
				chara.pX + offsetX + changeX,
				chara.pY + offsetY + changeY);
	}

	@Override
	public void update(int delta) {
		//攻撃開始直前の処理
		if (isAttackNow == false && attackInfoArray.size() > 0) {
			AttackInfo attackInfo = attackInfoArray.get(nowAttackIndex);
			charaAttack(attackInfo.attackChara, attackInfo.damageChara);
			isAttackNow = true;
		//攻撃開始後の処理
		} else if (isAttackNow == true && attackInfoArray.size() > 0){
			AttackInfo attackInfo = attackInfoArray.get(nowAttackIndex);
			Chara attackChara = attackInfo.attackChara;
			attackChara.setAttackTime(attackChara.getAttackTime() + 1);
			//攻撃時間が一定以上になったら次のキャラへ
			if (attackChara.getAttackTime() >= Chara.MAX_ATTACK_TIME) {
				//攻撃を受ける側にダメージを与える
				Chara damageChara = attackInfo.damageChara;
				damageChara.status.hp -= attackChara.status.power - damageChara.status.defence;

				//攻撃を受けた側のhpがなくなったら
				if (damageChara.status.hp < 0) {
					taskManager.characters.removeChara(damageChara);
					taskManager.checkCharaDieEvent(damageChara.id);
				}

				attackChara.setAttack(false);
				isAttackNow = false;
				nowAttackIndex++;
				//最後のキャラまで行ったら，もしくはダメージを受けたキャラが死んだら
				if (nowAttackIndex >= attackInfoArray.size() || damageChara.status.hp < 0) {
					AttackInfo standAttackInfo = attackInfoArray.get(0);
					standAttackInfo.attackChara.setStand(true);
					standAttackInfo.damageChara.resetState();
					attackInfoArray.clear();
					taskManager.taskEnd();
					taskManager.field.setCursorVisible(true);
				}
			}
		}
	}


	private void charaAttack(Chara chara, Chara damageChara) {
		taskManager.field.setCursorVisible(false);
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
