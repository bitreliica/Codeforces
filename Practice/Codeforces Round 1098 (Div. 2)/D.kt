import java.io.InputStream

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(32768)
    private var head = 0
    private var tail = 0

    private fun read(): Int {
        if (head >= tail) {
            head = 0
            tail = stream.read(buffer, 0, buffer.size)
            if (tail <= 0) return -1
        }
        return buffer[head++].toInt()
    }

    fun nextInt(): Int {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1
            c = read()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + c - '0'.code
            c = read()
        }
        return res
    }
}

fun main() {
    val scanner = FastScanner(System.`in`)
    val t = scanner.nextInt()
    if (t == -1) return

    val out = StringBuilder()

    for (tc in 0 until t) {
        val n = scanner.nextInt()

        val minYAtX = IntArray(n + 1) { Int.MAX_VALUE }
        val maxYAtX = IntArray(n + 1) { Int.MIN_VALUE }
        val hasY = BooleanArray(n + 1)

        for (i in 0 until n) {
            val x = scanner.nextInt()
            val y = scanner.nextInt()
            if (y < minYAtX[x]) minYAtX[x] = y
            if (y > maxYAtX[x]) maxYAtX[x] = y
            hasY[y] = true
        }

        val rankY = IntArray(n + 1)
        var currentRank = 1
        for (y in 1..n) {
            if (hasY[y]) {
                rankY[y] = currentRank++
            }
        }

        val uniqueX = IntArray(n)
        var u = 0
        for (x in 1..n) {
            if (minYAtX[x] != Int.MAX_VALUE) {
                uniqueX[u++] = x
            }
        }

        if (u < 2) {
            out.append(0).append('\n')
            continue
        }

        val prefMin = IntArray(u)
        val prefMax = IntArray(u)

        prefMin[0] = minYAtX[uniqueX[0]]
        prefMax[0] = maxYAtX[uniqueX[0]]
        for (i in 1 until u) {
            val x = uniqueX[i]
            prefMin[i] = minOf(prefMin[i - 1], minYAtX[x])
            prefMax[i] = maxOf(prefMax[i - 1], maxYAtX[x])
        }

        val suffMin = IntArray(u)
        val suffMax = IntArray(u)

        suffMin[u - 1] = minYAtX[uniqueX[u - 1]]
        suffMax[u - 1] = maxYAtX[uniqueX[u - 1]]
        for (i in u - 2 downTo 0) {
            val x = uniqueX[i]
            suffMin[i] = minOf(suffMin[i + 1], minYAtX[x])
            suffMax[i] = maxOf(suffMax[i + 1], maxYAtX[x])
        }

        var ans = 0L
        for (i in 0 until u - 1) {
            val rMaxL = rankY[prefMax[i]]
            val rMinL = rankY[prefMin[i]]
            val rMaxR = rankY[suffMax[i + 1]]
            val rMinR = rankY[suffMin[i + 1]]

            val high = minOf(rMaxL, rMaxR)
            val low = maxOf(rMinL, rMinR)

            if (high > low) {
                ans += (high - low)
            }
        }
        out.append(ans).append('\n')
    }
    print(out)
}