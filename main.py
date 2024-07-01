import sys
sys.setrecursionlimit(10**6) # 再起呼び出しの上限を指定


def longest_path(graph):
    # 深さ優先探索 (DFS) を用いて最長経路を探索する内部関数
    def dfs(node, visited, current_length, path):
        nonlocal max_length, longest_path
        # 現在の経路長がこれまでの最大経路長を超えた場合、更新
        if current_length > max_length:
            max_length = current_length
            longest_path = path.copy()
        
        # 隣接する各ノードに対して再帰的に探索
        for neighbor, weight in graph[node].items():
            if neighbor not in visited:
                visited.add(neighbor)  # ノードを訪問済みにする
                path.append(neighbor)  # 経路にノードを追加
                dfs(neighbor, visited, current_length + weight, path)
                path.pop()  # 探索終了後に経路からノードを削除
                visited.remove(neighbor)  # 探索終了後に訪問済みを解除

    max_length = 0  # 最長経路の長さを記録する変数
    longest_path = []  # 最長経路を記録するリスト
    for start_node in graph:
        visited = set()  # 訪問済みノードを記録する集合
        visited.add(start_node)  # 開始ノードを訪問済みにする
        dfs(start_node, visited, 0, [start_node])  # DFSを開始
    
    return max_length, longest_path


# 無向グラフを保持する辞書を初期化(鉄道路線網を辞書を用いた隣接リストで管理する)
graph = dict()

# 無限ループを開始して標準入力からデータを読み取る
while True:
    try:
        # 標準入力から1行を読み取る
        line = input()
        # 読み取った行をカンマで分割
        parts = line.split(',')
        # 分割した各部分を適切な型に変換
        u = int(parts[0])  # 始点のID
        v = int(parts[1])  # 終点のID
        w = float(parts[2])  # 距離

        # 始点がグラフに存在しない場合、新しい辞書を作成
        if u not in graph:
            graph[u] = dict()
        # 始点から終点への距離を記録
        graph[u][v] = w

        # 終点がグラフに存在しない場合、新しい辞書を作成
        if v not in graph:
            graph[v] = dict()
        # 終点から始点への距離を記録（無向グラフなので対称）
        graph[v][u] = w

    except EOFError:
        # 入力の終わり (EOF) に達したらループを終了
        break

# 最長経路を出力
length, path = longest_path(graph)
# 最後に始点に戻る場合を追加
if path and path[0] in graph[path[-1]]:
    path.append(path[0])
for node in path:
    print(node, end="\r\n")