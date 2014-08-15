package arcircle.ftsim.state.inputname;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.state.InputNameState;

public class InputNameModel implements KeyListner {

	private InputNameState inState;

	public int x = 100;
	public int y = 100;
	public String message = "S e l e c t  Y o u r N a m e ";
	private String charactor = "";
	public int CursorX = 0;  //カーソルが選択している座標Xを格納
	public int CursorY = 0;  //カーソルが選択している座標Yを格納
	public char[][] cursorchar = new char [][] {
			{'あ','か','さ','た','な','は','ま','や','ら','わ','ぁ','っ','が','ざ','だ','ば','ぱ','ー'},
			{'い','き','し','ち','に','ひ','み','　','り','　','ぃ','ゃ','ぎ','じ','ぢ','び','ぴ','～'},
			{'う','く','す','つ','ぬ','ふ','む','ゆ','る','を','ぅ','ゅ','ぐ','ず','づ','ぶ','ぷ','☆'},
			{'え','け','せ','て','ね','へ','め','　','れ','　','ぇ','ょ','げ','ぜ','で','べ','ぺ','卍'},
			{'お','こ','そ','と','の','ほ','も','よ','ろ','ん','ぉ','ゎ','ご','ぞ','ど','ぼ','ぽ','⑦'}
	};

	public InputNameModel(InputNameState inputNameState) {
		super();
		this.inState = inputNameState;
	}


	@Override
	public void keyInput(KeyInput keyInput) {
		if(keyInput.isKeyDown(Input.KEY_Z)) {
			if(CursorX == 17 && CursorY == 4 ){
				inState.nextState();							//決定キーが押されたので確定
			}
			if(CursorX == 17 && CursorY == 2 ){
				charactor = "";										//クリアキーが押されたので名前クリア
			}
			else{
				addGetCharFromCursor();
			}
		}
		if(keyInput.isKeyDown(Input.KEY_UP)) {
			if(CursorY>0){CursorY = CursorY-1;}
		}
		if(keyInput.isKeyDown(Input.KEY_DOWN)) {
			if(CursorY<4){CursorY = CursorY+1;}
		}
		if(keyInput.isKeyDown(Input.KEY_LEFT)) {
			if(CursorX>0){CursorX = CursorX-1;}
		}
		if(keyInput.isKeyDown(Input.KEY_RIGHT)) {
			if(CursorX<17){CursorX = CursorX+1;}
		}
	}

	/**
	 * カーソルの位置CursorY,CursorXから文字に変換し、String charactorに6文字まで累加算する
	 */
	public void addGetCharFromCursor(){
		if(charactor.length()>=6){return;}				//名前は6文字まで
		charactor = charactor + cursorchar[CursorY][CursorX] ;
		return;
	}

	/**
	 * 外部から主人公の名前を得るためのメソッド　返り血はString型
	 */
	public String getName(){
		return charactor;
	}

}
