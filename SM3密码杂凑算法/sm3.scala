object Main {
    //每个比特用一个字节来表示
    type Bits = List[Byte] 
    //256比特的初始值
    val IV = List("7380166f", "4914b2b9", "172442d7", "da8a0600", "a96f30bc", "163138aa", "e38dee4d", "b0fb0e4e")
    val HexTable = List("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")
    //初始值的二进制表示
    val IVb = hexStringToBinary(IV)
    
    val Tj = List("79cc4519", "7a879d8a")
    val Tjb = hexStringToBinary(Tj).grouped(32).toList
    def getTjb(j: Int) = if(j <= 15) Tjb(0) else Tjb(1)
    //获得整数int的二进制表达
    def getBinary(int: Int, result: Bits, times: Int): Bits = times match {
        case 0 => result
        case _ => getBinary(int >> 1, (int & 1).toByte +: result, times - 1)
    }
    //将16进制的字符串转为二进制表示
    def hexStringToBinary(hexString: List[String]) =
      (List[Byte]() /: hexString)((acc, elem) => (acc /: elem){
          (a, e) => a ::: getBinary(HexTable.indexOf(e.toString), List[Byte](), 4)
      })
     
    implicit class MyChar(val c: Char) extends AnyVal{
        def b = getBinary(c.toInt, List[Byte](), 8)
    }
     
    implicit class MyList[T <: Byte](l: List[T]){
      //左循环移位运算
      def <<<(n: Int): List[T] = {
          def shiftLeft[T](i: Int, list: List[T]) : List[T] =
            if(i > 0) {
              list match {
                case head :: xs => shiftLeft(i-1, xs ::: List(head))
                case Nil => shiftLeft(i-1, Nil)
              }
            }else list
          shiftLeft(n, l)
      }
      //按位异或运算
      def ^(l2: List[T]) = (l zip l2).map{
          case (bit1, bit2) => (bit1 ^ bit2).toByte
      }      
      //按位与操作
      def &(l2: List[T]) = (l zip l2).map{
          case (bit1, bit2) => (bit1 & bit2).toByte
      }  
      //按位或操作
      def |(l2: List[T]) = (l zip l2).map{
          case (bit1, bit2) => (bit1 | bit2).toByte
      }  
      //按位取反操作
      def unary_~ = l.map(~_ & 1).map(_.toByte)
      //加运算
      def +(l2: List[T]) = {
        val addTemp = l.zip(l2).map{case (bit1, bit2) => (bit1 + bit2).toByte}
        def carry(ca: Int, result: Bits, num: Int): Bits =
          if(num > 0) { 
            result match {
              case head :: tail => 
                val yushu = (head + ca) % 2
                val cane = if(head + ca > 1) 1 else 0
                carry(cane, tail ::: List(yushu.toByte), num - 1)
            }
          } else result
        carry(0, addTemp.reverse, addTemp.length).reverse
      }
    }
     
    def main(args: Array[String]) = {
      val message1 = "abc"
      val (padResult, l, k) = padding(stringToBit(message1))
      println(s"  message1: $message1\n  sm3 encrypt: ${binaryToHexString(iteration(padResult, l, k))}\n")
       
      val message2 = "abcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcdabcd"
      val (padResult2, l2, k2) = padding(stringToBit(message2))
      println(s"  message2: $message2\n  sm3 encrypt: ${binaryToHexString(iteration(padResult2, l2, k2))}")
    }
    //加密迭代过程
    def iteration(message: Bits, l: Int, k: Int) = {
      val B = message.grouped(512).toList
      val n = (l + k + 65) / 512
      def loop(times: Int, V: Bits): Bits =
        if(times <= n - 1) loop(times + 1, CF(V, B(times))) 
        else V
      loop(0, IVb)
    }
     
    def FF(X: Bits, Y: Bits, Z: Bits, j: Int) =
      if(j <= 15) X ^ Y ^ Z
      else (X & Y) | (X & Z) | (Y & Z)
       
    def GG(X: Bits, Y: Bits, Z: Bits, j: Int) =
      if(j <= 15) X ^ Y ^ Z
      else (X & Y) | (~ X & Z) 
     
    def CF(V: Bits, B: Bits): Bits = {
      val (wj, wjj) = expand(B)
      val WordRegister = V.grouped(32).toList
      def loop(A: Bits, B: Bits, C: Bits, D: Bits, E: Bits, 
          F: Bits, G: Bits, H: Bits, num: Int): Bits = num match {
        case j if j < 64 => {
          val SS1 = ((A <<< 12) + E + (getTjb(j) <<< j)) <<< 7
          val SS2 = SS1 ^ (A <<< 12)
          val TT1 = FF(A, B, C, j) + D + SS2 + wjj(j)
          val TT2 = GG(E, F, G, j) + H + SS1 + wj(j)
          loop(TT1, A, B <<< 9, C, P0(TT2), E, F <<< 19, G, j + 1)
        }
        case 64 => (A ::: B ::: C ::: D ::: E ::: F ::: G ::: H) ^ V
      }
      loop(WordRegister(0), WordRegister(1), WordRegister(2), WordRegister(3),
          WordRegister(4), WordRegister(5), WordRegister(6), WordRegister(7), 0)
    }
     
    def expand(B: Bits) = {
      val W16 = B.grouped(32).toList
      def loop(W: List[Bits], j: Int): List[Bits] =
        if(j >= 16 && j <= 67)
          loop(W :+ (P1(W(j - 16) ^ W(j - 9) ^ (W(j - 3) <<< 15)) ^ (W(j - 13) <<< 7) ^ W(j - 6)), j + 1)
        else W
      val Wj = loop(W16, 16)
      val Wjj = for(j <- 0 to 63) yield { Wj(j) ^ Wj(j + 4) }
      (Wj, Wjj.toList)
    }
     
    def P0(X: Bits) = X ^ (X <<< 9) ^ (X <<< 17)
     
    def P1(X: Bits) = X ^ (X <<< 15) ^ (X <<< 23)
     
    def binaryToHex(binarys: Bits) = (List[String]() /: binarys.grouped(4))((acc, elem) => {
      acc :+ s"${HexTable((0 to 3).map(j => Math.pow(2, j) * elem(3 - j)).sum.toInt)}"     
    })
     
    def binaryToHexString(binarys: Bits) = {
        val hex = binaryToHex(binarys).grouped(8)
        ("" /: hex)((acc, elem) => s"$acc ${("" /: elem)((a, e) => s"$a$e")}")
    }
     
    def stringToBit(string: String) = {
      (List[Byte]() /: string.toCharArray)((acc, elem) => acc ::: elem.b)
    }
     
    def padding(message: Bits) = {
       val l = message.length
       val temp = (l + 1) % 512
       val k = if(temp <= 448) 448 - temp else 512 - temp + 448
       val addOneZero = (message :+ 1.toByte) ::: getBinary(0, List[Byte](), k)
       val padResult = addOneZero ::: getBinary(l, List[Byte](), 64)
       (padResult, l, k)
     }
}
