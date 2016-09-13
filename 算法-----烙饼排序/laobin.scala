import scala.reflect.ClassTag
 
object sortLaobin {
   
    def printLaobin[T](strs: String, array: Array[T]) = {
        val astr = ("<" /: array){(acc, elem) => s"$acc ${elem.toString}"}
        println(s"$strs $astr >")
    }
     
    def sortLaobins[T](array: Array[T])(implicit ordering: Ordering[T], c: ClassTag[T]) = {
          var tmp = array
           
          printLaobin("start: ", tmp)
           
          var current = 0
          val size = array.length
           
          while(current < size - 1){
            //获取从current开始的最大元素和对应的索引
            val (max, max_index) = ((tmp(current), current) /: tmp.drop(current + 1).zip((0 until size).drop(current + 1))){(acc, elem) =>
               if(ordering.gt(acc._1, elem._1)) acc else elem
            }
            if(!ordering.equiv(tmp(current), max)){ //若最大的元素就是current所指向的位置则直接进入 
               if(!ordering.equiv(tmp(size - 1), max)){ //若最大元素在数组末尾则直接翻转current直到数组末尾的元素
                  println(s" reverse $max to ${tmp(size - 1)}")
                  tmp = Array.concat(tmp.take(max_index), tmp.drop(max_index).reverse)
               }
               println(s" reverse ${tmp(current)} to ${tmp(size - 1)}")
               tmp = Array.concat(tmp.take(current), tmp.drop(current).reverse)
               printLaobin(s" step $current done: ", tmp)
            }
            current += 1
          }
          printLaobin("done: ", tmp)
       
    }
   
    def main(args: Array[String]) = {
       
      println("\nexample one : ")
      val array1 = Array(3.0, 1.0, 2.0)
      sortLaobins(array1)
       
      println("\nexample two : ")
      val array2 = Array(4, 7, 5, 9, 8, 6, 1, 2, 3)
      sortLaobins(array2)
       
      println("\nexample three: ")
      val array3 = Array("a", "z", "v", "f", "g", "e", "q", "c", "p")
      sortLaobins(array3)
    }
   
}
