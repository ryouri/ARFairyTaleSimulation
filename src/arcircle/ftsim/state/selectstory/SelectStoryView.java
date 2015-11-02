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
	private Image[] storyTitleImg = new Image[SelectStoryModel.STORY_NUM];
	private int[][] storyTitlePos = new int[SelectStoryModel.STORY_NUM][2];
	private Image backGroundImg;
	private Image frame;
	private static final String imagePath = "./image";
	private Color filterColor = new Color(0.75f,0.75f,0.75f,0.5f);


	public SelectStoryView(SelectStoryModel ssModel, SelectStoryState ssState) {
		super();
		this.ssModel = ssModel;

		// 画像のロード
		try {
			for(int i = 0 ; i < SelectStoryModel.STORY_NUM; i++){
				storyTitleImg[i] = new Image(imagePath + "/storyTitle0" + (i+1) + ".png");
			}
			frame = new Image("./image/frame.png");
			backGroundImg = new Image("./image/StorySelect.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}

		// 位置の設定
		for(int i = 0 ; i < SelectStoryModel.STORY_NUM; i++){
			// ボスステージの場合
			if (i == SelectStoryModel.STORY_NUM - 1) {
				storyTitlePos[i][0] = (FTSimulationGame.WIDTH - storyTitleImg[i].getWidth()) / 2;
				storyTitlePos[i][1] = (FTSimulationGame.HEIGHT - storyTitleImg[i].getHeight()) / 2;
			} else {
				storyTitlePos[i][0] = 60 + (i % 3) * 350;
				storyTitlePos[i][1] = 120 + (i / 3) * 270;
			}
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		g.drawImage(backGroundImg,0,0);

		int selectStory = ssModel.story;
		int[] flamePos = new int[2];
		flamePos[0] = storyTitlePos[selectStory][0] - 10;
		flamePos[1] = storyTitlePos[selectStory][1] - 10;

		// フレームの描画
		g.drawImage(frame, flamePos[0], flamePos[1]);

		// 各ストーリーのタイトル画像の描画
		for(int i = 0 ; i < SelectStoryModel.STORY_NUM - 1 ; i++){ // boss stageの分 引く
			if(ssModel.isClearStage[i]){
				g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1], filterColor);
			}else{
				g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1]);
			}
		}

		// ボスステージのタイトル画像の描画
		if (ssModel.onlyBossStage) {
			int i = SelectStoryModel.STORY_NUM - 1;
			g.drawImage(storyTitleImg[i] , storyTitlePos[i][0], storyTitlePos[i][1]);
		}
	}
}
