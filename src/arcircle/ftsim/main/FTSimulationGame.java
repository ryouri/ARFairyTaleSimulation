package arcircle.ftsim.main;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.state.StateBasedGame;

import arcircle.JapaneseFont.JapaneseFontGenerater;
import arcircle.ftsim.state.GameStartState;
import arcircle.ftsim.state.InputNameState;
import arcircle.ftsim.state.SelectGenderState;
import arcircle.ftsim.state.StateConst;

public class FTSimulationGame extends StateBasedGame {
	public static final int WIDTH  = 960;
	public static final int HEIGHT = 960;
	public static final int FPS = 60;
	public static final String GAMENAME = "FairyTaleSimulation";

	public static UnicodeFont font;

	public FTSimulationGame(String name) {
		super(name);
		this.addState(new GameStartState(StateConst.GAME_START));
		this.addState(new SelectGenderState(StateConst.SELECT_GENDER));
		this.addState(new InputNameState(StateConst.INPUT_NAME));
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		this.getState(StateConst.GAME_START).init(container, this);
		this.getState(StateConst.SELECT_GENDER).init(container, this);
		this.getState(StateConst.INPUT_NAME).init(container, this);
		//this.enterState(StateConst.GAME_START);

		//コンストラクタで実行するとエラー発生するよー
		font = JapaneseFontGenerater.generateFont(20, false, false, null);
	}
}
