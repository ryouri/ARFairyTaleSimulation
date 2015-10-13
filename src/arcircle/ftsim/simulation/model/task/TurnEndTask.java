package arcircle.ftsim.simulation.model.task;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Chara;
import arcircle.ftsim.simulation.field.LoadField;
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
		taskManager.field.getCursor().isVisible = false;
	}


	public static final String TURN_END = "TURN END";

	@Override
	/**
	 * 描画
	 */
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		//Font font = new Font("Serif", Font.PLAIN, 10);
		UnicodeFont font = FTSimulationGame.font;
		g.setFont(font);
		g.drawString(TURN_END,
				(LoadField.MAP_VIEW_WIDTH / 2) - font.getWidth(TURN_END),
				(LoadField.MAP_VIEW_HEIGHT / 2) - font.getHeight(TURN_END));
	}

	@Override
	/**
	 * 更新
	 */
	public void update(int delta) {
		//このターンエンドを画像描画後に呼び出す
		if (!timer_start_flag){
			timer.schedule(task, 500L);
			timer_start_flag = true;
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
		taskManager.field.getCursor().isVisible = true;
	}

	private class TETask extends TimerTask {
		public void run() {
			turnEnd();
		}
	}
}
