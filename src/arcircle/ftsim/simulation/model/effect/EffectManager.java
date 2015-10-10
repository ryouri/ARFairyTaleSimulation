package arcircle.ftsim.simulation.model.effect;

import java.io.File;
import java.util.ArrayList;
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
	private final int CHIPSIZE = EfConst.CHIP_SIZE;
	/** エフェクトアニメーションの1コマあたりの時間*/
	private final int DURATION = 600;
	/** エフェクト(名前,アニメーションイメージ)のMap
	 * keyはEffectファイルの拡張子無の名前(EfConstと対応づける(手動)) */
	private HashMap<String, Animation> effectAnimationMap;
	private ArrayList<Effect> effectList;

	//-----------------------------------------------------------------------------------------------------
	/** コンストラクタ */
	public EffectManager(){
		effectList = new ArrayList<Effect>();
		effectAnimationMap = new HashMap<String, Animation>();
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
	/** エフェクトリストにオブジェクトがあるか判定*/
	public boolean existEffect() {
		return !effectList.isEmpty();
	}
	/**エフェクトリストに新たなエフェクトを追加する
	 * @param px
	 * @param py
	 * @param effectName */
	public void addEffect (int px, int py, String effectName) {
		effectList.add(new Effect(px, py, effectAnimationMap.get(effectName)));
	}
	/** エフェクトリストにオブジェクトがあればそれをすべて描画 */
	public void render(
			Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY){

		if (effectList.isEmpty()) {
			return;
		}
		for (Effect effect : effectList) {
			effect.render(g, offsetX, offsetY, firstTileX, lastTileX, firstTileY, lastTileY);
		}
	}
	/**アップデート
	 * @param px
	 * @param py
	 * @param effectName */
	public void update(int px, int py, String effectName){
		// エフェクトリストにオブジェクトがなかった場合はリターン
		if(effectList.isEmpty()){
			return;
		}
		// エフェクトのアニメーションがストップしていたらそのエフェクトを削除
		for(int i = 0 ; i < effectList.size() ; i++){
			if(effectList.get(i).animationStopped()){
				effectList.remove(i);
				i--;
			}
		}
	}

	public void effectEnd() {
		if (!effectList.isEmpty()) {
			effectList.clear();
		}
	}
}
