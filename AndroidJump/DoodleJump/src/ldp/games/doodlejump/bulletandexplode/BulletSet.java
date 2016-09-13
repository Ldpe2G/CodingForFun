package ldp.games.doodlejump.bulletandexplode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.monsters.AbstractMonster;

import android.graphics.Canvas;

public class BulletSet {

	HashMap<String, Bullet> bulletMap = new HashMap<String, Bullet>();
	
	
	public BulletSet(AbstractAndroid android, DoodleJumpActivity context){
		if(android.bullet_level == 1)
			LevelOne(android, context);
		else if(android.bullet_level == 2)
			LevelTwo(android, context);
		else
			LevelTHree(android, context);
		new Thread(new MoveThread()).start();
	}
	
	private void LevelTHree(AbstractAndroid android, DoodleJumpActivity context) {
		bulletMap.put(""+1, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 0, context));
		
		bulletMap.put(""+2, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -7, context));
		bulletMap.put(""+3, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -10, context));
		bulletMap.put(""+4, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -13, context));
		bulletMap.put(""+5, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -15, context));
		
		bulletMap.put(""+6, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 7, context));
		bulletMap.put(""+7, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 10, context));
		bulletMap.put(""+8, new Bullet((int)android.LTCoorX+17,(int)android.LTCoorY-10, 13, context));
		bulletMap.put(""+9, new Bullet((int)android.LTCoorX+17,(int)android.LTCoorY-10, 15, context));
	}

	private void LevelOne(AbstractAndroid android, DoodleJumpActivity context) {
		bulletMap.put(""+1, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 0, context));
		bulletMap.put(""+2, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -10, context));
		bulletMap.put(""+3, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -15, context));
		bulletMap.put(""+4, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 10, context));
		bulletMap.put(""+5, new Bullet((int)android.LTCoorX+17,(int)android.LTCoorY-10, 15, context));
	}

	private void LevelTwo(AbstractAndroid android, DoodleJumpActivity context) {
		bulletMap.put(""+1, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 0, context));
		
		bulletMap.put(""+2, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -7, context));
		bulletMap.put(""+3, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -10, context));
		bulletMap.put(""+4, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, -13, context));
		
		bulletMap.put(""+5, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 7, context));
		bulletMap.put(""+6, new Bullet((int)android.LTCoorX+17, (int)android.LTCoorY-10, 10, context));
		bulletMap.put(""+7, new Bullet((int)android.LTCoorX+17,(int)android.LTCoorY-10, 13, context));
		
		
	}

	public int GetY(){
		for(String key : bulletMap.keySet()){
			return bulletMap.get(key).CoorY;
		}
		return 0;
	}
	
	public void DrawBullets(Canvas canvas){
		for(String key : bulletMap.keySet()){
			bulletMap.get(key).DrawSelf(canvas);
		}
	}
	
	public boolean isTouchBullet(AbstractMonster monster){
		List<String> list = new ArrayList<String>();
		for(String key : bulletMap.keySet()){
			int temp_x = Math.abs(monster.GetX() - bulletMap.get(key).CoorX);
			float temp_y = Math.abs(monster.CoorY - bulletMap.get(key).CoorY);
			double xiebian = Math.sqrt((double)(temp_x*temp_x) + (double)(temp_y*temp_y));
			if(xiebian <= 30 * DoodleJumpActivity.height_mul){
				monster.BeingAttacked();
				list.add(key);
			}
		}
		for(String key : list){
			bulletMap.remove(key);
		}
		return monster.IsDestory();
	}
	
	private class MoveThread implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(30);
				} catch (Exception e) {
					// TODO: handle exception
				}
				for(String key : bulletMap.keySet()){
					bulletMap.get(key).Move();
				}
					
			}
		}
		
	}
	
}
