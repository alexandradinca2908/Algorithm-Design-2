import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class Numarare {
	public static final class Task {
		public static final String INPUT_FILE = "numarare.in";
		public static final String OUTPUT_FILE = "numarare.out";
		public static final Integer MODULO = 1000000000 + 7;

		int N;
		int M;
		int[] internal_grades;
		ArrayList<Integer>[] adj1;
		ArrayList<Integer>[] adj2;

		public void solve() {
			readInput();
			writeOutput(getResult());
		}

		private void readInput() {
			try {
				Reader reader = new FileReader(INPUT_FILE);
				MyScanner sc = new MyScanner(reader);

				N = sc.nextInt();
				M = sc.nextInt();

				adj1 = new ArrayList[N + 1];
				adj2 = new ArrayList[N + 1];

				//  Read first graph
				for (int i = 0; i < M; i++) {
					int v1 = sc.nextInt();
					int v2 = sc.nextInt();

					if (adj1[v1] == null) {
						adj1[v1] = new ArrayList<>();
					}
					adj1[v1].add(v2);
				}

				//  Read second graph but only add common edges
				//  This is the graph on which we count the chains
				internal_grades = new int[N + 1];
				for (int i = 0; i < M; i++) {
					int v1 = sc.nextInt();
					int v2 = sc.nextInt();

					if (adj1[v1] != null) {
						for (Integer neigh: adj1[v1]) {
							//  Add edge
							if (neigh == v2) {
								if (adj2[v1] == null) {
									adj2[v1] = new ArrayList<>();
								}
								adj2[v1].add(v2);
								internal_grades[v2]++;

								break;
							}
						}
					}
				}

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

		private long getResult() {
			long[] possibleWays = new long[N + 1];
			ArrayList<Integer> queue = new ArrayList<>();

			//  First add 1's neighbours, because there is only one way
			for (Integer neigh: adj2[1]) {
				possibleWays[neigh] = 1;
				internal_grades[neigh]--;

				if (internal_grades[neigh] == 0) {
					queue.add(neigh);
				}
			}

			//  Then parse all other nodes and add the possibilities
			//  Only add vertex to queue after all the ways were processed
			while (!queue.isEmpty()) {
				int node = queue.remove(0);

				if (adj2[node] != null) {
					for (Integer neigh: adj2[node]) {
						possibleWays[neigh] = (possibleWays[neigh]
								+ possibleWays[node]) % MODULO;
						internal_grades[neigh]--;

						if (internal_grades[neigh] == 0) {
							queue.add(neigh);
						}
					}
				}
			}

			return possibleWays[N];
		}
	}

	public static void main(String[] args) {
		new Task().solve();
	}
}
