package arcircle.ftsim.renderer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public interface Renderer {
	abstract public void render(GameContainer container, StateBasedGame game, Graphics g);
}
