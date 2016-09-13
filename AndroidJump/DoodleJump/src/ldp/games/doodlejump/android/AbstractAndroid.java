package ldp.games.doodlejump.android;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.OBjectsManager;
import android.graphics.Canvas;

public abstract class AbstractAndroid {
	
	
	
	
	public static final float DEFAULT_VERTICAL_ACCELERATE       = 1 * DoodleJumpActivity.height_mul; 
	protected static final float INITIAL_COORX                  = 145 * DoodleJumpActivity.width_mul;
	protected static final float INITIAL_COORY                  = 380 * DoodleJumpActivity.height_mul;
	public static final float INITIAL_VERTICAL_SPEED            = -19 * DoodleJumpActivity.height_mul; 
	public static final float MAX_VERTICAL_SPEED                = 19 * DoodleJumpActivity.height_mul;
	public static final int   STATE_GO_UP                       = 1;
	public static final int   STATE_GO_DOWN                     = 2;
    public static final int   INITIAL_LIFE_NUM                  = 10;
	public static final int   INITIAL_LIFE_BAR                  = 100;
    public static final int   INITIAL_LUANCHER_BULLET_TIMES     = 100;
	public static final int   HANDS_UP                          = 1;
	public static final int   HANDS_DOWN                        = 0;
    
	public float LTCoorX;
	public float LTCoorY;
	
	public float accelerameter;
	public float horizonal_speed;
	public float vertical_speed;
	
	public int current_state;
	public int life_bar;
	public int bullet_times;
	public int life_num;
	public boolean isfalldown;
	public int bitmap_index;
	public int bullet_level;
	
	public abstract void DrawSelf(Canvas canvas);
	public abstract void Move();
	public abstract void CheckAndroidCoor(OBjectsManager oBjectsManager);
	public abstract void MinusLifeBar(int num);
	public abstract void AddLifeBar(int num);
}
