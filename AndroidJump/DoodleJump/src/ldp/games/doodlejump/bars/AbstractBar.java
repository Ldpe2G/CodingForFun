package ldp.games.doodlejump.bars;

import java.security.PublicKey;

import ldp.games.doodlejump.Items.AbstractItem;

import android.graphics.Canvas;

public abstract class AbstractBar {
	
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_SPRING = 1;
	public static final int TYPE_SHIFT  = 2;
    public static final int TYPE_THRON  = 3;
	
	public int TLCoorX;
	public float TLCoorY;
	public int type;
	public int item_type;
	public AbstractItem item;
	public boolean isitemeaten;
	
	public abstract void clear();  //清除状态，在bar被remove时调用
	public abstract void drawSelf(Canvas canvas);
	public abstract boolean IsBeingStep(float CoorX, float CoorY);
}