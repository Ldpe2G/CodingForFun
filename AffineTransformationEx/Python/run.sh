
IMAGE="../datas/timg.jpg"
OUT_PATH="../datas/cropped.jpg"


BBOX="175,30,250,270"   #top_left_x,top_left_y,width,height
OUT_WH="125,135"  # width,height
ROTATE_ANGLE=0	
SHEAR_FACTOR=0.1

python affiex.py \
	--image $IMAGE \
	--bbox $BBOX \
	--rotate-angle $ROTATE_ANGLE \
	--out-wh $OUT_WH \
	--shear-factor $SHEAR_FACTOR \
	--out-path $OUT_PATH
