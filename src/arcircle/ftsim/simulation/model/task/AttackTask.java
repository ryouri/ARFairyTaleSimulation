package arcircle.ftsim.simulation.model.task;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.chara.battle.ExpectBattleInfo;
import arcircle.ftsim.simulation.chara.battle.SupportInfo;
import arcircle.ftsim.simulation.field.LoadField;
import arcircle.ftsim.simulation.item.Weapon;
import arcircle.ftsim.simulation.model.AttackInfo;
import arcircle.ftsim.simulation.sound.SoundManager;

public class AttackTask extends Task {
	ArrayList<AttackInfo> attackInfoArray;
	int nowAttackIndex;
	boolean isAttackNow;

	public AttackTask(TaskManager taskManager, Chara attackChara, Chara damageChara, Point attackPoint) {
		super(taskManager);

		calcAttackInfo(attackChara, damageChara, attackPoint);
	}

	private void calcAttackInfo(Chara attackChara, Chara damageChara, Point attackPoint) {
		ExpectBattleInfo expectBattleInfo =
				new ExpectBattleInfo(attackChara, attackChara.getEquipedWeapon(), new SupportInfo(),
						damageChara, damageChara.getEquipedWeapon(), new SupportInfo(), attackPoint);

		this.attackInfoArray = new ArrayList<AttackInfo>();
		attackInfoArray.add(new AttackInfo(attackChara, damageChara, expectBattleInfo.getFirstCharaBattleInfo()));

		if (expectBattleInfo.getSecondCharaBattleInfo().isAttackable()) {
			attackInfoArray.add(new AttackInfo(damageChara, attackChara, expectBattleInfo.getSecondCharaBattleInfo()));
		}

		//2回攻撃の処理
		if (expectBattleInfo.getFirstCharaBattleInfo().isTwiceAttack()) {
			attackInfoArray.add(new AttackInfo(attackChara, damageChara, expectBattleInfo.getFirstCharaBattleInfo()));
		} else if (expectBattleInfo.getSecondCharaBattleInfo().isTwiceAttack()
				&& expectBattleInfo.getSecondCharaBattleInfo().isAttackable()) {
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
		AttackInfo nowAttackInfo = attackInfoArray.get(nowAttackIndex);
		Chara damageChara = attackInfoArray.get(nowAttackIndex).damageChara;
		//時間の変化によって，キャラクタの移動量を制御
		if (change <= 5 || change >= Chara.MAX_ATTACK_TIME - 5) {
			change = 0;
		} else if (change < Chara.MAX_ATTACK_TIME / 2) {
			change -= 5;
			//あたってない時はキャラが透ける
			if (!nowAttackInfo.isHit()) {
				damageChara.setAlpha((100 - change * 3) / 100.0f);
			} else {
				//あたってる時は色が暗くなる
				float color = (100 - change * 1) / 100.0f;
				damageChara.setColor(new Color(color, color, color, 1.0f));
			}
		} else if (change >= Chara.MAX_ATTACK_TIME / 2) {
			change = Chara.MAX_ATTACK_TIME - change - 5;
			//あたってない時はキャラが透ける
			if (!nowAttackInfo.isHit()) {
				damageChara.setAlpha(change * 3);
			} else {
				//あたってる時は色が暗くなる
				float color = (change * 1) / 100.0f;
				damageChara.setColor(new Color(color, color, color, 1.0f));
			}
		}

		Animation anime = null;
		if (chara.direction == Chara.UP) {
			anime = taskManager.characters.upAttackAnimeMap.get(chara.getFolderName());
		} else if (chara.direction == Chara.RIGHT) {
			anime = taskManager.characters.rightAttackAnimeMap.get(chara.getFolderName());
		} else if (chara.direction == Chara.LEFT) {
			anime = taskManager.characters.leftAttackAnimeMap.get(chara.getFolderName());
		} else {//(chara.direction == Chara.DOWN) {
			anime = taskManager.characters.downAttackAnimeMap.get(chara.getFolderName());
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
				damageChara.pX = damageChara.x * LoadField.MAP_CHIP_SIZE;
				damageChara.pY = damageChara.y * LoadField.MAP_CHIP_SIZE;
			}
		}
	}

