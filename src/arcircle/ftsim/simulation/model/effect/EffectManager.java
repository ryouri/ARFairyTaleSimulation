package arcircle.ftsim.simulation.model.effect;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

/** Effectを描画するためのクラス
 * @author ゆきねこ */
public class EffectManager {
	//フィールド-------------------------------------------------------------------------------------------
	/** エフェクト画像のあるフォルダ */
	private final String effectFolderPath = "image/effect";
	/** チップサイズ */
	private final int CHIPSIZE = 32;
	/** エフェクトアニメーションの1コマあたりの時間*/
	private final int DURATION = 600;
	/** エフェクトの描画位置や種類情報 */
	private EffectInfo effectInfo;
	/** エフェクト(名前,アニメーションイメージ)のMap
	 * keyはEffectファイルの拡張子無の名前(EfConstと対応づける(手動)) */
	private HashMap<String, Animation> effectAnimationMap;

	/** コンストラクタ-------------------------------------------------------------------------------------
	 *
	 * */
	public EffectManager(EffectInfo info){
		this.effectInfo = info;
		loadEffect();
	}

	/** エフェクト画像をアニメーションに変換して読み込むためのメソッド */
	private void loadEffect(){
		// 効果グラフィックフォルダを取得
		File imageFolder = new File(effectFolderPath);
		String tempName;
		try {
			// エフェクト画像フォルダ内を走査
			for(String fileName : imageFolder.list()){
				// ファイル名の拡張子無し部分を取得
				tempName = (fileName.split("_")[0]);
				// スプライトシートを生成
				SpriteSheet ssheet = new SpriteSheet(new Image(effectFolderPath + "/" + fileName), CHIPSIZE, CHIPSIZE);
				// アニメーションに必要なイメージ配列をスプライトシートから取得
				Image[] images = new Image[ssheet.getHorizontalCount()];
				for(int i = 0  ; i < images.length ; i++){
					images[i] = ssheet.getSubImage(i, 0);
				}
				// エフェクトアニメーションマップにアニメーションを挿入
				effectAnimationMap.put(tempName, new Animation(images, DURATION, true));
			}
		} catch (SlickException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}



	/**  */
	public void render(Graphics g){
		g.drawAnimation(effectAnimationMap.get(effectInfo.name), effectInfo.px, effectInfo.py);
	}

	public void update(int px, int py, String effectName){
		effectInfo.px = px;
		effectInfo.py = py;
		effectInfo.name = effectName;
	}

}
