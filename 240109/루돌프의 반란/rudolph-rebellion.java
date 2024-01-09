import java.io.*;
import java.util.*;

public class Main {
    public static int n, m, p, num, c, d;
    public static int[][] board = new int[51][51];
    public static int[] rudolf;
    public static int[][] santas = new int[31][4];
    public static int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = num = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());

        int rr = Integer.parseInt(st.nextToken());
        int rc = Integer.parseInt(st.nextToken());

        board[rr][rc] = -1;
        rudolf = new int[]{rr, rc};

        for (int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine());

            int idx = Integer.parseInt(st.nextToken());
            int sr = Integer.parseInt(st.nextToken());
            int sc = Integer.parseInt(st.nextToken());

            board[sr][sc] = idx;
            santas[idx][0] = sr;
            santas[idx][1] = sc;
        }

        while (m != 0 && num != 0) {
            m--;
            moveRudolf();
            moveSantas();

            for (int i = 1; i <= p; i++) {
                if (santas[i][3] >= 0) {
                    santas[i][2]++;
                }
            }
        }


        for (int i = 1; i <= p; i++) {
            sb.append(santas[i][2]).append(" ");
        }

        System.out.println(sb);
    }

    public static void moveRudolf() {
        boolean flag = true;
        int x = 1;
        int[] santa = new int[2];

        while (flag) {
            for (int i = Math.max(rudolf[0] - x, 0); i <= Math.min(rudolf[0] + x, n); i++) {
                if (i == rudolf[0] - x || i == rudolf[0] + x) {
                    for (int j = Math.max(rudolf[1] - x, 0); j <= Math.min(rudolf[1] + x, n); j++) {
                        if (board[i][j] > 0) {
                            santa[0] = i;
                            santa[1] = j;
                            flag = false;
                        }
                    }
                } else {
                    if (rudolf[1] - x > 0 && board[i][rudolf[1] - x] > 0) {
                        santa[0] = i;
                        santa[1] = rudolf[1] - x;
                        flag = false;
                    }

                    if (rudolf[1] + x <= n && board[i][rudolf[1] + x] > 0) {
                        santa[0] = i;
                        santa[1] = rudolf[1] + x;
                        flag = false;
                    }
                }
            }

            x++;
        }

        int tr = santa[0] - rudolf[0];
        int tc = santa[1] - rudolf[1];
        int dirR = 0;
        int dirC = 0;

        if (tr != 0) {
            dirR = Math.abs(tr) / tr;
        }

        if (tc != 0) {
            dirC = Math.abs(tc) / tc;
        }

        int nr = rudolf[0] + dirR;
        int nc = rudolf[1] + dirC;

        board[rudolf[0]][rudolf[1]] = 0;

        if (board[nr][nc] > 0) {
            collide(nr, nc, dirR, dirC, -1, board[nr][nc]);
        }

        board[nr][nc] = -1;
        rudolf[0] = nr;
        rudolf[1] = nc;
    }

    public static void moveSantas() {
        for (int i = 1; i <= p; i++) {
            if (santas[i][3] != 0) {
                santas[i][3]--;
                continue;
            }

            int sr = santas[i][0];
            int sc = santas[i][1];
            int nr, nc;
            int fr = 0;
            int fc = 0;
            int minDis = (sr - rudolf[0]) * (sr - rudolf[0]) + (sc - rudolf[1]) * (sc - rudolf[1]);
            int minDir = -1;

            for (int j = 0; j < 4; j++) {
                nr = sr + dirs[j][0];
                nc = sc + dirs[j][1];

                if (nr < 1 || n < nr || nc < 1 || n < nc || board[nr][nc] > 0) {
                    continue;
                }

                int dis = (nr - rudolf[0]) * (nr - rudolf[0]) + (nc - rudolf[1]) * (nc - rudolf[1]);

                if (dis < minDis) {
                    fr = nr;
                    fc = nc;
                    minDis = dis;
                    minDir = j;
                }
            }

            if (minDir != -1) {
                board[sr][sc] = 0;

                if (board[fr][fc] == -1) {
                    collide(fr, fc, dirs[minDir][0], dirs[minDir][1], i, -1);
                } else {
                    board[fr][fc] = i;
                    santas[i][0] = fr;
                    santas[i][1] = fc;
                }

            }
        }
    }

    public static void collide(int nr, int nc, int dirR, int dirC, int idx1, int idx2) {
        int sr, sc, sDirR, sDirC, idx;

        if (idx1 == -1) {
            sr = nr + dirR * c;
            sc = nc + dirC * c;
            sDirR = dirR;
            sDirC = dirC;
            idx = idx2;
            santas[idx][2] += c;
        } else {
            sr = nr - dirR * d;
            sc = nc - dirC * d;
            sDirR = -dirR;
            sDirC = -dirC;
            idx = idx1;
            santas[idx][2] += d;
        }

        if (sr < 1 || n < sr || sc < 1 || n < sc) {
            num--;
            santas[idx][3] = -1;
        } else {
            if (board[sr][sc] > 0) {
                interact(sr, sc, sDirR, sDirC, board[sr][sc]);
            }

            board[sr][sc] = idx;
            santas[idx][0] = sr;
            santas[idx][1] = sc;

            if (idx1 != -1) {
                santas[idx][3] = 1;
            } else {
                santas[idx][3] = 2;
            }
        }
    }

    public static void interact(int sr, int sc, int dirR, int dirC, int idx) {
        int nr = sr + dirR;
        int nc = sc + dirC;

        if (nr < 1 || n < nr || nc < 1 || n < nc) {
            num--;
            santas[idx][3] = -1;
        } else {
            if (board[nr][nc] > 0) {
                interact(nr, nc, dirR, dirC, board[nr][nc]);
            }

            board[nr][nc] = idx;
            santas[idx][0] = nr;
            santas[idx][1] = nc;
        }
    }
}