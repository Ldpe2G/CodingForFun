ROOT=$(cd "$(dirname $0)"; pwd)

CLASS_PATH=$ROOT/target/scala-2.11/classes/:\
$HOME/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.8.jar:\
$HOME/.ivy2/cache/args4j/args4j/bundles/args4j-2.33.jar:\
$HOME/.ivy2/cache/nu.pattern/opencv/jars/opencv-2.4.9-7.jar

IMAGE="../datas/timg.jpg"
OUT_PATH="../datas/cropped.jpg"

BBOX="175,30,250,270"   #top_left_x,top_left_y,width,height
OUT_WH="125,135"  # width,height

ROTATE_ANGLE=20
SHEAR_FACTOR=0.2

java -Xmx4G -cp $CLASS_PATH \
	Affiex \
	--image $IMAGE \
	--bbox $BBOX \
	--rotate-angle $ROTATE_ANGLE \
	--shear-factor $SHEAR_FACTOR \
	--out-wh $OUT_WH \
	--out-path $OUT_PATH
