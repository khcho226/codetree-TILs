import java.io.*;
import java.util.*;

public class Main {
    static class Tuple implements Comparable<Tuple> {
        int p, t;
        String u;

        Tuple(int p, int t, String u) {
            this.p = p;
            this.t = t;
            this.u = u;
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
    static HashSet<String> set = new HashSet<>();
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
        String d = u.split("/")[0];

        map.put(d, mapIdx);
        mapIdx++;
        que[map.get(d)] = new PriorityQueue<>();
        que[map.get(d)].offer(new Tuple(1, 0, u));
        size++;
        set.add(u);

        for (int i = 1; i <= n; i++) {
            idxQue.offer(i);
        }
    }

    static void request(int t, int p , String u) {
        if (set.contains(u)) {
            return;
        }

        String d = u.split("/")[0];

        if (!map.containsKey(d)) {
            map.put(d, mapIdx);
            mapIdx++;
            que[map.get(d)] = new PriorityQueue<>();
        }

        que[map.get(d)].offer(new Tuple(p, t, u));
        size++;
        set.add(u);
    }

    static void assign(int t) {
        if (idxQue.isEmpty()) {
            return;
        }

        Tuple minTuple = new Tuple(Integer.MAX_VALUE, Integer.MAX_VALUE, "");
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
            set.remove(que[minIdx].poll().u);
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
        grader[idx1] = 0;
    }

    static void check() {
        sb.append(size).append("\n");
    }
}