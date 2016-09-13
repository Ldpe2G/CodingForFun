package ldp.games.doodlejump.bars;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ThronBar extends AbstractBar {

	public static final int THRON_BAR_DAMAGE = 20;
	Bitmap thron_bar;
	
	public ThronBar(int CoorX, int CoorY, DoodleJumpActivity context){
		this.TLCoorX = CoorX;
		this.TLCoorY = CoorY;
		this.type = TYPE_THRON;
		this.thron_bar = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.thron_bar)).getBitmap();
	}
	
	@Override
	public void drawSelf(Canvas canvas) {
		canvas.drawBitmap(thron_bar, TLCoorX, TLCoorY, null);
	}

	@Override
	public boolean IsBeingStep(float CoorX, float CoorY) {
		if(CoorX >= TLCoorX - 35 * DoodleJumpActivity.width_mul && CoorX <= TLCoorX + 45 * DoodleJumpActivity.width_mul && CoorY + 45 * DoodleJumpActivity.height_mul <= TLCoorY && TLCoorY - CoorY <= 45 * DoodleJumpActivity.height_mul)
			return true;
		return false;
	}

	@Override
	public void clear() {
	//	thron_bar.recycle();
	}

}
