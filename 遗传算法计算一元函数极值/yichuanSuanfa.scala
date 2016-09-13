object YiChuan {
 
  //基因长度
  val length = 10
   
  //迭代次数，最大子代数
  val MAX_GENERATION = 600
   
  //种群大小
  val POPULATION_SIZE = 20 
   
  //变异概率
  val MUTATION_RATE = 0.01 
   
  /**
   * 染色体
   * sequence 基因序列，表示一个整数的二进制形式，从最小位开始写
   *          比如10的二进制是 00 0000 1010，十位，
   *          则sequence表示为 0101 0000 00
   * fitness  适应度值
   */
  case class Chromosome(sequence: Array[Int], var fitness: Double)
   
  /**
   * 适应度函数，计算 (x-512)*(x-512)-10 的值
   */
  def CalFitnessOne(sequence: Array[Int]) = {
    //将二进制转为十进制
    val x = (0.0 /: (0 until sequence.length)){
        (acc, elem) =>  acc + sequence(elem) * Math.pow(2.0, elem)
    }
    //代入函数得出适应度值
    (x - 512) * (x - 512) - 10;  
  }
   
  /**
   * 适应度函数2，计算 (x-512)*(x-256)-10 的值
   */
  def CalFitnessTwo(sequence: Array[Int]) = {
    //将二进制转为十进制
    val x = (0.0 /: (0 until sequence.length)){
        (acc, elem) =>  acc + sequence(elem) * Math.pow(2.0, elem)
    }
    //代入函数得出适应度值
    (x - 512) * (x - 256) - 10;  
  }
   
  import scala.collection.mutable.PriorityQueue
  //优先级队列中元素比较的implicit ordering
  implicit object ChromosomeOrd extends Ordering[Chromosome] {
    def compare(x: Chromosome, y: Chromosome) = y.fitness.compare(x.fitness)
  }
   
  import java.util.Random
  val random = new Random
   
  /**
   * 初始化种群
   */
  def initPopulation(calFitness: Array[Int] => Double) = {
    val population = new PriorityQueue[Chromosome]
    for(i <- 0 until POPULATION_SIZE) {
      val sequence = Array.fill(length)(0).map(x => x ^ random.nextInt(2))
      population += Chromosome(sequence, calFitness(sequence))
    }
    population
  }
   
  /**
   * 简单的选择操作，保留优先级队列中前一半的基因
   */
  def selectChromosome(population: PriorityQueue[Chromosome]) = {
    val minList = (for(i <- 0 until POPULATION_SIZE / 2) 
            yield population dequeue).toList
    (new PriorityQueue[Chromosome] /: minList){
        (acc, elem) => acc += elem
    }
  }
   
  /**
   * 交叉变异操作
   */
  def CrossOver_Mutation(population: PriorityQueue[Chromosome],
            calFitness: Array[Int] => Double) = {
    //随机获取配偶的位置
    //po1为当前染色体的位置
    def getSpouse(po1: Int, population_size: Int): Int = {
      val spouse = random nextInt population_size
      if(spouse == po1) getSpouse(po1, population_size)
      else spouse
    }
     
    val populaSize = POPULATION_SIZE / 2
    val poList = population.toList
    val tempQueue = new PriorityQueue[Chromosome]
    for(i <- 0 until populaSize){
      val (seq1, seq2) = CrossOver(poList(i), 
                       poList(getSpouse(i, populaSize)),
                       calFitness)
      tempQueue += seq1
      tempQueue += seq2
    }
    tempQueue map (Mutation(_, calFitness))
  }
   
  /**
   * 交叉两个染色体，产生两个子代
   */
  def CrossOver(chromOne: Chromosome, chromTwo: Chromosome,
               calFitness: Array[Int] => Double) = {
    val position = random nextInt length - 1
    val seqOne =
      chromOne.sequence.take(position + 1) ++ 
      chromTwo.sequence.takeRight(length - position)
       
    val seqTwo =
      chromTwo.sequence.take(position) ++ 
      chromOne.sequence.takeRight(length - position)
       
    (Chromosome(seqOne, calFitness(seqOne)), 
        Chromosome(seqTwo, calFitness(seqTwo)))
  }
     
   /**
    * 染色体变异 
   */
   def Mutation(chrom: Chromosome, 
                calFitness: Array[Int] => Double) =
     //首先满足变异概率
     if(random.nextDouble > MUTATION_RATE){
          var seq = chrom.sequence
          val po = random nextInt length
          seq(po) = seq(po) ^ 1
          //若变异后适应值比原来大则不变异
          if(calFitness(seq) > calFitness(chrom.sequence))
            seq = chrom.sequence
          Chromosome(seq, calFitness(seq))
     } else chrom
     
   
  def main(args: Array[String]): Unit = {
    var population =  initPopulation(CalFitnessTwo)
    //var population =  initPopulation(CalFitnessOne)
    var smallest = population.max.fitness
    var temp = 0.0
    for(i <- 0 until MAX_GENERATION){
      population = selectChromosome(population)
      //population = CrossOver_Mutation(population, CalFitnessOne)
      population = CrossOver_Mutation(population, CalFitnessTwo)
      temp = population.max.fitness
      if(temp < smallest) smallest = temp
    }
    println(s"函数极值为 $smallest")
  }
 
}
