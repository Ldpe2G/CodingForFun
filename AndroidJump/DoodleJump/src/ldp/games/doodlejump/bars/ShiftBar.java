package ldp.games.doodlejump.bars;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.Items.AbstractItem;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ShiftBar extends AbstractBar {

	int horizonal_speed = 3;
	Bitmap shift_bar;
	boolean isrunning = true;
	
	public ShiftBar(int CoorX, int CoorY, AbstractItem item, DoodleJumpActivity context){
		this.TLCoorX = CoorX;
		this.TLCoorY = CoorY;
		this.type = TYPE_SHIFT;
		this.item = item;
		this.isitemeaten = false;
		shift_bar = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.shift_bar)).getBitmap();
		new Thread(new ShiftThread()).start();
	}
	@Override
	public void drawSelf(Canvas canvas) {
		if(this.item != null && !this.isitemeaten)
			this.item.DrawSelf(canvas, TLCoorX + 15* DoodleJumpActivity.height_mul, TLCoorY - 20* DoodleJumpActivity.height_mul);
		canvas.drawBitmap(shift_bar, TLCoorX, TLCoorY, null);
	}

	
	private class ShiftThread implements Runnable{

		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(40);
				} catch (Exception e) {
					// TODO: handle exception
				}
				finally{
					if(!GameView.ispause){
						TLCoorX += horizonal_speed;
						if(TLCoorX >= 270){
							TLCoorX = 270;
							horizonal_speed = -3;
						}
						if(TLCoorX <= 0){
							TLCoorX = 0;
							horizonal_speed = 3;
						}
					}
				}
			}
		}
		
	}


	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(CoorX >= TLCoorX - 35 * DoodleJumpActivity.width_mul && CoorX <= TLCoorX + 45 * DoodleJumpActivity.width_mul && CoorY + 45 * DoodleJumpActivity.height_mul <= TLCoorY && TLCoorY - CoorY <= 45 * DoodleJumpActivity.height_mul){
		//	DoodleJumpActivity.soundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			return true;
		}
		return false;
	}
	@Override
	public void clear() {
		isrunning = false;
		//shift_bar.recycle();
	}
}
