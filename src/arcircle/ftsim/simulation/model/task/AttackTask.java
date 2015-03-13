package arcircle.ftsim.simulation.model.task;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.chara.battle.SupportInfo;
import arcircle.ftsim.simulation.model.AttackInfo;
import arcircle.ftsim.simulation.model.Field;

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
		attackInfoArray.add(new AttackInfo(attackChara, damageChara, expectBattleInfo.getFirstCharaBattleInfo()));

		attackInfoArray.add(new AttackInfo(damageChara, attackChara, expectBattleInfo.getSecondCharaBattleInfo()));

		//2回攻撃の処理
		if (expectBattleInfo.getFirstCharaBattleInfo().isTwiceAttack()) {
			attackInfoArray.add(new AttackInfo(attackChara, damageChara, expectBattleInfo.getFirstCharaBattleInfo()));
		} else if (expectBattleInfo.getSecondCharaBattleInfo().isTwiceAttack()) {
			attackInfoArray.add(new AttackInfo(damageChara, attackChara, expectBattleInfo.getSecondCharaBattleInfo()));
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
		Chara damageChara = attackInfoArray.get(nowAttackIndex).damageChara;
		//時間の変化によって，キャラクタの移動量を制御
		if (change <= 5 || change >= Chara.MAX_ATTACK_TIME - 5) {
			change = 0;
		} else if (change < Chara.MAX_ATTACK_TIME / 2) {
			change -= 5;
			//あたってない時はキャラが透ける
			if (!attackInfoArray.get(nowAttackIndex).isHit()) {
				damageChara.setAlpha((100 - change * 3) / 100.0f);
			} else {
				//あたってる時は色が暗くなる
				damageChara.setColor((100 - change * 1) / 100.0f);
			}
		} else if (change >= Chara.MAX_ATTACK_TIME / 2) {
			change = Chara.MAX_ATTACK_TIME - change - 5;
			//あたってない時はキャラが透ける
			if (!attackInfoArray.get(nowAttackIndex).isHit()) {
				damageChara.setAlpha(change * 3);
			} else {
				//あたってる時は色が暗くなる
				damageChara.setColor(change * 1);
			}
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

		//必殺の時のみダメージを受けてるキャラクタを左右に揺らす
		if (attackInfoArray.get(nowAttackIndex).isDead()) {
			if (22 <= chara.getAttackTime() && chara.getAttackTime() <= 29) {
				int mod2 = chara.getAttackTime() % 8;
				int sign = mod2 <= 3 ? 1 : -1;
				damageChara.pX += (sign * 2f) * Integer.signum(changeY);
				damageChara.pY += (sign * 2f) * Integer.signum(changeX);
			} else if (chara.getAttackTime() >= 36) {
				damageChara.pX = damageChara.x * Field.MAP_CHIP_SIZE;
				damageChara.pY = damageChara.y * Field.MAP_CHIP_SIZE;
			}
		}
	}

	@Override
	public void update(int delta) {
		//攻撃開始直前の処理
		if (isAttackNow == false && attackInfoArray.size() > 0) {
			AttackInfo attackInfo = attackInfoArray.get(nowAttackIndex);
			charaAttackPrepareDir(attackInfo.attackChara, attackInfo.damageChara);
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
				damageChara.setAlpha(1.0f);

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


	private void charaAttackPrepareDir(Chara chara, Chara damageChara) {
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
