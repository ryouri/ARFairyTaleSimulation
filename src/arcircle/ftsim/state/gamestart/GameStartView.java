package arcircle.ftsim.state.gamestart;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.GameStartState;

public class GameStartView implements Renderer{
	GameStartModel gsModel;
	private GameStartState gsState;
	public GameStartView(GameStartModel gsModel, GameStartState gsState) {
		super();
		this.gsModel = gsModel;
		this.gsState = gsState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(gsState.getFont());
		int titleWidth = gsState.getFont().getWidth(gsModel.title);
		g.drawString(gsModel.title, (FTSimulationGame.WIDTH - titleWidth) / 2,
				150);
		g.drawRect(gsModel.x, gsModel.y, 64, 64);
	}
}
