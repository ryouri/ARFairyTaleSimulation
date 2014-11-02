package arcircle.ftsim.state.selectstory;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.renderer.Renderer;
import arcircle.ftsim.state.SelectStoryState;

public class SelectStoryView implements Renderer {
	SelectStoryModel ssModel;
	private SelectStoryState ssState;
	private Image[] storyTitleImg = new Image[6];
	private int[][] storyTitlePos = new int[6][2];
	private Image backGroundImg;
	private Image frame;
	private static final String imagePath = "./image";
	private Color filterColor = new Color(0.75f,0.75f,0.75f,0.5f);
	 
	
	public SelectStoryView(SelectStoryModel ssModel, SelectStoryState ssState) {
		super();
		this.ssModel = ssModel;
		this.ssState = ssState;
		
		
		
		try {
			for(int i = 0 ; i < ssState.storyNum ; i++){
				storyTitleImg[i] = new Image(imagePath + "/storyTitle0" + (i+1) + ".png");
			}
			frame = new Image("./image/frame.png");
			backGroundImg = new Image("./image/StorySelect.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		for(int i = 0 ; i < ssState.storyNum ; i++){
			storyTitlePos[i][0] = 60 + (i % 3) * 350;
			storyTitlePos[i][1] = 120 + (i / 3) * 270;
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(backGroundImg,0,0);

		int selectStory = ssModel.story;
		int flamePosX = 50 + (selectStory % 3) * 350;
		int flamePosY = 110 + (selectStory / 3) * 270;

		g.drawImage(frame, flamePosX, flamePosY);

		for(int i = 0 ; i < ssState.storyNum ; i++){
			if(ssState.isClearStage[i]){
				g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1], filterColor);
			}else{
				g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1]);
			}
		}
	}
}
