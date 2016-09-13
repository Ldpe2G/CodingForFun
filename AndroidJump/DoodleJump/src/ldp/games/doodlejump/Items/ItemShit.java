package ldp.games.doodlejump.Items;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ItemShit extends AbstractItem {
	
	Bitmap item_shit;
	
	public ItemShit(DoodleJumpActivity context){
		this.type = TYPE_SHIT;
		item_shit = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.dabian_item)).getBitmap();
	}

	@Override
	public void DrawSelf(Canvas canvas, float CoorX, float CoorY) {
		canvas.drawBitmap(item_shit, CoorX, CoorY, null);
	}

	@Override
	public void ModifyAndroid(AbstractAndroid android) {
		android.MinusLifeBar(10);
	}

}
