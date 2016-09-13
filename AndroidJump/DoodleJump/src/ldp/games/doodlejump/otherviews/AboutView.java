package ldp.games.doodlejump.otherviews;

import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AboutView extends View {

	DoodleJumpActivity doodleJumpActivity;
	boolean isclick = false;
	
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
	
	public AboutView(DoodleJumpActivity context) {
		super(context);
		doodleJumpActivity = context;
		new Thread(new AboutThread()).start();
		startEntryAnim();
	}
	
	
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		DrawTitle(canvas);
		DrawButton(canvas);
		DrawAbout(canvas);
		super.onDraw(canvas);
	}





	private void DrawAbout(Canvas canvas) {
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(20* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		int Y = (int) (DoodleJumpActivity.screen_height/8+100 * DoodleJumpActivity.height_mul);
		int X = (int) (DoodleJumpActivity.screen_width/2 - 70 * DoodleJumpActivity.height_mul);
		canvas.drawText("Android Jump", X, Y, paint2);
		canvas.drawText("Version 1.1.1", X, Y+30 * DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("Published by Liang Depeng", X-50 * DoodleJumpActivity.height_mul, Y+80 * DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("Original concept and game design", X-80 * DoodleJumpActivity.height_mul, Y+110 * DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("reference DoodleJump", X-20 * DoodleJumpActivity.height_mul, Y+140 * DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("innovated by Liang Depeng", X-50 * DoodleJumpActivity.height_mul, Y+180 * DoodleJumpActivity.height_mul, paint2);
	}





	private void DrawButton(Canvas canvas) {
		int y = (int) (DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul);
		int x = (int) (DoodleJumpActivity.screen_width/4*3);
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("back", DoodleJumpActivity.screen_width/4*3, DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul, paint2);
		paint2.setColor(Color.parseColor("#a0f60b"));
		paint2.setAlpha(60);
		if(isclick)
			canvas.drawRect(x - 20 * DoodleJumpActivity.height_mul, y - 40 * DoodleJumpActivity.height_mul, x + 70 * DoodleJumpActivity.height_mul, y + 20 * DoodleJumpActivity.height_mul, paint2);
	}





	private void DrawTitle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#f97f09"));
		int y = (int) (DoodleJumpActivity.screen_height / 12);
		canvas.drawText("About", DoodleJumpActivity.screen_width/2 - 60 * DoodleJumpActivity.height_mul, y, paint);
	}





	private void DrawBackground(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#c5c5c5"));
		for(int i=0; i < DoodleJumpActivity.screen_height; i = (int) (i + 10 * DoodleJumpActivity.height_mul))
			canvas.drawLine(0, i, DoodleJumpActivity.screen_width, i, paint);
		for(int i=0; i < DoodleJumpActivity.screen_width; i = (int) (i + 10 * DoodleJumpActivity.height_mul))
			canvas.drawLine(i, 0, i, DoodleJumpActivity.screen_height, paint);
	}



	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) (DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul);
		int x = (int) (DoodleJumpActivity.screen_width/4*3);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float preX = event.getX();
			float preY = event.getY();
			if(preX > x - 20 * DoodleJumpActivity.height_mul && preX < x+70 * DoodleJumpActivity.height_mul && preY > y - 40 * DoodleJumpActivity.height_mul && preY < y + 20 * DoodleJumpActivity.height_mul)
				isclick = true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(isclick){
				startExitAnim();
				doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.WELCOME);
			}
		}
		return true;
	}


	private class AboutThread implements Runnable{

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
