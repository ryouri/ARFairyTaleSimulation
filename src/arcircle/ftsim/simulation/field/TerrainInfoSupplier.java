package arcircle.ftsim.simulation.field;

import java.util.ArrayList;
import java.util.HashMap;

public class TerrainInfoSupplier {

	public int terrainChipWidth;
	public int terrainChipHeight;
	public HashMap<String, Terrain> terrainMap;
	public HashMap<Integer, Terrain> autoTileTerrainMap;
	public HashMap<String, Boolean> existClassMap;
	public ArrayList<Terrain> terrainArray;

	public static final int NONE_CHIP_NUM = 2000;
	public static final int DO_NOT_ENTER = 240;
	public static final String DO_NOT_ENTER_STR = "進入不可";

	public TerrainInfoSupplier() {
		terrainMap = new HashMap<String, Terrain>();
		autoTileTerrainMap = new HashMap<Integer, Terrain>();
		terrainArray = new ArrayList<Terrain>();
		LoadTerrainInfo.loadTerrainInfo(this);
		generateClassNameMap();
	}

	private void generateClassNameMap() {
		existClassMap = new HashMap<String, Boolean>();
		for (Terrain terrain : terrainArray) {
			for (String className : terrain.classNameCostMap.keySet()) {
				existClassMap.put(className, true);
			}
		}
	}

	public Terrain getTerrain(int mapChipNo) {
		if (mapChipNo >= NONE_CHIP_NUM) {
			return getTerrainAutoTile(mapChipNo);
		}

		int chipX = mapChipNo % LoadField.MAP_CHIP_COL;
		int chipY = mapChipNo / LoadField.MAP_CHIP_COL;

		for (Terrain terrain : terrainArray) {
			if (terrain.mapChipStartX <= chipX &&
				chipX < terrain.mapChipStartX + terrainChipWidth &&
				terrain.mapChipStartY <= chipY &&
				chipY < terrain.mapChipStartY + terrainChipHeight) {
				return terrain;
			}
		}

		if (mapChipNo >= DO_NOT_ENTER) {
			return terrainMap.get(DO_NOT_ENTER_STR);
		}

		return null;
	}

	private Terrain getTerrainAutoTile(int mapChipNo) {
		if (mapChipNo == NONE_CHIP_NUM) {
			return null;
		}
		return autoTileTerrainMap.get(mapChipNo);
	}
}
