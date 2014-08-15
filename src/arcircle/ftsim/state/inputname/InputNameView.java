package arcircle.ftsim.state.inputname;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.InputNameState;

public class InputNameView implements Renderer {
	InputNameModel inModel;
	private InputNameState inState;
	private Image NameBack;
	private String Cursor;

	public InputNameView(InputNameModel inModel, InputNameState inState) {
		super();
		this.inModel = inModel;
		this.inState = inState;
		this.NameBack = null;

		try {
			this.NameBack = new Image("./image/InputNameHiragana.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

		g.setColor(Color.white);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(inState.getFont());
		g.drawImage(NameBack, 0, 0);

		g.drawImage(inState.sprite[0], 100+inModel.CursorX*30,  100 + inModel.CursorY*30);

		int messageWidth = inState.getFont().getWidth(inModel.message);
		g.drawString(Cursor = inModel.message, (FTSimulationGame.WIDTH - messageWidth) / 2, 150);

		g.drawString(inModel.getcursor(), (FTSimulationGame.WIDTH -200), 600);
	}
}


