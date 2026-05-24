import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
import kotlin.math.max
import kotlin.math.min

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

    repeat(t) {
        val n = nextToken().toInt()
        val a = LongArray(n)
        for (i in 0 until n) {
            a[i] = nextToken().toLong()
        }
        
        var maxMin = 0L
        var sumMax = 0L
        
        for (i in 0 until n) {
            val b = nextToken().toLong()
            val av = a[i]
            val minVal = min(av, b)
            val maxVal = max(av, b)
            
            if (minVal > maxMin) {
                maxMin = minVal
            }
            sumMax += maxVal
        }
        
        out.append(maxMin + sumMax).append("\n")
    }
    print(out)
}