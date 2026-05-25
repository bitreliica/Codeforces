import java.io.BufferedReader
import java.io.InputStreamReader

fun main() {
    val reader = BufferedReader(InputStreamReader(System.`in`))
    val tInput = reader.readLine() ?: return
    val t = tInput.trim().toInt()
    
    val output = StringBuilder()
    
    for (i in 0 until t) {
        val n = reader.readLine().trim().toInt()
        
        for (j in 0 until n) {
            val oddNumber = 2 * j + 1
            output.append(oddNumber).append(if (j == n - 1) "" else " ")
        }
        output.append("\n")
    }
    
    print(output)
}