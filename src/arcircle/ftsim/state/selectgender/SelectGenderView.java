package arcircle.ftsim.state.selectgender;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SelectGenderState;

public class SelectGenderView implements Renderer {
	SelectGenderModel sgModel;
	private SelectGenderState sgState;
	private Image imageMale;
	private Image imageFemale;
	private Image flame;

	public SelectGenderView(SelectGenderModel sgModel, SelectGenderState sgState) {
		super();
		this.sgModel = sgModel;
		this.sgState = sgState;

		//稲井が画像ファイルをアップロードしていない？
//		try {
//			imageMale = new Image("./image/genderimage/male.png");
//			imageFemale = new Image("./image/genderimage/female.png");
//			flame = new Image("./image/genderimage/flame.png");
//		} catch (SlickException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(sgState.getFont());
		int messageWidth = sgState.getFont().getWidth(sgModel.message);
		g.drawString(sgModel.message,
				(FTSimulationGame.WIDTH - messageWidth) / 2, 100);

//		int imageGenderWidth = imageMale.getWidth();
//		int imageGenderHeight = imageMale.getHeight();
//		int flameWidth = flame.getWidth();
//		int flameHeight = flame.getHeight();
//
//		if (sgModel.gender == SelectGenderModel.MALE) {
//			g.drawImage(imageMale, FTSimulationGame.WIDTH / 4
//					- imageGenderWidth / 2, FTSimulationGame.HEIGHT / 3 * 2
//					- imageGenderHeight / 2);
//			g.drawImage(imageFemale, FTSimulationGame.WIDTH / 4 * 3
//					- imageGenderWidth / 2, FTSimulationGame.HEIGHT / 3 * 2
//					- imageGenderHeight / 2);
//			g.drawImage(flame, FTSimulationGame.WIDTH / 4 - flameWidth / 2,
//					FTSimulationGame.HEIGHT / 3 * 2 - flameHeight / 2);
//		} else if (sgModel.gender == SelectGenderModel.FEMALE) {
//			g.drawImage(imageMale, FTSimulationGame.WIDTH / 4
//					- imageGenderWidth / 2, FTSimulationGame.HEIGHT / 3 * 2
//					- imageGenderHeight / 2);
//			g.drawImage(imageFemale, FTSimulationGame.WIDTH / 4 * 3
//					- imageGenderWidth / 2, FTSimulationGame.HEIGHT / 3 * 2
//					- imageGenderHeight / 2);
//			g.drawImage(flame, FTSimulationGame.WIDTH / 4 * 3 - flameWidth / 2,
//					FTSimulationGame.HEIGHT / 3 * 2 - flameHeight / 2);
//		}

	}
}
