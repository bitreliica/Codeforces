import java.util.Scanner

fun main() {
    val t = readln().toInt()

    for (i in 0 until t) {
        val (n, a, b) = readln().split(" ").map { it.toLong() }
        val costAllIndividual = n * a
        val groupsOfThree = n / 3
        val remainder = n % 3
        val costMixed = (groupsOfThree * b) + (remainder * a)

        val costAllGroup = if (remainder == 0L) {
            groupsOfThree * b
        } else {
            (groupsOfThree + 1) * b
        }

        println(minOf(costAllIndividual, costMixed, costAllGroup))
    }
}