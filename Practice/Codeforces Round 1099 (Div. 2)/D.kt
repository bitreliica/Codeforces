import java.io.InputStream
import java.io.PrintWriter
import java.io.BufferedOutputStream

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1024 * 64)
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
        var sign = 1
        if (c == 45) { 
            sign = -1
            c = read()
        }
        while (c > 32) {
            res = res * 10 + c - 48
            c = read()
        }
        return res * sign
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1L
            c = read()
        }
        var res = 0L
        var sign = 1L
        if (c == 45) { // '-'
            sign = -1L
            c = read()
        }
        while (c > 32) {
            res = res * 10 + c - 48
            c = read()
        }
        return res * sign
    }

    fun nextString(): String {
        var c = read()
        while (c <= 32) {
            if (c == -1) return ""
            c = read()
        }
        val sb = StringBuilder()
        while (c > 32) {
            sb.append(c.toChar())
            c = read()
        }
        return sb.toString()
    }
}

fun main() {
    val scanner = FastScanner(System.`in`)
    val out = PrintWriter(BufferedOutputStream(System.out))
    
    val t = scanner.nextInt()
    if (t == -1) return
    
    for (tc in 0 until t) {
        val n = scanner.nextInt()
        val s = scanner.nextString()
        
        val a = LongArray(n + 1)
        for (i in 1..n) {
            a[i] = scanner.nextLong()
        }
        
        val c = LongArray(n + 1)
        for (i in 1..n) {
            c[i] = scanner.nextLong()
        }
        
        var possible = true
        for (i in 2..n) {
            if (c[i] < c[i - 1]) {
                possible = false
                break
            }
        }
        
        val B = LongArray(n + 1)
        
        if (possible) {
            var L = 1
            while (L <= n) {
                var R = L
                while (R < n && s[R] == '1') {
                    R++
                }
                
                if (L == 1) {
                    if (s[0] == '1' && a[1] != c[1]) {
                        possible = false
                        break
                    }
                    B[1] = c[1]
                    for (k in 2..R) {
                        B[k] = B[k - 1] + a[k]
                    }
                    for (k in 1..R) {
                        if (B[k] > c[k]) {
                            possible = false
                            break
                        }
                        if (k > 1 && c[k] > c[k - 1]) {
                            if (B[k] != c[k]) {
                                possible = false
                                break
                            }
                        }
                    }
                    if (!possible) break
                } else {
                    val S = LongArray(R - L + 1)
                    S[0] = 0L
                    for (k in L + 1..R) {
                        S[k - L] = S[k - 1 - L] + a[k]
                    }
                    
                    var U = Long.MAX_VALUE 
                    var V: Long? = null
                    
                    for (k in L..R) {
                        val currentS = S[k - L]
                        val maxAllowed = c[k] - currentS
                        if (maxAllowed < U) {
                            U = maxAllowed
                        }
                        
                        if (c[k] > c[k - 1]) {
                            val reqVal = c[k] - currentS
                            if (V != null && V != reqVal) {
                                possible = false 
                                break
                            }
                            V = reqVal
                        }
                    }
                    
                    if (!possible) break
                    if (V != null) {
                        if (V > U) {
                            possible = false
                            break
                        }
                        B[L] = V
                    } else {
                        B[L] = U
                    }
                    
                                        for (k in L + 1..R) {
                        B[k] = B[L] + S[k - L]
                    }
                }
                
                L = R + 1
            }
        }
        
        if (possible) {
            out.println("Yes")
            a[1] = B[1]
            for (i in 2..n) {
                a[i] = B[i] - B[i - 1]
            }
            for (i in 1..n) {
                out.print(a[i])
                if (i < n) out.print(" ")
            }
            out.println()
        } else {
            out.println("No")
        }
    }
    
    out.flush()
}