import java.io.InputStream
import java.io.PrintWriter

const val MOD = 998244353L
const val MAX_FACT = 1500005

val fact = LongArray(MAX_FACT)
val invFact = LongArray(MAX_FACT)
val tree = LongArray(4 * 300005 * 9)
val tmpPool = LongArray(100 * 9)
val a = LongArray(300005)
val resultOut = LongArray(9)

fun power(base: Long, exp: Long): Long {
    var res = 1L
    var b = base % MOD
    var e = exp
    while (e > 0) {
        if ((e and 1L) == 1L) res = (res * b) % MOD
        b = (b * b) % MOD
        e = e shr 1
    }
    return res
}

fun inv(n: Long): Long = power(n, MOD - 2)

fun precompute() {
    fact[0] = 1
    invFact[0] = 1
    for (i in 1 until MAX_FACT) {
        fact[i] = (fact[i - 1] * i) % MOD
    }
    invFact[MAX_FACT - 1] = inv(fact[MAX_FACT - 1])
    for (i in MAX_FACT - 2 downTo 1) {
        invFact[i] = (invFact[i + 1] * (i + 1)) % MOD
    }
}

fun nCr(n: Long, r: Long): Long {
    if (r < 0 || r > n) return 0
    return fact[n.toInt()] * invFact[r.toInt()] % MOD * invFact[(n - r).toInt()] % MOD
}

fun initLeaf(treeArr: LongArray, off: Int, v: Long) {
    treeArr[off] = 1L
    if (v == -1L) {
        treeArr[off + 1] = 1L
        treeArr[off + 2] = 0L
        treeArr[off + 3] = 0L
        treeArr[off + 4] = 1L
        treeArr[off + 5] = 0L
        treeArr[off + 6] = 1L
        treeArr[off + 7] = 0L
        treeArr[off + 8] = 0L
    } else {
        val p = v % MOD
        treeArr[off + 1] = 0L
        treeArr[off + 2] = p
        treeArr[off + 3] = v
        treeArr[off + 4] = 0L
        treeArr[off + 5] = p
        treeArr[off + 6] = 0L
        treeArr[off + 7] = (p * p) % MOD
        treeArr[off + 8] = 0L
    }
}

fun merge(outArr: LongArray, outOff: Int, lArr: LongArray, lOff: Int, rArr: LongArray, rOff: Int) {
    val lLen = lArr[lOff]
    val lK = lArr[lOff + 1]
    val lP = lArr[lOff + 2]
    val lRealP = lArr[lOff + 3]
    val lSumK = lArr[lOff + 4]
    val lSumP = lArr[lOff + 5]
    val lSumK2 = lArr[lOff + 6]
    val lSumP2 = lArr[lOff + 7]
    val lSumPK = lArr[lOff + 8]

    val rLen = rArr[rOff]
    val rK = rArr[rOff + 1]
    val rP = rArr[rOff + 2]
    val rRealP = rArr[rOff + 3]
    val rSumK = rArr[rOff + 4]
    val rSumP = rArr[rOff + 5]
    val rSumK2 = rArr[rOff + 6]
    val rSumP2 = rArr[rOff + 7]
    val rSumPK = rArr[rOff + 8]

    outArr[outOff] = lLen + rLen
    outArr[outOff + 1] = (lK + rK) % MOD
    outArr[outOff + 2] = (lP + rP) % MOD
    outArr[outOff + 3] = lRealP + rRealP

    outArr[outOff + 4] = (lSumK + (rLen * lK) % MOD + rSumK) % MOD
    outArr[outOff + 5] = (lSumP + (rLen * lP) % MOD + rSumP) % MOD

    val k2 = (lK * lK) % MOD
    outArr[outOff + 6] = (lSumK2 + (rLen * k2) % MOD + (2L * lK % MOD * rSumK) % MOD + rSumK2) % MOD

    val p2 = (lP * lP) % MOD
    outArr[outOff + 7] = (lSumP2 + (rLen * p2) % MOD + (2L * lP % MOD * rSumP) % MOD + rSumP2) % MOD

    val pk = (lP * lK) % MOD
    outArr[outOff + 8] = (lSumPK + (rLen * pk) % MOD + (lP * rSumK) % MOD + (lK * rSumP) % MOD + rSumPK) % MOD
}

