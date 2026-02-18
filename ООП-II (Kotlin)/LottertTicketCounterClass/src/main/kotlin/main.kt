fun main() {
    val t1 = LotteryTicketCounterClass("Easy Money", "1dEmon", 100)

    t1.addTickets(10_000)
    println(t1.toString())
    println()

    t1.sellTickets(1)
    println(t1.toString())
    println()

    t1.changeLotteryName("Easy NOmoney")
    println(t1.toString())
    println()

    // альтернативыный способ вызова метода toString
    println(t1)

    println("-----------------------------------------------------")

    val t2 = LotteryTicketCounterClass.loadTicketsFromFile("Tickets.txt")

    println()

    t2[0].addTickets(50)
    println(t2[0])
    println()

    t2[1].sellTickets(1)
    println(t2[1])
    println()

    println(t2[3])
    t2[3].changeLotteryName("новое имя")
    println(t2[3])

    println("-----------------------------------------------------")

    println(t1)
    t1.saveToFile("save.txt")

    println("-----------------------------------------------------")

    println(t2[3])
    t2[3].saveToFile("save.txt")

    println("-----------------------------------------------------")

    println(t2)
    LotteryTicketCounterClass.saveAllToFile(t2, "save.txt")

}
