package ldp.games.doodlejump.bars;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class SpringBar extends AbstractBar {
	
	Bitmap spring_bar;
	
	
	public SpringBar(int CoorX, int CoorY, DoodleJumpActivity context){
		this.TLCoorX = CoorX;
		this.TLCoorY = CoorY;
		this.type = TYPE_SPRING;
		spring_bar = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.spring_bar)).getBitmap();
	}
	

	@Override
	public void drawSelf(Canvas canvas) {
		canvas.drawBitmap(spring_bar, TLCoorX, TLCoorY, null);

	}


	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(CoorX >= TLCoorX - 35 * DoodleJumpActivity.width_mul && CoorX <= TLCoorX + 45 * DoodleJumpActivity.width_mul && CoorY + 45 * DoodleJumpActivity.height_mul <= TLCoorY && TLCoorY - CoorY <= 45 * DoodleJumpActivity.height_mul){
		    SoundPlayer.playSound(SoundPlayer.SOUND_SPRING_BAR);
			return true;
		}
		return false;
	}


	@Override
	public void clear() {
	//	spring_bar.recycle();
	}

}
