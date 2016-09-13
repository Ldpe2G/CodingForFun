import scala.collection.immutable.Nil
import Matrix._
import akka.actor.{Props, ActorRef, Actor, ActorSystem, Kill}
import Ldpe2GActor._
 
object Jacobi {
 
  def main(args: Array[String]): Unit = {
    if(args.length < 1){
        println(" not enough argment, usage: program inputFilePath")
        println(" input file example：\n 3 \n 8 -3 2 20 \n 4 11 -1 33 \n 6 3 12 36")
    }else{
        //首先读取输入文件获得方程组系数矩阵和常数向量
        val (matrix, vector) = Matrix(args(0))
         
        //然后判断系数矩阵是否满足迭代收敛
        if(isConvergence(matrix)){
            //若满足迭代收敛则创建一个管理者actor
            //然后给myActor发送一个StartCal消息
            val system = ActorSystem("mySystem")
            val myActor =
                system.actorOf(Props(new Ldpe2GActor(matrix.matrix, vector)))
            myActor ! StartCal
        }else{
          println("the matrix do not satisfy the condition of convergence")
        }
       
    }
   
  }
   
  //判断矩阵是否满足Jacobi迭代法收敛条件
  def isConvergence(A: Matrix) = {
    val D = A getDiagonal  
    val LU = A - D
    val NLU = -1 * LU
    val DI = Matrix getDiagonalReverse D
    (DI * NLU).getSpectralradius < 1
  }
   
  //提供了这个隐式转换就可以写出上述 val NLU = -1 * LU
  implicit class MaDouble(val scaler: Double) extends AnyVal{
      def *(matrix: Matrix) = {
        Matrix(matrix.matrix.map(row => row.map(_ * scaler)))
      }
  }
}
//管理者actor，负责给worker actor发送计算任务和判断迭代是否继续进行
class Ldpe2GActor(val matrix: matrix, 
                  val vector: List[Double]) extends Actor {
  var times = 0
   
  var result = Map.empty[Int, Double]
  var workers = Map.empty[Int, ActorRef]
  
  def receive = {
    case StartCal => //迭代开始前先产生足够的worker actor 
      val rownum = matrix.length
      val x = for(i <- 0 until rownum) yield 0.0
      for(i <- 0 until rownum){
        workers += i -> context.actorOf(Props[Worker], name = s"worker$i")
        //给worker actor发送相应计算所需的数据
        //并行的思想是按行划分数据，看公式就清楚了
        workers(i) ! PartialData(matrix(i), x.toList, vector(i), i)
      }
    case Result(num, localX, oldx) => //worker actor返回的结果
        val rownum = matrix.length
        times += 1
        result += num -> localX
        //若times == rownum说明一轮迭代结束
        if(times == rownum){
          times = 0
          val newX = {for(i <- 0 until rownum) yield result(i)}.toList
          val init = Math.abs(newX(0) - oldx(0))
          //求的新一轮的迭代结果向量和上一轮的迭代解向量
          //的差向量的最大范数
          val diff =
            (init /: (newX zip oldx).map(z => Math.abs(z._1 - z._2))){
                (max, elem) => if(elem > max) elem else max
          }
          //如果差向量的最大范数diff不满足条件则继续
          //下一轮迭代
          if(diff > 1E-10){
            for(i <- 0 until rownum)
              workers(i) ! PartialData(matrix(i), newX, vector(i), i)
          }else{
              print("<")
              for(i <- 0 until rownum - 1)
                print(s"${result(i).toFloat}, ")
              println(s"${result(rownum - 1).toFloat}>")
              context.system.shutdown
          }
        }
   }
}
 
