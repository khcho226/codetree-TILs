import java.io.*
import java.util.*

val parents = IntArray(100001)
val powers = IntArray(100001)
val arr = Array(100001) { IntArray(21) }
val values = IntArray(100001)
val onOffs = BooleanArray(100001)
val sb = StringBuilder()

fun main() = BufferedReader(InputStreamReader(System.`in`)).run {
    var st = StringTokenizer(readLine())
    val n = st.nextToken().toInt()
    val q = st.nextToken().toInt()

    st = StringTokenizer(readLine())
    st.nextToken()

    for (i in 1..n) {
        parents[i] = st.nextToken().toInt()
    }

    for (i in 1..n) {
        powers[i] = minOf(st.nextToken().toInt(), 20)
    }

    for (i in 1..n) {
        var cur = i
        var power = powers[i]

        arr[cur][power]++

        while (cur != 0 && power != 0) {
            cur = parents[cur]
            power--

            if (power != 0) {
                arr[cur][power]++
            }

            values[cur]++
        }
    }

    repeat(q - 1) {
        st = StringTokenizer(readLine())

        when (st.nextToken()) {
            "200" -> toggleOnOffs(st.nextToken().toInt())
            "300" -> changePower(st.nextToken().toInt(), st.nextToken().toInt())
            "400" -> changeParent(st.nextToken().toInt(), st.nextToken().toInt())
            "500" -> appendValue(st.nextToken().toInt())
        }
    }

    print(sb)
}

fun toggleOnOffs(c: Int) {
    var cur = parents[c]
    var num = 1

    while (cur != 0) {
        for (i in num..20) {
            var temp = arr[c][i]

            if (onOffs[c].not()) {
                temp *= -1
            }

            if (i != num) {
                arr[cur][i - num] += temp
            }

            values[cur] += temp
        }

        if (onOffs[cur]) {
            break
        }

        cur = parents[cur]
        num++
    }

    onOffs[c] = onOffs[c].not()
}

fun changePower(c: Int, power: Int) {
    val beforePower = powers[c]
    val afterPower = minOf(power, 20)

    powers[c] = afterPower
    arr[c][beforePower]--
    arr[c][afterPower]++

    if (onOffs[c].not()) {
        var cur = parents[c]
        var num = 1

        while (cur != 0) {
            if (beforePower > num) {
                arr[cur][beforePower - num]--
            }

            if (beforePower >= num) {
                values[cur]--
            }

            if (onOffs[cur]) {
                break
            }

            cur = parents[cur]
            num++
        }

        cur = parents[c]
        num = 1

        while (cur != 0) {
            if (afterPower > num) {
                arr[cur][afterPower - num]++
            }

            if (afterPower >= num) {
                values[cur]++
            }

            if (onOffs[cur]) {
                break
            }

            cur = parents[cur]
            num++
        }
    }
}

fun changeParent(c1: Int, c2: Int) {
    val beforeOnOff1 = onOffs[c1].not()
    val beforeOnOff2 = onOffs[c2].not()
    val temp = parents[c1]

    if (beforeOnOff1) {
        toggleOnOffs(c1)
    }

    if (beforeOnOff2) {
        toggleOnOffs(c2)
    }

    parents[c1] = parents[c2]
    parents[c2] = temp

    if (beforeOnOff1) {
        toggleOnOffs(c1)
    }

    if (beforeOnOff2) {
        toggleOnOffs(c2)
    }
}

fun appendValue(c: Int) {
    sb.append("${values[c]}\n")
}