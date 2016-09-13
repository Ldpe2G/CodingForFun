import scala.collection.mutable.PriorityQueue
import scala.annotation.tailrec
 
object Main {
   
  case class Node(score: Int, depth: Int, map: Array[Array[Int]]){
    override def equals(o: Any): Boolean = o match {
      case that: Node => {
         for(i <- 0 to 2; j <- 0 to 2)
               if(map(i)(j) != that.map(i)(j)) return false
         true        
      }
      case _ => false
    }
    override def hashCode = super.hashCode
  }
  //隐式Ordering对象，下面的优先级队列需要用到
  implicit object NodeOrdering extends Ordering[Node] {
     def compare(a: Node, b: Node) = if(a.score > b.score) -1 else 1
  }
   
  val openArray = new PriorityQueue
  val closeArray = new PriorityQueue
  val table = Array((-1, 0), (1, 0), (0, -1), (0, 1))
   
   
   def main(args: Array[String]) = {
     val (sourceNode, targetNode) = getSourceTarget
     openArray enqueue sourceNode
      
     //辅助函数，a*算法的核心部分，
     //节点的深度其实等价于从初始节点进过多少步能到达该节点
     @tailrec
     def calStep: Int = {
       val tempNode = openArray.dequeue
       closeArray enqueue tempNode
      
       if(tempNode.equals(targetNode))
           return tempNode.depth
        
       val (x, y) = getZeroPosition(tempNode)
       for(i <- 0 to 3){
         val tempMap = createNewMap(tempNode.map)
         val (newX, newY) = (x + table(i)._1, y + table(i)._2)
         //判断扩展的位置是否满足边界条件
         if(newX >= 0 && newX <= 2 && newY >=0 && newY <= 2){ 
           tempMap(x)(y) = tempMap(newX)(newY)
           tempMap(newX)(newY) = 0
           val tempScore = calScore(tempMap, targetNode.map) + tempNode.depth + 1
           val newNode = Node(tempScore, tempNode.depth + 1, tempMap)
           if(isNodeNew(newNode)) openArray enqueue newNode 
         }
       }
       calStep
     } 
     if(hasSolution(sourceNode, targetNode)){
       val minStep = calStep
       println(s"最小步数： $minStep\n")
     }else println("8数码问题无解！！")
   }
  //判断8数码问题有无解
  //参考： http://blog.csdn.net/ju136/article/details/6876647
  def hasSolution(sourceNode: Node, targetNode: Node) = {
    val (source, target) = (sourceNode.map.flatten, targetNode.map.flatten)
    var sum1, sum2 = 0;
    for(i <- 0 to 8; j <- 0 to i) {
      if(source(i) != 0 && source(i) < source(j)) sum1 += 1
      if(target(i) != 0 && target(i) < target(j)) sum2 += 1
    }
    if(sum1 % 2 == sum2 % 2) true
    else false
  }
   
  //复制一个新的棋盘
  def createNewMap(Map: Array[Array[Int]]) = {
     val newMap = Array.ofDim[Int](3, 3)
     for(i <- 0 to 2; j <- 0 to 2) newMap(i)(j) = Map(i)(j)
     newMap
  }
  //判断新扩展的节点是否已经遍历过
  def isNodeNew(node: Node): Boolean = {
    for(n <- openArray) if(n.equals(node)) return false
    for(n <- closeArray) if(n.equals(node)) return false
    true
  }
  //计算当前棋盘 Map的得分，也就是和目标棋盘有多少个位置不一样
  def calScore(Map: Array[Array[Int]], targetMap: Array[Array[Int]]) = {
      (0 /: Map.zip(targetMap)){(result, arrays) => 
        (result /: arrays._1.zip(arrays._2)){(acc, elems) => 
          if(elems._1 == elems._2) acc else acc + 1
      }}
  }
   
  def printNode(node: Node) = {
     for(Array <- node.map){
       Array.foreach(x => print(s"$x "))
       println
     }
  }
  //获取棋盘九个格子中空格子的位置，代码中用0代替空格子，也就是寻找0的位置
  def getZeroPosition(node: Node): (Int, Int) = {
    for(i <- 0 to 2; j <- 0 to 2)
          if(node.map(i)(j) == 0) return (i, j)
    (-1, -1)
  }
  //从终端输入获取初始状态和目标状态
  def getSourceTarget = {
    println("source node:")
    val sourceMap = (for(i <- 1 to 3) yield {
      io.Source.stdin.getLines.next.split(" ").map(_.toInt).toArray
    }).toArray
     
    println("\ntarget node:")
    val targetMap = (for(i <- 1 to 3) yield {
      io.Source.stdin.getLines.next.split(" ").map(_.toInt).toArray
    }).toArray
     
    (Node(0, 0, sourceMap), Node(0, 0, targetMap))
  }
}
