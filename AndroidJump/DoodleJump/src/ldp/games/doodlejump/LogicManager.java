package ldp.games.doodlejump;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Canvas;
import android.util.Log;

import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.android.Android;
import ldp.games.doodlejump.bars.AbstractBar;
import ldp.games.doodlejump.bars.ThronBar;
import ldp.games.doodlejump.bulletandexplode.Bullet;
import ldp.games.doodlejump.bulletandexplode.BulletSet;
import ldp.games.doodlejump.bulletandexplode.Explode;
import ldp.games.doodlejump.monsters.AbstractMonster;
import ldp.games.doodlejump.resource.SoundPlayer;


public class LogicManager {
	
	public  static final int FALL_DOWN_DAMAGE = 40;  
	private static final int SLEEP_TIME       = 40;
//	private static LogicManager logicManager  = null;
	
	private OBjectsManager objectsManager;
	private AbstractAndroid android;
	private List<BulletSet> bulletSets = new ArrayList<BulletSet>();
	private List<Explode> explodes = new ArrayList<Explode>();
	
	
	private boolean game_started = false;
	public static boolean isrunning;

	//同步互斥，person算法
	public static boolean[] person;
	public static int choose;
	DoodleJumpActivity context;
	
	public LogicManager(DoodleJumpActivity context){
		this.context = context;
	    objectsManager = new OBjectsManager(context);
		android = Android.AndroidFactory(context);
		person = new boolean[2];
     	person[0] = false;
		person[1] = false;
	//	soundPlayer = new SoundPlayer(context);
		isrunning = true;
		new Thread(new LogicThread()).start();
	}
	
	public void Clear(){
		isrunning = false;
		android = null;
		bulletSets.clear();
		explodes.clear();
		objectsManager.Clear();
	}
	
	public void DrawAndroidAndBars(Canvas canvas){
		person[0] = true;
		choose = 1;
		while(person[1]&&choose==1);
		/*************临界区***********************/
		
		objectsManager.DrawBarsAndMonsters(canvas);
		android.DrawSelf(canvas);
	    for(BulletSet bulletSet : bulletSets){
	    	bulletSet.DrawBullets(canvas);
	    }
	    for(Explode explode : explodes){
	    	explode.DrawSelf(canvas);
	    }
	    RemoveBulletSets();
	    RemoveExplodes();
	    
		/*************临界区***********************/
		person[0] = false;
	}
	
	private void RemoveExplodes() {
		 for(int i=0; i<explodes.size(); i++){
			 if(explodes.get(i) != null){
				 if(explodes.get(i).isDone())
						 explodes.remove(i);
			 }
		 }
	}

	//清除飞出屏幕范围的子弹
	private void RemoveBulletSets() {
		for(int i=0; i<bulletSets.size(); i++){
			if(bulletSets.get(i).GetY() < 0)
				bulletSets.remove(i);
		}
	}

	public void AddNewBulletSet(){
		if(android.bullet_times > 0){
			SoundPlayer.playSound(SoundPlayer.SOUND_LAUNCH);
			bulletSets.add(new BulletSet(android, context));
			android.bullet_times --;
		}
	}
	
	public void SetAndroidHands(){
		android.bitmap_index = Android.HANDS_UP;
	}
	
	//根据重力感应来改变水平速度
	public void SetAndroid_HSpeed(float horizonal_speed){
		android.horizonal_speed = - horizonal_speed;
	}
	
	

	
	private class LogicThread implements Runnable{

		float add;
		@Override
		public void run() {
			while(isrunning){
				try {
					Thread.sleep(SLEEP_TIME);
				} catch (Exception e) {
					
				}
				finally{
					if(!GameView.ispause){
						android.Move();
						android.CheckAndroidCoor(objectsManager);
						CheckVertivalSpeed();
						isBulletToucnMonsters();
						objectsManager.CheckisTouchItems(android);
						objectsManager.CheckTouchMonsters(android);
						if(android.life_num < 0){
							isrunning = false;
							GameView.isGameOver = true;
						}
					}
				}
			}
		}
		
		

