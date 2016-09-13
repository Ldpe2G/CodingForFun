package ldp.games.doodlejump.otherviews;

//import com.juzi.main.AppConnect;

import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class FailView extends View {

	DoodleJumpActivity doodleJumpActivity;
	boolean isrunning = true;
	
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
	
	
	public FailView(DoodleJumpActivity context) {
		super(context);
		doodleJumpActivity = context;
	    SoundPlayer.playSound(SoundPlayer.SOUND_FAIL);
		DoodleJumpActivity.isGame_Running = false;
		new Thread(new FailThread()).start();
		startEntryAnim();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		DrawButton(canvas);
		super.onDraw(canvas);
	}

	private void DrawButton(Canvas canvas) {
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("restart", DoodleJumpActivity.screen_width/2-40* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/10+50* DoodleJumpActivity.height_mul, paint2);
		canvas.drawText("exit", DoodleJumpActivity.screen_width/2-20* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/10+150* DoodleJumpActivity.height_mul, paint2);
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
    			float x = event.getX();
    			float y = event.getY();
    			if(x > DoodleJumpActivity.screen_width/2-60* DoodleJumpActivity.height_mul && x < DoodleJumpActivity.screen_width/2+80* DoodleJumpActivity.height_mul
    			   && y > DoodleJumpActivity.screen_height/10+30* DoodleJumpActivity.height_mul && y<DoodleJumpActivity.screen_height/10+90){
    				startExitAnim();
    				doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.GAME_START);
    			}
    			if(x > DoodleJumpActivity.screen_width/2-40* DoodleJumpActivity.height_mul && x < DoodleJumpActivity.screen_width/2+100* DoodleJumpActivity.height_mul
    			   && y > DoodleJumpActivity.screen_height/10+130* DoodleJumpActivity.height_mul && y<DoodleJumpActivity.screen_height/10+190* DoodleJumpActivity.height_mul){
    				startExitAnim();
    				android.os.Process.killProcess(android.os.Process.myPid());
    				isrunning = false;
    			}
    	    	
    		}
		
		return super.onTouchEvent(event);
	}
	
	private class FailThread implements Runnable{

		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
				postInvalidate();
			}
			
		}
		
	}

}
