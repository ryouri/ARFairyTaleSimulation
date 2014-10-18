package arcircle.ftsim.state.selectstory;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SelectStoryState;

public class SelectStoryView implements Renderer {
	SelectStoryModel ssModel;
	private SelectStoryState ssState;
	private Image[] storyTitleImg = new Image[6];
	private int[][] storyTitlePos = new int[6][2];
	private Image flame;
	private static final String imagePath = "./image";

	public SelectStoryView(SelectStoryModel ssModel, SelectStoryState ssState) {
		super();
		this.ssModel = ssModel;
		this.ssState = ssState;

		try {
			for(int i = 0 ; i < 6 ; i++){
				storyTitleImg[i] = new Image(imagePath + "/storyTitle0" + (i+1) + ".png");
			}
			flame = new Image("./image/frame.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		for(int i = 0 ; i < 6 ; i++){
			storyTitlePos[i][0] = 60 + (i % 3) * 350;
			storyTitlePos[i][1] = 100 + (i / 3) * 270;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(ssState.getFont());
		int messageWidth = ssState.getFont().getWidth(ssModel.message);
		g.drawString(ssModel.message, (FTSimulationGame.WIDTH - messageWidth) / 2, 30);

		int selectStory = ssModel.story;
		int flamePosX = 50 + (selectStory % 3) * 350;
		int flamePosY = 90 + (selectStory / 3) * 270;

		g.drawImage(flame, flamePosX, flamePosY);

		for(int i = 0 ; i < 6 ; i++){
			g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1]);
		}
	}
}
