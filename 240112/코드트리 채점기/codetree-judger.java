import java.io.*;
import java.util.*;

public class Main {
    static class Tuple implements Comparable<Tuple> {
        int p, t, id;

        Tuple(int p, int t, int id) {
            this.p = p;
            this.t = t;
            this.id = id;
        }

        @Override
        public int compareTo(Tuple other) {
            if (this.p != other.p) {
                return Integer.compare(this.p, other.p);
            }

            return Integer.compare(this.t, other.t);
        }
    }

    static HashMap<String, Integer> map = new HashMap<>();
    static int mapIdx = 1;
    static PriorityQueue<Tuple>[] que = new PriorityQueue[301];
    static int size = 0;
    static HashSet<Integer>[] set = new HashSet[301];
    static PriorityQueue<Integer> idxQue = new PriorityQueue<>();
    static int[][] info = new int[301][3];
    static int[] grader = new int[50001];
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int q = Integer.parseInt(st.nextToken());
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());

            switch (st.nextToken()) {
                case "100":
                    init(Integer.parseInt(st.nextToken()), st.nextToken());
                    break;
                case "200":
                    request(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken());
                    break;
                case "300":
                    assign(Integer.parseInt(st.nextToken()));
                    break;
                case "400":
                    finish(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    break;
                case "500":
                    check();
            }
        }

        System.out.println(sb);
    }

    static void init(int n, String u) {
        String[] data = u.split("/");
        String d = data[0];
        int id = Integer.parseInt(data[1]);

        map.put(d, mapIdx);
        mapIdx++;

        int idx = map.get(d);

        que[idx] = new PriorityQueue<>();
        que[idx].offer(new Tuple(1, 0, id));
        size++;
        set[idx] = new HashSet<>();
        set[idx].add(id);

        for (int i = 1; i <= n; i++) {
            idxQue.offer(i);
        }
    }

    static void request(int t, int p , String u) {
        String[] data = u.split("/");
        String d = data[0];
        int id = Integer.parseInt(data[1]);
        int idx = 0;

        if (!map.containsKey(d)) {
            map.put(d, mapIdx);
            mapIdx++;
            idx = map.get(d);
            que[idx] = new PriorityQueue<>();
            set[idx] = new HashSet<>();
        }

        idx = map.get(d);

        if (set[idx].contains(id)) {
            return;
        }

        que[map.get(d)].add(new Tuple(p, t, id));
        size++;
        set[idx].add(id);
    }

    static void assign(int t) {
        if (idxQue.isEmpty()) {
            return;
        }

        Tuple minTuple = new Tuple(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        int minIdx = 0;

        for (int i = 1; i < mapIdx; i++) {
            if (que[i].isEmpty() || info[i][0] > 0 || info[i][2] > t) {
                continue;
            }

            Tuple tuple = que[i].peek();

            if (minTuple.compareTo(tuple) > 0) {
                minTuple = tuple;
                minIdx = i;
            }
        }

        if (minIdx > 0) {
            int idx = idxQue.poll();

            size--;
            set[minIdx].remove(que[minIdx].poll().id);
            info[minIdx][0] = idx;
            info[minIdx][1] = t;
            grader[idx] = minIdx;
        }
    }

    static void finish(int t, int idx1) {
        int idx2 = grader[idx1];

        if (idx2 == 0) {
            return;
        }

        idxQue.offer(idx1);
        info[idx2][0] = 0;
        info[idx2][2] = t * 3 - info[idx2][1] * 2;
    }

    static void check() {
        sb.append(size).append("\n");
    }
}