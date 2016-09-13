package ldp.games.doodlejump.Items;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ItemUpgradeBullet extends AbstractItem {

	Bitmap Upgrade;
	
	public ItemUpgradeBullet(DoodleJumpActivity context){
		Upgrade = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.upgrade)).getBitmap();
	}
	
	@Override
	public void DrawSelf(Canvas canvas, float CoorX, float CoorY) {
		canvas.drawBitmap(Upgrade, CoorX, CoorY, null);
	}

	@Override
	public void ModifyAndroid(AbstractAndroid android) {
		if(android.bullet_level < 3)
			android.bullet_level ++;
	}

}
