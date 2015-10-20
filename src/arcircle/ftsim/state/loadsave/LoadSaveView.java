package arcircle.ftsim.state.loadsave;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.LoadSaveState;

public class LoadSaveView implements Renderer{
	LoadSaveModel lsModel;
	private LoadSaveState lsState;

	public LoadSaveView(LoadSaveModel lsModel, LoadSaveState lsState){
		super();
		this.lsModel = lsModel;
		this.lsState = lsState;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(lsState.getFont());
		int messageWidth = lsState.getFont().getWidth(lsModel.message);
		g.drawString(lsModel.message,
				(FTSimulationGame.WIDTH - messageWidth) / 2, 100);
	}
}
