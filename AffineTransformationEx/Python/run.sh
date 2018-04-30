IMAGE="../datas/timg.jpg"
BBOX="175,30,250,270"   #top_left_x,top_left_y,width,height
ROTATE_ANGLE=20
OUT_WH="250,270"  # width,height

python affiex.py \
	--image $IMAGE \
	--bbox $BBOX \
	--rotate-angle $ROTATE_ANGLE \
	--out-wh $OUT_WH 
