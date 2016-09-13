package ldp.games.doodlejump.bulletandexplode;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;



public class Explode {
	
	int index;
	boolean isdone;
	
	int CoorX;
	public int CoorY;
	public Bitmap[] explodes;
	
	public Explode(int _CoorX, int _CoorY, DoodleJumpActivity context){
		index = 0;
		isdone = false;
		this.CoorX = _CoorX;
		this.CoorY = _CoorY;
		initBitmap(context);
		new Thread(new ExplodeThread()).start();
	}
	

	
	private void initBitmap(DoodleJumpActivity context) {
		explodes = new Bitmap[6];
		explodes[0] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode1)).getBitmap();
		explodes[1] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode2)).getBitmap();
		explodes[2] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode3)).getBitmap();
		explodes[3] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode4)).getBitmap();
		explodes[4] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode5)).getBitmap();
		explodes[5] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode6)).getBitmap();
	}


	public void DrawSelf(Canvas canvas){
		if(index <= 5)
			canvas.drawBitmap(explodes[index], CoorX, CoorY, null);
	}
	
	public boolean isDone(){
		return isdone;
	}
	
	private class ExplodeThread implements Runnable{

		@Override
		public void run() {
			while(!isdone){
				try {
					Thread.sleep(90);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(!isdone){
					index ++;
					if(index > 5)
						isdone = true;
				}
			}
		}
		
	}
	

}
