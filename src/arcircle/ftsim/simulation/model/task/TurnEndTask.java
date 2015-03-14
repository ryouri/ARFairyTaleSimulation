package arcircle.ftsim.simulation.model.task;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.model.Characters;

public class TurnEndTask extends Task {

	private Characters characters;
	private int camp;
	private Timer timer;
	private TimerTask task;
	private boolean timer_start_flag;

	public TurnEndTask(TaskManager taskManager, Characters characters, int camp) {
		super(taskManager);
		this.characters = characters;
		this.camp = camp;
		timer = new Timer();
		task = new TETask();
		timer_start_flag = false;
	}


	@Override
	/**
	 * 描画
	 */
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		//Font font = new Font("Serif", Font.PLAIN, 10);
		g.setFont(FTSimulationGame.font);
		g.drawString("Tern End", offsetX, offsetY);
	}

	@Override
	/**
	 * 更新
	 */
	public void update(int delta) {
		//このターンエンドを画像描画後に呼び出す
		if (!timer_start_flag){
			timer.schedule(task, 3L);
			timer_start_flag = true;
			System.out.println("よばれてる！");
		}
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

	private class TETask extends TimerTask {
		public void run() {
			turnEnd();
		}
	}
}
