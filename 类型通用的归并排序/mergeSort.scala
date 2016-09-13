def msort[T](list: List[T])(fn: (T, T) => Boolean): List[T] = {
    //辅助函数，合并两个有序List
     def merge(xs: List[T], ys: List[T]): List[T] = (xs, ys) match {
        case (Nil, ys) => ys
        case (xs, Nil) => xs
        case (x :: xs1, y :: ys1) =>
        if(fn(x, y)) x :: merge(xs1, ys)
        else y :: merge(xs, ys1)
    }
     
    val n = list.length / 2
    if(n == 0) list
    else{
        val (f, s) = list splitAt n
        merge(msort(f)(fn), msort(s)(fn))
    }
}
     
val list1 = List(2, -4, 5, 7, 1)
val list2 = List("ldp", "fake", "you", "harmonious", "jiong")
println( msort(list1){ (x: Int, y: Int) => x < y } )
println( msort(list2){ (x: String, y: String) => x.compareTo(y) < 0 })
