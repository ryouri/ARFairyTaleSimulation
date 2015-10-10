package arcircle.ftsim.simulation.model.effect;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;


/** タスクからEffectManagerを呼び出すときに渡すパッケージ
 * @author ゆきねこ */
public class Effect {
	/** チップサイズ */
	private final int CHIPSIZE = EffectConst.CHIP_SIZE;
	// エフェクト描画位置
	private int px,py;
	/** エフェクトのアニメーション */
	private Animation effectAnimation;

	//--------------------------------------------------------------------------------------------------------------
	/**コンストラクタ
	 * @param px
	 * @param py
	 * @param effectName */
	public Effect(int px, int py, Animation animation){
		this.px = px;
		this.py = py;
		this.effectAnimation = animation;
	}
	public boolean animationStopped(){
		return effectAnimation.isStopped();
	}

	public void render(
			Graphics g, int offsetX, int offsetY,
			int firstTileX, int lastTileX,
			int firstTileY, int lastTileY) {
		// ウィンドウ表示範囲内でなければ
		int x = px / CHIPSIZE;
		int y = py / CHIPSIZE;
		if (!(firstTileX <= x && x <= lastTileX
				&& firstTileY <= y && y <= lastTileY)) {
				return;
		}
		effectAnimation.draw(px + offsetX, py + offsetY);
	}

	public void update() {
	}
}
