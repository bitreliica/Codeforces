import java.io.InputStream

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1024 * 16)
    private var head = 0
    private var tail = 0

    private fun read(): Int {
        if (head >= tail) {
            head = 0
            tail = stream.read(buffer, 0, buffer.size)
            if (tail <= 0) return -1
        }
        return buffer[head++].toInt()
    }

    fun nextInt(): Int {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1
            c = read()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + c - '0'.code
            c = read()
        }
        return res
    }
}

fun main() {
    val scanner = FastScanner(System.`in`)
    var t = scanner.nextInt()
    if (t == -1) return

    val out = StringBuilder()
    while (t-- > 0) {
        val n = scanner.nextInt()
        val x1 = scanner.nextInt()
        val x2 = scanner.nextInt()
        val k = scanner.nextInt()

        val d1 = if (x1 > x2) x1 - x2 else x2 - x1
        val d2 = n - d1
        val d = if (d1 < d2) d1 else d2

        if (n <= 3) {
            out.append(d).append('\n')
        } else {
            out.append(d + k).append('\n')
        }
    }
    print(out)
}