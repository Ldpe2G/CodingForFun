package ldp.games.doodlejump.otherviews;

import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class OptionView extends View {
	
	public static final String DIFFICULTY_ROOKIE    = "Rookie";
	public static final String DIFFICULTY_NORMAL    = "Normal";
	public static final String DIFFICULTY_MASTER    = "Master";
	public static final String DIFFICULTY_BONE_ASH  = "Bone ash";

	public static final String PREFS_DIFF   = "difficulty";
	public static final String PREFS_NAME   = "MyPrefsFile";
	public static final String SOUND_STRING = "isplay_spund?";
	DoodleJumpActivity doodleJumpActivity;
	boolean isclick_back = false;
	boolean isplay_sound = true;
	SharedPreferences sound_setting;
	SharedPreferences difficulty_setting;
	String diffString;
	
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
	
	public OptionView(DoodleJumpActivity context) {
		super(context);
		doodleJumpActivity = context;
		difficulty_setting = context.getSharedPreferences(PREFS_DIFF, 0);
		sound_setting = context.getSharedPreferences(PREFS_NAME, 0);
		isplay_sound = sound_setting.getBoolean(SOUND_STRING, true);
		diffString = difficulty_setting.getString(PREFS_DIFF, DIFFICULTY_ROOKIE);
		new Thread(new OptionThread()).start();
		startEntryAnim();
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		DrawTitle(canvas);
		DrawSoundButton(canvas);
		DrawBackButton(canvas);
		super.onDraw(canvas);
	}
	
	private void DrawSoundButton(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(30* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#173403"));
		canvas.drawText("Sound?", DoodleJumpActivity.screen_width/2 - 40* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/2 - 90* DoodleJumpActivity.height_mul, paint);
		canvas.drawText("Difficulty?", DoodleJumpActivity.screen_width/2 - 55* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/2 - 0* DoodleJumpActivity.height_mul, paint);
		paint.setTextSize(20* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#7f7f7e"));
		canvas.drawText(diffString, DoodleJumpActivity.screen_width/2 - 30* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/2 + 30* DoodleJumpActivity.height_mul, paint);
		if(isplay_sound)
			canvas.drawText("On", DoodleJumpActivity.screen_width/2-10* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/2-60* DoodleJumpActivity.height_mul, paint);
		else
			canvas.drawText("Off", DoodleJumpActivity.screen_width/2-10* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_height/2-60* DoodleJumpActivity.height_mul, paint);
		
	}


	private void DrawBackButton(Canvas canvas) {
		int y = (int) (DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul);
		int x = (int) (DoodleJumpActivity.screen_width/4*3);
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(30* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		canvas.drawText("back", DoodleJumpActivity.screen_width/4*3, DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul, paint2);
		paint2.setColor(Color.parseColor("#a0f60b"));
		paint2.setAlpha(60);
		if(isclick_back)
			canvas.drawRect(x - 20 * DoodleJumpActivity.height_mul, y - 40 * DoodleJumpActivity.height_mul, x + 70 * DoodleJumpActivity.height_mul, y + 20 * DoodleJumpActivity.height_mul, paint2);
	}


	private void DrawTitle(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(50* DoodleJumpActivity.height_mul);
		paint.setColor(Color.parseColor("#f97f09"));
		int y = (int) (DoodleJumpActivity.screen_height / 12);
		canvas.drawText("Option", DoodleJumpActivity.screen_width/2 - 80* DoodleJumpActivity.height_mul, y, paint);
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
		int y = (int) (DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul);
		int x = (int) (DoodleJumpActivity.screen_width/4*3);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float preX = event.getX();
			float preY = event.getY();
			if(preX > x - 20 * DoodleJumpActivity.height_mul && preX < x+70 * DoodleJumpActivity.height_mul && preY > y - 20 * DoodleJumpActivity.height_mul && preY < y + 40* DoodleJumpActivity.height_mul)
				isclick_back = true;
			x = (int) (DoodleJumpActivity.screen_width/2);
			y = (int) (DoodleJumpActivity.screen_height/2);
			if(preX > x - 30 * DoodleJumpActivity.height_mul && preX < x + 40 * DoodleJumpActivity.height_mul && preY > y - 80 * DoodleJumpActivity.height_mul && preY < y - 20 * DoodleJumpActivity.height_mul){
				isplay_sound = isplay_sound == true ? false : true;
				SharedPreferences.Editor editor = sound_setting.edit();
			    editor.putBoolean(SOUND_STRING, isplay_sound);
			    editor.commit();
			    SoundPlayer.isplay_sound = isplay_sound;
			}
			if(preX > x - 50 * DoodleJumpActivity.height_mul && preX < x + 50 * DoodleJumpActivity.height_mul && preY > y + 10 * DoodleJumpActivity.height_mul && preY < y + 70 * DoodleJumpActivity.height_mul){
				diffString = getNewDifficulty();
				SharedPreferences.Editor editor = difficulty_setting.edit();
				editor.putString(PREFS_DIFF, diffString);
				editor.commit();
			}
		}
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(isclick_back){
				startExitAnim();
				doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.WELCOME);
			}
		}
		return true;
	}
	
	
	private String getNewDifficulty() {
		if(diffString.equalsIgnoreCase(DIFFICULTY_ROOKIE))
			return DIFFICULTY_NORMAL;
		else if(diffString.equalsIgnoreCase(DIFFICULTY_NORMAL))
			return DIFFICULTY_MASTER;
		else if(diffString.equalsIgnoreCase(DIFFICULTY_MASTER))
			return DIFFICULTY_BONE_ASH;
		return DIFFICULTY_ROOKIE;
	}


	private class OptionThread implements Runnable{

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
