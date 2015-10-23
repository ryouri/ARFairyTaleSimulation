package arcircle.ftsim.state.staffroll;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.StaffRollState;

public class StaffRollView implements Renderer {
	StaffRollModel srModel;
	private StaffRollState srState;

	public StaffRollView(StaffRollModel srModel, StaffRollState srState){
		super();
		this.srModel = srModel;
		this.srState = srState;
	}
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

	}

}
