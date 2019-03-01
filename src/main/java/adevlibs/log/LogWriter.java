/**
 *ClassName : LogWriter</br>

 * 
 * <p>2013© e-future.com.cn 版权所有 翻版必究</p>
 * <p>未经允许不得使用</p>
 *
 */
package adevlibs.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ClassName : LogWriter
 * <br>功能描述: 将log信息写出到指定文件中的工具
 * <br>History
 * <br>Create User: wangfan
 * <br>Create Date: 2014年6月13日 上午10:05:14
 * <br>Update User:
 * <br>Update Date:
 */
public class LogWriter {  
    
    private static LogWriter mLogWriter;  
  
    private static String mPath;  
      
    private static Writer mWriter;  
      
    private static SimpleDateFormat df;  
      
    private LogWriter(String file_path) {  
        this.mPath = file_path;  
        this.mWriter = null;  
    }  
      
    /**
     * 打开LogWriter
     * 通过调用这个方法设定Log输	出的文件位置，初始化LogWriter
     * @param file_path	文件地址
     * @return	返回LogWriter实例
     * @throws IOException
     */
    public static LogWriter open(String file_path) throws IOException {  
        if (mLogWriter == null) {  
            mLogWriter = new LogWriter(file_path);  
        }  
        File mFile = new File(mPath);  
        mWriter = new BufferedWriter(new FileWriter(mPath), 2048);  
        df = new SimpleDateFormat("[yy-MM-dd hh:mm:ss]: ");  
          
        return mLogWriter;  
    }  
      
    /**
     * 关闭LogWriter
     * @throws IOException
     */
    public void close() throws IOException {  
        mWriter.close();  
    }  
      
    /**
     * 输出log信息的方法
     * @param log 将要输出的log信息
     */
    public void print(String log) {  
        try {
            mWriter.write(df.format(new Date()));
            mWriter.write(log);  
            mWriter.write("\n");  
            mWriter.flush();  
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }  
      
    /**
     * 输出log信息的方法
     * @param cls 类信息
     * @param log	将要输出的log信息
     * @throws IOException
     */
    public void print(Class cls, String log) throws IOException { //如果还想看是在哪个类里可以用这个方法  
        mWriter.write(df.format(new Date()));  
        mWriter.write(cls.getSimpleName() + " ");  
        mWriter.write(log);  
        mWriter.write("\n");  
        mWriter.flush();  
    }  
}  
