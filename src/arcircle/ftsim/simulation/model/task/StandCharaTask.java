package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;

import arcircle.ftsim.simulation.chara.Chara;

public class StandCharaTask extends Task {
	Chara chara;

	public StandCharaTask(TaskManager taskManager, Chara chara) {
		super(taskManager);
		this.chara = chara;
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void update(int delta) {
		chara.setStand(true);
		taskManager.taskEnd();
	}
}
