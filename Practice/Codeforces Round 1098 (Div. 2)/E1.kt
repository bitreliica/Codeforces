import java.io.InputStream

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
        var sign = 1
        if (c == '-'.code) {
            sign = -1
            c = read()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + c - '0'.code
            c = read()
        }
        return res * sign
    }
}

val MOD = 998244353L

fun power(base: Long, exp: Long): Long {
    var res = 1L
    var b = base % MOD
    var e = exp
    while (e > 0) {
        if (e % 2L == 1L) res = (res * b) % MOD
        b = (b * b) % MOD
        e /= 2L
    }
    return res
}

fun modInverse(n: Long): Long {
    return power(n, MOD - 2)
}

val MAXF = 2000005
val fact = LongArray(MAXF)
val invFact = LongArray(MAXF)

fun precompute() {
    fact[0] = 1
    invFact[0] = 1
    for (i in 1 until MAXF) {
        fact[i] = (fact[i - 1] * i) % MOD
    }
    invFact[MAXF - 1] = modInverse(fact[MAXF - 1])
    for (i in MAXF - 2 downTo 1) {
        invFact[i] = (invFact[i + 1] * (i + 1)) % MOD
    }
}

fun nCr(n: Int, r: Int): Long {
    if (r < 0 || r > n) return 0
    return fact[n] * invFact[r] % MOD * invFact[n - r] % MOD
}

fun main() {
    precompute()
    val scanner = FastScanner(System.`in`)
    val out = StringBuilder()

    var t = scanner.nextInt()
    if (t == -1) return

    while (t-- > 0) {
        val n = scanner.nextInt()
        val q = scanner.nextInt()

        val a = IntArray(n + 1)
        for (i in 1..n) {
            a[i] = scanner.nextInt()
        }

        for (qi in 0 until q) {
            val op = scanner.nextInt()
            val l = scanner.nextInt()
            val r = scanner.nextInt()
            val m = scanner.nextInt().toLong()

            var k = 0
            var S = 0L
            for (i in l..r) {
                if (a[i] == -1) {
                    k++
                } else {
                    S += a[i]
                }
            }

            val M = m - S
            if (M < 0) {
                out.append("0\n")
                continue
            }

            if (k == 0) {
                if (M == 0L) {
                    var ans = 0L
                    var s = 0L
                    for (i in l..r) {
                        s = (s + a[i]) % MOD
                        ans = (ans + (s * s) % MOD) % MOD
                    }
                    out.append(ans).append("\n")
                } else {
                    out.append("0\n")
                }
                continue
            }
            val nSpace = (M + k - 1).toInt()
            val C0 = nCr(nSpace, k - 1)
            val C1 = nCr(nSpace, k)
            val C2 = nCr(nSpace, k + 1)

            var ans = 0L
            var s_i = 0L
            var u_i = 0L

            for (i in l..r) {
                if (a[i] == -1) {
                    u_i++
                } else {
                    s_i = (s_i + a[i]) % MOD
                }

                val term0 = C0 * ((s_i * s_i) % MOD) % MOD

                val term1Inner = (2 * s_i + 1) % MOD
                val term1U = (u_i * term1Inner) % MOD
                val term1 = C1 * term1U % MOD

                val term2Inner = (u_i * (u_i + 1)) % MOD
                val term2 = C2 * term2Inner % MOD

                val current = (term0 + term1 + term2) % MOD
                ans = (ans + current) % MOD
            }
            out.append(ans).append("\n")
        }
    }
    print(out)
}