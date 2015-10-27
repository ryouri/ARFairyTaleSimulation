package arcircle.ftsim.simulation.model.task.lose;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;

public class LoseTaskView implements Renderer {
	LoseTaskModel ltModel;

	public LoseTaskView(LoseTaskModel ltModel) {
		this.ltModel = ltModel;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		// 暗転の暗さ具合の更新
		if (ltModel.alpha < 0.8f) {
			ltModel.alpha += 0.02;
		} else {
			ltModel.isActive = true;
		}


		// 暗転の描画
		g.setColor(new Color(0f, 0f, 0f, ltModel.alpha));
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		// ゲームオーバーの表示
		if (ltModel.isActive) {
			g.drawImage(ltModel.back,  (FTSimulationGame.WIDTH - ltModel.back.getWidth()) / 2, (FTSimulationGame.HEIGHT - ltModel.back.getHeight()) / 2);
			UnicodeFont font = FTSimulationGame.font;
			g.setFont(font);
			g.drawString(ltModel.messageGameover, (FTSimulationGame.WIDTH - font.getWidth(ltModel.messageGameover)) / 2, (FTSimulationGame.HEIGHT - font.getHeight(ltModel.messageGameover)) / 2 - 20);
			g.drawString(ltModel.messageRestart, (FTSimulationGame.WIDTH - font.getWidth(ltModel.messageRestart)) / 2, (FTSimulationGame.HEIGHT - font.getHeight(ltModel.messageRestart)) / 2 + 20);
		}
	}

}
