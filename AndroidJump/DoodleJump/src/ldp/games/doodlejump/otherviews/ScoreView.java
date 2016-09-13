package ldp.games.doodlejump.otherviews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ldp.games.doodlejump.Constants;
import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.sqlite.DataBaseOperation;
import ldp.games.doodlejump.sqlite.MyString;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class ScoreView extends View implements OnGestureListener{
	
	
	List<MyString> scoreList = new ArrayList<MyString>();
	List<Coor> list = new ArrayList<ScoreView.Coor>();
	private DataBaseOperation dataBaseOperation;
	private boolean isclick = false;
	DoodleJumpActivity doodleJumpActivity;
	
	
	Coor current_point = new Coor(0, 0);
	Coor pre_point = new Coor(0, 0);
	
	float preX;
	float preY;
	float y_distance;
	//float tempy;
	Animation alphAnimation;
	
	public ScoreView(DoodleJumpActivity context) {
		super(context);
		GetScoreMap(context);
		doodleJumpActivity = context;
		new Thread(new ScoreThread()).start();
		startEntryAnim();
	}
	
	public void startEntryAnim(){
		alphAnimation = new AlphaAnimation(0.1f, 1.0f);  
		alphAnimation.setDuration(Constants.ANIMATION_TIME);
		startAnimation(alphAnimation);
	}
	
	public void startExitAnim(){
		alphAnimation = new AlphaAnimation(1.0f, 0.1f);  
		alphAnimation.setDuration(Constants.ANIMATION_TIME);
		startAnimation(alphAnimation);
	}
	

	private void GetScoreMap(Context context) {
		int X = (int) (DoodleJumpActivity.screen_width/4-60* DoodleJumpActivity.height_mul);
		int Y = (int) (DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul);
		dataBaseOperation = new DataBaseOperation(context);
		scoreList = dataBaseOperation.GetScoreList();
		for(int i=0; i<scoreList.size(); i++){
			list.add(new Coor(X, Y));
			Y += 60 * DoodleJumpActivity.height_mul;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.parseColor("#faf0cc"));
		DrawBackground(canvas);
		DrawTitle(canvas);
		DrawButton(canvas);
		DrawScore(canvas);
		super.onDraw(canvas);
	}
	
	private void DrawScore(Canvas canvas) {
		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setTextSize(20* DoodleJumpActivity.height_mul);
		paint2.setColor(Color.parseColor("#173403"));
		int num = 0;
		for(int i=0; i<scoreList.size(); i++){
			if(list.get(num).Y >= DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul && list.get(num).Y <= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul)
				canvas.drawText(scoreList.get(i).getLine_one(), list.get(num).X, list.get(num).Y, paint2);
			if(list.get(num).Y + 20  * DoodleJumpActivity.height_mul  >= DoodleJumpActivity.screen_height/8+20 * DoodleJumpActivity.height_mul && list.get(num).Y + 20  * DoodleJumpActivity.height_mul <= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul)	
				canvas.drawText(scoreList.get(i).getLine_two(), list.get(num).X + 20* DoodleJumpActivity.height_mul, list.get(num).Y + 20 * DoodleJumpActivity.height_mul, paint2);
			
			num++;
		}
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
		canvas.drawText("Score", DoodleJumpActivity.screen_width/2 - 60* DoodleJumpActivity.height_mul, y, paint);
		
			
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) (DoodleJumpActivity.screen_height-50* DoodleJumpActivity.height_mul);
		int x = (int) (DoodleJumpActivity.screen_width/4*3);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			preX = event.getX();
			preY = event.getY();
		/*	int X = (int) (DoodleJumpActivity.screen_width/4-30* DoodleJumpActivity.height_mul);
			int Y = (int) (DoodleJumpActivity.screen_height/8);*/
			if(preX > x - 20* DoodleJumpActivity.height_mul && preX < x+70* DoodleJumpActivity.height_mul && preY > y - 40* DoodleJumpActivity.height_mul && preY < y + 20* DoodleJumpActivity.height_mul)
				isclick = true;
			
			pre_point.Y = (int) event.getY();
			for(int i=0; i<list.size(); i++)
				list.get(i).length_y = list.get(i).Y - pre_point.Y;
		/*	if(preX > X - 20 * DoodleJumpActivity.height_mul && preX < X + 60* DoodleJumpActivity.height_mul && preY > Y - 20* DoodleJumpActivity.height_mul && preY < Y + 40* DoodleJumpActivity.height_mul)
				MoveUp();
			
			if(preX > X - 50* DoodleJumpActivity.height_mul && preX < X + 60* DoodleJumpActivity.height_mul && preY > DoodleJumpActivity.screen_height-70* DoodleJumpActivity.height_mul && preY < DoodleJumpActivity.screen_height-10* DoodleJumpActivity.height_mul)
				MoveDown();*/
			
			
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
			current_point.Y = (int) event.getY();
		///	int dis = current_point.Y - pre_point.Y;
			if(list.size() > 0){
				if((list.get(0).Y < DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul && list.get(list.size() - 1).Y <= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul)  
						|| (list.get(0).Y <= DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul && list.get(list.size() - 1).Y >= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul) 
						|| (list.get(0).Y >= DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul && list.get(list.size() - 1).Y >= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul)){
						for(int i=0; i<list.size(); i++){
							/*if((list.get(0).Y < DoodleJumpActivity.screen_height/8+20* DoodleJumpActivity.height_mul && dis > 0) 
									|| (list.get(list.size() - 1).Y >= DoodleJumpActivity.screen_height-80* DoodleJumpActivity.height_mul && dis < 0))*/
								list.get(i).Y = current_point.Y + list.get(i).length_y;
						}
				}
			}
		}
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(isclick){
				startExitAnim();
				doodleJumpActivity.handler.sendEmptyMessage(DoodleJumpActivity.WELCOME);
			}
		}
		
		return true;
	}

	
	private class ScoreThread implements Runnable{

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
	
	private class Coor{
		int X;
		int Y;
		int length_y;
		public Coor(int X, int Y){
			this.X = X;
			this.Y = Y;
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if(list.size() > 0){
			
		}
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}
