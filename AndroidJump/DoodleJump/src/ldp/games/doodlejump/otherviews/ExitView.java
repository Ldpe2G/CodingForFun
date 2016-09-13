package ldp.games.doodlejump.otherviews;

//import com.juzi.main.AppConnect;

import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class ExitView extends View {

	boolean[] isclick = new boolean[2];
	DoodleJumpActivity context;
	
	private Animation alphAnimation;
	
	public void startEntryAnim(){
		alphAnimation = new AlphaAnimation(0.0f, 1.0f);  
		alphAnimation.setDuration(Constants.ANIMATION_TIME);
		startAnimation(alphAnimation);
	}
	
	public void startExitAnim(){
		alphAnimation = new AlphaAnimation(1.0f, 0.0f);  
		alphAnimation.setDuration(Constants.ANIMATION_TIME);
		startAnimation(alphAnimation);
	}
	
	public ExitView(DoodleJumpActivity context) {
		super(context);
		this.context = context;
		for(int i=0; i<2; i++)
			isclick[i] = false;
		new Thread(new ExitThread()).start();
		startEntryAnim();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		DrawTitle(canvas);
		DrawButton(canvas);
		super.onDraw(canvas);
	}

	private void DrawButton(Canvas canvas) {
		int X = (int) (DoodleJumpActivity.screen_width / 2 - 70* DoodleJumpActivity.height_mul);
		int Y = (int) (DoodleJumpActivity.screen_height / 2);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#a0f60b"));
		paint.setAlpha(60);
        if(isclick[0]){
        	//startExitAnim();
        	//AppConnect.getInstance(context).finalize();
        	android.os.Process.killProcess(android.os.Process.myPid()); 
        }
        if(isclick[1]){
        	//startExitAnim();
			context.handler.sendEmptyMessage(DoodleJumpActivity.WELCOME);
        }
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("exit", X, Y, paint2);
		canvas.drawText("back", X + 100* DoodleJumpActivity.height_mul, Y, paint2);
	}

	private void DrawBackground(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#c5c5c5"));
		for(int i=0; i < DoodleJumpActivity.screen_height; i = (int) (i + 10* DoodleJumpActivity.height_mul))
			canvas.drawLine(0, i, DoodleJumpActivity.screen_width, i, paint);
		for(int i=0; i < DoodleJumpActivity.screen_width; i = (int) (i + 10* DoodleJumpActivity.height_mul))
			canvas.drawLine(i, 0, i, DoodleJumpActivity.screen_height, paint);
		
	}

	private void DrawTitle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#f97f09"));
		int y = (int) (DoodleJumpActivity.screen_height / 12);
		canvas.drawText("Exit?", DoodleJumpActivity.screen_width/2 - 40* DoodleJumpActivity.height_mul, y+100* DoodleJumpActivity.height_mul, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			int X = (int) (DoodleJumpActivity.screen_width / 2 - 70* DoodleJumpActivity.height_mul);
			int Y = (int) (DoodleJumpActivity.screen_height / 2);
			int x = (int) event.getX();
			int y = (int) event.getY();
			if(x > X-20 * DoodleJumpActivity.height_mul&& x < X + 50 * DoodleJumpActivity.height_mul&& y > Y - 40* DoodleJumpActivity.height_mul && y < Y + 40* DoodleJumpActivity.height_mul)
				isclick[0] = true;
			if(x > X+80 * DoodleJumpActivity.height_mul&& x < X + 150 * DoodleJumpActivity.height_mul&& y > Y - 40 * DoodleJumpActivity.height_mul&& y < Y + 40* DoodleJumpActivity.height_mul)
				isclick[1] = true;
		}
		return super.onTouchEvent(event);
	}

	
	
	private class ExitThread implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					// TODO: handle exception
				}
				postInvalidate();
			}
		}
		
		
	}
	

}
