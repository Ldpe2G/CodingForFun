package ldp.games.doodlejump.android;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.LogicManager;
import ldp.games.doodlejump.OBjectsManager;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.otherviews.OptionView;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class Android extends AbstractAndroid {
	
	private boolean ishurt = false;
	private int  invincible_time = 0;
	
	public Bitmap[] normal_android;
	public Bitmap[] hurt_android;
	public Bitmap bullet;
	public Bitmap life_littleandroid;
	
	public Android(DoodleJumpActivity context, int X, int Y){
		this(context);
		this.LTCoorX = X;
		this.LTCoorY = Y;
		
	}
	
	public Android(DoodleJumpActivity context){
		this.LTCoorX = INITIAL_COORX;
		this.LTCoorY = INITIAL_COORY;
		this.horizonal_speed = 0;
		this.vertical_speed = INITIAL_VERTICAL_SPEED + 5 * DoodleJumpActivity.height_mul;
		this.current_state = STATE_GO_UP;
	    accelerameter = DEFAULT_VERTICAL_ACCELERATE;
	    this.life_bar = INITIAL_LIFE_BAR;
	    this.life_num = INITIAL_LIFE_NUM;
	    this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES;
	    this.bitmap_index = HANDS_DOWN; 
	    this.isfalldown = false;
	    this.bullet_level = 1;
	    
	    getDiffSetting(context);
	    initBitMap(context);
	}
	
	private void getDiffSetting(DoodleJumpActivity context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(OptionView.PREFS_DIFF, 0);
		String diffString = sharedPreferences.getString(OptionView.PREFS_DIFF, "Rookie");
		if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_ROOKIE)){
			 this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES * 2;
		}
		else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_NORMAL)){
			this.life_num = INITIAL_LIFE_NUM - 1;
		}
		else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_MASTER)){
			this.life_num = INITIAL_LIFE_NUM - 3;
		    this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES - 20;
		}
		else{
			this.life_num = INITIAL_LIFE_NUM - 5;
		    this.bullet_times = INITIAL_LUANCHER_BULLET_TIMES - 50;
		}
	}

	private void initBitMap(DoodleJumpActivity context){
		normal_android = new Bitmap[2];
		normal_android[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android)).getBitmap();
		normal_android[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_launch)).getBitmap();
		
		hurt_android = new Bitmap[2];
		hurt_android[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt)).getBitmap();
		hurt_android[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt_launch)).getBitmap();
		
		life_littleandroid = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.life_bar_1)).getBitmap();
		
		bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet)).getBitmap(); 
	}

	@Override
	public void DrawSelf(Canvas canvas) {
		if(!ishurt)
			canvas.drawBitmap(normal_android[bitmap_index], LTCoorX, LTCoorY, null);
		else{
			if(invincible_time %2 == 0)
				canvas.drawBitmap(normal_android[bitmap_index], LTCoorX, LTCoorY, null);
			else
				canvas.drawBitmap(hurt_android[bitmap_index], LTCoorX, LTCoorY, null);
			invincible_time ++;
			if(invincible_time > 20){
				invincible_time = 0;
				ishurt = false;
			}
		}
		DrawLifeBar(canvas);
		DrawLifeNumandBulletTimes(canvas);
		if(bitmap_index == HANDS_UP)
			bitmap_index = HANDS_DOWN;
	}

	
	

	private void DrawLifeNumandBulletTimes(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(15* DoodleJumpActivity.height_mul);
		canvas.drawText("Life X", DoodleJumpActivity.screen_width/3, 12* DoodleJumpActivity.height_mul, paint);
		for(int i=0; i<life_num; i++){
			canvas.drawBitmap(life_littleandroid, DoodleJumpActivity.screen_width/3+25* DoodleJumpActivity.height_mul+i*17*DoodleJumpActivity.height_mul, -2 * DoodleJumpActivity.height_mul, null);
		}
		canvas.drawBitmap(bullet, DoodleJumpActivity.screen_width/3+130* DoodleJumpActivity.height_mul, 15* DoodleJumpActivity.height_mul, null);
		canvas.drawText(" X"+bullet_times, DoodleJumpActivity.screen_width/3+140* DoodleJumpActivity.height_mul, 25* DoodleJumpActivity.height_mul, paint);
	}

	private void DrawLifeBar(Canvas canvas) {
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setTextSize(15* DoodleJumpActivity.height_mul);
		canvas.drawText("HP:", DoodleJumpActivity.screen_width/3, 25* DoodleJumpActivity.height_mul, paint);
		if(ishurt)
			paint.setColor(Color.RED);
		else
			paint.setColor(Color.GREEN);
		canvas.drawRect(DoodleJumpActivity.screen_width/3+25* DoodleJumpActivity.height_mul, 15* DoodleJumpActivity.height_mul, DoodleJumpActivity.screen_width/3+25* DoodleJumpActivity.height_mul+life_bar * DoodleJumpActivity.height_mul, 25* DoodleJumpActivity.height_mul, paint);
	}

	public static AbstractAndroid AndroidFactory(DoodleJumpActivity context){
		return new Android(context);
	}

	@Override
	public void Move() {
		vertical_speed += accelerameter*1;
		LTCoorX += horizonal_speed;
		horizonal_speed = 0;
	}

	@Override
	public void CheckAndroidCoor(OBjectsManager oBjectsManager) {
		if(LTCoorY > DoodleJumpActivity.screen_height){
			this.MinusLifeBar(LogicManager.FALL_DOWN_DAMAGE);
			vertical_speed = Android.INITIAL_VERTICAL_SPEED;
			current_state = Android.STATE_GO_UP;
			oBjectsManager.isrepeated = false;
		}
		if(LTCoorX <= -40 * DoodleJumpActivity.width_mul)
			LTCoorX = DoodleJumpActivity.screen_width - 40 * DoodleJumpActivity.width_mul;
		if(LTCoorX >= DoodleJumpActivity.screen_width)
			LTCoorX = 0;
	}

	@Override
	public void MinusLifeBar(int num) {
		if(!ishurt){
		    SoundPlayer.playSound(SoundPlayer.SOUND_INJURY);
			ishurt = true;
			this.life_bar -= num;
			if(this.life_bar <= 0){
				this.life_bar = INITIAL_LIFE_BAR;
				this.life_num --;
				this.bullet_level = 1;
			}
		}
	}

	
	@Override
	public void AddLifeBar(int num) {
		this.life_bar += num;
		if(this.life_bar > INITIAL_LIFE_BAR)
			this.life_bar = INITIAL_LIFE_BAR;
	}

}
