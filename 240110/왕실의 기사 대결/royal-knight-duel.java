import java.io.*;
import java.util.*;

public class Main {
    static int l, n, q;
    static int[][] board = new int[41][41];
    static int[][] knights = new int[31][5];
    static int[][] newKnights = new int[31][3];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int[] originalHps = new int[31];
        int answer = 0;

        l = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());

        for (int i = 1; i <= l; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 1; j <= l; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 1; i <= n; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < 5; j++) {
                knights[i][j] = Integer.parseInt(st.nextToken());
            }

            originalHps[i] = knights[i][4];
        }

        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            moveKnight(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
        }

        for (int i = 1; i <= n; i++) {
            if (knights[i][4] > 0) {
                answer += originalHps[i] - knights[i][4];
            }
        }

        System.out.println(answer);
    }

    static void moveKnight(int idx, int dir) {
        if (knights[idx][4] <= 0) {
            return;
        }

        if (canMove(idx, dir)) {
            for (int i = 1; i <= n; i++) {
                knights[i][0] = newKnights[i][0];
                knights[i][1] = newKnights[i][1];
                knights[i][4] -= newKnights[i][2];
            }
        }
    }

    static boolean canMove(int idx, int dir) {
        Queue<Integer> que = new LinkedList<>();
        boolean[] visited = new boolean[31];
        int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        for (int i = 1; i <= n; i++) {
            newKnights[i][0] = knights[i][0];
            newKnights[i][1] = knights[i][1];
            newKnights[i][2] = 0;
        }

        que.offer(idx);
        visited[idx] = true;

        while (!que.isEmpty()) {
            int cur = que.poll();

            newKnights[cur][0] += dirs[dir][0];
            newKnights[cur][1] += dirs[dir][1];

            int tr = knights[cur][2] + newKnights[cur][0];
            int tc = knights[cur][3] + newKnights[cur][1];

            if (newKnights[cur][0] < 1 || l < tr - 1 || newKnights[cur][1] < 1 || l < tc - 1) {
                return false;
            }

            for (int i = newKnights[cur][0]; i < tr; i++) {
                for (int j = newKnights[cur][1]; j < tc; j++) {
                    if (board[i][j] == 1) {
                        newKnights[cur][2]++;
                    } else if (board[i][j] == 2) {
                        return false;
                    }
                }
            }

            for (int i = 1; i <= n; i++) {
                if (visited[i] || knights[i][4] <= 0 || knights[i][0] + knights[i][2] - 1 < newKnights[cur][0] || tr - 1 < knights[i][0] || knights[i][1] + knights[i][3] - 1 < newKnights[cur][1] || tc - 1 < knights[i][1]) {
                    continue;
                }

                que.offer(i);
                visited[i] = true;
            }
        }

        newKnights[idx][2] = 0;
        return true;
    }
}