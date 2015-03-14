package arcircle.ftsim.simulation.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


/**トークステートで用いる画像をすべて読み込み, 持っておく
 * @author ゆきねこ*/
public class LoadTalkGraphics {
	/**キャラID(String)_種類とイメージのMap*/
	private HashMap<String, Image> allCharaImageMap;
	/**キャラIDとキャラネームのMap*/
	private HashMap<String, String> allCharaNameMap;
	//キャラクターフォルダにある各キャラフォルダの名前リスト
	private String[] charaIDArray;

	//アクセッタ
	public HashMap<String, Image> getAllCharaImageMap(){ return allCharaImageMap; }
	public HashMap<String, String> getAllCharaNameMap(){ return allCharaNameMap; }

	/**コンストラクタ */
	public LoadTalkGraphics(String folderPath){
		this.allCharaImageMap = new HashMap<String, Image>();
		this.allCharaNameMap = new HashMap<String, String>();
		loadChara(folderPath);
	}

	/**Characterフォルダにある各キャラの画像を読み込む
	 * @param characterPath */
	private void loadChara(String characterPath){

		File characterFolder = new File(characterPath);	//CharacterフォルダのFile
		charaIDArray = characterFolder.list();

		//各画像データの読み込み
		try{
			//顔無しの画像読み込み
			Image notFaceImg = new Image("./Image/NotFace.png");
			//全キャラを読み込む
			for(int i = 0 ; i < charaIDArray.length ; i++){
				//立ち絵と顔画像の読み込み
				loadCharaImage(characterPath, notFaceImg, i);
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
	private void loadCharaImage(String characterPath, Image notFaceImg, int i) throws SlickException{
		//立ち絵の読み込み
		allCharaImageMap.put(charaIDArray[i] + "Stand", new Image(characterPath + "/" +
				charaIDArray[i] + "/stand.png"));
		//通常顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath + "/" + charaIDArray[i] + "/FaceStandard.png").exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i] + "FaceStandard",
					(new Image(characterPath + "/" + charaIDArray[i] + "/faceStandard.png").getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i] + "FaceStandard", notFaceImg);
		}
		//笑い顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath + "/" + charaIDArray[i] + "/FaceLaugh.png").exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i] + "FaceLaugh",
					(new Image(characterPath + "/" + charaIDArray[i] + "/faceLaugh.png").getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i] + "FaceLaugh", notFaceImg);
		}
		//怒り顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath + "/" + charaIDArray[i] + "/FaceAngry.png").exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i] + "FaceAngry",
					(new Image(characterPath + "/" + charaIDArray[i] + "/faceAngry.png").getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i] + "FaceAngry", notFaceImg);
		}
		//苦しみ顔の読み込み(トーク用に1.5倍拡大)
		if(new File(characterPath + "/" + charaIDArray[i] + "/FaceSuffer.png").exists()){
			/* 顔画像ありの場合 */
			allCharaImageMap.put(charaIDArray[i] + "FaceSuffer",
					(new Image(characterPath + "/" + charaIDArray[i] + "/faceSuffer.png").getScaledCopy(1.5f)));
		}else{
			//顔画像無しの場合
			allCharaImageMap.put(charaIDArray[i] + "FaceSuffer", notFaceImg);
		}
	}

	/**各キャラの名前を読込むメソッド
	 * try文で囲って使うこと
	 * @param characterPath
	 * @param i : 読み込むキャラのcharaIDArrayインデックス
	 * @throws IOException : エラー吐くので注意 */
	private void loadCharaName(String characterPath, int i) throws IOException{
		File file = new File(characterPath + "/" + charaIDArray[i] + "/parameter.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			// 空行を読み飛ばす
			if (line.equals("")){
				continue;
				//コメントを読み飛ばす
			}else if (line.startsWith("#")){
				continue;
			}
			String[] strs = line.split(",");
			if(!strs[0].equals("")){
				allCharaNameMap.put(charaIDArray[i] + "Name", strs[0]);
			}else{
				System.out.println("error_TalkView__paramater.txt読み込み");
			}
			break;
		}
		br.close();  // ファイルを閉じる
	}

}
