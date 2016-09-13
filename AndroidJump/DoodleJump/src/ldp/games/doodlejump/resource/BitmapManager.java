package ldp.games.doodlejump.resource;



import ldp.games.doodlejump.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class BitmapManager {

	private static BitmapManager bitmapManager = null;
	public Bitmap title;
	
	public Bitmap[] normal_android;
	public Bitmap[] hurt_android;
	
	public Bitmap[] bars;
	public Bitmap bullet;
	public Bitmap[] explodes;
	
	public Bitmap life_bar;
	
	public Bitmap[] items;  
	
	public Bitmap[] fruits;
	
	public Bitmap black_stone;
	public Bitmap[] rotate_monster;
	public Bitmap[] eatinghead;
			
	private BitmapManager(Context context){
		title = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.title)).getBitmap();
		
		normal_android = new Bitmap[2];
		normal_android[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android)).getBitmap();
		normal_android[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_launch)).getBitmap();
		
		hurt_android = new Bitmap[2];
		hurt_android[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt)).getBitmap();
		hurt_android[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.android_hurt_launch)).getBitmap();
		
		bullet = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet)).getBitmap(); 
		
		bars = new Bitmap[4];
	    bars[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bar)).getBitmap();
	    bars[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.spring_bar)).getBitmap();
	    bars[2] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.shift_bar)).getBitmap();
	    bars[3] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.thron_bar)).getBitmap();
	    
	    explodes = new Bitmap[6];
		explodes[0] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode1)).getBitmap();
		explodes[1] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode2)).getBitmap();
		explodes[2] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode3)).getBitmap();
		explodes[3] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode4)).getBitmap();
		explodes[4] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode5)).getBitmap();
		explodes[5] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.explode6)).getBitmap();
		
		eatinghead = new Bitmap[4];
		eatinghead[0] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_left_close)).getBitmap();
		eatinghead[1] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_left_open)).getBitmap();
		eatinghead[2] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_right_close)).getBitmap();
		eatinghead[3] =  ((BitmapDrawable)context.getResources().getDrawable(R.drawable.monster1_right_open)).getBitmap();
		
		rotate_monster = new Bitmap[3];
		rotate_monster[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.rotate1)).getBitmap();
		rotate_monster[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.rotate2)).getBitmap();
		rotate_monster[2] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.rotate3)).getBitmap();
		
		black_stone = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.blackstone)).getBitmap();
		
		life_bar = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.life_bar_1)).getBitmap();
		
		items = new Bitmap[3];
		items[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.bullet_item)).getBitmap();
		items[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.life_item)).getBitmap();
		items[2] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.dabian_item)).getBitmap();
	
	    fruits = new Bitmap[9];
	    fruits[0] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_apple)).getBitmap();
	    fruits[1] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_banana)).getBitmap();
	    fruits[2] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_mango)).getBitmap();
	    fruits[3] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_orange)).getBitmap();
	    fruits[4] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_peach)).getBitmap();
	    fruits[5] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_pear)).getBitmap();
	    fruits[6] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_strawberry)).getBitmap();
	    fruits[7] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_tomato)).getBitmap();
	    fruits[8] = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.fruit_watermelon)).getBitmap();
	}
	
	public static BitmapManager getBitmapManager(Context context){
		if(bitmapManager == null)
			bitmapManager = new BitmapManager(context);
		return bitmapManager;
	}
}
