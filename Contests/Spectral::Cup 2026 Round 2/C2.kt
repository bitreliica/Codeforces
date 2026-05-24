import java.io.InputStream
import kotlin.collections.ArrayDeque
import kotlin.math.abs

class FastInput(private val stream: InputStream) {
    private val buffer = ByteArray(1 shl 16)
    private var head = 0
    private var tail = 0

    private fun readChar(): Int {
        if (head >= tail) {
            head = 0
            tail = stream.read(buffer, 0, buffer.size)
            if (tail <= 0) return -1
        }
        return buffer[head++].toInt()
    }

    fun nextInt(): Int {
        var c = readChar()
        while (c <= 32) {
            if (c == -1) return -1
            c = readChar()
        }
        var sign = 1
        if (c == 45) {
            sign = -1
            c = readChar()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + c - 48
            c = readChar()
        }
        return res * sign
    }

    fun nextLong(): Long {
        var c = readChar()
        while (c <= 32) {
            if (c == -1) return -1L
            c = readChar()
        }
        var sign = 1L
        if (c == 45) {
            sign = -1L
            c = readChar()
        }
        var res = 0L
        while (c > 32) {
            res = res * 10 + c - 48
            c = readChar()
        }
        return res * sign
    }
}

fun main() {
    val input = FastInput(System.`in`)
    val testCases = input.nextInt()
    if (testCases == -1) return
    val output = StringBuilder()

    for (tc in 0 until testCases) {
        val n = input.nextInt()
        val a = LongArray(n + 1)
        for (i in 1..n) {
            a[i] = input.nextLong()
        }

        val absPrefix = LongArray(n + 1)
        for (i in 1..n) {
            absPrefix[i] = absPrefix[i - 1] + abs(a[i])
        }

        val suffixSum = LongArray(n + 2)
        for (i in n downTo 1) {
            suffixSum[i] = suffixSum[i + 1] + a[i]
        }

        var bestSum = suffixSum[1]
        var bestTarget = 0

        for (i in 1..n) {
            if (a[i] > 0) {
                val currentSum = absPrefix[i - 1] - a[i] + suffixSum[i + 1]
                if (currentSum > bestSum) {
                    bestSum = currentSum
                    bestTarget = i
                }
            }
        }

        if (bestTarget == 0) {
            output.append("0\n\n")
        } else {
            val state = IntArray(n + 2) { 1 }
            for (i in 1 until bestTarget) {
                state[i] = if (a[i] > 0) 1 else -1
            }
            state[bestTarget] = -1

            val ops = ArrayDeque<Int>()
            for (i in n downTo 1) {
                val transition = state[i] * state[i + 1]
                if (transition == -1) {
                    if (a[i] > 0) {
                        ops.addFirst(i)
                    } else {
                        val front = ops.removeFirst()
                        ops.addFirst(i)
                        ops.addFirst(front)
                    }
                }
            }

            output.append(ops.size).append("\n")
            for (op in ops) {
                output.append(op).append(" ")
            }
            output.append("\n")
        }
    }
    print(output)
}