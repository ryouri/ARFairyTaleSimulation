package arcircle.ftsim.simulation.field;

import java.util.ArrayList;
import java.util.HashMap;

public class TerrainManager {

	public int terrainChipWidth;
	public int terrainChipHeight;
	public HashMap<String, Terrain> terrainMap;
	public ArrayList<Terrain> terrainArray;

	public TerrainManager() {
		terrainMap = new HashMap<String, Terrain>();
		terrainArray = new ArrayList<Terrain>();
		LoadTerrainInfo.loadTerrainInfo(this);
	}

	public Terrain getTerrain(int chipX, int chipY) {
		for (Terrain terrain : terrainArray) {
			if (terrain.mapChipStartX <= chipX &&
				chipX < terrain.mapChipStartX + terrainChipWidth &&
				terrain.mapChipStartY <= chipY &&
				chipY < terrain.mapChipStartY + terrainChipHeight) {
				return terrain;
			}
		}
		return terrainMap.get("侵入不可");
	}
}
