import java.io.InputStream
import java.io.PrintWriter

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1 shl 16)
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
            if (c < 48 || c > 57) {
                c = read()
                continue
            }
            res = res * 10 + c - 48
            c = read()
        }
        return res
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1L
            c = read()
        }
        var res = 0L
        while (c > 32) {
            if (c < 48 || c > 57) {
                c = read()
                continue
            }
            res = res * 10 + c - 48
            c = read()
        }
        return res
    }
}

val MAXN = 200005
val head = IntArray(MAXN)
val next = IntArray(MAXN * 2)
val to = IntArray(MAXN * 2)
val parent = IntArray(MAXN)
val order = IntArray(MAXN)
val vis = BooleanArray(MAXN)
val sub = LongArray(MAXN)
val dp = IntArray(MAXN)
val a = LongArray(MAXN)

val childStart = IntArray(MAXN)
val childLen = IntArray(MAXN)
val childrenArr = IntArray(MAXN)

val lDp = IntArray(MAXN)
val lSub = LongArray(MAXN)
val prefMax = IntArray(MAXN)

fun main() {
    val scanner = FastScanner(System.`in`)
    val out = PrintWriter(System.out)

    var t = scanner.nextInt()
    if (t == -1) return

    while (t-- > 0) {
        val n = scanner.nextInt()
        val k = scanner.nextInt()

        for (i in 1..n) {
            a[i] = scanner.nextLong()
            head[i] = -1
            vis[i] = false
        }

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

        var qHead = 0
        var qTail = 0
        order[qTail++] = 1
        vis[1] = true

        while (qHead < qTail) {
            val u = order[qHead++]
            var e = head[u]
            while (e != -1) {
                val v = to[e]
                if (!vis[v]) {
                    vis[v] = true
                    parent[v] = u
                    order[qTail++] = v
                }
                e = next[e]
            }
        }

        for (i in 1..n) sub[i] = a[i]
        for (i in n - 1 downTo 0) {
            val u = order[i]
            if (u != 1) {
                sub[parent[u]] += sub[u]
            }
        }

        val S = sub[1]

        var childPtr = 0
        for (i in 1..n) {
            childStart[i] = childPtr
            var count = 0
            var e = head[i]
            while (e != -1) {
                val v = to[e]
                if (v != parent[i]) {
                    childrenArr[childPtr + count] = v
                    count++
                }
                e = next[e]
            }
            childLen[i] = count

            if (count > 0) {
                val temp = Array(count) { childrenArr[childPtr + it] }
                temp.sortBy { sub[it] }
                for (j in 0 until count) {
                    childrenArr[childPtr + j] = temp[j]
                }
                childPtr += count
            }
        }

        fun check(W: Long): Boolean {
            var ansK = 0

            for (i in n - 1 downTo 0) {
                val u = order[i]
                var curDp = -1
                if (sub[u] >= W) {
                    curDp = 0
                }

                val start = childStart[u]
                val len = childLen[u]
                var m = 0

                for (j in 0 until len) {
                    val v = childrenArr[start + j]
                    if (dp[v] != -1) {
                        lDp[m] = dp[v]
                        lSub[m] = sub[v]
                        m++
                    }
                }

                if (m > 0) {
                    if (m >= 2) {
                        prefMax[0] = lDp[0]
                        for (j in 1 until m) {
                            prefMax[j] = if (prefMax[j - 1] > lDp[j]) prefMax[j - 1] else lDp[j]
                        }

                        var p1 = 0
                        val target = S - W
                        for (p2 in m - 1 downTo 1) {
                            if (p1 >= p2) p1 = p2 - 1
                            while (p1 + 1 < p2 && lSub[p1 + 1] + lSub[p2] <= target) {
                                p1++
                            }
                            if (p1 < p2 && lSub[p1] + lSub[p2] <= target) {
                                val poss = prefMax[p1] + lDp[p2] + 2
                                if (poss > ansK) ansK = poss
                            }
                        }
                    }

                    for (j in 0 until m) {
                        if (S - lSub[j] >= W) {
                            val poss = lDp[j] + 1
                            if (poss > ansK) ansK = poss
                        }
                    }

                    for (j in 0 until m) {
                        if (sub[u] - lSub[j] >= W) {
                            val poss = lDp[j] + 1
                            if (poss > curDp) curDp = poss
                        }
                    }
                }
                dp[u] = curDp
                if (ansK >= k) return true
            }
            return ansK >= k
        }

        if (!check(1L)) {
            out.println(-1)
        } else {
            var low = 1L
            var high = S
            var best = -1L

            while (low <= high) {
                val mid = (low + high) / 2L
                if (check(mid)) {
                    best = mid
                    low = mid + 1
                } else {
                    high = mid - 1
                }
            }
            out.println(best)
        }
    }
    out.flush()
}