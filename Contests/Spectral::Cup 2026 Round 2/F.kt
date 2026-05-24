import java.io.InputStream

class FastScanner(private val stream: InputStream) {
    private val buffer = ByteArray(1024)
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
            res = res * 10 + c - '0'.toInt()
            c = read()
        }
        return res
    }

    fun nextLong(): Long {
        var c = read()
        while (c <= 32) {
            if (c == -1) return -1L
            c = read()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + c - '0'.toInt()
            c = read()
        }
        return res
    }
}

fun check(remMask: Int, c: Int, V: Long, a: LongArray, n: Int): Boolean {
    var bitCount = 0
    var temp = remMask
    while (temp > 0) {
        bitCount += temp and 1
        temp = temp shr 1
    }
    
    val items = LongArray(bitCount)
    var idx = 0
    for (i in 0 until n) {
        if ((remMask and (1 shl i)) != 0) {
            items[idx++] = a[i]
        }
    }
    items.sortDescending()
    
    val suffixSum = LongArray(items.size + 1)
    for (i in items.size - 1 downTo 0) {
        suffixSum[i] = suffixSum[i + 1] + items[i]
    }
    
    val bins = LongArray(c)
    
    fun dfs(i: Int): Boolean {
        var count = 0
        var req = 0L
        for (j in 0 until c) {
            if (bins[j] >= V) {
                count++
            } else {
                req += V - bins[j]
            }
        }
        if (count == c) return true
        if (i == items.size) return false
        if (req > suffixSum[i]) return false
        
        for (j in 0 until c) {
            if (bins[j] < V) {
                bins[j] += items[i]
                if (dfs(i + 1)) return true
                bins[j] -= items[i]
                if (bins[j] == 0L) break
            }
        }
        
        if (req <= suffixSum[i + 1]) {
            if (dfs(i + 1)) return true
        }
        return false
    }
    
    return dfs(0)
}

fun main() {
    val scanner = FastScanner(System.`in`)
    val t = scanner.nextInt()
    if (t == -1) return
    
    val maxN = 18
    val sum = LongArray(1 shl maxN)
    val maxVal = LongArray(1 shl maxN)
    val validMasks = IntArray(1 shl maxN)
    val packed = LongArray(1 shl maxN)
    
    for (tc in 0 until t) {
        val n = scanner.nextInt()
        val k = scanner.nextInt()
        val a = LongArray(n)
        var totalSum = 0L
        
        for (i in 0 until n) {
            a[i] = scanner.nextLong()
            totalSum += a[i]
        }
        
        if (k == 1) {
            println(totalSum)
            continue
        }
        
        val numMasks = 1 shl n
        sum[0] = 0L
        maxVal[0] = 0L
        
        for (i in 0 until n) {
            val bit = 1 shl i
            sum[bit] = a[i]
            maxVal[bit] = a[i]
        }
        
        for (mask in 1 until numMasks) {
            val lsb = mask and (-mask)
            if (mask != lsb) {
                sum[mask] = sum[mask xor lsb] + sum[lsb]
                maxVal[mask] = maxOf(maxVal[mask xor lsb], maxVal[lsb])
            }
        }
        
        var validCount = 0
        for (mask in 1 until numMasks) {
            val sSum = sum[mask]
            val v = sSum - maxVal[mask]
            val remSum = totalSum - sSum
            if (remSum >= (k - 1) * v) {
                validMasks[validCount++] = mask
            }
        }
        
        for (i in 0 until validCount) {
            val mask = validMasks[i]
            packed[i] = (sum[mask] shl 18) or mask.toLong()
        }
        
        packed.sort(0, validCount)
        
        var ans = 0L
        for (i in validCount - 1 downTo 0) {
            val mask = (packed[i] and 0x3FFFFL).toInt()
            val sSum = sum[mask]
            val v = sSum - maxVal[mask]
            val remMask = (numMasks - 1) xor mask
            
            if (check(remMask, k - 1, v, a, n)) {
                ans = sSum
                break
            }
        }
        println(ans)
    }
}