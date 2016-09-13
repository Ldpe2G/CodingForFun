package ldp.games.doodlejump.monsters;

import ldp.games.doodlejump.android.AbstractAndroid;
import android.R.integer;
import android.graphics.Canvas;

public abstract class AbstractMonster {

	public static final int DIRECTION_LEFT  = 1;
	public static final int DIRECTION_RIGHT = 2;
	
	//Ô²ÐÄ×ø±ê
	protected int CoorX;
	public float CoorY;
	int direction;
	int bitmap_index;
	int horizontal_speed;
	protected boolean isdestory;
	public boolean isrunning;
	
	public abstract void BeingAttacked();
	public abstract boolean IsDestory(); 
	public abstract int GetX();
	public abstract void DrawSelf(Canvas canvas);
	public abstract void CheckDistance(AbstractAndroid android);
	public abstract boolean IsBeingStep(float CoorX, float CoorY);
}