	@Override
	public void update(int delta) {
		//攻撃開始直前の処理
		if (isAttackNow == false && attackInfoArray.size() > 0) {
			AttackInfo nowAttackInfo = attackInfoArray.get(nowAttackIndex);
			charaAttackPrepareDir(nowAttackInfo.attackChara, nowAttackInfo.damageChara);
			isAttackNow = true;
		//攻撃開始後の処理
		} else if (isAttackNow == true && attackInfoArray.size() > 0){
			AttackInfo nowAttackInfo = attackInfoArray.get(nowAttackIndex);
			Chara attackChara = nowAttackInfo.attackChara;
			attackChara.setAttackTime(attackChara.getAttackTime() + 1);

			Chara damageChara = nowAttackInfo.damageChara;

			//攻撃時間が一定以上の時，攻撃処理
			//攻撃の処理
			if (!nowAttackInfo.isProcessed() && attackChara.getAttackTime() >= Chara.MAX_ATTACK_TIME / 2) {
				//あたった時のみ処理
				if (nowAttackInfo.isHit()) {
					//ダメージの取得
					int damage = nowAttackInfo.charaBattleInfo.getDamage();
					//必殺発動ならダメージ3倍
					if (nowAttackInfo.isDead()) {
						damage *= 3;
						//必殺攻撃
						taskManager.field.getSoundManager().playSound(SoundManager.SOUND_ATTACK_DEAD);
					} else {
						//通常攻撃の音
						taskManager.field.getSoundManager().playSound(SoundManager.SOUND_ATTACK);
					}
					//攻撃を受ける側にダメージを与える
					int nextHp = damageChara.status.getHp() - damage;
					damageChara.status.setHp(nextHp);

					Weapon attackWeapon = null;
					if (attackChara.getEquipedWeapon() instanceof Weapon) {
						attackWeapon = (Weapon) attackChara.getEquipedWeapon();
					} else {
						System.err.println("攻撃出来てるのに、Weaponを装備してない");
						System.exit(1);
					}

					//TODO:エフェクト
					int effectX = damageChara.pX;
					int effectY = damageChara.pY;
					taskManager.occurEffect(effectX, effectY, attackWeapon.effectName);
				} else {
					//外れた音
					taskManager.field.getSoundManager().playSound(SoundManager.SOUND_AVOID);
				}
				//攻撃処理完了
				nowAttackInfo.setProcessed(true);
			}

			//攻撃時間が一定以上になったら次のキャラへ
			if (attackChara.getAttackTime() >= Chara.MAX_ATTACK_TIME) {
				attackChara.setAttackTime(0);
				damageChara.setAlpha(1.0f);

				//攻撃を受けた側のhpがなくなったら
				if (damageChara.status.getHp() <= 0) {
					taskManager.addCharaDieTask(damageChara);
				}

				attackChara.setAttack(false);
				isAttackNow = false;
				nowAttackIndex++;
				//最後のキャラまで行ったら，もしくはダメージを受けたキャラが死んだらタスクの終了処理
				if (nowAttackIndex >= attackInfoArray.size() || damageChara.status.getHp() <= 0) {
					AttackInfo standAttackInfo = attackInfoArray.get(0);
					standAttackInfo.attackChara.setStand(true);
					//生きている時のみ状態を戻す
					if (damageChara.status.getHp() > 0) {
						standAttackInfo.damageChara.resetState();
					};
					attackInfoArray.clear();
					taskManager.taskEnd();

					//経験値の追加処理を入れる
					//attackCharaに経験値
					if (attackChara.getCamp() == Chara.CAMP_FRIEND && attackChara.status.getHp() > 0) {
						if (damageChara.status.getHp() > 0) {
							taskManager.addExpTask(attackChara, ATTACK_EXP);
						} else {
							taskManager.addExpTask(attackChara, KILL_EXP);
						}
					//damageCharaに経験値
					} else if (damageChara.getCamp() == Chara.CAMP_FRIEND && damageChara.status.getHp() > 0) {
						if (attackChara.status.getHp() > 0) {
							taskManager.addExpTask(damageChara, ATTACK_EXP);
						} else {
							taskManager.addExpTask(damageChara, KILL_EXP);
						}
					}
				}
			}
		}
	}

	public static final int ATTACK_EXP = 25;
	public static final int KILL_EXP = 50;


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
