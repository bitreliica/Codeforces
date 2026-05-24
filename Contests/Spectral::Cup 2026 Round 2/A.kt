import java.util.Scanner
import kotlin.math.max
import kotlin.math.min

fun main() {
    val scanner = Scanner(System.`in`)
    if (!scanner.hasNextInt()) return
    val t = scanner.nextInt()

    repeat(t) {
        val n = scanner.nextInt()
        var minVal = Int.MAX_VALUE
        var maxVal = Int.MIN_VALUE

        repeat(n) {
            val a = scanner.nextInt()
            minVal = min(minVal, a)
            maxVal = max(maxVal, a)
        }

        val ans = (maxVal - minVal + 1) / 2
        println(ans)
    }
}