object Ldpe2GActor {
  case object StartCal
  case class PartialData(row: row, x: List[Double], bv: Double, rownum: Int)
  case class Result(rownum: Int, localX: Double, oldx: List[Double])
}
//计算每一轮迭代解向量的一个分量
class Worker extends Actor {
  def receive = {
    case PartialData(row, x, bv, rownum) => 
      var localX = 0.0
      localX = (0.0 /: (row zip x).map(z => z._1 * z._2))(_ + _)
      localX = localX - row(rownum) * x(rownum)
      localX = (bv - localX) / row(rownum)
      sender ! Result(rownum, localX, x)
  }
}
//矩阵类
class Matrix(val matrix: matrix) {
  //矩阵减法
  def -(ma: Matrix) = {
    val result = (matrix zip ma.matrix).map(rows => 
        (rows._1 zip rows._2).map(elems => elems._1 - elems._2)
    ) 
    Matrix(result)
  }
  //矩阵乘法
  def *(ma: Matrix) = {
    val (row, col) = (matrix.length, matrix(0).length)
    val zipElemRow = for{i <- 0 until row
                        j <- 0 until col
                    } yield ma.matrix(j).map(_ * matrix(i)(j))  
     var result = List[row]()
     var temp = zipElemRow
     for(i <- 1 to row){
         result = (List[Double]() /: temp.takeRight(row))((reduce, elem) => 
         reduce match {
           case Nil => elem
           case _ => (reduce zip elem).map(tu => tu._1 + tu._2)
         }) :: result
         temp = temp.dropRight(row)
    }
    Matrix(result)      
  }
  //获得矩阵的对角矩阵
  def getDiagonal = {
     val row =  matrix.length
     val zeroMatrix = { 
      for(i <- 0 until row) yield {
        for(j <- 0 until row) yield 0.0
      }.toList
    }.toList
    val result = for(i <- 0 until row) 
                 yield zeroMatrix(i).updated(i, matrix(i)(i))
    Matrix(result.toList)
  }
  //幂乘法求解矩阵最大特征值，
  //也就是普半径
  def getSpectralradius = {
    val row = matrix.length
    var v = {for(i <- 0 until row) yield 1.0}.toList
    val e = 0.001
    var max = 0.0
    var diff = 1.0
    var sumList = List[Double]()
    var oldMax = 0.0
    while(Math.abs(diff) > e){
        sumList = {
          for(i <- 0 until row) 
              yield (0.0 /: matrix(i).map(_ * v(i)))(_ + _)
        }.toList
        max = (sumList(0) /: sumList)((max, elem) => 
                                   if(elem > max) elem else max)
        v = sumList.map(_ / max)
        diff = max - oldMax
        oldMax = max
    }
    max
  }
  //将矩阵转换为string
  override def toString = {
      ("" /: matrix)((s,row) => s"$s\n${("" /: row)((acc,elem) => s"$acc $elem")}")
  }
}
//Matrix类的伴生对象
object Matrix {
   
  type elem = Double
  type row = List[elem]
  type matrix = List[row]
   
  def apply(matrix: matrix) = new Matrix(matrix)
  //从文件读入数据
  def apply(filename: String) = {
    import java.io.BufferedReader
    import java.io.FileReader
    val file = new BufferedReader(new FileReader(filename))
    val matrixOrder = file.readLine.toInt
    val MaAndB = for(i <- 1 to matrixOrder) yield {
      val temp = file.readLine.split(" ")
      val doubles = temp.map(_.toDouble)
      (doubles.take(matrixOrder).toList, doubles.takeRight(1)(0))
    }
    val (matrix, vector) = (MaAndB :\ (List[row](), List[Double]()))((elem, acc)  
                                   => (elem._1 :: acc._1, elem._2 :: acc._2))
    (new Matrix(matrix), vector)
  }
  //根据给定的维数生成一个单位矩阵
  def getIdentity(order: Int) = {
    val zeroMatrix = {  
      for(i <- 0 until order) yield {
        for(j <- 0 until order) yield 0.0
      }.toList
    }.toList
    val result = for(i <- 0 until order) yield zeroMatrix(i).updated(i, 1.0)
    Matrix(result.toList)
  } 
  //求解单位矩阵的逆矩阵
  def getDiagonalReverse(matrix: Matrix) = {
    val row = matrix.matrix.length
    val reverse = for(i <- 0 until row) 
               yield matrix.matrix(i).updated(i, 1 / matrix.matrix(i)(i))
    Matrix(reverse.toList)
  }
 }
