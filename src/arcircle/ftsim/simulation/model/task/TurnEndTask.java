package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;

public class TurnEndTask extends Task {

	private Characters characters;
	private int camp;

	public TurnEndTask(TaskManager taskManager, Characters characters, int camp) {
		super(taskManager);
		this.characters = characters;
		this.camp = camp;
	}


	@Override
	/**
	 * 描画
	 */
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	/**
	 * 更新
	 */
	public void update(int delta) {
		//このターンエンドを画像描画後に呼び出す
		turnEnd();
	}

	private void turnEnd() {
		if (camp == Chara.CAMP_FRIEND) {
			characters.getField().changeTurnEnemy();
			characters.standForAllCampChara(Chara.CAMP_FRIEND);
			taskManager.taskEnd();
		} else if (camp == Chara.CAMP_ENEMY) {
			characters.getField().changeTurnFriend();
			characters.standForAllCampChara(Chara.CAMP_ENEMY);
			taskManager.taskEnd();
		}
	}
}
