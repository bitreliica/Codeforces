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

    val out = java.lang.StringBuilder()

    for (tc in 0 until t) {
        val a = nextToken().toLong()
        val n = nextToken().toInt()
        val d = IntArray(n)
        for (i in 0 until n) {
            d[i] = nextToken().toInt()
        }

        val candidates = mutableSetOf<Long>()
        val S = a.toString()
        val d1 = d[0].toString()[0]
        val d2 = d[1].toString()[0]
        val dChars = listOf(d1, d2)

        fun addCandidate(s: String) {
            if (s.isEmpty()) return
            if (s.length > 1 && s[0] == '0') return
            s.toLongOrNull()?.let { candidates.add(it) }
        }

        if (S.length > 1) {
            val cand = java.lang.StringBuilder()
            for (j in 0 until S.length - 1) cand.append(d2)
            addCandidate(cand.toString())
        } else {
            if (d1 == '0') addCandidate("0")
        }

        val firstNonZero = if (d1 != '0') d1 else d2
        if (firstNonZero != '0') {
            val cand = java.lang.StringBuilder()
            cand.append(firstNonZero)
            for (j in 0 until S.length) cand.append(d1)
            addCandidate(cand.toString())
        }

        var prefixValid = true
        for (i in S.indices) {
            val c = S[i]

            var upC: Char? = null
            for (dc in dChars) {
                if (dc > c) {
                    if (upC == null || dc < upC) upC = dc
                }
            }

            if (upC != null) {
                val cand = java.lang.StringBuilder()
                cand.append(S.substring(0, i))
                cand.append(upC)
                for (j in i + 1 until S.length) cand.append(d1)
                addCandidate(cand.toString())
            }

            var downC: Char? = null
            for (dc in dChars) {
                if (dc < c) {
                    if (downC == null || dc > downC) downC = dc
                }
            }

            if (downC != null) {
                val cand = java.lang.StringBuilder()
                cand.append(S.substring(0, i))
                cand.append(downC)
                for (j in i + 1 until S.length) cand.append(d2)
                addCandidate(cand.toString())
            }

            if (c !in dChars) {
                prefixValid = false
                break
            }
        }

        if (prefixValid) {
            addCandidate(S)
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