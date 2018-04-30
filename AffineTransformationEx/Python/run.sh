IMAGE="../datas/timg.jpg"
BBOX="175,30,250,270"   #top_left_x,top_left_y,width,height
ROTATE_ANGLE=0	
OUT_WH="125,135"  # width,height
OUT_PATH="../datas/cropped.jpg"

python affiex.py \
	--image $IMAGE \
	--bbox $BBOX \
	--rotate-angle $ROTATE_ANGLE \
	--out-wh $OUT_WH \
	--out-path $OUT_PATH
