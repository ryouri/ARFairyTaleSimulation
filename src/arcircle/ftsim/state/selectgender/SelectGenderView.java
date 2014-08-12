package arcircle.ftsim.state.selectgender;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SelectGenderState;

public class SelectGenderView implements Renderer{
	SelectGenderModel sgModel;
	private SelectGenderState sgState;
	public SelectGenderView(SelectGenderModel sgModel, SelectGenderState sgState) {
		super();
		this.sgModel = sgModel;
		this.sgState = sgState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(sgState.getFont());
		int messageWidth = sgState.getFont().getWidth(sgModel.message);
		g.drawString(sgModel.message, (FTSimulationGame.WIDTH - messageWidth) / 2,
				150);
	}
}
