package arcircle.ftsim.state.gamestart;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.GameStartState;

public class GameStartView implements Renderer{
	GameStartModel gsModel;
	private GameStartState gsState;

	private Image title;	//背景画像

	public GameStartView(GameStartModel gsModel, GameStartState gsState) {
		super();
		this.gsModel = gsModel;
		this.gsState = gsState;
		try{
			title = new Image("./Image/title.png");	//背景画像の読み込み
		}catch (SlickException ex) {
	        ex.printStackTrace();
	    }
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.cyan);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);
		g.drawImage(title, 0, 0);
	}
}
