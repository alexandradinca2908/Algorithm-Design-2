
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class Trenuri {
	public static class Pair {
		int index;
		int internalGrade;

		Pair(int index, int internalGrade) {
			this.index = index;
			this.internalGrade = internalGrade;
		}
	}

	public static final class Task {
		public static final String INPUT_FILE = "trenuri.in";
		public static final String OUTPUT_FILE = "trenuri.out";

		public static final int WEIGHT = 1;

		String start;
		String stop;
		int nrOfEdges;
		HashMap<String, ArrayList<String>> roads;
		HashMap<String, Pair> cities;


		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Reader reader = new FileReader(INPUT_FILE);
				MyScanner sc = new MyScanner(reader);

				start = sc.next();
				stop = sc.next();
				nrOfEdges = sc.nextInt();

				roads = new HashMap<>();
				cities = new HashMap<>();

				int cityIndex = 0;

				for (int i = 0; i < nrOfEdges; i++) {
					String v1 = sc.next();
					String v2 = sc.next();

					//  Add nodes
					if (!cities.containsKey(v1)) {
						cities.put(v1, new Pair(cityIndex, 0));
						cityIndex++;
					}
					if (!cities.containsKey(v2)) {
						cities.put(v2, new Pair(cityIndex, 0));
						cityIndex++;
					}

					//  Add a city and the list of neighbours
					//  Or just update the list, if key exists
					ArrayList<String> neighbours;
					if (!roads.containsKey(v1)) {
						neighbours = new ArrayList<>();
					} else {
						neighbours = roads.get(v1);
					}
					neighbours.add(v2);
					roads.put(v1, neighbours);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private void writeOutput(Integer result) {
			try {
				Writer writer = new FileWriter(OUTPUT_FILE);
				PrintWriter pw = new PrintWriter(writer);

				pw.printf("%d\n", result);

				pw.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		private int getResult() {
			//  Determine internal grades of nodes start vertex can reach
			//  We basically filter out the required subgraph
			//  This way we reduce temporal overhead
			ArrayList<String> queue = new ArrayList<>();
			boolean[] visited = new boolean[cities.size() + 1];

			queue.add(start);

			while (!queue.isEmpty()) {
				//  Pop first elem
				String city = queue.remove(0);

				if (roads.containsKey(city)) {
					for (String neigh : roads.get(city)) {
						Pair pair = cities.get(neigh);
						pair.internalGrade++;

						cities.put(neigh, pair);

						if (!visited[pair.index]) {
							queue.add(neigh);
							visited[pair.index] = true;
						}
					}
				}
			}

			//  Topological sort using BFS on the subgraph we obtained earlier
			//  Reduce time by not processing anything further than stop (slight optimization)
			ArrayList<String> topSort = new ArrayList<>();

			queue.add(start);

			while (!queue.isEmpty()) {
				//  Pop first elem
				String city = queue.remove(0);

				//  Add to topSort
				topSort.add(city);
				if (city.equals(stop)) {
					break;
				}

				if (roads.containsKey(city)) {
					for (String neigh : roads.get(city)) {
						//  Update internal grade
						Pair pair = cities.get(neigh);
						pair.internalGrade--;

						cities.put(neigh, pair);

						if (pair.internalGrade == 0) {
							queue.add(neigh);
						}
					}
				}
			}

			//  Distance array
			int[] distance = new int[cities.size() + 1];

			//  Parse topSort and update distances
			for (String vertex : topSort) {
				int vertexIndex = cities.get(vertex).index;

				//  Update distances
				if (roads.containsKey(vertex)) {
					for (String neigh : roads.get(vertex)) {
						int neighIndex = cities.get(neigh).index;

						if (distance[neighIndex] < distance[vertexIndex] + WEIGHT) {
							distance[neighIndex] = distance[vertexIndex] + WEIGHT;
						}
					}
				}
			}

			return distance[cities.get(stop).index] + 1;
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
