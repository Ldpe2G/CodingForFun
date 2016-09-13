import akka.actor.{Props, ActorRef, Actor, ActorSystem}
import admin._
 
object Pallelnqueens {
 
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("parallelnqueens")
    system.actorOf(Props[admin]) ! Start(12)
  }
 
}
 
class admin extends Actor {
  import akka.actor.ReceiveTimeout
  import scala.concurrent.duration._
   
  context.setReceiveTimeout(10 seconds)
   
  var sum = 0
   
  def receive = {
    case Start(n) => 
      //n皇后n个actor
      for(i <- 0 until n)
          context.actorOf(Props[worker], name = s"worker$i") ! Mission(n, List(i))
    case Result(result) =>
      sum += result.size
      println((result map show) mkString "\n")
    case ReceiveTimeout =>
      println(s"sum: $sum")
      context.system.shutdown
  }
   
  //把List对应的一个解转换成String表示形式
  def show(queens: List[Int]) = {
    val lines =
      for ( col <- queens.reverse )
        yield Vector.fill(queens.length)("* ").updated(col, "^ ").mkString
    s"\n${lines mkString "\n"}"
  }    
   
}
 
object admin {
  case class Start(n: Int)
  case class Result(result: Set[List[Int]])
  case class Mission(n: Int, searchList: List[Int])
}
 
class worker extends Actor {
   
  def receive = {
    case Mission(n, searchList) =>
        def placeQueens(k: Int): Set[List[Int]] = {
            if( k == 1 ) Set(searchList)
            else
            for{
                queens <- placeQueens(k - 1)
                col <- 0 until n
                if isSafe(col, queens)
                } yield col :: queens
        }
        context.parent ! Result(placeQueens(n))
  }
   
  //判断在col该位置能否放一个皇后
  def isSafe(col: Int, queens: List[Int]) = {
        val row = queens.length
        //将每一行和对应的皇后的位置放在一个原组中
        val queensWithRow = (row - 1 to 0 by -1) zip queens
        queensWithRow forall {
            //col不能和存在的列重合，还有行差和列差不能相等，防止对角线重合
            case (r,c) => col != c && math.abs(col - c) != row - r
        }
  }   
   
}
