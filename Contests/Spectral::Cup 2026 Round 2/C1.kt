import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.StringTokenizer

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    var st: StringTokenizer? = null

    fun next(): String {
        while (st == null || !st!!.hasMoreTokens()) {
            val line = br.readLine() ?: return ""
            st = StringTokenizer(line)
        }
        return st!!.nextToken()
    }

    val tc = next()
    if (tc.isEmpty()) return
    val t = tc.toInt()
    val sb = StringBuilder()

    repeat(t) {
        val n = next().toInt()
        val a = IntArray(n)
        for (i in 0 until n) {
            a[i] = next().toInt()
        }

        val res = ArrayList<Int>()
        var f = false

        for (i in n - 1 downTo 0) {
            val check = if (!f) a[i] > 0 else a[i] < 0
            if (check) {
                res.add(i + 1)
                f = !f
            }
        }

        sb.append(res.size).append("\n")
        if (res.isNotEmpty()) {
            sb.append(res.joinToString(" ")).append("\n")
        }
    }
    print(sb)
}