		private void isBulletToucnMonsters() {
			List<String> DestoryMonsters = new ArrayList<String>();
			
			person[1] = true;
			choose = 0;
			while(person[0]&&choose==0);
			/**************临界区****************/
 			for(BulletSet bulletSet : bulletSets){
				for(String key : objectsManager.monsterMap.keySet()){
					if(bulletSet.isTouchBullet(objectsManager.monsterMap.get(key))){
						DestoryMonsters.add(key);
						SoundPlayer.playSound(SoundPlayer.SOUND_EXPLODE);
						explodes.add(new Explode((int) (objectsManager.monsterMap.get(key).GetX() - 25* DoodleJumpActivity.height_mul), (int) (objectsManager.monsterMap.get(key).CoorY - 25* DoodleJumpActivity.height_mul), context));
					}
				}
				for(String key : DestoryMonsters){
					objectsManager.monsterMap.remove(key);
				}
				DestoryMonsters.clear();
			}
 			/**************临界区****************/
 			person[1] = false;
 			
		}



		private void CheckVertivalSpeed() {
			if(android.current_state == Android.STATE_GO_UP){
				android.LTCoorY += android.vertical_speed;
				if(game_started/* && !objectsManager.isrepeated*/){
					objectsManager.MoveBarsAndMonstersDown(android.vertical_speed, add);
					for(int i=0; i<explodes.size(); i++){
						 if(explodes.get(i) != null){
							explodes.get(i).CoorY -= android.vertical_speed;
							explodes.get(i).CoorY += add;
						 }
					 }
					
				}
				if(android.vertical_speed >= 0){
					android.vertical_speed = 0;
					android.current_state = Android.STATE_GO_DOWN;
					game_started = true;
				}
			
			}
		    if(android.current_state == Android.STATE_GO_DOWN) {
		    	if(android.vertical_speed >= Android.MAX_VERTICAL_SPEED)
			    		android.vertical_speed = Android.MAX_VERTICAL_SPEED;
		    	float temp =  android.vertical_speed;
		    	for(float i=0; i<=temp; i += 0.5){	//将增加的像素，分开加，每次加一
		    		android.LTCoorY += 0.5;
			    	if(objectsManager.isTouchBars(android.LTCoorX, android.LTCoorY)){
			    		if(objectsManager.isrepeated){
			    			SoundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			    			android.vertical_speed = Android.INITIAL_VERTICAL_SPEED;
			    		}
			    		else{ 
			    			if(objectsManager.touch_bar_type == AbstractBar.TYPE_NORMAL 
			    			   || objectsManager.touch_bar_type == AbstractBar.TYPE_SHIFT){
			    				Log.e("eror", "error");
			    				SoundPlayer.playSound(SoundPlayer.SOUND_NORMAL_BAR);
			    				add = getAdd(android.LTCoorY);
			    				android.vertical_speed = Android.INITIAL_VERTICAL_SPEED + add;
			    			}
			    			else{
			    				add = getAdd(android.LTCoorY);
			    				android.vertical_speed = Android.INITIAL_VERTICAL_SPEED + add;
			    				add = 30 * DoodleJumpActivity.height_mul;
			    			}
			    			if(objectsManager.touch_bar_type == AbstractBar.TYPE_THRON){
			    				android.MinusLifeBar(ThronBar.THRON_BAR_DAMAGE);
			    			}
			    		}
			    		android.current_state = Android.STATE_GO_UP;
			    		break;
			    	}
		    	}
		      
			}
		}

		private int getAdd(float lTCoorY) {
			if(lTCoorY >= 350 * DoodleJumpActivity.height_mul)
				return (int) (0 * DoodleJumpActivity.height_mul);
			else if(lTCoorY >= 300 * DoodleJumpActivity.height_mul)
				return (int) (5 * DoodleJumpActivity.height_mul);
			else 
				return (int) (10 * DoodleJumpActivity.height_mul);
		}
		
	}

}
