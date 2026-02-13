fun main(){
    val t1 = LotteryTicketCounterClass("Easy Money", "1dEmon", 100)

    t1.addTickets(10_000)
    println(t1.toString())
    println()

    t1.sellTickets(12)
    println(t1.toString())
    println()

    t1.changeLotteryName("Easy NOmoney")
    println(t1.toString())
    println()

    // альтернативыный способ вызова метода toString
    println(t1)

    println()

    val t2 = LotteryTicketCounterClass()

    t2.loadTicketsFromFile("Tickets.txt")

    println(t2)

    t2.addTickets(50)
    println(t2)
    println()

    t2.sellTickets(200)
    println(t2)
    println()

}