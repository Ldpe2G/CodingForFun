package ldp.games.doodlejump.monsters;

import java.util.Random;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class EatingHead extends AbstractMonster {
	
	
	
	public Bitmap[] eatinghead;
	
	public EatingHead(float f, DoodleJumpActivity context){
		this.CoorX = new Random().nextInt(271) + 25;
		this.direction = new Random().nextInt(2) + 1;
		bitmap_index = direction == DIRECTION_LEFT ? 0 : 2;
		this.CoorY = f;
		this.horizontal_speed = direction == DIRECTION_LEFT ? -10 : 10;
		intiBitmap(context);
		isdestory = false;
		isrunning = true;
		new Thread(new MoveThread()).start();
	}
	

	private void intiBitmap(DoodleJumpActivity context) {
		eatinghead = new Bitmap[4];
		eatinghead[0] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_left_close)).getBitmap();
		eatinghead[1] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_left_open)).getBitmap();
		eatinghead[2] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_right_close)).getBitmap();
		eatinghead[3] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_right_open)).getBitmap();
		
	}


	@Override
	public void DrawSelf(Canvas canvas) {
		canvas.drawBitmap(eatinghead[bitmap_index], CoorX-25* DoodleJumpActivity.height_mul, CoorY-25* DoodleJumpActivity.height_mul, null);
	}
	
	private void Move(){
		this.CoorX += this.horizontal_speed;
		if(this.CoorX < 25* DoodleJumpActivity.height_mul){
			this.direction = DIRECTION_RIGHT;
			this.horizontal_speed *= -1;
		}
		if(this.CoorX > DoodleJumpActivity.screen_width - 25* DoodleJumpActivity.height_mul){
			this.direction = DIRECTION_LEFT;
			this.horizontal_speed *= -1;
		}
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
						if(direction == DIRECTION_LEFT){
							if(bitmap_index == 0)
								bitmap_index = 1;
							else 
								bitmap_index = 0;
						}
						else{
							if(bitmap_index == 2)
								bitmap_index = 3;
							else 
								bitmap_index = 2;
						}
						Move();
					}
				}
			}
		}
		
	}

	@Override
	public int GetX() {
		// TODO Auto-generated method stub
		return CoorX;
	}
	
	private int GetDistance(float temp_x, float temp_y, int x, float y) {
		double x_length = temp_x - x;
		double y_length = temp_y - y;
		return (int) Math.sqrt(x_length*x_length + y_length*y_length);
	}

	@Override
	public void CheckDistance(AbstractAndroid android) {
		float temp_x = android.LTCoorX + 22;
		float temp_y = android.LTCoorY + 22;
		if(GetDistance(temp_x, temp_y, CoorX, CoorY) <= 40 * DoodleJumpActivity.height_mul)
			android.MinusLifeBar(10);
	}


	@Override
	public boolean IsDestory() {
		// TODO Auto-generated method stub
		return isdestory;
	}


	@Override
	public void BeingAttacked() {
		isdestory = true;
	}


	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(CoorX >= this.CoorX - 55 * DoodleJumpActivity.height_mul && CoorX <= this.CoorX + 10 * DoodleJumpActivity.height_mul && CoorY + 70 <= this.CoorY * DoodleJumpActivity.height_mul && this.CoorY - CoorY <= 70 * DoodleJumpActivity.height_mul)
			return true;
		return false;
	}

}
