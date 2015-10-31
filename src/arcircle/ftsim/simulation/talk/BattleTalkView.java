package arcircle.ftsim.simulation.talk;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.ftsim.state.TalkManagerState;
import arcircle.ftsim.state.talk.TalkModel;
import arcircle.ftsim.state.talk.TalkView;

public class BattleTalkView extends TalkView {

	public BattleTalkView(TalkModel tModel, TalkManagerState tState) {
		super(tModel, tState);
	}

	@Override
	protected void drawBackGround(GameContainer container, StateBasedGame game,
			Graphics g) {
		//戦闘画面では背景は描画しない
	}
}
