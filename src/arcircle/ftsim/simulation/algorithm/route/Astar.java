package arcircle.ftsim.simulation.algorithm.route;

import java.awt.Point;
import java.util.LinkedList;


public class Astar {
    // オープンリスト
    private PriorityList openList;
    // クローズドリスト
    private LinkedList<Node> closedList;
    // マップへの参照
    private Map map;

    public Astar(Map map) {
        this.map = map;
        // PriorityListは自作クラス（Astarの内部クラスとして定義）
        openList = new PriorityList();
        closedList = new LinkedList<Node>();
    }

    /**
     * A*で求めたスタートからゴールまでのパスを返す
     *
     * @param startPos スタート地点
     * @param goalPos ゴール地点
     * @return A*で求めたパス
     */
    public LinkedList<Node> searchPath(Point startPos, Point goalPos) {
        int cnt = 0;
        // スタートノードとゴールノードを作成
        Node startNode = new Node(startPos);
        Node goalNode = new Node(goalPos);

        // スタートノードを設定
        startNode.costFromStart = 0;
        startNode.heuristicCostToGoal = startNode.getHeuristicCost(goalNode);
        startNode.parentNode = null;

        // スタートノードをオープンリストに追加
        openList.add(startNode);

        // オープンリストが空になるまでまわす
        while (!openList.isEmpty()) {

            // openListはコストが小さい順に並んでいるため
            // 最小コストノードは一番目にある
            // そのノードを取り出す
            Node curNode = (Node) openList.removeFirst();

            // そのノードがゴールノードと一致しているか
            // 一致（equals）の定義はNodeクラスで定義
            if (curNode.equals(goalNode)) {
                // ゴールノードからパスを生成
                // goalNodeはコストなどが設定されてないので
                // 引数としてcurNodeを渡すところに注意
                return constructPath(curNode);
            } else { // 一致してない場合
                // 現在のノードをクローズドリストに移す
                closedList.add(curNode);
                // 現在のノードに隣接する各ノードを調べる
                LinkedList<Node> neighbors = curNode.getNeighbors();
                for (int i = 0; i < neighbors.size(); i++) { // 各隣接ノードに対して
                    // ノードを1つ取得
                    Node neighborNode = (Node) neighbors.get(i);
                    // 条件検査用情報を取得
                    // オープンリストに含まれているか？
                    boolean isOpen = openList.contains(neighborNode);

                    // クローズドリストに含まれているか？
                    boolean isClosed = closedList.contains(neighborNode);
                    // 障害物でないか？
                    boolean isHit = map.isHit(neighborNode.pos.x,
                            neighborNode.pos.y);

                    if (isHit){
                    	continue;
                    }

                    // 隣接ノードのコストの計算
                    neighborNode.costFromStart = curNode.costFromStart + map.getCost(neighborNode.pos);
                    neighborNode.heuristicCostToGoal = neighborNode.getHeuristicCost(goalNode);
                    int newCost = neighborNode.costFromStart + neighborNode.heuristicCostToGoal;
                    // 親ノード
                    neighborNode.parentNode = curNode;

                    // もし初出のノードだったら
                    if (!isOpen && !isClosed) {
                        // オープンリストに追加
                        openList.add(neighborNode);
                    }
                    // オープンリストに存在したけど新しいノードのほうがコストが低ければ更新
                    else if (isOpen){
                    	Node oldNode = (Node)openList.get(openList.indexOf(neighborNode));
                    	int oldCost = oldNode.costFromStart + oldNode.heuristicCostToGoal;
                        if (newCost < oldCost){
                        	openList.remove(neighborNode);
                        	openList.add(neighborNode);
                        }
                    }
                    //クローズリストに存在したけど新しいノードのほうがコストが低ければ更新してオープンリストに移動
                    else if (isClosed){
                    	Node oldNode = (Node)closedList.get(closedList.indexOf(neighborNode));
                    	int oldCost = oldNode.costFromStart + oldNode.heuristicCostToGoal;
                        if (newCost < oldCost){
                        	closedList.remove(neighborNode);
                        	openList.add(neighborNode);
                        }
                    }
                }
            }
        }


        // 再呼び出しがあるかもしれないので消去しておく
        openList.clear();
        closedList.clear();

        // ループをまわしてパスが見つからなかった場合はnullを返す
        return null;
    }

    /**
     * ゴールノードまでのパスを構築する
     *
     * @param node ゴールノード
     * @return スタートノードからゴールノードまでのパス
     */
    private LinkedList<Node> constructPath(Node node) {
    	LinkedList<Node> path = new LinkedList<Node>();

        // 親ノードを次々たどる
        while (node.parentNode != null) {
            // 最初に追加するのがミソ
            // スタートノードがLinkedListの先頭
            // ゴールノードがLinkedListの最後になるようにする
            path.addFirst(node);
            node = node.parentNode;
        }

        // スタートノード（node.parentNode == nullとなるノード）
        // も追加しておく
        path.addFirst(node);

        return path;
    }

    /**
     * オープンリストの中身を表示する補助メソッド
     */
    private void showOpenList() {
        System.out.println("【オープンリスト】");
        for (int i = 0; i < openList.size(); i++) {
            Node node = (Node) openList.get(i);
            System.out.print("(" + node.pos.x + ", " + node.pos.y + ")");
        }
        System.out.println();
    }

    /**
     * クローズドリストの中身を表示する補助メソッド
     */
    private void showClosedList() {
        System.out.println("【クローズドリスト】");
        for (int i = 0; i < closedList.size(); i++) {
            Node node = (Node) closedList.get(i);
            System.out.print("(" + node.pos.x + ", " + node.pos.y + ")");
        }
        System.out.println();
    }

    /**
     * 自動的にコストの小さい順にノードが並ぶように拡張したリスト
     */
    private class PriorityList extends LinkedList {
        // リストに追加するaddメソッドをオーバーライド
        public void add(Node node) {
            for (int i = 0; i < size(); i++) {
                // ノードの大小を調べて小さい順に並ぶように追加する
                // ノードの大小を調べるcompareToはNodeクラスで定義
                if (node.compareTo(get(i)) <= 0) {
                    add(i, node);
                    return;
                }
            }
            addLast(node);
        }
    }
}
