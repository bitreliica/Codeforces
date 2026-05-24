import java.io.InputStream

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1024 * 64)
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
            if (c >= '0'.code && c <= '9'.code) {
                res = res * 10 + c - '0'.code
            }
            c = read()
        }
        return res
    }
}

fun main() {
    val scanner = FastScanner(System.`in`)
    val multTestQ = scanner.nextInt()
    if (multTestQ == -1) return

    val out = StringBuilder()

    for (t in 0 until multTestQ) {
        val n = scanner.nextInt()
        val a = IntArray(n + 1)
        for (i in 1..n) {
            a[i] = scanner.nextInt()
        }
        val b = IntArray(n + 1)
        for (i in 1..n) {
            b[i] = scanner.nextInt()
        }

        val dp = IntArray(n + 2)
        val parent = IntArray(n + 2)
        val minFail = IntArray(n + 2)

        for (i in 1..n + 1) {
            parent[i] = i
            minFail[i] = n + 1
        }

        fun find(i: Int): Int {
            var root = i
            while (root != parent[root]) {
                root = parent[root]
            }
            var curr = i
            while (curr != root) {
                val nxt = parent[curr]
                parent[curr] = root
                curr = nxt
            }
            return root
        }

        for (i in 1..n) {
            val x = a[i]
            val y = b[i]

            if (x != y) {
                var l = dp[x] + 1
                var r = if (x == 1) i else dp[x - 1]
                if (l <= r) {
                    var curr = find(l)
                    while (curr <= r) {
                        minFail[curr] = i
                        parent[curr] = curr + 1
                        curr = find(curr)
                    }
                }

                l = dp[y] + 1
                r = if (y == 1) i else dp[y - 1]
                if (l <= r) {
                    var curr = find(l)
                    while (curr <= r) {
                        minFail[curr] = i
                        parent[curr] = curr + 1
                        curr = find(curr)
                    }
                }
            } else {
                dp[x] = if (x == 1) i else dp[x - 1]
            }
        }

        var ans: Long = 0
        for (i in 1..n) {
            val maxR = minFail[i] - 1
            if (maxR >= i) {
                ans += (maxR - i + 1).toLong()
            }
        }
        out.append(ans).append("\n")
    }
    print(out.toString())
}