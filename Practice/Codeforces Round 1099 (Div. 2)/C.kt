import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer
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

    fun getOps(from: Int, to: Int): Int {
        var current = from
        var ops = 0
        while (current != to && ops < 100) {
            current = if (current % 2 == 0) current / 2 else current + 1
            ops++
        }
        return if (current == to) ops else -1
    }

    fun getDepth(num: Int): Int {
        var current = num
        var depth = 0
        while (current != 1 && depth < 100) {
            current = if (current % 2 == 0) current / 2 else current + 1
            depth++
        }
        return depth
    }

    for (case in 0 until t) {
        val n = nextToken().toInt()
        val a = IntArray(n)
        
        var maxDepth = -1
        var deepestValue = -1

        for (i in 0 until n) {
            a[i] = nextToken().toInt()
            val d = getDepth(a[i])
            if (d > maxDepth) {
                maxDepth = d
                deepestValue = a[i]
            }
        }
        val candidates = LinkedHashSet<Int>()
        var curr = deepestValue
        candidates.add(curr)
        var steps = 0
        while (curr != 1 && steps < 100) {
            curr = if (curr % 2 == 0) curr / 2 else curr + 1
            candidates.add(curr)
            steps++
        }
        
        candidates.add(1)
        candidates.add(2)

        var minTotalOps = Long.MAX_VALUE

        for (target in candidates) {
            var totalOps = 0L
            var possible = true

            for (i in 0 until n) {
                val ops = getOps(a[i], target)
                if (ops == -1) {
                    possible = false
                    break
                }
                totalOps += ops
            }

            if (possible) {
                minTotalOps = min(minTotalOps, totalOps)
            }
        }

        out.append(minTotalOps).append("\n")
    }

    print(out)
}