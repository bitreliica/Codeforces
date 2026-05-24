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
    val q = scanner.nextInt()
    if (q == -1) return

    val MAX_N = q + 5
    val MAX_VAL = 22
    val MAX_EDGES = MAX_N * 2

    val to = IntArray(MAX_EDGES)
    val edgeVal = IntArray(MAX_EDGES)
    
    val prevEdge = IntArray(MAX_EDGES) { -1 }
    val nextEdge = IntArray(MAX_EDGES) { -1 }
    val headVal = Array(MAX_N) { IntArray(MAX_VAL) { -1 } }
    
    val counts = Array(MAX_N) { IntArray(MAX_VAL) }
    val currentOut = Array(MAX_N) { IntArray(MAX_VAL) }
    
    var edgeCount = 0

    fun addToList(v: Int, V: Int, e: Int) {
        val h = headVal[v][V]
        nextEdge[e] = h
        if (h != -1) prevEdge[h] = e
        prevEdge[e] = -1
        headVal[v][V] = e
    }

    fun removeFromList(v: Int, V: Int, e: Int) {
        val p = prevEdge[e]
        val n = nextEdge[e]
        if (p != -1) nextEdge[p] = n else headVal[v][V] = n
        if (n != -1) prevEdge[n] = p
    }

    fun calcVal(u: Int, excludedVal: Int): Int {
        var c = 0
        var ans = -1
        if (excludedVal != -1) counts[u][excludedVal]--
        
        for (i in MAX_VAL - 1 downTo 0) {
            c += counts[u][i]
            if (c >= 2) {
                ans = i
                break
            }
        }
        
        if (excludedVal != -1) counts[u][excludedVal]++
        return if (ans == -1) 0 else ans + 1
    }

    var globalMaxUnrooted = 0
    fun updateUnrooted(u: Int) {
        val newLvl = calcVal(u, -1)
        if (newLvl > globalMaxUnrooted) {
            globalMaxUnrooted = newLvl
        }
    }

    val queue = IntArray(5000000)
    var qHead = 0
    var qTail = 0
    val inQ = BooleanArray(MAX_N)

    val out = StringBuilder()

    for (i in 1..q) {
        val p = scanner.nextInt()
        val newNode = i + 1

        val e1 = edgeCount++
        val e2 = edgeCount++ 

        to[e1] = p
        to[e2] = newNode

        edgeVal[e1] = 0
        counts[p][0]++
        addToList(p, 0, e1)
        val pOut = calcVal(p, 0)
        edgeVal[e2] = pOut
        counts[newNode][pOut]++
        addToList(newNode, pOut, e2)

        updateUnrooted(newNode)
        updateUnrooted(p)

        if (!inQ[p]) { inQ[p] = true; queue[qTail++] = p }
        if (!inQ[newNode]) { inQ[newNode] = true; queue[qTail++] = newNode }

        while (qHead < qTail) {
            val u = queue[qHead++]
            inQ[u] = false

            updateUnrooted(u)

            for (v in 0 until MAX_VAL) {
                if (counts[u][v] > 0) {
                    val nOut = calcVal(u, v)
                    if (nOut > currentOut[u][v]) {
                        currentOut[u][v] = nOut
                        var e = headVal[u][v]
                        while (e != -1) {
                            val nextE = nextEdge[e]
                            val revE = e xor 1
                            val dest = to[revE]
                            val oldVal = edgeVal[revE]
                            if (nOut > oldVal) {
                                edgeVal[revE] = nOut

                                counts[dest][oldVal]--
                                counts[dest][nOut]++

                                removeFromList(dest, oldVal, revE)
                                addToList(dest, nOut, revE)

                                if (!inQ[dest]) {
                                    inQ[dest] = true
                                    queue[qTail++] = dest
                                }
                            }
                            e = nextE
                        }
                    }
                }
            }
        }
        
        out.append(globalMaxUnrooted + 1).append(" ")
    }

    println(out.toString().trimEnd())
}