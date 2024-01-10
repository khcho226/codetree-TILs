import java.io.*;
import java.util.*;

public class Main {
    static int l, q;
    static HashMap<String, Integer> map = new HashMap<>();
    static int mapIdx = 0;
    static Stack<int[]>[] stack = new Stack[15000];
    static PriorityQueue<Integer>[] que = new PriorityQueue[15000];
    static int[] nums = new int[2];
    static boolean[] isEnter = new boolean[15000];
    static int[][] info = new int[15000][3];
    static StringBuilder sb = new StringBuilder();
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        l = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());

            switch (st.nextToken()) {
                case "100": 
                    makeSushi(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken());
                    break;
                case "200":
                    enterStore(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()), st.nextToken(), Integer.parseInt(st.nextToken()));
                    break;
                case "300":
                    takePhoto(Integer.parseInt(st.nextToken()));
            }
        }

        System.out.println(sb);
    }

    static void makeSushi(int t, int x, String name) {
        checkMap(name);
        nums[1]++;

        int idx = map.get(name);

        if (!isEnter[idx]) {
            stack[idx].add(new int[]{t, x});
        } else {
            putQue(t, x, info[idx][0], info[idx][1], idx);
        }
    }

    static void enterStore(int t, int x, String name, int n) {
        checkMap(name);
        nums[0]++;

        int idx = map.get(name);

        isEnter[idx] = true;
        info[idx][0] = t;
        info[idx][1] = x;
        info[idx][2] = n;

        while (!stack[idx].isEmpty()) {
            int[] temp = stack[idx].pop();

            putQue(temp[0], temp[1], t, x, idx);
        }
    }

    static void takePhoto(int t) {
        for (int i = 0; i < mapIdx; i++) {
            while (!que[i].isEmpty() && que[i].peek() <= t) {
                que[i].poll();
                info[i][2]--;

                if (info[i][2] == 0) {
                    nums[0]--;
                }

                nums[1]--;
            }
        }

        sb.append(nums[0]).append(" ").append(nums[1]).append("\n");
    }

    static void checkMap(String name) {
        if (!map.containsKey(name)) {
            map.put(name, mapIdx);
            stack[mapIdx] = new Stack<>();
            que[mapIdx] = new PriorityQueue<>();
            mapIdx++;
        }
    }

    static void putQue(int t1, int x1, int t2, int x2, int idx) {
        int temp = x1 + t2 - t1;

        if (temp >= l) {
            temp -= l;
        }

        temp = x2 - temp;

        if (temp < 0) {
            temp += l;
        }

        que[idx].offer(t2 + temp);
    }
}