package arcircle.ftsim.state.simgame;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SimGameState;

public class SimGameView implements Renderer {
	SimGameModel sgModel;
	SimGameState sgState;

	public SimGameView(SimGameModel sgModel, SimGameState simGameState) {
		this.sgModel = sgModel;
		this.sgState = simGameState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(sgState.getFont());
		int messageWidth = sgState.getFont().getWidth("S i m G a m e");
		g.drawString("S i m G a m e", (FTSimulationGame.WIDTH - messageWidth) / 2,
				150);

	}

}
