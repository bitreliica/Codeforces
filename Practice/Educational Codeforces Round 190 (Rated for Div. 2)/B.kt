import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.math.max

fun main() {
    val br = BufferedReader(InputStreamReader(System.`in`))
    val tLine = br.readLine() ?: return
    val t = tLine.trim().toInt()
    
    val sb = StringBuilder()
    
    for (i in 0 until t) {
        val s = br.readLine().trim()
        
        var total13 = 0
        for (ch in s) {
            if (ch == '1' || ch == '3') {
                total13++
            }
        }
  
        var maxValidLength = total13
        
        var current2 = 0
        var current13 = 0
        
        for (ch in s) {
            when (ch) {
                '2' -> current2++
                '1', '3' -> current13++
            }

            val currentValidLength = current2 + (total13 - current13)
            maxValidLength = max(maxValidLength, currentValidLength)
        }
        
        val minRemovals = s.length - maxValidLength
        sb.append(minRemovals).append("\n")
    }
        print(sb.toString())
}