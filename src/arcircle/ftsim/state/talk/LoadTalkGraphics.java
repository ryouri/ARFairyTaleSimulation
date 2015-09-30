package arcircle.ftsim.state.talk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import arcircle.ftsim.main.FTSimulationGame;
import arcircle.ftsim.simulation.chara.Status;


/**トークステートで用いる画像をすべて読み込み, 持っておく
 * @author ゆきねこ*/
public class LoadTalkGraphics {
	//キャラの各画像ファイル名
    /** 立ち絵 "stand.png" */
    private final String STpng = "stand.png";
    /** 普通の顔画像 "faceStandard.png" */
    private final String FSTpng = "faceStandard.png";
    /** 笑った顔画像 "faceLaugh.png" */
    private final String FLApng = "faceLaugh.png";
    /** 怒った顔画像 "faceAngry.png" */
    private final String FANpng = "faceAngry.png";
    /** 苦しそうな顔画像 "faceSuffer.png" */
    private final String FSUpng = "faceSuffer.png";
    /** キャラのパラメータファイル "parameter.txt" */
    private final String PAR = "parameter.txt";
    /** 顔無し画像のファイルパス "./Image/NotFace.png" */
    private final String NotFaceFilePath = "./Image/NotFace.png";
    /** 立ち絵無し画像のファイルパス "./Image/Transparent.png" */
    private final String NotStandFilePath = "./Image/Transparent.png";

	/**キャラID(String)_種類とイメージのMap*/
	private HashMap<String, Image> allCharaImageMap;
	/**キャラIDとキャラネームのMap*/
	private HashMap<String, String> allCharaNameMap;
	/** キャラクターフォルダにある各キャラのフォルダ名リスト */
	private String[] charaIDArray;

	//アクセッタ
	public HashMap<String, Image> getAllCharaImageMap(){ return allCharaImageMap; }
	public HashMap<String, String> getAllCharaNameMap(){ return allCharaNameMap; }

	/**コンストラクタ
	 * @param folderPath:Characterフォルダパス
	 */
	public LoadTalkGraphics(String folderPath){
		this.allCharaImageMap = new HashMap<String, Image>();
		this.allCharaNameMap = new HashMap<String, String>();
		loadChara(folderPath);
	}

	/**Characterフォルダにある各キャラの画像を読み込む
	 * @param characterPath */
	private void loadChara(String characterPath){
		//CharacterフォルダのFileインスタンスを生成
		File characterFolder = new File(characterPath);
		//Characterフォルダ内の全フォルダの名前をcharaIDArrayに格納
		charaIDArray = characterFolder.list();

		//各画像データの読み込み
		try{
			//顔無しの画像読み込み
			allCharaImageMap.put(T_Const.NO_FACE, new Image(NotFaceFilePath).getScaledCopy(1.5f));
			allCharaImageMap.put(T_Const.NO_STAND, new Image(NotStandFilePath));
			//全キャラを読み込む
			for(int i = 0 ; i < charaIDArray.length ; i++){
				//立ち絵と顔画像の読み込み
				loadCharaImage(characterPath, i);
				//各キャラの名前をparamater.txtから読み込み
				loadCharaName(characterPath, i);
			}
			}catch(SlickException e){
			e.printStackTrace();
		}catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**各キャラの立ち絵と顔画像を読込むメソッド
	 * try文で囲って使うこと
	 * @param characterPath
	 * @param i : 読み込むキャラのcharaIDArrayインデックス
	 * @throws SlickException : エラー吐くので注意 */
	private void loadCharaImage(String characterPath, int i) throws SlickException{
		//立ち絵の読み込み
		allCharaImageMap.put(charaIDArray[i]+T_Const.STAND, new Image(characterPath+"/"+charaIDArray[i]+"/"+STpng));
		//通常顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath + "/" + charaIDArray[i] + "/" + FSTpng).exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_ST,
					(new Image(characterPath+"/"+charaIDArray[i]+"/"+FSTpng).getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_ST, allCharaImageMap.get(T_Const.NO_FACE));
		}
		//笑い顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath+"/"+charaIDArray[i]+"/"+FLApng).exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_LA,
					(new Image(characterPath + "/" + charaIDArray[i] + "/" + FLApng).getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_LA, allCharaImageMap.get(T_Const.NO_FACE));
		}
		//怒り顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath+"/"+charaIDArray[i]+"/"+FANpng).exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_AN,
					(new Image(characterPath + "/" + charaIDArray[i] + "/" + FANpng).getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_AN, allCharaImageMap.get(T_Const.NO_FACE));
		}
		//苦しみ顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath+"/"+charaIDArray[i]+"/"+FSUpng).exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_SU,
					(new Image(characterPath + "/" + charaIDArray[i] + "/" + FSUpng).getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i]+T_Const.FACE_SU, allCharaImageMap.get(T_Const.NO_FACE));
		}
	}

	/**各キャラの名前を読込むメソッド
	 * try文で囲って使うこと
	 * @param characterPath
	 * @param i : 読み込むキャラのcharaIDArrayインデックス
	 * @throws IOException : エラー吐くので注意 */
	private void loadCharaName(String characterPath, int i) throws IOException{
		//各キャラのフォルダ内にあるparamater.txt Fileのインスタンスを生成
		File file = new File(characterPath + "/" + charaIDArray[i] + "/" + PAR);
		//テキスト読み込み用のバッファリーダー
		BufferedReader br = new BufferedReader(new FileReader(file));
		//テキスト読み込み用バッファ
		String line;
		while ((line = br.readLine()) != null) {
			if (line.equals("")){
				// 空行を読み飛ばす
				continue;
			}else if (line.startsWith("#")){
				//コメントを読み飛ばす
				continue;
			}
			// バッファに取り込まれた1行のテキストを","で分割
			String[] strs = line.split(",");

			if(!strs[0].equals("")){
				/* キャラの名前をallCharaNameMapに格納．キーはキャラID+Name */
				allCharaNameMap.put(charaIDArray[i]+T_Const.NAME, strs[0]);
			}else{
				System.out.println("error_LoadTalkGraphics__paramater.txt読み込み");
			}
			break;
		}
		br.close();  // ファイルを閉じる

		//主人公の名前だけ書き替える
		if(FTSimulationGame.save.getPlayer().gender == Status.MALE){
			/* 主人公が男の場合 */
			allCharaNameMap.put(T_Const.PLAYER_M+T_Const.NAME, FTSimulationGame.save.getPlayer().name);
		} else if (FTSimulationGame.save.getPlayer().gender == Status.FEMALE){
			/* 主人公が女の場合 */
			allCharaNameMap.put(T_Const.PLAYER_F+T_Const.NAME, FTSimulationGame.save.getPlayer().name);
		} else {
			/* エラー */
			System.out.println("error_LoadTalkGraphics_FTSimulationGame.save.getPlayer().gender");
		}
	}
}