import java.io.*;
import java.util.*;

public class Main {
    public static int n, q;
    public static int[] parents = new int[100001];
    public static int[] powers = new int[100001];
    public static int[][] arr = new int[100001][21];
    public static int[] values = new int[100001];
    public static boolean[] onOffs = new boolean[100001];
    public static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        st = new StringTokenizer(br.readLine());
        st.nextToken();

        for (int i = 1; i <= n; i++) {
            parents[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 1; i <= n; i++) {
            powers[i] = Math.min(Integer.parseInt(st.nextToken()), 20);
        }

        for (int i = 1; i <= n; i++) {
            int cur = i;
            int power = powers[i];

            arr[cur][power]++;

            while (cur != 0 && power != 0) {
                cur = parents[cur];
                power--;

                if (power != 0) {
                    arr[cur][power]++;
                }
                
                values[cur]++;
            }
        }

        while (q-- > 1) {
            st = new StringTokenizer(br.readLine());

            switch (st.nextToken()) {
                case "200":
                    toggleOnOffs(Integer.parseInt(st.nextToken()));
                    break;
                case "300":
                    changePower(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    break;
                case "400":
                    changeParent(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    break;
                case "500":
                    appendValue(Integer.parseInt(st.nextToken()));
            }
        }

        System.out.println(sb);
    }

    public static void toggleOnOffs(int c) {
        int cur = parents[c];
        int num = 1;

        while (cur != 0) {
            for (int i = num; i <= 20; i++) {
                int temp = arr[c][i];

                if (!onOffs[c]) {
                    temp *= -1;
                }

                if (i != num) {
                    arr[cur][i - num] += temp;
                }

                values[cur] += temp;
            }

            if (onOffs[cur]) {
                break;
            }

            cur = parents[cur];
            num++;
        }

        onOffs[c] = !onOffs[c];
    }

    public static void changePower(int c, int power) {
        int beforePower = powers[c];

        power = Math.min(power, 20);
        powers[c] = power;
        arr[c][beforePower]--;
        arr[c][power]++;

        if (!onOffs[c]) {
            int cur = parents[c];
            int num = 1;

            while (cur != 0) {
                if (beforePower > num) {
                    arr[cur][beforePower - num]--;
                }
                
                if (beforePower >= num) {
                    values[cur]--;
                }

                if (onOffs[cur]) {
                    break;
                }

                cur = parents[cur];
                num++;
            }

            cur = parents[c];
            num = 1;

            while (cur != 0) {
                if (power > num) {
                    arr[cur][power - num]++;
                }
                
                if (power >= num) {
                    values[cur]++;
                }

                if (onOffs[cur]) {
                    break;
                }

                cur = parents[cur];
                num++;
            }
        }
    }

    public static void changeParent(int c1, int c2) {
        boolean beforeOnOff1 = onOffs[c1];
        boolean beforeOnOff2 = onOffs[c2];
        int temp = parents[c1];

        if (!beforeOnOff1) {
            toggleOnOffs(c1);
        }

        if (!beforeOnOff2) {
            toggleOnOffs(c2);
        }

        parents[c1] = parents[c2];
        parents[c2] = temp;

        if (!beforeOnOff1) {
            toggleOnOffs(c1);
        }

        if (!beforeOnOff2) {
            toggleOnOffs(c2);
        }
    }

    public static void appendValue(int c) {
        sb.append(values[c]).append("\n");
    }
}