/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package adevlibs.sound;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;

/**
 * Class used to manage related sound resources.
 */
public class SoundPoolUtil {
    private static SoundPoolUtil mInstance = null;
	private static SoundPool mSoundPool; 
	private static HashMap<Integer, Integer> mSoundPoolMap; 
	private static AudioManager  mAudioManager;
	private static Vibrator mVibrator;
	private static Context mContext;
	
    private static final float FX_VOLUME = 1.0f;
    private static boolean mSilentMode;
	private static int mPlayingStreamID = 0;
//	private static int mPlayingSoundID = 0;
	private static int MAX_STREAMS = 4;
    
    SoundPoolUtil() 
    {
    }
    
    /**
	 * 获取声音管理器单件实例对象
	 * 注意： 必须先initSounds
	 */
    static public synchronized SoundPoolUtil getInstance(Context context) 
    {
        if (null == mInstance) 
        {
        	mInstance = new SoundPoolUtil();
        	initSounds(context);
        	updateRingerMode();
        }
        
        return mInstance;
    }
    
    /**
	 * 初始化声音资源管理器实例
	 */
	private static  void initSounds(Context context) 
	{
		 mContext = context;
	     mSoundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
	     mSoundPoolMap = new HashMap<Integer, Integer>(); 
	     mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE); 	
	     mVibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
	} 
	
	/*
	 * 更新铃声模式，判断是否静音
	 */
    public static void updateRingerMode()
    {
        mSilentMode = (mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_NORMAL);
    }
	
	/**
	 * 根据声音资源ID添加
	 */
	public static void addSound(int Index,int SoundResID)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundResID, 1));
	}

	/*
	 * 根据音乐资源路径添加
	 */
	public static void addSound(int Index, String soundResFilePath)
	{
		mSoundPoolMap.put(Index, mSoundPool.load(soundResFilePath, 1));
	}	
	
	/**
	 * 播放指定索引的声音
	 */
	public static void playSound(int index, float volumeRate, float playbackRate) 
	{
		int playSoundID = mSoundPoolMap.get(index);
		if (playSoundID == 0)
			return ; // 无效声音资源ID直接返回	
	    		
	     stopSound();
				
		if(mSoundPool != null && mAudioManager != null)
		{
			int streamID  = 0;
			 float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
		     streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		     streamID = mSoundPool.play(playSoundID, streamVolume * volumeRate, streamVolume * volumeRate, 1, 0, playbackRate); 
			// 记录当前正在播放的声音资源ID
			mPlayingStreamID = streamID;
//			mPlayingSoundID = playSoundID;
		}
	}
	
	/**
	 * 停止当前正在播放的声音[声音ID保留，因为外部用户可能再次使用]
	 */
	public static void stopSound()
	{
		if(mPlayingStreamID != 0/* && mPlayingSoundID != 0*/)
		{
			if(mSoundPool != null && mAudioManager != null)
			{
				mSoundPool.stop(mPlayingStreamID);
			}
			mPlayingStreamID = 0;
			//act_unload(mPlayingSoundID);			
			//mPlayingSoundID = 0;
		}
	}
	
	/**
	 * 卸载掉指定索引的音乐
	 */
	public static void unloadSound(int index)
	{
		mSoundPool.unload(mSoundPoolMap.get(index));
	}
	
	/**
	 * 清除实例对象
	 */
	public static void cleanup()
	{
		mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    mInstance = null;	    
	}

	/**
	 * 播放按键音    
	 */
    public static void playKeyDown() 
    {
        if (mAudioManager == null) {
            updateRingerMode();
        }
        if (!mSilentMode) {
            int sound = AudioManager.FX_KEYPRESS_STANDARD;
            mAudioManager.playSoundEffect(sound, FX_VOLUME);
        }
    }

    /**
     * 震动
     * @param milliseconds
     */
	public static void vibrate(long milliseconds)
	{
		mVibrator.vibrate(milliseconds);
	}
}
