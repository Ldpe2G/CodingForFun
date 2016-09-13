package ldp.games.doodlejump.Items;

import java.util.Random;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.GameView;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.android.AbstractAndroid;
import ldp.games.doodlejump.resource.SoundPlayer;

import android.R.plurals;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

public class ItemFruit extends AbstractItem {

	private static final int APPLE       = 0;
	private static final int BANANA      = 1;
	private static final int MANGO       = 2;
	private static final int ORANGE      = 3;
	private static final int PEACH       = 4;
	private static final int PEAR        = 5;
	private static final int STRAWBERRY  = 6;
	private static final int TOMATO      = 7;
	private static final int WATERMELON  = 8;
	
	int index;
	public Bitmap[] fruits;
	
	public ItemFruit(DoodleJumpActivity context){
		this.type = TYPE_FRUIT;
		index = new Random().nextInt(9);
		initBitmap(context);
	}
	
	private void initBitmap(DoodleJumpActivity context) {
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

	@Override
	public void DrawSelf(Canvas canvas, float CoorX, float CoorY) {
		canvas.drawBitmap(fruits[index], CoorX, CoorY, null);
	}

	@Override
	public void ModifyAndroid(AbstractAndroid android) {
		android.AddLifeBar(GetAdd());
	}

	private int GetAdd() {
	    SoundPlayer.playSound(SoundPlayer.SOUND_EAT_FRUIT);
		switch(this.type){
		case APPLE:
		case BANANA:
		case MANGO:
		case ORANGE:
			return 20;
		case PEACH:
		case PEAR:
		case STRAWBERRY:
			return 30;
		case TOMATO:
		case WATERMELON:
			return 40;
		}
		return 0;
	}
	
	

}
