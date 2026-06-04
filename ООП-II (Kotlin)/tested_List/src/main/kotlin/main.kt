class MyList<T>(initialCap: Int = 10){

    class Node<T>(
        val value: T,
        var next: Node<T>? = null
    )

    private var head: Node<T>?=null
    private var size: Int = 0

    fun add(element: T){

        val newNode =Node(element)

        if (head == null){
            head = newNode
        } else{
            var current = head

            while (current?.next != null){
                current = current.next
            }

            current?.next =newNode
        }
        size++
    }

    fun removeAt(index: Int):T{
        require(index in 0 until size) {"Индекс выходит за границу"}

        if (index == 0){

            val node = head ?:throw NoSuchElementException("Список пуст")

            head = node.next
            size--

            return node.value

        }

        var prev = head

        repeat(index-1){
            prev = prev!!.next
        }

        val remove = prev!!.next!!

        prev.next = remove.next
        size--

        return remove.value
    }

    fun removeEl(target: T): Boolean{

        if (head == null) return false

        if (head?.value == target){
            head = head?.next
            size--
        }

        var current = head

        while (current?.next != null){
            if (current.next?.value == target){
                current.next = current.next?.next
                size--
                return true
            }
            current = current.next
        }

        return false
    }

    fun size () = size

    override fun toString(): String{
        val SB = StringBuilder("[")
        var current = head
        while (current != null){
            SB.append(current.value)
            current = current.next

            if (current != null) SB.append(", ")
        }
        return SB.append("]").toString()
    }
}