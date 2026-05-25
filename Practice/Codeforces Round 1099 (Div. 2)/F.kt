import java.io.InputStream
import java.io.PrintWriter
import java.io.BufferedOutputStream

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
            if (c in '0'.code..'9'.code) {
                res = res * 10 + c - '0'.code
            }
            c = read()
        }
        return res
    }
}

val MAXN = 200000
val isSq = BooleanArray(MAXN + 1)
val isSumOfSq = BooleanArray(MAXN + 1)
val squares = IntArray(500)
var sqCount = 0

val minAdd = LongArray(MAXN + 1) { 1000000000000000000L }
val minSub = LongArray(MAXN + 1) { 1000000000000000000L }

fun init() {
    for (i in 1..450) {
        if (i * i <= MAXN) {
            isSq[i * i] = true
            squares[sqCount++] = i * i
        }
    }

    for (i in 0 until sqCount) {
        for (j in i until sqCount) {
            val sum = squares[i] + squares[j]
            if (sum <= MAXN) {
                isSumOfSq[sum] = true
            }
        }
    }

    val limit = Math.sqrt(MAXN.toDouble()).toInt()
    for (u in 1..limit) {
        var v = u + 2
        while (u * v <= MAXN) {
            val D = u * v
            val A = (v + u) / 2L
            val B = (v - u) / 2L
            val sqA = A * A
            val sqB = B * B
            if (sqA < minAdd[D]) minAdd[D] = sqA
            if (sqB < minSub[D]) minSub[D] = sqB
            v += 2
        }
    }
}

fun dist2(a: Int, b: Int, n: Int): Boolean {
    if (a == b) return true
    val a0 = if (a < b) a else b
    val b0 = if (a < b) b else a
    val D = b0 - a0
    if (isSq[D] || isSumOfSq[D] || a0 + minAdd[D] <= n || a0 - minSub[D] >= 1L) {
        return true
    }
    return false
}

fun dist3(a: Int, b: Int, n: Int): Boolean {
    for (i in 0 until sqCount) {
        val sq = squares[i]
        
        val v1 = a + sq
        if (v1 <= n) {
            val a0 = if (v1 < b) v1 else b
            val b0 = if (v1 < b) b else v1
            val D = b0 - a0
            if (D == 0 || isSq[D] || isSumOfSq[D] || a0 + minAdd[D] <= n || a0 - minSub[D] >= 1L) {
                return true
            }
        }
        
        val v2 = a - sq
        if (v2 >= 1) {
            val a0 = if (v2 < b) v2 else b
            val b0 = if (v2 < b) b else v2
            val D = b0 - a0
            if (D == 0 || isSq[D] || isSumOfSq[D] || a0 + minAdd[D] <= n || a0 - minSub[D] >= 1L) {
                return true
            }
        }
    }
    return false
}

fun main() {
    init()
    val scanner = FastScanner(System.`in`)
    val out = PrintWriter(BufferedOutputStream(System.out))
    
    val t = scanner.nextInt()
    if (t == -1) return
    for (tc in 0 until t) {
        val n = scanner.nextInt()
        val q = scanner.nextInt()
        for (i in 0 until q) {
            val a = scanner.nextInt()
            val b = scanner.nextInt()
            
            if (a == b) {
                out.println(0)
                continue
            }
            
            val D = Math.abs(a - b)
            if (isSq[D]) {
                out.println(1)
            } else if (dist2(a, b, n)) {
                out.println(2)
            } else if (dist3(a, b, n)) {
                out.println(3)
            } else {
                out.println(4)
            }
        }
    }
    out.flush()
}