package adevlibs.sound;

import android.content.Context;

import cnsxhza985.com.adevlibs.R;

public class SoundManager
{
	private static SoundManager mInstance = null;
	private static int mRefCount = 0;	
	private static Context mFatherContext;
	
	private static final float DEFAULT_VOLUME_RATE = 1.0f;
	private static final float DEFAULT_PLAYBACK_RATE = 1.0f;
	
	// 声音资源索引
	public static int SOUND_INDEX_SHAKE_MATCH = 3; //摇一摇
	public static int SOUND_INDEX_SHAKE_SOUND_MALE = 4;//摇一摇 找到礼品
	
	/**
	 * 构造函数：初始化声音资源管理器实例
	 */
	SoundManager(Context context) 
    {
		mFatherContext = context;
		SoundPoolUtil.getInstance(context);
		
		// 添加声音资源
		SoundPoolUtil.addSound(SOUND_INDEX_SHAKE_MATCH, R.raw.shake_match);
		SoundPoolUtil.addSound(SOUND_INDEX_SHAKE_SOUND_MALE, R.raw.shake_sound_male);
		
    }	
	
    /**
	 * 获取声音单件实例对象
	 */
    static public synchronized SoundManager getInstance(Context context) 
    {
		
        if (null == mInstance) 
        {
        	mInstance = new SoundManager(context);        	
        }
        mRefCount++;
        return mInstance;
    }
	
    /**
	 * 宠物鱼声音实例引用释放，当引用计数为0销毁实例对象
	 */
	public static void release()
	{
		mRefCount--;
        if (mRefCount == 0)
        {
        	SoundPoolUtil.cleanup();
    		mInstance = null;
        }		
	}
		
	/**
	 * 声音停止
	 */
	public static void stop()
	{
		SoundPoolUtil.stopSound();
	}
	
	/**
	 * 播放按键声音
	 */
	public static void playKeyDown()
	{
		SoundPoolUtil.playKeyDown();
	}
	
    /**
     * 震动
     * @param milliseconds
     */
	public static void vibrate(long milliseconds)
	{
		SoundPoolUtil.vibrate(milliseconds);
	}
	
	/**
	 * 播放指定索引的声音
	 */
	public static void playSoundByIndex(int soundIndex, float volumeRate, float playbackRate)
	{
		if (volumeRate == -1)
			volumeRate = DEFAULT_VOLUME_RATE;
		if (playbackRate == -1)
			playbackRate = DEFAULT_PLAYBACK_RATE;
		SoundPoolUtil.playSound(soundIndex, volumeRate, playbackRate);
	}
}
    



