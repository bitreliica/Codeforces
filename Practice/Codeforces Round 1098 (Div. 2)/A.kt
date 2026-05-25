import java.util.Scanner
import kotlin.math.min

fun main() {
    val scanner = Scanner(System.`in`)
    if (!scanner.hasNextInt()) return

    val t = scanner.nextInt()
    repeat(t) {
        val n = scanner.nextInt()

        var count0 = 0
        var count1 = 0
        var count2 = 0

        repeat(n) {
            when (scanner.nextInt()) {
                0 -> count0++
                1 -> count1++
                2 -> count2++
            }
        }

        var ans = count0
        val pairs = min(count1, count2)
        ans += pairs

        count1 -= pairs
        count2 -= pairs

        ans += count1 / 3
        ans += count2 / 3

        println(ans)
    }
}