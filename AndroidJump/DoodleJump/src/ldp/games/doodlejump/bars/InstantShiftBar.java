package ldp.games.doodlejump.bars;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class InstantShiftBar extends AbstractBar {

	private static final int STATE_DISAPPEAR  = 1;
	private static final int STATE_APPEAR = 2;
	private Bitmap InstantShift;
	private int interval = 0;
	private static final int DEFAULT_INTERVAL = 15;
	private static final int DISAPPEAR_RATE = 20;

	private boolean isdisappear = false;
	private int appear_time = 0;
	private int appear_state = STATE_APPEAR;
	//List<SplitBar> list = new ArrayList<SplitBar>();
	boolean isrunning = true;
	//Í¬²½»¥³â£¬personËã·¨
	private static boolean[] person;
	private static int choose;
	
	public InstantShiftBar(int CoorX, int CoorY, DoodleJumpActivity context){
		this.TLCoorX = CoorX;
		this.TLCoorY = CoorY;
		InstantShift = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.instantshift)).getBitmap();
	//	initList();
		person = new boolean[2];
     	person[0] = false;
		person[1] = false;
		new Thread(new ShiftThread()).start();
	}

	@Override
	public void drawSelf(Canvas canvas) {
		if(!isdisappear)
			canvas.drawBitmap(InstantShift, TLCoorX, TLCoorY, null);
		else{
			Paint paint = new Paint();
			paint.setAlpha(appear_time * DISAPPEAR_RATE);
			canvas.drawBitmap(InstantShift, TLCoorX, TLCoorY, paint);
		}
	}


	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		//person[0] = true;
		//choose = 1;
		//while(person[1]&&choose==1);
		/*****************critical section**************************/
		if(isdisappear){
		//	person[0] = false;
			return false;
		}
		if(CoorX >= TLCoorX - 35 * DoodleJumpActivity.width_mul && CoorX <= TLCoorX + 45 * DoodleJumpActivity.width_mul && CoorY + 45 * DoodleJumpActivity.height_mul <= TLCoorY && TLCoorY - CoorY <= 45 * DoodleJumpActivity.height_mul){
		//	DoodleJumpActivity.soundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			return true;
		}
		//person[0] = false;
		return false;
	}
	

	private class ShiftThread implements Runnable{

		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(!GameView.ispause){
				//	person[1] = true;
				//	choose = 0;
				//	while(person[0]&&choose==0);
					/*****************critical section**************************/
					if(isdisappear){
						if(appear_state == STATE_APPEAR){
							appear_time ++;
							if(appear_time > 3){
								appear_state = STATE_DISAPPEAR;
							}
							
						}
						else{
							appear_time --;
							if(appear_time < 0){
								appear_time = 0;
								appear_state = STATE_APPEAR;
								isdisappear = false;
								interval = 0;
							}
						}
					}
					else{
						interval ++;
						if(interval > DEFAULT_INTERVAL){
							isdisappear = true;
							appear_state = STATE_APPEAR;
						}
					}
					
					
				}
					/*****************critical section**************************/
				//	person[1] = false;
			}
		}
		
		
	}


	@Override
	public void clear() {
		isrunning = false;
	///	InstantShift.recycle();
	}

}
