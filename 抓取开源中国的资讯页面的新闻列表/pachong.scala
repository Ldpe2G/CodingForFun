import scala.io.Source
import scala.util.matching.Regex
import scala.io._
import java.net._
import java.io._
 
 
object OsChinaPaChong {
   
  //有了这个隐式转换就可以写出 "" | console 类似linux命令
  implicit class MyString(val s: String) extends AnyVal{
    def |(f: String => Unit) = f(s)
  }
  def console(msg: String) = println(msg)
 
  //用到的正则表达式
  val pattern_href = """href\s*=\s*["]?(.+?)["]""".r 
  val pattern_h2 = """<h2>(.*?)</h2>""".r            
  val pattern_text = """>(.*?)<""".r                 
  val pattern_href2 = """["](.+?)["]""".r            
   
  val fileprefix = "/home/hadoop/scala/MicroBlog/"
 
  def main(args: Array[String]): Unit = {
      downloadOsChina | console
  }
   
  def downloadOsChina = {
        val filepath = downloadPage("http://www.oschina.net/news", 
                                                 "oschina.txt")
        //读取下载好的内容
        val lines = Source.fromFile(filepath).mkString
        //找出符合pattern_h2的内容
        val te = ( pattern_h2 findAllIn lines ) mkString(",") split(",") 
        //遍历te数组获取新闻列表
        ("" /: te)((acc, elem) => {
            val h2_url = ( pattern_href findAllIn elem ) mkString(",") split(",") 
            var CH = getChinese(elem)
            var url = getUrl(h2_url(0))
            if(!url.startsWith("http://www.oschina.net"))
                url = s"http://www.oschina.net$url"
            s"$acc$CH: $url\n\n"
        })
   }
   
   //获取herf中的网址
   def getUrl(url: String) = {
     var temp = ( pattern_href2 findAllIn url ) mkString;
     if(temp.length > 2)
         temp substring(1, temp.length() - 1)
     else ""
   }
    
   //获取中文内容
   def getChinese(origin: String) = {
     var temp = ( pattern_text findAllIn origin ) mkString(",") split(",") filter(_.length > 5)
     temp(0).substring(1, temp(0).length - 1)
   }
    
   
  //根据url下载网页，并保存在文件file中
    def downloadPage(urls: String, filename: String) = {
         
        val localfile = s"$fileprefix/$filename"
        val url = new URL(urls)
        val connection =
                    url.openConnection().asInstanceOf[HttpURLConnection]
        connection.setRequestMethod("GET")
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:17.0) Gecko/20100101 Firefox/17.0");
        if(connection.getResponseCode == 200) {
            val out = new java.io.FileWriter(new File(localfile))
            val in = Source.fromInputStream(connection.getInputStream)
            in.getLines.foreach(out.write(_))
            localfile
        }else{
            ""
        }
    } 
}
