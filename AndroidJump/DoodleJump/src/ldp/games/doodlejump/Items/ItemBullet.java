package ldp.games.doodlejump.Items;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ItemBullet extends AbstractItem {

	Bitmap item_bullet;
	
	public ItemBullet(DoodleJumpActivity context){
		this.type = TYPE_BULLET;
		item_bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet_item)).getBitmap();
	}
	
	@Override
	public void DrawSelf(Canvas canvas, float CoorX, float CoorY) {
		canvas.drawBitmap(item_bullet, CoorX, CoorY, null);

	}

	@Override
	public void ModifyAndroid(AbstractAndroid android) {
		android.bullet_times += 5;
	}

}
