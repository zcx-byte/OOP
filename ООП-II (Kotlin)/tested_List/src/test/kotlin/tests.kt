import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MyListTEst{

    @Test
    fun `add работает`(){
        val list = MyList<Int>()

        list.add(1)
        list.add(2)
        list.add(3)
        assertEquals(3, list.size())
        assertEquals("[1, 2, 3]", list.toString())
    }


}
