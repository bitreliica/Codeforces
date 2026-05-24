import java.io.InputStream
import kotlin.math.min

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

    fun nextLong(): Long {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1L
            c = read()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + c - '0'.code
            c = read()
        }
        return res
    }
}

fun main() {
    val scanner = FastScanner(System.`in`)
    val multTestQ = scanner.nextInt()
    if (multTestQ == -1) return
    val out = java.lang.StringBuilder()
    
    for (i in 0 until multTestQ) {
        val n = scanner.nextInt()
        var sumP = 0L
        var slots = 0L
        var countP = 0
        var countU = 0L

        for (j in 0 until n) {
            val c = scanner.nextLong()
            if (c >= 2L) {
                sumP += c
                slots += (c / 2L) - 1L
                countP++
            } else {
                countU++
            }
        }

        if (countP == 0) {
            out.append(0).append('\n')
        } else if (countP == 1) {
            val s = sumP / 2L
            val ans = sumP + min(countU, s)
            if (ans < 3L) {
                out.append(0).append('\n')
            } else {
                out.append(ans).append('\n')
            }
        } else {
            val ans = sumP + min(countU, slots)
            out.append(ans).append('\n')
        }
    }
    print(out)
}