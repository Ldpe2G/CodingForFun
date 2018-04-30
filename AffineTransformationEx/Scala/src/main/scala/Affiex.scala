import org.opencv.highgui.Highgui
import visual.Imshow
import org.opencv.imgproc.Imgproc
import org.opencv.core.Mat
import org.opencv.core.CvType
import org.opencv.core.Size
import org.kohsuke.args4j.Option
import scala.collection.JavaConverters._
import org.kohsuke.args4j.CmdLineParser
import org.opencv.core.Core

/**
 * @author Depeng Liang
 */
object Affiex {
  
  nu.pattern.OpenCV.loadShared()
  
  def main(args: Array[String]): Unit = {
    val opt = new Argments()
    val parser: CmdLineParser = new CmdLineParser(opt)
    try {
      parser.parseArgument(args.toList.asJava)
      val img = Highgui.imread(opt.image)
      
      val oriShow = new Imshow("original image")
      oriShow.showImage(img)
      
      val bbox = opt.bbox.split(",").map(_.toInt)
      val outWh = opt.outWH.split(",").map(_.toInt)

      val cropMat = new Mat(3, 3, CvType.CV_32F)
      var tmpArr = Array[Float](
          1, 0, 0,
          0, 1, 0,
          0, 0, 1
      )
      cropMat.put(0, 0, tmpArr)

      val bboxCenterX = bbox(0) + bbox(2) / 2;
      val bboxCenterY = bbox(1) + bbox(3) / 2;

      val srcPt = Array(bboxCenterX, bboxCenterY) // bbox center
      val dstPt = Array(outWh(0) / 2, outWh(1) / 2) // croped image center

      val scaleX = outWh(0) / bbox(2)
      val scaleY = outWh(1) / bbox(3)
  
      tmpArr(0) = scaleX
      tmpArr(4) = scaleY
      
      // mapping the center of bbox to the center of croped image after affine transformation
      tmpArr(2) = dstPt(0) - tmpArr(0) * srcPt(0)
      tmpArr(5) = dstPt(1) - tmpArr(4) * srcPt(1)
      cropMat.put(0, 0, tmpArr)
      
      val shiftMat1 = new Mat(3, 3, CvType.CV_32F)
      tmpArr = Array[Float](
          1, 0, 0,
          0, 1, 0,
          0, 0, 1
      )
      shiftMat1.put(0, 0, tmpArr)
  
      tmpArr(2) = -dstPt(0)
      tmpArr(5) = -dstPt(1)
      
      shiftMat1.put(0, 0, tmpArr)  
      
      val rotateMat = new Mat(3, 3, CvType.CV_32F)
      tmpArr = Array[Float](
          1, 0, 0,
          0, 1, 0,
          0, 0, 1
      )
      rotateMat.put(0, 0, tmpArr)
  
      val angle = opt.rotateAngle / 180.0 * 3.14159265358979323846
      val cosA = Math.cos(angle).toFloat
      val sinA = Math.sin(angle).toFloat
      
      tmpArr(0) = cosA
      tmpArr(1) = sinA
      tmpArr(3) = -sinA
      tmpArr(4) = cosA
      
      rotateMat.put(0, 0, tmpArr)  

      val shiftMat2 = new Mat(2, 3, CvType.CV_32F)
      tmpArr = Array[Float](
          1, 0, 0,
          0, 1, 0
      )
      shiftMat2.put(0, 0, tmpArr)
  
      tmpArr(2) = dstPt(0)
      tmpArr(5) = dstPt(1)
      
      shiftMat2.put(0, 0, tmpArr)    

      val out = new Mat()
      val tranMat = new Mat()
      
      Core.gemm(shiftMat2, rotateMat, 1, new Mat(), 0, tranMat)
      Core.gemm(tranMat, shiftMat1, 1, new Mat(), 0, tranMat)
      Core.gemm(tranMat, cropMat, 1, new Mat(), 0, tranMat)
      Imgproc.warpAffine(img, out, tranMat, new Size(outWh(0), outWh(1)))
      
      val cropShow = new Imshow("croped image")
      cropShow.showImage(out)
      
    } catch {
      case ex: Exception => {
        println(ex.getMessage, ex)
        parser.printUsage(System.err)
        sys.exit(1)
      }
    }
  }

  class Argments {
    @Option(name = "--image", usage = "input image")
    var image: String = "../datas/timg.jpg"
    @Option(name = "--bbox", usage = "")
    var bbox: String = "175,30,250,270" // top_left_x,top_left_y,width,height
    @Option(name = "--rotate-angle", usage = "")
    var rotateAngle: Float = 20
    @Option(name = "--out-wh", usage = "")
    var outWH: String = "250,270" // output_width,output_height
  }
}

