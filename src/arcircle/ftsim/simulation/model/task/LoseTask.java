package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.model.Field;

public class LoseTask extends Task{
	Field field;
	float alpha;
	boolean isActive;
	String message;

	public LoseTask(TaskManager taskManager, Field field) {
		super(taskManager);
		this.field = field;
		alpha = 0f;
		isActive = false;
		message = "死んでしまうとはなさけない";
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {

		// 暗転の描画
		g.setColor(new Color(0f, 0f, 0f, alpha));
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		if (isActive) {

		}

	}

	@Override
	public void update(int delta) {
		// 暗転の暗さ具合の更新
		if (alpha < 0.8f) {
			alpha += 0.02;
		} else {
			isActive = true;
		}
	}
}
