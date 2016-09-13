package ldp.games.doodlejump.bulletandexplode;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class Bullet {
	
//	private static final int DEFAULT_VERTICAL_ACCELERATE = 2;
	private static final int DEFAULT_HORIZONTAL_SPEED    = (int) (5 * DoodleJumpActivity.height_mul);
	private static final int INITIAL_VERTICAL_SPEED      = (int) (-20 * DoodleJumpActivity.height_mul);
	public Bitmap bullet;
	//Ô²ÐÄ×ø±ê
	int CoorX; 
	int CoorY;
	
	int horizontal_speed;
	int vertical_speed;
	
	public Bullet(int _CoorX, int _CoorY, int _horizontal_speed, DoodleJumpActivity context){
		this.CoorX = _CoorX;
		this.CoorY = _CoorY;
		this.horizontal_speed = _horizontal_speed;
		this.vertical_speed = INITIAL_VERTICAL_SPEED;	
		bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet)).getBitmap(); 
	}
	
	public void Move(){
		CoorY += vertical_speed;
		CoorX += horizontal_speed;
	}
	
	public void DrawSelf(Canvas canvas){
		canvas.drawBitmap(bullet, CoorX - 5 * DoodleJumpActivity.height_mul, CoorY - 5 * DoodleJumpActivity.height_mul, null);
	}

}
