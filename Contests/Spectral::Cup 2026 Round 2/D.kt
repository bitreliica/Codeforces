import java.io.InputStream

class FastInputReader(private val stream: InputStream) {
    private val buffer = ByteArray(1 shl 16)
    private var head = 0
    private var tail = 0

    private fun nextByte(): Int {
        if (head >= tail) {
            head = 0
            tail = stream.read(buffer, 0, buffer.size)
            if (tail <= 0) return -1
        }
        return buffer[head++].toInt()
    }

    fun fetchInt(): Int {
        var c = nextByte()
        while (c <= 32) {
            if (c == -1) return -1
            c = nextByte()
        }
        var res = 0
        while (c > 32) {
            res = res * 10 + (c - 48)
            c = nextByte()
        }
        return res
    }
}

fun main() {
    val reader = FastInputReader(System.`in`)
    val t = reader.fetchInt()
    if (t == -1) return
    
    val output = StringBuilder()
    
    for (caseIndex in 0 until t) {
        val size = reader.fetchInt()
        val firstArr = IntArray(size) { reader.fetchInt() }
        val secondArr = IntArray(size) { reader.fetchInt() }
        
        var left = 1
        var right = 2 * size
        var maxPossible = 1
        
        while (left <= right) {
            val candidate = (left + right) shr 1
            
            var strongPairs = 0
            var weakSegments = 0
            var insideWeak = false
            
            for (i in 0 until size) {
                var strength = 0
                if (firstArr[i] >= candidate) strength++
                if (secondArr[i] >= candidate) strength++
                
                val value = strength - 1
                
                if (value == 1) {
                    strongPairs++
                    insideWeak = false
                } else if (value == -1) {
                    if (!insideWeak) {
                        weakSegments++
                        insideWeak = true
                    }
                }
            }
            
            if (strongPairs - weakSegments >= 1) {
                maxPossible = candidate
                left = candidate + 1
            } else {
                right = candidate - 1
            }
        }
        
        output.append(maxPossible).append("\n")
    }
    
    print(output.toString())
}