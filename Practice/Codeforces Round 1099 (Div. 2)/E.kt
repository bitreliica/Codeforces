import java.io.InputStream

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
            if (c < '0'.code || c > '9'.code) {
            } else {
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
    
    for (tc in 0 until t) {
        val n = scanner.nextInt()
        val d = scanner.nextInt()
        
        val head = IntArray(n + 1) { -1 }
        val to = IntArray((n - 1) * 2)
        val next = IntArray((n - 1) * 2)
        var edgeCount = 0
        
        fun addEdge(u: Int, v: Int) {
            to[edgeCount] = v
            next[edgeCount] = head[u]
            head[u] = edgeCount++
        }
        
        for (i in 0 until n - 1) {
            val u = scanner.nextInt()
            val v = scanner.nextInt()
            addEdge(u, v)
            addEdge(v, u)
        }
        
        var ans = 0L
        val dp1 = Array(n + 1) { LongArray(d) }
        val dp2 = Array(n + 1) { LongArray(d) }
        val len1 = IntArray(n + 1)
        val len2 = IntArray(n + 1) { -1 }
        
        fun dfs(u: Int, p: Int) {
            dp1[u][0] = 1
            len1[u] = 0
            len2[u] = -1
            
            var e = head[u]
            while (e != -1) {
                val v = to[e]
                if (v != p) {
                    dfs(v, u)
                    
                    for (i in 0..minOf(d - 1, len1[u])) {
                        val j = d - 1 - i
                        if (j in 1..(len2[v] + 1)) {
                            ans += dp1[u][i] * dp2[v][j - 1]
                        }
                    }
                    
                    for (i in 0..minOf(d - 1, len2[u])) {
                        val j = d - 1 - i
                        if (j in 1..(len1[v] + 1)) {
                            ans += dp2[u][i] * dp1[v][j - 1]
                        }
                    }
                    
                    for (i in 0..minOf(d - 1, len1[u])) {
                        for (j in 1..minOf(d - 1 - i, len1[v] + 1)) {
                            dp2[u][i + j] += dp1[u][i] * dp1[v][j - 1]
                        }
                    }
                    
                    for (j in 1..minOf(d - 1, len2[v] + 1)) {
                        dp2[u][j] += dp2[v][j - 1]
                    }
                    
                    for (j in 1..minOf(d - 1, len1[v] + 1)) {
                        dp1[u][j] += dp1[v][j - 1]
                    }
                    
                    val newLen2FromV = if (len2[v] != -1) len2[v] + 1 else -1
                    len2[u] = maxOf(len2[u], newLen2FromV, len1[u] + len1[v] + 1)
                    if (len2[u] >= d) len2[u] = d - 1
                    
                    len1[u] = maxOf(len1[u], len1[v] + 1)
                    if (len1[u] >= d) len1[u] = d - 1
                }
                e = next[e]
            }
        }
        
        dfs(1, 0)
        out.append(ans).append("\n")
    }
    print(out)
}