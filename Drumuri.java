import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Drumuri {
	public static class Pair implements Comparable<Pair> {
		public int destination;
		public long cost;

		Pair(int destination, long cost) {
			this.destination = destination;
			this.cost = cost;
		}

		public int compareTo(Pair pair) {
			return Long.compare(cost, pair.cost);
		}
	}

	public static class DistAndParents {
		public long distance;
		public int[] parents;

		DistAndParents(long distance, int[] parents) {
			this.distance = distance;
			this.parents = parents;
		}
	}

	public static final class Task {
		public static final String INPUT_FILE = "drumuri.in";
		public static final String OUTPUT_FILE = "drumuri.out";

		int x;
		int y;
		int z;
		int nrNodes;
		int nrEdges;
		ArrayList<Pair>[] adj;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Reader reader = new FileReader(INPUT_FILE);
				MyScanner sc = new MyScanner(reader);

				nrNodes = sc.nextInt();
				nrEdges = sc.nextInt();

				adj = new ArrayList[nrNodes + 1];

				for (int i = 1; i <= nrNodes; i++) {
					adj[i] = new ArrayList<>();
				}

				for (int i = 0; i < nrEdges; i++) {
					int v1 = sc.nextInt();
					int v2 = sc.nextInt();
					int w = sc.nextInt();

					adj[v1].add(new Pair(v2, w));
				}

				x = sc.nextInt();
				y = sc.nextInt();
				z = sc.nextInt();

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(Long result) {
			try {
				Writer writer = new FileWriter(OUTPUT_FILE);
				PrintWriter pw = new PrintWriter(writer);

				pw.printf("%d\n", result);

				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private DistAndParents dijkstra(int source, int dest) {
			long[] d = new long[nrNodes + 1];

			//  Set distance to MAX_VALUE
			for (int i = 0; i <= nrNodes; i++) {
				d[i] = Long.MAX_VALUE;
			}

			//  Use priority queue for faster convergence
			PriorityQueue<Pair> pq = new PriorityQueue<>();

			//  Add source to queue
			pq.add(new Pair(source, 0));

			//  Set distance and parent
			d[source] = 0;
			int[] p = new int[nrNodes + 1];
			p[source] = -1;

			while (!pq.isEmpty()) {
				//  Take the node and its distance from source
				long cost = pq.peek().cost;
				int node = pq.poll().destination;

				//  If cost is worse, move on to next node
				if (cost > d[node]) {
					continue;
				}

				//  Check all neighbours of this node
				for (Pair neigh : adj[node]) {
					long w = neigh.cost;

					if (d[node] + w < d[neigh.destination]) {
						d[neigh.destination] = d[node] + w;
						p[neigh.destination] = node;

						pq.add(new Pair(neigh.destination, d[neigh.destination]));
					}
				}
			}

			for (int i = 0; i <= nrNodes; i++) {
				if (d[i] == Integer.MAX_VALUE) {
					d[i] = -1;
				}
			}

			return new DistAndParents(d[dest], p);
		}

		private long extractDuplicates(long result, DistAndParents path1, DistAndParents path2) {
			//  Create 2 HashMaps and check for common edges
			//  Subtract the cost of a common edge
			HashMap<Integer, Integer> parents1 = new HashMap<>();
			HashMap<Integer, Integer> parents2 = new HashMap<>();

			int iter = z;
			while (iter != -1) {
				parents1.put(path1.parents[iter], iter);
				iter = path1.parents[iter];
			}
			iter = z;
			while (iter != -1) {
				parents2.put(path2.parents[iter], iter);
				iter = path2.parents[iter];
			}

			//  Traverse one of the HashMaps and check for common edges
			for (HashMap.Entry<Integer, Integer> entry : parents1.entrySet()) {
				int source = entry.getKey();
				int dest1 = entry.getValue();

				if (parents2.containsKey(source)) {
					int dest2 = parents2.get(source);
					if (dest1 == dest2) {
						for (Pair pair: adj[source]) {
							if (pair.destination == dest1) {
								result -= pair.cost;
								break;
							}
						}
					}
				}
			}
			return result;
		}
		
		private long getResult() {
			//  One dijkstra for x, and one for y
			DistAndParents path1 = dijkstra(x, z);
			DistAndParents path2 = dijkstra(y, z);
			
			//  Sum paths
			long result = path1.distance + path2.distance;
			result = extractDuplicates(result, path1, path2);
			
			return result;
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
