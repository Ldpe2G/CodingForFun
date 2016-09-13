package ldp.games.doodlejump;

import java.util.Calendar;

import ldp.games.doodlejump.resource.BitmapManager;
import ldp.games.doodlejump.resource.SoundPlayer;
import ldp.games.doodlejump.sqlite.DataBaseOperation;
import android.R.bool;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class GameView extends View /* SurfaceView implements SurfaceHolder.Callback*/ {
   
	private static final int SLEEP_TIME = 50;
	//public static BitmapManager bitmapManager;
	public LogicManager logicManager;
	private Paint paint;
	public  static boolean isGameOver;  //判断游戏是否结束
	DoodleJumpActivity doodleJumpActivity;
	private DataBaseOperation dataBaseOperation;
	public static boolean ispause = false;
	int save_time = 0;
	private boolean isgamerunning = true;
	
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
	
	public GameView(DoodleJumpActivity context) {
		super(context);
		//bitmapManager = BitmapManager.getBitmapManager(context);
		logicManager = new LogicManager(context);
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#c5c5c5"));
		isGameOver = false;
		this.doodleJumpActivity = context;
		dataBaseOperation = new DataBaseOperation(context);
		ispause = false;
		new Thread(new GameThread()).start();
		startEntryAnim();
	}

	public void ReFresh(){
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		if(!ispause){
			logicManager.DrawAndroidAndBars(canvas);
			DrawTitle(canvas);
		}
		else{
			DrawPause(canvas);
		}
		super.onDraw(canvas);
	}
	
	private void DrawPause(Canvas canvas) {
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30 * DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("resume", DoodleJumpActivity.screen_width/2-40* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/10+50* DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("exit", DoodleJumpActivity.screen_width/2-20* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/10+150* DoodleJumpActivity.height_mul, paint2);
	}


	private void DrawTitle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.parseColor("#45faab"));
		paint.setAlpha(60);
		canvas.drawRect(0, 0, DoodleJumpActivity.screen_width, DoodleJumpActivity.screen_height/16, paint);
	}


	private void DrawBackground(Canvas canvas) {
		for(int i=0; i < DoodleJumpActivity.screen_height; i = (int) (i + 10 * DoodleJumpActivity.height_mul))
			canvas.drawLine(0, i, DoodleJumpActivity.screen_width, i, paint);
		for(int i=0; i < DoodleJumpActivity.screen_width; i = (int) (i + 10 * DoodleJumpActivity.height_mul))
			canvas.drawLine(i, 0, i, DoodleJumpActivity.screen_height, paint);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
    	if(event.getAction() == MotionEvent.ACTION_DOWN){
    		if(!ispause){
				logicManager.SetAndroidHands();
				logicManager.AddNewBulletSet();
    		}
    		else{
    			float x = event.getX();
    			float y = event.getY();
    			if(x > DoodleJumpActivity.screen_width/2-60* DoodleJumpActivity.height_mul && x < DoodleJumpActivity.screen_width/2+80* DoodleJumpActivity.height_mul
    			   && y > DoodleJumpActivity.screen_height/10+30* DoodleJumpActivity.height_mul && y<DoodleJumpActivity.screen_height/10+90* DoodleJumpActivity.height_mul)
    				ispause = false;
    			if(x > DoodleJumpActivity.screen_width/2-40* DoodleJumpActivity.height_mul && x < DoodleJumpActivity.screen_width/2+100* DoodleJumpActivity.height_mul
    			   && y > DoodleJumpActivity.screen_height/10+130* DoodleJumpActivity.height_mul && y<DoodleJumpActivity.screen_height/10+190* DoodleJumpActivity.height_mul){
    				LogicManager.isrunning = false;
    				isgamerunning = false;
    				doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.WELCOME);
    				GameView.ispause = false;
    			}
    		}
		}
		
		return super.onTouchEvent(event);
	}

	private String GetDate() {
		 Calendar c = Calendar.getInstance();
		 int mYear = c.get(Calendar.YEAR); //获取当前年份
		 int mMonth = c.get(Calendar.MONTH);//获取当前月份
		 int mDay = c.get(Calendar.DAY_OF_MONTH);//获取当前月份的日期号码
		 int mHour = c.get(Calendar.HOUR_OF_DAY);//获取当前的小时数
		 int mMinute = c.get(Calendar.MINUTE);//获取当前的分钟数
		 
		 return ""+mYear+"-"+mMonth+"-"+mDay+" "+mHour+":"+mMinute;
	}
		

	private class GameThread implements Runnable{

		@Override
		public void run() {
		
				while(isgamerunning){
					try {
						//repaint();
						Thread.sleep(GameView.SLEEP_TIME);
					} catch (Exception e) {
						// TODO: handle exception
					}
					postInvalidate();
				//	repaint();
					if(isGameOver == true){
						if(save_time == 0)
							dataBaseOperation.SaveHeight(OBjectsManager.sum, GetDate());
						save_time ++;
						isgamerunning = false;
	    				logicManager.Clear();
						doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.GAME_OVER);
					}
				}
				
			}

		
		
	}
/*
	public void repaint(){
		SurfaceHolder surfaceHolder = this.getHolder();
		Canvas canvas = surfaceHolder.lockCanvas();
		try{
			synchronized (surfaceHolder) {
				onDraw(canvas);
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
