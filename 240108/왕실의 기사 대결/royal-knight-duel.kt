import java.io.*
import java.util.*

val board = Array(41) { IntArray(41) }
val knights = Array(31) { IntArray(5) }
val newKnights = Array(31) { IntArray(3) }
var l = 0
var n = 0
var q = 0

fun main() = BufferedReader(InputStreamReader(System.`in`)).run {
    val originalHps = IntArray(31)
    var answer = 0

    StringTokenizer(readLine()).also {
        l = it.nextToken().toInt()
        n = it.nextToken().toInt()
        q = it.nextToken().toInt()
    }

    for (i in 1..l) {
        StringTokenizer(readLine()).also {
            for (j in 1..l) {
                board[i][j] = it.nextToken().toInt()
            }
        }
    }

    for (i in 1..n) {
        StringTokenizer(readLine()).also {
            for (j in 0..4) {
                knights[i][j] = it.nextToken().toInt()
            }
        }

        originalHps[i] = knights[i][4]
    }

    repeat(q) {
        StringTokenizer(readLine()).also {
            moveKnight(it.nextToken().toInt(), it.nextToken().toInt())
        }
    }

    for (i in 1..n) {
        if (knights[i][4] > 0) {
            answer += originalHps[i] - knights[i][4]
        }
    }

    print(answer)
}

fun moveKnight(idx: Int, dir: Int) {
    if (knights[idx][4] <= 0) {
        return
    }

    if (canMove(idx, dir)) {
        for (i in 1..n) {
            knights[i][0] = newKnights[i][0]
            knights[i][1] = newKnights[i][1]
            knights[i][4] -= newKnights[i][2]
        }
    }
}

fun canMove(idx: Int, dir: Int): Boolean {
    val que: Queue<Int> = LinkedList()
    val visited = BooleanArray(31)
    val dirs = arrayOf(intArrayOf(-1, 0), intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(0, -1))

    for (i in 1..n) {
        newKnights[i][0] = knights[i][0]
        newKnights[i][1] = knights[i][1]
        newKnights[i][2] = 0
    }

    que.offer(idx)
    visited[idx] = true

    while (que.isNotEmpty()) {
        val cur = que.poll()

        newKnights[cur][0] += dirs[dir][0]
        newKnights[cur][1] += dirs[dir][1]

        if (newKnights[cur][0] < 1 || l < knights[cur][2] + newKnights[cur][0] - 1 || newKnights[cur][1] < 1 || l < knights[cur][3] + newKnights[cur][1] - 1) {
            return false
        }

        for (i in newKnights[cur][0] until knights[cur][2] + newKnights[cur][0]) {
            for (j in newKnights[cur][1] until knights[cur][3] + newKnights[cur][1]) {
            if (board[i][j] == 1) {
                newKnights[cur][2]++
            } else if (board[i][j] == 2) {
                return false
            }
        }
        }

        for (i in 1..n) {
            if (visited[i] || knights[i][4] <= 0) {
                continue
            }

            if (knights[i][0] + knights[i][2] - 1 < newKnights[cur][0] || knights[cur][2] + newKnights[cur][0] - 1 < knights[i][0]) {
                continue
            }

            if (knights[i][1] + knights[i][3] - 1 < newKnights[cur][1] || knights[cur][3] + newKnights[cur][1] - 1 < knights[i][1]) {
                continue
            }

            que.offer(i)
            visited[i] = true
        }
    }

    newKnights[idx][2] = 0
    return true
}