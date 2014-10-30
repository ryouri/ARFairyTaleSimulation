package arcircle.ftsim.simulation.model;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.simulation.chara.Chara;

public class SubInfoWindow implements Renderer{
	public static int START_WIDTH  = 800;
	public static int START_HEIGHT = 0;
	public static int WIDTH  = 320;
	public static int HEIGHT = 640;

	private Image backGround;

	private Field field;

	public SubInfoWindow(Field field) {
		super();

		this.field = field;

		try {
			backGround = new Image("image/subInfoWindow/subInfoWindow.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(backGround, START_WIDTH, START_HEIGHT);

		//情報を描画する対象のCharaを取得
		//TODO: 下で取得したキャラの情報を描画してくれればOK
		Chara renderInfoChara = field.getSelectedChara();

	}
}
