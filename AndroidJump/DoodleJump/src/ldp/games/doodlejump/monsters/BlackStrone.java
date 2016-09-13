package ldp.games.doodlejump.monsters;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.android.Android;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class BlackStrone extends AbstractMonster {

	
	private static final int STATE_SPLIT  = 1;
	private static final int STATE_GATHER = 2;
	
	private float temp_y; //用来记录分开前的y坐标
	private boolean issplitting = false;
	private int split_time = 0;
	private int split_state = STATE_SPLIT;
	private Bitmap black_stone;
	private int interval = 0;
	
	private static final int DEFAULT_INTERVAL = 15;
	private static final int DISAPPEAR_RATE = 20;
	
	private List<SplitMonster> list = new ArrayList<BlackStrone.SplitMonster>();
	
	public BlackStrone(float CoorY, DoodleJumpActivity context){
		this.CoorX = (int) (new Random().nextInt(271) + 25* DoodleJumpActivity.height_mul);
		bitmap_index = direction == DIRECTION_LEFT ? 0 : 2;
		this.CoorY = CoorY;
		this.bitmap_index = 0;
		intiBitmap(context);
		isdestory = false;
		initList();
		isrunning = true;
		SoundPlayer.playSound(SoundPlayer.SOUND_BLACK_STONE);
		new Thread(new MoveThread()).start();
	}
	
	private void initList() {
		list.add(new SplitMonster(-10* DoodleJumpActivity.height_mul, -10* DoodleJumpActivity.height_mul));
		list.add(new SplitMonster(-15* DoodleJumpActivity.height_mul, 0));
		list.add(new SplitMonster(-10* DoodleJumpActivity.height_mul, 10* DoodleJumpActivity.height_mul));
		list.add(new SplitMonster(0, 15* DoodleJumpActivity.height_mul));
		list.add(new SplitMonster(10, 10* DoodleJumpActivity.height_mul));
		list.add(new SplitMonster(15* DoodleJumpActivity.height_mul, 0));
		list.add(new SplitMonster(10, -10* DoodleJumpActivity.height_mul));
		list.add(new SplitMonster(0, -15* DoodleJumpActivity.height_mul));
	}

	private void intiBitmap(DoodleJumpActivity context) {
		black_stone = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.blackstone)).getBitmap();
	}

	@Override
	public int GetX() {
		// TODO Auto-generated method stub
		return this.CoorX;
	}

	@Override
	public void DrawSelf(Canvas canvas) {
		if(!issplitting){
			canvas.drawBitmap(black_stone, CoorX-25* DoodleJumpActivity.height_mul, CoorY-25* DoodleJumpActivity.height_mul, null);
		}
		else{
			DrawSplit(canvas);
		}
	}

	private void DrawSplit(Canvas canvas) {
		for(int i=0; i<list.size(); i++){
			list.get(i).Move();
			list.get(i).DrawSelf(canvas, split_time, black_stone);
		}
	}
	
	private int GetDistance(float temp_x, float temp_y, int x, float y) {
		double x_length = temp_x - x;
		double y_length = temp_y - y;
		return (int) Math.sqrt(x_length*x_length + y_length*y_length);
	}
/*
	@Override
	public void CheckDistance(AbstractAndroid android) {
		float temp_x = android.LTCoorX + 22;
		float temp_y = android.LTCoorY + 22;
		if(GetDistance(temp_x, temp_y, CoorX, CoorY) <= 40)
			android.MinusLifeBar(20);
	}*/

	@Override
	public void CheckDistance(AbstractAndroid android) {
		if(!issplitting){
			if(android.LTCoorX + 20* DoodleJumpActivity.height_mul >= CoorX - 25 * DoodleJumpActivity.height_mul&& android.LTCoorX <= CoorX  + 15* DoodleJumpActivity.height_mul
				&& android.LTCoorY <= CoorY + 25* DoodleJumpActivity.height_mul && Math.abs(android.LTCoorY - CoorY) <= 1){
				android.current_state = Android.STATE_GO_DOWN;
				android.MinusLifeBar(30);
			}
			else{
				float temp_x = android.LTCoorX + 22* DoodleJumpActivity.height_mul;
				float temp_y = android.LTCoorY + 22* DoodleJumpActivity.height_mul;
				if(GetDistance(temp_x, temp_y, CoorX, CoorY) <= 40* DoodleJumpActivity.height_mul)
					android.MinusLifeBar(20);
			}
		}
	}

	@Override
	public boolean IsDestory() {
		return false;
	}

	@Override
	public void BeingAttacked() {
		isdestory = false;
	}
	
	private class MoveThread implements Runnable{

		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally{
					if(!GameView.ispause){
					
						if(!issplitting){
							interval ++;
							if(interval > DEFAULT_INTERVAL){
								issplitting = true;
								interval = 0;
								SetListXY();
								temp_y = CoorY;
							}
						}
						else{
							temp_y = temp_y - CoorY;
							for(int i=0; i<list.size(); i++)
								list.get(i).Y -= temp_y;
							temp_y = CoorY;
							if(split_state == STATE_SPLIT){
								split_time ++;
								if(split_time > 3){
									split_state = STATE_GATHER;
									GetNewPositionXY();
									ReverseListSpeed();
								}
							}
							else{
								split_time --;
								if(split_time <= 0){
									split_time = 0;
									split_state = STATE_SPLIT;
									issplitting = false;
									ReverseListSpeed();
								}
							}
						}
						
					}
				}
			}
		}

		private void GetNewPositionXY() {
			int temp_x = (int) (new Random().nextInt(271) + 25* DoodleJumpActivity.height_mul);
			int temp = temp_x - CoorX;
			CoorX = temp_x;
		//	int Y_temp = temp_y - CoorY;
			for(int i=0; i<list.size(); i++){
				list.get(i).X += temp;
		//		list.get(i).Y -= Y_temp;
			}
		}

		private void SetListXY() {
			for(int i=0; i<list.size(); i++)
				list.get(i).SetXY(CoorX, CoorY);
		}

		private void ReverseListSpeed() {
			for(int i=0; i<list.size(); i++)
				list.get(i).ReverseSpeed();
		}
		
	}
	
	
	private class SplitMonster{
		
		float X;
		float Y;
		int vertical_s;
		int horizonal_s;
		
		public SplitMonster(float f, float g){
			this.X = 0;
			this.Y = 0;
			this.vertical_s = (int) f;
			this.horizonal_s = (int) g;
		}
		
		public void DrawSelf(Canvas canvas, int split_time, Bitmap black_Stone){
			Paint paint = new Paint();
			paint.setAlpha(split_time * DISAPPEAR_RATE);
			canvas.drawBitmap(black_Stone, X-25, Y-25, paint);
		}
		
		public void Move(){
			this.X += horizonal_s;
			this.Y += vertical_s;
		}
		
		public void ReverseSpeed(){
			this.horizonal_s *= -1;
			this.vertical_s *= -1;
		}
		
		public void SetXY(int X, float coorY){
			this.X = X;
			this.Y = coorY;
		}
	}


	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(issplitting)
			return false;
		if(CoorX >= this.CoorX - 55 * DoodleJumpActivity.height_mul && CoorX <= this.CoorX + 10 * DoodleJumpActivity.height_mul && CoorY + 70 * DoodleJumpActivity.height_mul <= this.CoorY && this.CoorY - CoorY <= 70 * DoodleJumpActivity.height_mul)
			return true;
		return false;
	}


}
