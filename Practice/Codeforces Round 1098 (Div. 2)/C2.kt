import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import kotlin.math.abs

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    var tokenizer: StringTokenizer? = null

    fun nextToken(): String {
        while (tokenizer == null || !tokenizer!!.hasMoreTokens()) {
            val line = reader.readLine() ?: return ""
            tokenizer = StringTokenizer(line)
        }
        return tokenizer!!.nextToken()
    }

    val tStr = nextToken()
    if (tStr.isEmpty()) return
    val t = tStr.toInt()

    val out = StringBuilder()

    for (tc in 0 until t) {
        val aStr = nextToken()
        val a = aStr.toLong()
        val n = nextToken().toInt()

        val d = CharArray(n)
        for (i in 0 until n) {
            d[i] = nextToken()[0]
        }

        val candidates = mutableSetOf<Long>()
        val len = aStr.length

        val dMin = d.first()
        val dMax = d.last()
        val dMinNz = d.firstOrNull { it != '0' }

        if (len > 1) {
            val cand = dMax.toString().repeat(len - 1)
            cand.toLongOrNull()?.let { candidates.add(it) }
        } else if ('0' in d) {
            candidates.add(0L)
        }

        if (dMinNz != null) {
            val cand = dMinNz.toString() + dMin.toString().repeat(len)
            cand.toLongOrNull()?.let { candidates.add(it) }
        } else {
            candidates.add(0L)
        }

        var prefixValid = true
        for (i in 0 until len) {
            val c = aStr[i]

            val up = d.firstOrNull { it > c }
            if (up != null) {
                val cand = aStr.substring(0, i) + up + dMin.toString().repeat(len - 1 - i)
                cand.toLongOrNull()?.let { candidates.add(it) }
            }

            val down = d.lastOrNull { it < c }
            if (down != null) {
                val cand = aStr.substring(0, i) + down + dMax.toString().repeat(len - 1 - i)
                cand.toLongOrNull()?.let { candidates.add(it) }
            }

            if (c !in d) {
                prefixValid = false
                break
            }
        }

        if (prefixValid) {
            candidates.add(a)
        }

        var minDiff = Long.MAX_VALUE
        for (cand in candidates) {
            val diff = abs(a - cand)
            if (diff < minDiff) {
                minDiff = diff
            }
        }
        out.append(minDiff).append("\n")
    }
    print(out)
}