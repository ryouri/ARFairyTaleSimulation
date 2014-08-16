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
	/**
	 * ひらがな=0,  カタカナ=1, 記号=2
	 */
	public int charactorOption =0;
	public int CursorX = 0;  //カーソルが選択している座標Xを格納
	public int CursorY = 0;  //カーソルが選択している座標Yを格納
	private char[][][] cursorchar = new char [][][] {
			{
			{'あ','か','さ','た','な','は','ま','や','ら','わ','ぁ','っ','が','ざ','だ','ば','ぱ','ー'},
			{'い','き','し','ち','に','ひ','み','　','り','　','ぃ','ゃ','ぎ','じ','ぢ','び','ぴ','～'},
			{'う','く','す','つ','ぬ','ふ','む','ゆ','る','を','ぅ','ゅ','ぐ','ず','づ','ぶ','ぷ','〇'},
			{'え','け','せ','て','ね','へ','め','　','れ','　','ぇ','ょ','げ','ぜ','で','べ','ぺ','〇'},
			{'お','こ','そ','と','の','ほ','も','よ','ろ','ん','ぉ','ゎ','ご','ぞ','ど','ぼ','ぽ','〇'}
			},
			{
			{'ア','カ','サ','タ','ナ','ハ','マ','ヤ','ラ','ワ','ァ','ッ','ガ','ザ','ダ','バ','パ','ー'},
			{'イ','キ','シ','チ','ニ','ヒ','ミ','　','リ','　','ィ','ャ','ギ','ジ','ヂ','ビ','ピ','～'},
			{'ウ','ク','ス','ツ','ネ','フ','ム','ユ','ル','ヲ','ゥ','ュ','グ','ズ','ヅ','ブ','プ','〇'},
			{'エ','け','セ','テ','ネ','ヘ','メ','　','レ','　','ェ','ョ','ゲ','ゼ','デ','ベ','ペ','〇'},
			{'オ','コ','ソ','ト','ノ','ホ','モ','ヨ','ロ','ン','ォ','ヮ','ゴ','ゾ','ド','ボ','ポ','〇'}
			},
			{
			{'１','２','３','４','５','５','６','７','８','９','０','！','？','＠','＃','＄','＾','＆','〇'},
			{'Ａ','Ｂ','Ｃ','Ｄ','Ｅ','Ｆ','Ｇ','Ｈ','Ｉ','Ｊ','Ｋ','Ｌ','Ｍ','Ｎ','Ｏ','Ｐ','Ｑ','〇'},
			{'Ｒ','Ｓ','Ｔ','Ｕ','Ｖ','Ｗ','Ｘ','Ｙ','Ｚ','ａ','ｂ','ｃ','ｄ','ｅ','ｆ','ｇ','ｈ','〇'},
			{'え','け','せ','て','ね','へ','め','　','れ','　','ぇ','ょ','げ','ぜ','で','べ','ぺ','〇'},
			{'ｚ','㍿','漆','黒','の','堕','天','使','†','卍','滅','龍','神','Ｘ','充','爆','発','〇'}
			}
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
			else if(CursorX == 17 && CursorY == 3 ){
				charactor = "";										//クリアキーが押されたので名前クリア
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
	 * 外部からcharactorOptionをint型で得る
	 * 0=ひらがな入力,　1=かな入力,　2=記号入力
	 */
	public int getcharactorOption(){
		return charactorOption;

	}

	/**
	 * カーソルの位置charactorOption,CursorY,CursorXから文字に変換し、String charactorに8文字まで累加算する
	 */
	public void addGetCharFromCursor(){
		if(charactor.length()>=24){return;}				//名前は8文字まで
		charactor = charactor + cursorchar[charactorOption][CursorY][CursorX] + "  " ;
		return;
	}

	/**
	 * 外部から主人公の名前を得るためのメソッド　返り血はString型
	 */
	public String getName(){
		return charactor;
	}

}
