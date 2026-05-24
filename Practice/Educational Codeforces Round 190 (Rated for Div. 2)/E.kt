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
            res = res * 10 + c - '0'.code
            c = read()
        }
        return res
    }
}

data class Point(val p: Int, val c: Int)

fun main() {
    val scanner = FastScanner(System.`in`)
    val n = scanner.nextInt()
    if (n == -1) return

    val pArr = IntArray(n)
    for (i in 0 until n) pArr[i] = scanner.nextInt()
    
    val cArr = IntArray(n)
    for (i in 0 until n) cArr[i] = scanner.nextInt()
    val points = Array(n) { Point(pArr[it], cArr[it]) }
    points.sortWith(compareBy({ it.p }, { it.c }))

    val paretoP = IntArray(n)
    val paretoC = IntArray(n)
    var k = 0
    var minC = Int.MAX_VALUE

    for (pt in points) {
        if (pt.c < minC) {
            paretoP[k] = pt.p
            paretoC[k] = pt.c
            k++
            minC = pt.c
        }
    }

    val log = IntArray(k + 1)
    for (i in 2..k) log[i] = log[i / 2] + 1
    val maxLog = log[k] + 1
    val st = Array(maxLog) { IntArray(k) }

    for (i in 0 until k) {
        st[0][i] = paretoP[i] + paretoC[i]
    }

    for (j in 1 until maxLog) {
        val len = 1 shl (j - 1)
        for (i in 0..k - (1 shl j)) {
            st[j][i] = minOf(st[j - 1][i], st[j - 1][i + len])
        }
    }

    fun queryST(L: Int, R: Int): Int {
        if (L > R) return Int.MAX_VALUE
        val j = log[R - L + 1]
        return minOf(st[j][L], st[j][R - (1 shl j) + 1])
    }

    val m = scanner.nextInt()
    val tpArr = IntArray(m)
    for (i in 0 until m) tpArr[i] = scanner.nextInt()
    val tcArr = IntArray(m)
    for (i in 0 until m) tcArr[i] = scanner.nextInt()
    val dArr = IntArray(m)
    for (i in 0 until m) dArr[i] = scanner.nextInt()

    val out = StringBuilder()

    fun lowerBoundP(target: Int): Int {
        var l = 0; var r = k - 1; var ans = k
        while (l <= r) {
            val mid = (l + r) ushr 1
            if (paretoP[mid] >= target) { ans = mid; r = mid - 1 }
            else { l = mid + 1 }
        }
        return ans
    }

    fun upperBoundP(target: Int): Int {
        var l = 0; var r = k - 1; var ans = k
        while (l <= r) {
            val mid = (l + r) ushr 1
            if (paretoP[mid] > target) { ans = mid; r = mid - 1 }
            else { l = mid + 1 }
        }
        return ans
    }

    fun firstCLe(target: Int): Int {
        var l = 0; var r = k - 1; var ans = k
        while (l <= r) {
            val mid = (l + r) ushr 1
            if (paretoC[mid] <= target) { ans = mid; r = mid - 1 }
            else { l = mid + 1 }
        }
        return ans
    }

    fun firstCLt(target: Int): Int {
        var l = 0; var r = k - 1; var ans = k
        while (l <= r) {
            val mid = (l + r) ushr 1
            if (paretoC[mid] < target) { ans = mid; r = mid - 1 }
            else { l = mid + 1 }
        }
        return ans
    }

    for (i in 0 until m) {
        val tp = tpArr[i]
        val tc = tcArr[i]
        val d = dArr[i]

        fun getCost(idx: Int): Int {
            if (idx !in 0 until k) return Int.MAX_VALUE
            val p = paretoP[idx]
            val c = paretoC[idx]
            val ip = if (p < tp) 0 else minOf(p, tp + d)
            val ic = if (c < tc) 0 else minOf(c, tc + d)
            return ip + ic
        }

        val idx1 = lowerBoundP(tp)
        val idx2 = upperBoundP(tp + d)
        val idx3 = firstCLe(tc + d)
        val idx4 = firstCLt(tc)
        var ans = minOf(getCost(0), getCost(k - 1))
        
        val indices = intArrayOf(idx1, idx2, idx3, idx4)
        for (idx in indices) {
            ans = minOf(ans, getCost(idx - 1))
            ans = minOf(ans, getCost(idx))
            ans = minOf(ans, getCost(idx + 1))
        }

        val lRmq = maxOf(idx1, idx3)
        val rRmq = minOf(idx2 - 1, idx4 - 1)
        if (lRmq <= rRmq) {
            ans = minOf(ans, queryST(lRmq, rRmq))
        }

        out.append(ans).append("\n")
    }

    print(out)
}