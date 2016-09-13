package ldp.games.doodlejump.Items;

import ldp.games.doodlejump.android.AbstractAndroid;
import android.graphics.Canvas;

public abstract class AbstractItem {
	public static final int TYPE_NULL   = 0;
	public static final int TYPE_FRUIT  = 1;
	public static final int TYPE_SHIT   = 2;
	public static final int TYPE_LIFE   = 3;
	public static final int TYPE_BULLET = 4;
	
	public int type;
	
	
	public abstract void DrawSelf(Canvas canvas, float CoorX, float CoorY);
	public abstract void ModifyAndroid(AbstractAndroid android); 
	
}
