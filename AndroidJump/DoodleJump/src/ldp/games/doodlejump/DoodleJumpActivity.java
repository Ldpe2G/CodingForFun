package ldp.games.doodlejump;




import ldp.games.doodlejump.otherviews.AboutView;
import ldp.games.doodlejump.otherviews.ExitView;
import ldp.games.doodlejump.otherviews.FailView;
import ldp.games.doodlejump.otherviews.OptionView;
import ldp.games.doodlejump.otherviews.ScoreView;
import ldp.games.doodlejump.otherviews.WelcomeView;
import ldp.games.doodlejump.resource.SoundPlayer;
import android.R.integer;
import android.app.Activity;
import android.graphics.MaskFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class DoodleJumpActivity extends Activity {
    /** Called when the activity is first created. */
	
	public static final int GAME_OVER  = 0;
	public static final int GAME_START = 1;
	public static final int SCORE      = 2;
	public static final int ABOUT      = 3;
	public static final int EXIT       = 4;
	public static final int WELCOME    = 5;
	public static final int OPTION     = 6;
	
	private GameView gameView;
	private WelcomeView welcomeView; 
	private ExitView exitView;
	private ScoreView scoreView;
	private AboutView aboutView;
	private FailView failView;
	private OptionView optionView;
	private SensorManager sensorManager;
	private MySensorEventListener sensorEventListener;
	int pre_speed = 0;
	View current_view;
	
	public static boolean isGame_Running = false;
	public static float screen_width;
	public static float screen_height;
	public static float width_mul;
	public static float height_mul;
	
	
	public Handler handler = new Handler(){
		 public void handleMessage(Message msg) {
			 if(msg.what == GAME_OVER){ //ÓÎÏ·Ê§°Ü,È¥µ½failview
				current_view = null;
				initFailView();
			 }
			 if(msg.what == GAME_START){
				isGame_Running = true;
				welcomeView = null;
			    initGameView();
			 }
			 if(msg.what == SCORE){
				 current_view = null;
				 initScoreView();
			 }
			 if(msg.what == EXIT){
				current_view = null;
				initExitView();    
			}
			 if(msg.what == WELCOME){
				 isGame_Running = false;
				 current_view = null;
				 initWelcomeView();
			 }
			 if(msg.what == ABOUT){
				 current_view = null;
				 initAboutView();
				
			 }
			 if(msg.what == OPTION){
				 current_view = null;
				 initOptionView();
			 }
		 }

	

	};
	
	private void initOptionView() {
		//AppConnect.getInstance(DoodleJumpActivity.this).showAdFull(DoodleJumpActivity.this);
		optionView = new OptionView(DoodleJumpActivity.this);
		current_view = optionView;
		setContentView(optionView);
		welcomeView = null;
	}

	private void initFailView() {
		failView = new FailView(DoodleJumpActivity.this);
		current_view = failView;
		setContentView(failView);
		gameView = null;
	}


	
	private void initAboutView() {
		//AppConnect.getInstance(this).showOffers(this);
		aboutView = null;
		aboutView = new AboutView(DoodleJumpActivity.this);
		current_view = aboutView;
		setContentView(aboutView);
		welcomeView = null;
	}

	


	
	private void initScoreView() {
		scoreView = new ScoreView(DoodleJumpActivity.this);
		current_view = scoreView;
		setContentView(scoreView);
		welcomeView = null;
	}

	
	private void initExitView() {
		exitView = new ExitView(DoodleJumpActivity.this);
		current_view = exitView;
		setContentView(exitView);
		welcomeView = null;
	}
	
	private void initGameView() {
		gameView = new GameView(DoodleJumpActivity.this);
		current_view = gameView;
		setContentView(gameView);
	}
	
	private void initWelcomeView(){
		welcomeView = new WelcomeView(DoodleJumpActivity.this);
		current_view = welcomeView;
		setContentView(welcomeView);
		failView = null;
		exitView = null;
		scoreView = null;
		optionView = null;
		aboutView = null;
	}
	
	
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      //  Debug.startMethodTracing("AndroidJumpTrace");
        DisplayMetrics dm;
		dm = new DisplayMetrics();  
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);     
		screen_width = dm.widthPixels;  
		screen_height = dm.heightPixels;
		width_mul = screen_width/320;
		height_mul = screen_height/480;
		if(screen_height >= 800)
			height_mul = (float) 1.5;
		SoundPlayer.initSound(this);
	//	soundPlayer = new SoundPlayer(this);
        intiViews();
        setContentView(welcomeView);
        current_view = welcomeView;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
       
      
		Log.e("fuck", ""+DoodleJumpActivity.height_mul);
    }
    
    
    
    
    private void intiViews() {
    	 welcomeView = new WelcomeView(this);
	}

    
    


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(current_view == gameView){
			  if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){  
		           GameView.ispause = true;
		           return true;  
		      }  
		}
		else if(current_view == welcomeView){
			 if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){  
		           handler.sendEmptyMessage(EXIT);
		           return true;  
		      } 
		}
		else{
			 if( (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) || (keyCode == KeyEvent.KEYCODE_HOME && event.getRepeatCount() == 0)){  
		           return true;  
		      } 
		}
		return super.onKeyDown(keyCode, event);
	}





	@Override
	protected void onResume() {
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorEventListener = new MySensorEventListener();
		sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		super.onResume();
	}
    

	@Override
	protected void onPause() {
		sensorManager.unregisterListener(sensorEventListener);
	//	Debug.stopMethodTracing();
		super.onPause();
	}

	
	private final class MySensorEventListener implements SensorEventListener{
        
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(current_view == gameView){
				float X = event.values[SensorManager.DATA_X];
				pre_speed += X;
				if(X > 0.45 || X < -0.45){
					int temp = X > 0 ? 4 : -4;
					if(pre_speed > 7 || pre_speed < -7)
						pre_speed = pre_speed > 0 ? 7 : -7;
					gameView.logicManager.SetAndroid_HSpeed(pre_speed + temp);
				}
			}
		}
		
	}
	
    
}