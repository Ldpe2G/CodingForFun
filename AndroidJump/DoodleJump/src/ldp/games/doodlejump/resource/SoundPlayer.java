package ldp.games.doodlejump.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ldp.games.doodlejump.DoodleJumpActivity;
import ldp.games.doodlejump.R;
import ldp.games.doodlejump.otherviews.OptionView;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;

public class SoundPlayer {
	 	
		public static final int SOUND_EAT_FRUIT   = R.raw.eat_fruit;
		public static final int SOUND_BLACK_STONE = R.raw.blackstone;
		public static final int SOUND_EXPLODE     = R.raw.explode;
		public static final int SOUND_FAIL        = R.raw.fail;
		public static final int SOUND_LAUNCH      = R.raw.launch;
		public static final int SOUND_NORMAL_BAR  = R.raw.normal_bar;
		public static final int SOUND_SPRING_BAR  = R.raw.spring_bar;
		public static final int SOUND_INJURY      = R.raw.injure;
		
		private MediaPlayer music;
	    private static SoundPool soundPool;
	 
	    private static boolean musicSt = true; //���ֿ���
	    private static boolean soundSt = true; //��Ч����
	    private static Context context;
	    public static boolean isplay_sound = false;
	   // private static final int[] musicId = {R.raw.bg,R.raw.bg1,R.raw.bg2,R.raw.bg3};
	    private static  Map<Integer,Integer> soundMap; //��Ч��Դid����ع������Դid��ӳ���ϵ��
	    
	    /**
	     * ��ʼ������
	     * @param c
	     */
	/*    public SoundPlayer(Context c)
	    {
	        context = c;
	      
	        initSound();
	    }
	    */
	    //��ʼ����Ч������
	    public static void initSound(Context c)
	    {
	    	
	    	context = c;
	    	isplay_sound = context.getSharedPreferences(OptionView.PREFS_NAME, 0).getBoolean(OptionView.SOUND_STRING, true);
	    	
	        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
	        
	        soundMap = new HashMap<Integer,Integer>();
	        soundMap.put(SOUND_EAT_FRUIT, soundPool.load(context, SOUND_EAT_FRUIT, 1));
	        soundMap.put(SOUND_BLACK_STONE, soundPool.load(context, SOUND_BLACK_STONE, 1));
	        soundMap.put(SOUND_EXPLODE, soundPool.load(context, SOUND_EXPLODE, 1));
	        soundMap.put(SOUND_FAIL, soundPool.load(context, SOUND_FAIL, 1));
	        soundMap.put(SOUND_LAUNCH, soundPool.load(context, SOUND_LAUNCH, 1));
	        soundMap.put(SOUND_NORMAL_BAR, soundPool.load(context, SOUND_NORMAL_BAR, 1));
	        soundMap.put(SOUND_SPRING_BAR, soundPool.load(context, SOUND_SPRING_BAR, 1));
	        soundMap.put(SOUND_INJURY, soundPool.load(context, SOUND_INJURY, 1));
	    }
	    
	  /*  
	    public static void ReleaseSoundPool(){
	    	soundPool.release();
	    	soundMap.clear();
	    }*/
	    
	    /**
	     * ������Ч
	     * @param resId ��Ч��Դid
	     */
	    public static void playSound(int resId)
	    {
	        if(soundSt == false)
	            return;
	        if(DoodleJumpActivity.isGame_Running == true && isplay_sound){
		        Integer soundId = soundMap.get(resId);
		      //  Log.e("error", ""+soundId);
		        if(soundId != null)
		            soundPool.play(soundId, 1, 1, 1, 0, 1);
	        }
	    }

	    /**
	     * ��ͣ����
	     */
	    public static void pauseMusic()
	    {
	   //     if(music.isPlaying())
	  //          music.pause();
	    }
	    
	    /**
	     * ��������
	     */
	    public static void startMusic()
	    {
	  //      if(musicSt)
	  //          music.start();
	    }
	    
	    /**
	     * �л�һ�����ֲ�����
	     */
	    public static void changeAndPlayMusic()
	    {
	    //    if(music != null)
	    //        music.release();
	    //    initMusic();
	   //     startMusic();
	    }
	    
	    /**
	     * ������ֿ���״̬
	     * @return
	     */
	 //   public static boolean isMusicSt() {
	  //      return musicSt;
	//    }
	    
	    /**
	     * �������ֿ���
	     * @param musicSt
	     */
	 /*   public static void setMusicSt(boolean musicSt) {
	        SoundPlayer.musicSt = musicSt;
	        if(musicSt)
	            music.start();
	        else
	            music.stop();
	    }
*/
	    /**
	     * �����Ч����״̬
	     * @return
	     */
	    public static boolean isSoundSt() {
	        return soundSt;
	    }

	    /**
	     * ������Ч����
	     * @param soundSt
	     */
	    public static void setSoundSt(boolean soundSt) {
	        SoundPlayer.soundSt = soundSt;
	    }
	    
	    /**
	     * �������������
	     */
	    public static void boom()
	    {
	     //   playSound(R.raw.itemboom);
	    }
	}
