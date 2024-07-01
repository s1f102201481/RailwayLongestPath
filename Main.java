import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    // 最長経路の長さを記録する変数
    private static double maxLength = 0;
    // 最長経路を記録するリスト
    private static List<Integer> longestPath = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 鉄道路線網を保持する無向グラフ（隣接リスト）
        Map<Integer, Map<Integer, Double>> graph = new HashMap<>();

        // 入力データの読み込み
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(",");   // 読み取った行をカンマで分割
            int u = Integer.parseInt(parts[0].trim());  // 始点のID
            int v = Integer.parseInt(parts[1].trim());  // 終点のID
            double w = Double.parseDouble(parts[2].trim());  // 距離

            // 始点がグラフに存在しない場合、新しいマップを作成して追加
            graph.putIfAbsent(u, new HashMap<>());
            // 始点から終点への距離を記録
            graph.get(u).put(v, w);

            // 終点がグラフに存在しない場合、新しいマップを作成して追加
            graph.putIfAbsent(v, new HashMap<>());
            // 終点から始点への距離を記録（無向グラフなので対称）
            graph.get(v).put(u, w);
        }
        scanner.close();

        // 最長経路を探索
        for (int startNode : graph.keySet()) {
            Set<Integer> visited = new HashSet<>();
            visited.add(startNode);  // 開始ノードを訪問済みにする
            dfs(graph, startNode, visited, 0, new ArrayList<>(List.of(startNode)));  // DFSを開始
        }

        // 最後に始点に戻る場合を追加
        if (!longestPath.isEmpty() && graph.get(longestPath.get(longestPath.size() - 1)).containsKey(longestPath.get(0))) {
            longestPath.add(longestPath.get(0));
        }

        // 結果を出力
        for (int node : longestPath) {
            System.out.printf("%d\r\n", node);
        }
    }

    // 深さ優先探索 (DFS) を用いて最長経路を探索するメソッド
    private static void dfs(Map<Integer, Map<Integer, Double>> graph, int node, Set<Integer> visited, double currentLength, List<Integer> path) {
        // 現在の経路長がこれまでの最大経路長を超えた場合、更新
        if (currentLength > maxLength) {
            maxLength = currentLength;
            longestPath = new ArrayList<>(path);
        }

        // 隣接する各ノードに対して再帰的に探索
        for (Map.Entry<Integer, Double> neighborEntry : graph.get(node).entrySet()) {
            int neighbor = neighborEntry.getKey();
            double weight = neighborEntry.getValue();
            if (!visited.contains(neighbor)) {
                visited.add(neighbor);  // ノードを訪問済みにする
                path.add(neighbor);  // 経路にノードを追加
                dfs(graph, neighbor, visited, currentLength + weight, path);
                path.remove(path.size() - 1);  // 探索終了後に経路からノードを削除
                visited.remove(neighbor);  // 探索終了後に訪問済みを解除
            }
        }
    }
}
