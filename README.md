Tema 2 - Proiectarea algoritmilor

Task 1 - Numarare
    For the first task, the main algorithm that was used is BFS. Firstly, when given the 
    two graphs, the first one is stored entirely, but from the second one we only keep the common
    edges between the two. Then, we parse this graph (the second) and, via BFS, we increase the possible 
    ways to the nodes' neighbours. For example, if we can reach the 3rd node from two different paths, and 
    3 -> 4, then we can also reach the 4th node from those two paths, plus any other existent ways. The order
    of which the nodes are parsed is a topological sort.
    The temporal complexity of this algorithm is O(N + M), where N is the number of vertices and M is the
    number of edges.

Task 2 - Trenuri
    Similarly to task 1, this algorithm also requires a topological sort via BFS. The difference here is that
    we first start parsing the graph from the start node, and calculate the internal grades based on only the
    nodes that can be reached by the start node. This way, we reduce temporal overhead and only focus on the
    nodes that contribute to the final result. Then, we parse this obtained subgraph again, and create a
    topological sort. The topSort is then parsed in order, updating the distances until stop is reached. Then,
    we know the maximum distance has been calculated, therefore we can return the result.
    Note: due to the nodes being represented as Strings, I used HashMaps to encode every vertex as integers.
    The temporal complexity of this algorithm is O(N + M), where N is the number of vertices and M is the
    number of edges. There is a high chance for the complexity to be lower, since we avoid any redundant edge.

Task 3 - Drumuri
    This task relies on Dijkstra's algorithm. It simply calculates the shortest distance from x to z and y to
    z, combines the two results, and, via the parents' array, removes common edges from the final result.
    Note: Dijkstra's algorithm was taken from the official lab solutions and modified a bit.
    The temporal complexity of this algorithm is O((N + M) * logN), where N is the number of vertices and M is the
    number of edges.