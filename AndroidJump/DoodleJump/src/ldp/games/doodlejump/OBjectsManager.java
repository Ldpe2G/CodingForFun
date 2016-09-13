package ldp.games.doodlejump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import ldp.games.doodlejump.Items.AbstractItem;
import ldp.games.doodlejump.Items.ItemBullet;
import ldp.games.doodlejump.Items.ItemFruit;
import ldp.games.doodlejump.Items.ItemLife;
import ldp.games.doodlejump.Items.ItemShit;
import ldp.games.doodlejump.Items.ItemUpgradeBullet;
import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.bars.AbstractBar;

import ldp.games.doodlejump.bars.InstantShiftBar;
import ldp.games.doodlejump.bars.NormalBar;
import ldp.games.doodlejump.bars.ShiftBar;
import ldp.games.doodlejump.bars.SpringBar;
import ldp.games.doodlejump.bars.ThronBar;
import ldp.games.doodlejump.monsters.AbstractMonster;
import ldp.games.doodlejump.monsters.BlackStrone;
import ldp.games.doodlejump.monsters.EatingHead;
import ldp.games.doodlejump.monsters.RotateMonster;
import ldp.games.doodlejump.otherviews.OptionView;
import ldp.games.doodlejump.resource.SoundPlayer;

import android.R.integer;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class OBjectsManager {
    
	
	public static  long sum;  //随着bar的向下移动增加
	
	
	public int bar_level = 0;
	public int barApperrate = 35;
	public int monsterApperrate = 80;
	public int itemAppearate = 75;
	
	
	private int monstersappear = 0;
	public int itemappear = 0;
	
	private Map<String, AbstractBar> barMap;
	public Map<String, AbstractMonster> monsterMap;
	
	private long bar_identifier = 0;  //标识每一条bar
	private long monster_identifier = 0; //标识每一个怪兽
	
	private long repeate_long = 100;
	//private String repeate_String; 
    public boolean isrepeated = false;
	public int touch_bar_type = AbstractBar.TYPE_NORMAL;
	
	//同步互斥，person算法
	public static boolean[] person;
	public static int choose;
	private DoodleJumpActivity context;
	private Paint paint;
	SharedPreferences get_Diff_Setting;
	
	public OBjectsManager(DoodleJumpActivity context){
		//repeate_String = new String("100");
		this.context = context;
		barMap = new HashMap<String, AbstractBar>();
		monsterMap = new HashMap<String, AbstractMonster>();
		initBarMap();
		initPaint();
		person = new boolean[2];
     	person[0] = false;
		person[1] = false;
		sum = 0;
		SetDiff(context);
	}
	
	
	private void initPaint() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setTextSize(20* DoodleJumpActivity.height_mul);
	}


	private void SetDiff(DoodleJumpActivity context) {
		get_Diff_Setting = context.getSharedPreferences(OptionView.PREFS_DIFF, 0);
		String diffString = get_Diff_Setting.getString(OptionView.PREFS_DIFF, "Rookie");
		if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_ROOKIE)){
			barApperrate = 30;
			monsterApperrate = 90;
			itemAppearate = 60;
		}
		else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_NORMAL)){
			barApperrate = 35;
			monsterApperrate = 80;
			itemAppearate = 70;
		}
		else if(diffString.equalsIgnoreCase(OptionView.DIFFICULTY_MASTER)){
			barApperrate = 40;
			monsterApperrate = 60;
			itemAppearate = 80;
		}
		else{
			barApperrate = 45;
			monsterApperrate = 50;
			itemAppearate = 95;
		}
	}


	public void Clear(){
		barMap.clear();
		monsterMap.clear();
	}
	
	//初始化游戏
	public void initBarMap(){
		int count = 0;
		int CoorX;
		while(count < DoodleJumpActivity.screen_height / (20 * DoodleJumpActivity.height_mul)){
			if(hasBar()){
				int range = (int) (DoodleJumpActivity.screen_width - 50 * DoodleJumpActivity.width_mul);
				CoorX = new Random().nextInt(range);
				AbstractBar bar = new NormalBar(CoorX, count * (20 * DoodleJumpActivity.height_mul), GetRandomItem(), context);
				barMap.put(""+bar_identifier, bar);
				bar_identifier ++;
			}
			count ++;
		}
	}
	
	//确定该行是否会有bar出现
	private boolean hasBar() {
		int temp = new Random().nextInt(100);
		if(temp > barApperrate)
			return true;
		return false;
	}
	
	//确定该行是否会有bar出现
	private boolean hasMonster() {
		int temp = new Random().nextInt(100);
		if(temp > monsterApperrate)
			return true;
		return false;
	}
	
	
    //画出所有bars
	public void DrawBarsAndMonsters(Canvas canvas){
		person[0] = true;
		choose = 1;
		while(person[1]&&choose==1);
		/**************临界区****************/
		for(String key : barMap.keySet()){
			barMap.get(key).drawSelf(canvas);
		}
		for(String key : monsterMap.keySet()){
			monsterMap.get(key).DrawSelf(canvas);
		}
		drawHeight(canvas);
		RemoveOuterBarsAndMonsters();
		/**************临界区****************/
		person[0] = false;
	}
	
	//在左上角画出当前高度
	private void drawHeight(Canvas canvas) {
		canvas.drawText(""+sum, 5 * DoodleJumpActivity.width_mul, 20 * DoodleJumpActivity.height_mul, paint);
	}
	
	//判断android是否碰到bar
	public boolean isTouchBars(float CoorX, float CoorY){
		
		for(String key : barMap.keySet()){
		//	tempX = barMap.get(key).TLCoorX;
		//	tempY = barMap.get(key).TLCoorY;
			if(barMap.get(key).IsBeingStep(CoorX, CoorY)/*CoorX >= tempX - 35 && CoorX <= tempX + 45 && CoorY + 45 <= tempY && tempY - CoorY <= 45*/){
				if(repeate_long == Long.parseLong(key))
					isrepeated = true;
				else{
					isrepeated = false;
					repeate_long = Long.parseLong(key);
					touch_bar_type = barMap.get(key).type;
				}
				return true;
			}
		}
		
		if(isStepOnMonsters(CoorX, CoorY))
			return true;
		
		return false;
	}
	
	
	
	
	private boolean isStepOnMonsters(float CoorX, float CoorY) {	
		for(String key : monsterMap.keySet()){
			if(monsterMap.get(key).IsBeingStep(CoorX, CoorY)){
				return true;
			}
		}
		return false;
	}
	
	//将所有bars向下移动
	public void MoveBarsAndMonstersDown(float vertical_speed, float add){
		person[1] = true;
		choose = 0;
		while(person[0]&&choose==0);
		/**************临界区****************/
		for(String key : barMap.keySet()){
			barMap.get(key).TLCoorY -= vertical_speed;
			barMap.get(key).TLCoorY += add;
		}
		for(String key : monsterMap.keySet()){
			monsterMap.get(key).CoorY -= vertical_speed;
			monsterMap.get(key).CoorY += add;
		}
		sum += add - vertical_speed;
		monstersappear = itemappear += add - vertical_speed;
		bar_level += add - vertical_speed;
		AddNewBarsAndMonsters();
		/**************临界区****************/
		person[1] = false;
	}
	
	private void RemoveOuterBarsAndMonsters() {
		List<String> temp = new ArrayList<String>();
		
		for(String key : barMap.keySet()){
			if(barMap.get(key).TLCoorY > DoodleJumpActivity.screen_height + 20){
				barMap.get(key).clear();
				temp.add(key);
			}
		}
		for(String key : temp){
			barMap.remove(key);
		}
		temp.clear();
		for(String key : monsterMap.keySet()){
			if(monsterMap.get(key).CoorY > DoodleJumpActivity.screen_height + 20){
				monsterMap.get(key).isrunning = false;
				temp.add(key);
			}
		}
		for(String key : temp){
			monsterMap.remove(key);
		}
	}
	
	public void AddNewBarsAndMonsters(){
		
		CheckLevel();
		
		AbstractBar bar;
		float tempY = 100;
		
		tempY = GetTopCoorY();
		if(tempY > (20 * DoodleJumpActivity.height_mul)){
			if(hasBar()){
				int temp = new Random().nextInt(100);  //决定出现什么bar
				int CoorX = new Random().nextInt((int) (DoodleJumpActivity.screen_width - 50));
				if(temp <= 5){
					bar = new SpringBar(CoorX, (int) (-15 * DoodleJumpActivity.height_mul), context);
				}
				else if(temp <= 15){
					bar = new ShiftBar(CoorX, 0, GetRandomItem(), context);
				}
				else if(temp <= 25){
					bar = new ThronBar(CoorX, (int) (-10 * DoodleJumpActivity.height_mul), context);
				}
				else if(temp <= 50){
					bar = new InstantShiftBar(CoorX, (int) (-10 * DoodleJumpActivity.height_mul), context);
				}
				else{
					bar = new NormalBar(CoorX, 0, GetRandomItem(), context);
				}
				barMap.put(""+bar_identifier, bar);
				bar_identifier ++;
				
			}
			else if(hasMonster() && monstersappear >= 300 * DoodleJumpActivity.height_mul){
				monstersappear = 0;
				int temp2 = new Random().nextInt(100);
				if(temp2 < 40)
					monsterMap.put(""+monster_identifier, new EatingHead(-10 * DoodleJumpActivity.height_mul, context));
				else if(temp2 < 80)
					monsterMap.put(""+monster_identifier, new RotateMonster(-10 * DoodleJumpActivity.height_mul, context));
				else {
					monsterMap.put(""+monster_identifier, new BlackStrone(-10 * DoodleJumpActivity.height_mul, context));
					
				}
				monster_identifier ++;
			}
		}
		
	}
	
	private void CheckLevel() {
		if(bar_level >= 2000){
			if(barApperrate < 55)
				barApperrate += 2;
			bar_level = 0;
		}
	}
	
	private AbstractItem GetRandomItem() {
		if(itemappear >= 400 * DoodleJumpActivity.height_mul){
			itemappear = 0;
			int temp = new Random(bar_identifier).nextInt(100);
			if(temp > monsterApperrate){
				temp = new Random().nextInt(100);
				if(temp < 10)
					return new ItemUpgradeBullet(context);
				else if(temp < 30)
					return new ItemBullet(context);
				else if(temp < 80)
					return new ItemFruit(context);
				else if(temp < 90)
					return new ItemLife(context);
				else 
					return new ItemShit(context);
			}
		}
		return null;
	}
	
	public void CheckisTouchItems(AbstractAndroid android){
		float temp_x = android.LTCoorX + 22 * DoodleJumpActivity.height_mul;
		float temp_y = android.LTCoorY + 22 * DoodleJumpActivity.height_mul;
		for(String key : barMap.keySet()){
			if(barMap.get(key).isitemeaten == false && barMap.get(key).item != null){
				int X = (int) (barMap.get(key).TLCoorX + 25 * DoodleJumpActivity.height_mul);
				float Y = barMap.get(key).TLCoorY - 10 * DoodleJumpActivity.height_mul;
				if(GetLength(temp_x, temp_y, X, Y) < 30 * DoodleJumpActivity.height_mul){
					barMap.get(key).item.ModifyAndroid(android);
					barMap.get(key).isitemeaten = true;
				}
			}
			
		}
	}
	
	
	private int GetLength(float temp_x, float temp_y, int x, float y) {
		double x_length = temp_x - x;
		double y_length = temp_y - y;
		return (int) Math.sqrt(x_length*x_length + y_length*y_length);
	}
	
	//获得最顶端的 bar 或 monster 的Y坐标
	private float GetTopCoorY() {
		float tempY = 100;
		for(String key : barMap.keySet()){
			if(barMap.get(key).item == null){
				if(barMap.get(key).TLCoorY <= tempY)
					tempY = barMap.get(key).TLCoorY;
			}
			else if(barMap.get(key).TLCoorY - 20 * DoodleJumpActivity.height_mul <= tempY){
				tempY = barMap.get(key).TLCoorY - 20 * DoodleJumpActivity.height_mul;
			}
		}
		for(String key : monsterMap.keySet()){
			if(monsterMap.get(key).CoorY - 25  * DoodleJumpActivity.height_mul <= tempY)
				tempY = monsterMap.get(key).CoorY - 25 * DoodleJumpActivity.height_mul;
		}
		return tempY;
	}
	
	public void CheckTouchMonsters(AbstractAndroid android) {
		for(String key : monsterMap.keySet()){
			monsterMap.get(key).CheckDistance(android);
		}
	}
}
