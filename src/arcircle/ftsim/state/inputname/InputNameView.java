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
	public Image InputNameHiragana;
	public Image InputNameKatakana;
	public Image InputNameKigou;
	public Image NameCursor;

	public InputNameView(InputNameModel inModel, InputNameState inState) {
		super();
		this.inModel = inModel;
		this.inState = inState;
		this.InputNameHiragana = null;
		this.InputNameKatakana = null;
		this.InputNameKigou = null;
		this.NameCursor = null;

		try {
			this.InputNameHiragana = new Image("./image/InputNameHiragana.png");	//ひらがな入力のimage登録
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			this.InputNameKatakana = new Image("./image/InputNameKatakana.png");//カナ入力のimage登録
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			this.InputNameKigou = new Image("./image/InputNameKigou.png");//記号入力のimage登録
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			this.NameCursor = new Image("./image/Cursor.png");//カーソルののimage登録
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) {

		g.setColor(Color.white);
		g.fillRect(0, 0, FTSimulationGame.WIDTH, FTSimulationGame.HEIGHT);

		g.setColor(Color.black);
		g.setFont(inState.getInputNameFont());

		switch(inModel.getcharactorOption()){										//charactorOptionの値で文字入力の背景変更
		case 0: g.drawImage(InputNameHiragana , 0, 0); break;
		case 1: g.drawImage(InputNameKatakana , 0, 0); break;
		case 2: g.drawImage(InputNameKigou , 0, 0); break;
		}

		g.drawImage(NameCursor, 20+inModel.CursorX*56,  212 + inModel.CursorY*80);
		//g.drawImage(inState.sprite[0], 22+inModel.CursorX*56,  220 + inModel.CursorY*80);

		int messageWidth = inState.getFont().getWidth(inModel.message);

		for(int i=0; i<8; i++){
		g.drawString(String.valueOf(inModel.cursorcharArrey[i]), (105+i*120),77);
		}
	}
}


