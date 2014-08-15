package arcircle.ftsim.state.inputname;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.InputNameState;

public class InputNameView implements Renderer {
	InputNameModel inModel;
	private InputNameState inState;
	public InputNameView(InputNameModel inModel, InputNameState inState) {
		super();
		this.inModel = inModel;
		this.inState = inState;
	}

	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.red);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(inState.getFont());
		g.drawString("Hello World!",200,200);
		g.drawImage(inState.sprite[0], inModel.x*10, inModel.y*10);
		
		int messageWidth = inState.getFont().getWidth(inModel.message);
		g.drawString(inModel.message, (FTSimulationGame.WIDTH - messageWidth) / 2,
				150);
	}
}


