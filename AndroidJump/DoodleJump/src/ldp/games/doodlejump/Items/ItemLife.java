package ldp.games.doodlejump.Items;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ItemLife extends AbstractItem {

	Bitmap item_life;
	
	public ItemLife(DoodleJumpActivity context){
		this.type = TYPE_LIFE;
		item_life = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.life_item)).getBitmap();
	}
	
	@Override
	public void DrawSelf(Canvas canvas, float CoorX, float CoorY) {
		canvas.drawBitmap(item_life, CoorX, CoorY, null);

	}

	@Override
	public void ModifyAndroid(AbstractAndroid android) {
		android.life_num ++;
	}

}
