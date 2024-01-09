import java.io.*;
import java.util.*;

public class Main {
    static class Tuple implements Comparable<Tuple> {
        int first, second, third;

        Tuple(int first, int second, int third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        @Override
        public int compareTo(Tuple other) {
            if (this.first != other.first) {
                return Integer.compare(this.first, other.first);
            }

            if (this.second != other.second) {
                return Integer.compare(this.second, other.second);
            }

            return Integer.compare(this.third, other.third);
        }
    }

    static int n, m, p, c, d, num;
    static int[][] board = new int[51][51];
    static int[] rudolf;
    static int[][] santas = new int[31][4];
    static int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        p = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        d = Integer.parseInt(st.nextToken());
        num = p;
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

    static void moveRudolf() {
        int[] santa = {100, 100};

        for (int i = 1; i <= p; i++) {
            if (santas[i][3] >= 0) {
                int sr = santas[i][0];
                int sc = santas[i][1];
                Tuple newTuple = new Tuple((sr - rudolf[0]) * (sr - rudolf[0]) + (sc - rudolf[1]) * (sc - rudolf[1]), -sr, -sc);
                Tuple curTuple = new Tuple((santa[0] - rudolf[0]) * (santa[0] - rudolf[0]) + (santa[1] - rudolf[1]) * (santa[1] - rudolf[1]), -santa[0], -santa[1]);

                if (newTuple.compareTo(curTuple) < 0) {
                    santa[0] = sr;
                    santa[1] = sc;
                }
            }
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

    static void moveSantas() {
        for (int i = 1; i <= p; i++) {
            if (santas[i][3] != 0) {
                santas[i][3]--;
                continue;
            }

            int sr = santas[i][0];
            int sc = santas[i][1];
            int tr, tc;
            int nr = 0;
            int nc = 0;
            int minDis = (sr - rudolf[0]) * (sr - rudolf[0]) + (sc - rudolf[1]) * (sc - rudolf[1]);
            int minDir = -1;

            for (int j = 0; j < 4; j++) {
                tr = sr + dirs[j][0];
                tc = sc + dirs[j][1];

                if (tr < 1 || n < tr || tc < 1 || n < tc || board[tr][tc] > 0) {
                    continue;
                }

                int dis = (tr - rudolf[0]) * (tr - rudolf[0]) + (tc - rudolf[1]) * (tc - rudolf[1]);

                if (dis < minDis) {
                    nr = tr;
                    nc = tc;
                    minDis = dis;
                    minDir = j;
                }
            }

            if (minDir != -1) {
                board[sr][sc] = 0;

                if (board[nr][nc] == -1) {
                    collide(nr, nc, dirs[minDir][0], dirs[minDir][1], i, -1);
                } else {
                    board[nr][nc] = i;
                    santas[i][0] = nr;
                    santas[i][1] = nc;
                }

            }
        }
    }

    static void collide(int ir, int ic, int dirR, int dirC, int idx1, int idx2) {
        int nr, nc, nDirR, nDirC, idx, s;
        
        if (idx1 == -1) {
            nDirR = dirR;
            nDirC = dirC;
            idx = idx2;
            s = c;
        } else {
            nDirR = -dirR;
            nDirC = -dirC;
            idx = idx1;
            s = d;
        }
        
        santas[idx][2] += s;
        nr = ir + nDirR * s;
        nc = ic + nDirC * s;

        if (nr < 1 || n < nr || nc < 1 || n < nc) {
            num--;
            santas[idx][3] = -1;
        } else {
            if (board[nr][nc] > 0) {
                interact(nr, nc, nDirR, nDirC, board[nr][nc]);
            }

            board[nr][nc] = idx;
            santas[idx][0] = nr;
            santas[idx][1] = nc;

            if (idx1 == -1) {
                santas[idx][3] = 2;
            } else {
                santas[idx][3] = 1;
            }
        }
    }

    static void interact(int ir, int ic, int dirR, int dirC, int idx) {
        int nr = ir + dirR;
        int nc = ic + dirC;

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