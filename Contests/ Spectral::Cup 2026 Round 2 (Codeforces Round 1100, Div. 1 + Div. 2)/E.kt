import java.io.InputStream
import java.lang.StringBuilder

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1024)
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
            if (c in '0'.code..'9'.code) {
                res = res * 10 + c - '0'.code
            }
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
    val MOD = 998244353
    val MAXN = 200005
    
    val head = IntArray(MAXN)
    val next = IntArray(MAXN * 2)
    val to = IntArray(MAXN * 2)
    val parent = IntArray(MAXN)
    val order = IntArray(MAXN)
    val q = IntArray(MAXN)
    val f = IntArray(MAXN)
    val req = IntArray(MAXN)
    val dp = IntArray(MAXN)
    val S = IntArray(MAXN)

    for (tc in 0 until t) {
        val n = scanner.nextInt()
        
        for (i in 0..n) {
            head[i] = -1
        }
        
        var edgeCount = 0
        fun addEdge(u: Int, v: Int) {
            to[edgeCount] = v
            next[edgeCount] = head[u]
            head[u] = edgeCount++
        }

        var degN = 0
        for (i in 0 until n - 1) {
            val u = scanner.nextInt()
            val v = scanner.nextInt()
            addEdge(u, v)
            addEdge(v, u)
            if (u == n || v == n) degN++
        }

        if (degN == 1) {
            out.append("1\n")
            continue
        }

        var qHead = 0
        var qTail = 0
        var orderIdx = 0
        
        q[qTail++] = n
        parent[n] = 0
        
        while (qHead < qTail) {
            val u = q[qHead++]
            order[orderIdx++] = u
            var e = head[u]
            while (e != -1) {
                val v = to[e]
                if (v != parent[u]) {
                    parent[v] = u
                    q[qTail++] = v
                }
                e = next[e]
            }
        }

        var L0 = 0
        for (i in n - 1 downTo 0) {
            val u = order[i]
            if (u == n) continue
            var maxReq = 0
            var hasChild = false
            var e = head[u]
            while (e != -1) {
                val v = to[e]
                if (v != parent[u]) {
                    hasChild = true
                    if (req[v] > maxReq) {
                        maxReq = req[v]
                    }
                }
                e = next[e]
            }
            if (!hasChild) {
                f[u] = 0
                if (u > L0) L0 = u
            } else {
                f[u] = maxReq
            }
            req[u] = maxOf(f[u], u + 1)
        }

        var max1 = -1
        var max2 = -1
        var en = head[n]
        while (en != -1) {
            val v = to[en]
            if (v != parent[n]) {
                val r = req[v]
                if (r > max1) {
                    max2 = max1
                    max1 = r
                } else if (r > max2) {
                    max2 = r
                }
            }
            en = next[en]
        }
        val reqN = if (max2 == -1) 0 else max2

        for (i in 0 until L0) {
            dp[i] = 0
            S[i] = 0
        }
        dp[L0] = 1
        S[L0] = 1

        for (v in L0 + 1 until n) {
            val limit = maxOf(L0, f[v])
            var ways = 0
            if (limit <= v - 1) {
                val sub = if (limit - 1 >= 0) S[limit - 1] else 0
                ways = S[v - 1] - sub
                if (ways < 0) ways += MOD
            }
            dp[v] = ways
            S[v] = S[v - 1] + ways
            if (S[v] >= MOD) S[v] -= MOD
        }

        val startSum = maxOf(L0, reqN)
        var ans = 0
        if (startSum < n) {
            val sub = if (startSum - 1 >= 0) S[startSum - 1] else 0
            ans = S[n - 1] - sub
            if (ans < 0) ans += MOD
        }
        
        out.append(ans).append("\n")
    }
    print(out.toString())
}