object What{
    def The(fuck: Any) = Fuck
}
object Fuck {
    def !!() = println(s"debug message !!")
    def !!!(s: String) = println(s)
}
     
What The Fuck !! //这里要最好有个回车，不然可能编译出错  
                       
What The Fuck !!! "debug message !!"