fun build(node: Int, l: Int, r: Int) {
    if (l == r) {
        initLeaf(tree, node * 9, a[l])
        return
    }
    val mid = (l + r) shr 1
    build(node shl 1, l, mid)
    build((node shl 1) or 1, mid + 1, r)
    merge(tree, node * 9, tree, (node shl 1) * 9, tree, ((node shl 1) or 1) * 9)
}

fun update(node: Int, l: Int, r: Int, idx: Int, v: Long) {
    if (l == r) {
        initLeaf(tree, node * 9, v)
        return
    }
    val mid = (l + r) shr 1
    if (idx <= mid) {
        update(node shl 1, l, mid, idx, v)
    } else {
        update((node shl 1) or 1, mid + 1, r, idx, v)
    }
    merge(tree, node * 9, tree, (node shl 1) * 9, tree, ((node shl 1) or 1) * 9)
}

fun query(node: Int, l: Int, r: Int, ql: Int, qr: Int, outArr: LongArray, outOff: Int, depth: Int) {
    if (ql <= l && r <= qr) {
        System.arraycopy(tree, node * 9, outArr, outOff, 9)
        return
    }
    val mid = (l + r) shr 1
    if (qr <= mid) {
        query(node shl 1, l, mid, ql, qr, outArr, outOff, depth)
    } else if (ql > mid) {
        query((node shl 1) or 1, mid + 1, r, ql, qr, outArr, outOff, depth)
    } else {
        val leftOff = depth * 18
        val rightOff = leftOff + 9
        query(node shl 1, l, mid, ql, qr, tmpPool, leftOff, depth + 1)
        query((node shl 1) or 1, mid + 1, r, ql, qr, tmpPool, rightOff, depth + 1)
        merge(outArr, outOff, tmpPool, leftOff, tmpPool, rightOff)
    }
}

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1 shl 16)
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
        if (c == 45) {
            sign = -1
            c = read()
        }
        var res = 0
        while (c > 32) {
            if (c < 48 || c > 57) {
                c = read()
                continue
            }
            res = res * 10 + c - 48
            c = read()
        }
        return res * sign
    }
}

fun main() {
    precompute()
    val scanner = FastScanner(System.`in`)
    val out = PrintWriter(System.out)

    var t = scanner.nextInt()
    if (t == -1) return
    while (t-- > 0) {
        val n = scanner.nextInt()
        val q = scanner.nextInt()

        for (i in 1..n) {
            a[i] = scanner.nextInt().toLong()
        }

        build(1, 1, n)

        for (i in 0 until q) {
            val op = scanner.nextInt()
            if (op == 1) {
                val p = scanner.nextInt()
                val v = scanner.nextInt().toLong()
                update(1, 1, n, p, v)
            } else {
                val l = scanner.nextInt()
                val r = scanner.nextInt()
                val m = scanner.nextInt().toLong()

                query(1, 1, n, l, r, resultOut, 0, 0)

                val k = resultOut[1]
                val realP = resultOut[3]
                val sumK = resultOut[4]
                val sumK2 = resultOut[6]
                val sumP2 = resultOut[7]
                val sumPK = resultOut[8]

                val w = m - realP

                if (w < 0L) {
                    out.println(0)
                } else if (k == 0L) {
                    if (w == 0L) {
                        out.println(sumP2 % MOD)
                    } else {
                        out.println(0)
                    }
                } else {
                    val nWays = nCr(w + k - 1, k - 1)
                    val wMod = w % MOD

                    val term1 = sumP2 % MOD

                    val term2 = (2L * wMod) % MOD * inv(k) % MOD * sumPK % MOD

                    val w2 = (wMod * wMod) % MOD
                    val num3 = (w2 - wMod + MOD) % MOD
                    val den3 = (k * (k + 1)) % MOD
                    val term3 = num3 * inv(den3) % MOD * sumK2 % MOD

                    val num4 = (w2 + (k % MOD) * wMod) % MOD
                    val term4 = num4 * inv(den3) % MOD * sumK % MOD

                    var total = (term1 + term2 + term3 + term4) % MOD
                    total = (total * nWays) % MOD

                    out.println(total)
                }
            }
        }
    }
    out.flush()
}