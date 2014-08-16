package arcircle.ftsim.state.inputname;

import org.newdawn.slick.Input;

import arcircle.ftsim.keyinput.KeyInput;
import arcircle.ftsim.keyinput.KeyListner;
import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.state.InputNameState;

public class InputNameModel implements KeyListner {
	private InputNameState inState;


	public String message = "S e l e c t  Y o u r N a m e ";
	private String charactor = null;

	/**
	 * ひらがな=0,  カタカナ=1, 記号=2
	 */
	public int charactorOption =0;
	public char cursorcharArrey[] = new char [8];	//入力された名前をcharで保管、最大8文字
	public int cursorcharArreyLength  = 0;					//長さ
	public int CursorX = 0;  //カーソルが選択している座標Xを格納
	public int CursorY = 0;  //カーソルが選択している座標Yを格納
	private char[][][] cursorchar = new char [][][] {
			{
			{'あ','か','さ','た','な','は','ま','や','ら','わ','ぁ','っ','が','ざ','だ','ば','ぱ','ー'},
			{'い','き','し','ち','に','ひ','み','　','り','ー','ぃ','ゃ','ぎ','じ','ぢ','び','ぴ','～'},
			{'う','く','す','つ','ぬ','ふ','む','ゆ','る','を','ぅ','ゅ','ぐ','ず','づ','ぶ','ぷ','〇'},
			{'え','け','せ','て','ね','へ','め','　','れ','～','ぇ','ょ','げ','ぜ','で','べ','ぺ','〇'},
			{'お','こ','そ','と','の','ほ','も','よ','ろ','ん','ぉ','ゎ','ご','ぞ','ど','ぼ','ぽ','〇'}
			},
			{
			{'ア','カ','サ','タ','ナ','ハ','マ','ヤ','ラ','ワ','ァ','ッ','ガ','ザ','ダ','バ','パ','ー'},
			{'イ','キ','シ','チ','ニ','ヒ','ミ','　','リ','ー','ィ','ャ','ギ','ジ','ヂ','ビ','ピ','～'},
			{'ウ','ク','ス','ツ','ネ','フ','ム','ユ','ル','ヲ','ゥ','ュ','グ','ズ','ヅ','ブ','プ','〇'},
			{'エ','け','セ','テ','ネ','ヘ','メ','　','レ','～','ェ','ョ','ゲ','ゼ','デ','ベ','ペ','〇'},
			{'オ','コ','ソ','ト','ノ','ホ','モ','ヨ','ロ','ン','ォ','ヮ','ゴ','ゾ','ド','ボ','ポ','〇'}
			},
			{
			{'１','２','３','４','５','６','７','８','９','０','！','？','＠','＃','＄','＾','＆','〇'},
			{'Ａ','Ｂ','Ｃ','Ｄ','Ｅ','Ｆ','Ｇ','Ｈ','Ｉ','Ｊ','Ｋ','Ｌ','Ｍ','Ｎ','Ｏ','Ｐ','Ｑ','〇'},
			{'Ｒ','Ｓ','Ｔ','Ｕ','Ｖ','Ｗ','Ｘ','Ｙ','Ｚ','ａ','ｂ','ｃ','ｄ','ｅ','ｆ','ｇ','ｈ','〇'},
			{'ｉ','ｊ','ｋ','ｌ','ｍ','ｎ','ｏ','ｐ','ｑ','ｒ','ｓ','ｔ','ｕ','ｖ','ｗ','ｘ','ｙ','ｚ'},
			{'ｚ','†','漆','黒','の','堕','天','使','†','卍','滅','龍','神','卍','解','爆','発','〇'}
			}
	};

	public InputNameModel(InputNameState inputNameState) {				//コンストラクタ
		super();
		this.inState = inputNameState;
		ClearCursorcharArrey();																				//cuｒsorcharArrey[]を初期化
	}


	@Override
	public void keyInput(KeyInput keyInput) {
		
		if(keyInput.isKeyDown(Input.KEY_D)){
			FTSimulationGame.save.getPlayer().name = "朝倉 こずえ";
			inState.nextState();
		}
		if(keyInput.isKeyDown(Input.KEY_Z)) {
			if(CursorX == 17 && CursorY == 4 ){
				inState.nextState();							//決定キーが押されたので確定
			}
			else if(CursorX == 17 && CursorY == 3 ){
				cursorcharArreyLength  = 0;			//クリアキーが押されたので名前クリア
				ClearCursorcharArrey();
			}
			else if(CursorX == 17 && CursorY == 0 ){
				charactorOption = 0;							//ひら　が押されたのでひらがな入力モード
			}
			else if(CursorX == 17 && CursorY == 1 ){
				charactorOption = 1;							//カナ　が押されたのでカナ入力モード
			}
			else if(CursorX == 17 && CursorY == 2 ){
				charactorOption = 2;							//記号　が押されたので記号入力モード
			}
			else{
				GetCharFromCursor();
			}
		}

		if(keyInput.isKeyDown(Input.KEY_UP)) {
			if(CursorY>0){CursorY = CursorY-1;}
			else if(CursorY==0){CursorY = 4;}
		}
		if(keyInput.isKeyDown(Input.KEY_DOWN)) {
			if(CursorY<4){CursorY = CursorY+1;}
			else if(CursorY==4){CursorY = 0;}
		}
		if(keyInput.isKeyDown(Input.KEY_LEFT)) {
			if(CursorX>0){CursorX = CursorX-1;}
			else if(CursorX==0){CursorX = 17;}
		}
		if(keyInput.isKeyDown(Input.KEY_RIGHT)) {
			if(CursorX<17){CursorX = CursorX+1;}
			else if(CursorX==17){CursorX = 0;}
		}
	}

	/**
	 * 外部からcharactorOptionをint型で得る
	 * 0=ひらがな入力,　1=かな入力,　2=記号入力
	 */
	public int getcharactorOption(){
		return charactorOption;
	}


	/**
	 * ｚが押されるたびに呼び出され、カーソルの座標からchar文字に変換しcursorcharArrey[]に格納する
	 */
	public void GetCharFromCursor(){
		if(cursorcharArreyLength>7){
			return;
		}
		else {
		cursorcharArrey[cursorcharArreyLength] = cursorchar[charactorOption][CursorY][CursorX] ;
		cursorcharArreyLength ++;
		}
		return;
	}

	/**
	 * cursorcharArrey[]を全部スペースで埋める
	 */
	public void ClearCursorcharArrey(){
		for(int i=0; i<8; i++){
			cursorcharArrey[i] = '　';
		}
		return;
	}


	/**
	 * 外部から主人公の名前を得るためのメソッド　返り血はString型
	 */
	public String getName(){
		return String.valueOf(cursorcharArrey);			//cursorcharArreyをStringに結合
	}
}