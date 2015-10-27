package arcircle.ftsim.simulation.model.task;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.model.Field;

public class WinTask extends Task {
	Field field;
	String message;
	Image back;
	int count;

	public WinTask(TaskManager taskManager, Field field) {
		super(taskManager);
		this.field = field;
		String path = "./image/";
		message = "You Win!!";
		count = 0;
		try {
			back = new Image(path + "gameover.png");
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	@Override
	public void render(Graphics g, int offsetX, int offsetY, int firstTileX,
			int lastTileX, int firstTileY, int lastTileY) {
		g.drawImage(back,  (FTSimulationGame.WIDTH - back.getWidth()) / 2, (FTSimulationGame.HEIGHT - back.getHeight()) / 2);
		UnicodeFont font = FTSimulationGame.font;
		g.setFont(font);
		g.drawString(message, (FTSimulationGame.WIDTH - font.getWidth(message)) / 2, (FTSimulationGame.HEIGHT - font.getHeight(message)) / 2);
	}

	@Override
	public void update(int delta) {
		//勝利！
		//talkStateへ
		count++;
		if (count > 100) {
			field.saveData();
			field.getSgModel().nextState();
		}
	}
}
