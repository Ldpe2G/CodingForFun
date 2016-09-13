package ldp.games.doodlejump.otherviews;


import java.util.Date;
import java.util.Random;


import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.android.Android;
import ldp.games.doodlejump.bars.NormalBar;
import ldp.games.doodlejump.monsters.AbstractMonster;
import ldp.games.doodlejump.monsters.BlackStrone;
import ldp.games.doodlejump.monsters.EatingHead;
import ldp.games.doodlejump.monsters.RotateMonster;
import ldp.games.doodlejump.resource.BitmapManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class WelcomeView extends View/* SurfaceView implements SurfaceHolder.Callback*/{

	private boolean[] isclick;
	DoodleJumpActivity doodleJumpActivity;
	
	Android[] android;
	int index;
	NormalBar[] normalBars;
	AbstractMonster monster;
	AbstractMonster monster2;
	
	
	private boolean isviewrunning = true;
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
	
	
	public WelcomeView(DoodleJumpActivity context) {
		super(context);
		this.doodleJumpActivity = context;
		isclick = new boolean[5];
		for(int i=0; i<5; i++)
			isclick[i] = false;
		index = 0;
		int android_heihgt = (int) (DoodleJumpActivity.screen_height / 4);
		int android_width = (int) (DoodleJumpActivity.screen_width / 5);
		android = new Android[2];
		android[0] = new Android(context, android_width-50, android_heihgt);
		android[1] = new Android(context, android_width*4, android_heihgt);
		normalBars = new NormalBar[2];
		normalBars[0] = new NormalBar(android_width-55, DoodleJumpActivity.screen_height/3+250 * DoodleJumpActivity.height_mul, null, context);
		normalBars[1] = new NormalBar(android_width*4-5, DoodleJumpActivity.screen_height/3+250 * DoodleJumpActivity.height_mul, null, context);
		GetMonster(context);
		new Thread(new WelcomeThread()).start();
		startEntryAnim();
	}	
	
	

	private void GetMonster(DoodleJumpActivity context) {
		int temp = new Random().nextInt(3);
		if(temp == 0)
			monster = new EatingHead(DoodleJumpActivity.screen_height/3 - 25* DoodleJumpActivity.height_mul, context);
		else if(temp == 1)
			monster = new RotateMonster(DoodleJumpActivity.screen_height/3 - 25* DoodleJumpActivity.height_mul, context);
		else 
			monster = new BlackStrone(DoodleJumpActivity.screen_height/3 - 25* DoodleJumpActivity.height_mul, context);
		
		temp = new Random().nextInt(3);
		if(temp == 0)
			monster2 = new EatingHead(DoodleJumpActivity.screen_height/12+30* DoodleJumpActivity.height_mul, context);
		else if(temp == 1)
			monster2 = new RotateMonster(DoodleJumpActivity.screen_height/12+30* DoodleJumpActivity.height_mul, context);
		else 
			monster2 = new BlackStrone(DoodleJumpActivity.screen_height/12+30* DoodleJumpActivity.height_mul, context); 
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		monster2.DrawSelf(canvas);
		DrawTitle(canvas);
		DrawButton(canvas);
		monster.DrawSelf(canvas);
		DrawAndroid(canvas);
		super.onDraw(canvas);
	}

	private void DrawAndroid(Canvas canvas) {
		for(int i=0; i<2; i++){
			canvas.drawBitmap(android[i].normal_android[android[i].bitmap_index], android[i].LTCoorX, android[i].LTCoorY, null);
			android[i].bitmap_index = 0;
			normalBars[i].drawSelf(canvas);
		}
	}

	private void DrawButton(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		int X = (int) (DoodleJumpActivity.screen_width/2);
		int Y = (int) (DoodleJumpActivity.screen_height/3);
		paint.setColor(Color.parseColor("#a0f60b"));
		paint.setAlpha(60);
		if(isclick[0]){
			canvas.drawRect(X - 60 * DoodleJumpActivity.height_mul, Y - 40 * DoodleJumpActivity.height_mul, X + 40 * DoodleJumpActivity.height_mul, Y + 20 * DoodleJumpActivity.height_mul, paint);
			//doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.GAME_START);
		}
		if(isclick[1]){
			canvas.drawRect(X - 60 * DoodleJumpActivity.height_mul, Y + 30 * DoodleJumpActivity.height_mul, X + 60 * DoodleJumpActivity.height_mul, Y + 90 * DoodleJumpActivity.height_mul, paint);
			//doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.SCORE);
		}
		if(isclick[2]){
			canvas.drawRect(X - 60 * DoodleJumpActivity.height_mul, Y + 100 * DoodleJumpActivity.height_mul, X + 70 * DoodleJumpActivity.height_mul, Y + 160 * DoodleJumpActivity.height_mul, paint);
			//doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.OPTION);
		}
		if(isclick[3]){
			canvas.drawRect(X - 60 * DoodleJumpActivity.height_mul, Y + 170 * DoodleJumpActivity.height_mul, X + 60 * DoodleJumpActivity.height_mul, Y + 230 * DoodleJumpActivity.height_mul, paint);
			//doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.ABOUT);
		}
		if(isclick[4]){
			canvas.drawRect(X - 60 * DoodleJumpActivity.height_mul, Y + 240 * DoodleJumpActivity.height_mul, X + 30 * DoodleJumpActivity.height_mul, Y + 300 * DoodleJumpActivity.height_mul, paint);
			//doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.EXIT);
		}
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("Start", X - 40* DoodleJumpActivity.height_mul, Y, paint2);
		canvas.drawText("Score", X - 40* DoodleJumpActivity.height_mul, Y+70* DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("Option", X - 40* DoodleJumpActivity.height_mul, Y+140* DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("About", X - 40* DoodleJumpActivity.height_mul, Y+210* DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("Exit", X - 40* DoodleJumpActivity.height_mul, Y+280* DoodleJumpActivity.height_mul, paint2);
	}

	private void DrawTitle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#f97f09"));
		int y = (int) (DoodleJumpActivity.screen_height / 12);
		canvas.drawText("Android", DoodleJumpActivity.screen_width/4, y, paint);
		canvas.drawText("Jump", DoodleJumpActivity.screen_width/4+100* DoodleJumpActivity.height_mul, y+50* DoodleJumpActivity.height_mul, paint);
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
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		int X = (int) (DoodleJumpActivity.screen_width/2);
    		int Y = (int) (DoodleJumpActivity.screen_height/3);
    		int touch_x = (int) event.getX();
    		int touch_Y= (int) event.getY();
    		
    		if(touch_x > X - 60* DoodleJumpActivity.height_mul && touch_x < X + 60 * DoodleJumpActivity.height_mul&& touch_Y > Y - 40 * DoodleJumpActivity.height_mul&& touch_Y < Y + 20* DoodleJumpActivity.height_mul)
    			isclick[0] = true;
    		else if(touch_x > X - 60* DoodleJumpActivity.height_mul && touch_x < X + 60 * DoodleJumpActivity.height_mul&& touch_Y > Y + 30* DoodleJumpActivity.height_mul && touch_Y < Y + 90* DoodleJumpActivity.height_mul)
    			isclick[1] = true;
    		else if(touch_x > X - 60* DoodleJumpActivity.height_mul && touch_x < X + 60 * DoodleJumpActivity.height_mul&& touch_Y > Y + 100* DoodleJumpActivity.height_mul && touch_Y < Y + 160* DoodleJumpActivity.height_mul)
    			isclick[2] = true;
    		else if(touch_x > X - 60* DoodleJumpActivity.height_mul && touch_x < X + 60 * DoodleJumpActivity.height_mul&& touch_Y > Y + 170* DoodleJumpActivity.height_mul && touch_Y < Y + 230* DoodleJumpActivity.height_mul)
    			isclick[3] = true;
    		else if(touch_x > X - 60 * DoodleJumpActivity.height_mul&& touch_x < X + 60* DoodleJumpActivity.height_mul && touch_Y > Y + 240* DoodleJumpActivity.height_mul && touch_Y < Y + 300* DoodleJumpActivity.height_mul)
    			isclick[4] = true;
    		else{
    			index = index == 0 ? 1 : 0;
    			android[index].bitmap_index = Android.HANDS_UP;
    		}
		}
    	
    	if(event.getAction() == MotionEvent.ACTION_UP){
    		if(isclick[0]){
    			startExitAnim();
    			isviewrunning = false;
    			doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.GAME_START);
    		}
    		if(isclick[1]){
    			startExitAnim();
    			isviewrunning = false;
    			doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.SCORE);
    		}
    		if(isclick[2]){
    			startExitAnim();
    			isviewrunning = false;
    			doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.OPTION);
    		}
    		if(isclick[3]){
    			startExitAnim();
    			isviewrunning = false;
    			doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.ABOUT);
    		}
    		if(isclick[4]){
    			startExitAnim();
    			isviewrunning = false;
    			doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.EXIT);
    		}
    	}
		return true;
	}
	

	private class WelcomeThread implements Runnable{

		@Override
		public void run() {
			while(isviewrunning){
				try {
					//repaint();
					Thread.sleep(50);
				} catch (Exception e) {
					// TODO: handle exception
				}
				for(int i=0; i<2; i++){
					android[i].Move();
					android[i].LTCoorY += (int)android[i].vertical_speed;
					if(android[i].vertical_speed > Android.MAX_VERTICAL_SPEED)
						android[i].vertical_speed = Android.MAX_VERTICAL_SPEED;
					if(android[i].LTCoorY >= DoodleJumpActivity.screen_height/3+ 200 * DoodleJumpActivity.height_mul)
						android[i].vertical_speed = Android.INITIAL_VERTICAL_SPEED;
				}
				postInvalidate();
				//repaint();
			}
		}
		
	}
	
/*
	public void repaint(){
		SurfaceHolder surfaceHolder = this.getHolder();
		Canvas canvas = surfaceHolder.lockCanvas();
		try{
			synchronized (surfaceHolder) {
				//onDraw(canvas);
				canvas.drawColor(Color.parseColor("#faf0cc"));
				DrawBackground(canvas);
				monster2.DrawSelf(canvas);
				DrawTitle(canvas);
				DrawButton(canvas);
				monster.DrawSelf(canvas);
				DrawAndroid(canvas);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(canvas != null)
				surfaceHolder.unlockCanvasAndPost(canvas);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		repaint();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
	*/

}
