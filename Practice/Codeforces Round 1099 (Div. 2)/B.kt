import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import kotlin.math.max

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

    for (case in 0 until t) {
        val n = nextToken().toInt()
        val a = LongArray(n)
        for (i in 0 until n) {
            a[i] = nextToken().toLong()
        }

        var k: Long = 0
        for (i in 0 until n - 1) {
            if (a[i] > a[i + 1]) {
                k = max(k, a[i] - a[i + 1])
            }
        }

        if (k == 0L) {
            out.append("YES\n")
            continue
        }

        var canBeZero = true
        var canBeOne = true

        for (i in 0 until n - 1) {
            var nextZero = false
            var nextOne = false

            val current = a[i]
            val next = a[i + 1]

            if (canBeZero) {
                if (current <= next) nextZero = true    
                if (current <= next + k) nextOne = true 
            }
            
                        if (canBeOne) {
                if (current + k <= next) nextZero = true 
                if (current <= next) nextOne = true     
            }

            canBeZero = nextZero
            canBeOne = nextOne

            if (!canBeZero && !canBeOne) {
                break
            }
        }

        if (canBeZero || canBeOne) {
            out.append("YES\n")
        } else {
            out.append("NO\n")
        }
    }
    print(out)
}