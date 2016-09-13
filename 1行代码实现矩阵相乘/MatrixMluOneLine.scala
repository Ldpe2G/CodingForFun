val matrix = List(
        List(0.5, 1, 2),
        List(1, 0.5, 4))
                          
val matrix2 = List(
        List(2, 3),
        List(3, 2)，
        List(4, 5))
                                                  
(List[List[Double]]() /: matrix)((acc, row) => acc :+ row.zip(matrix2).map(z => 
                z._2.map(_ * z._1)).reduceLeft(_.zip(_).map(x => x._1 + x._2)))
