package adevlibs.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;

import android.content.Context;

/**
 * android开发文件系统常用方法
 * 
 * @author wangfan
 * 
 *         <p>
 *         本工具类中覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>按照增删改查的顺序整理了一些方法</li>
 *         <li>增</li>
 *         <li>创建目录，创建文件，</li>
 *         <li>删</li>
 *         <li>通过命令行删，通过递归的方式删</li>
 *         <li>改</li>
 *         <li>将内容写入指定文件中可以选择覆盖和追加的方式，对私有文件写入也有支持</li>
 *         <li>查</li>
 *         <li>文件是否存在，读取文件内容对私有文件也有支持，文件名解析，MIME类型获取</li>
 *         <li>其他</li>
 *         <li>文件复制</li>
 *         </ul>
 * 
 */
public class FileHelper {
	// /**SD卡的根目录*/
	// private static String SDCARD_ROOT_PATH = null;
	//
	// /**
	// * 根据不同的手机，甚至不同的Android版本获取SD卡的调用函数可能不同
	// */
	// static{
	// if(true){
	// SDCARD_ROOT_PATH = Environment.getExternalStorageDirectory() + "/";
	// }
	// }

	// 目录操作
	/**
	 * 创建目录
	 * 
	 * @param dirName
	 *            目录名称
	 * @return 被创建的目录
	 */
	public File creatFloder(String dirName) {
		File dir = null;
		try {
			dir = new File(dirName);
			dir.mkdir();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dir;
	}

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 *            文件名
	 * @return 被创建的文件
	 * @throws IOException
	 */
	public File creatFile(String fileName) throws IOException {
		File file = new File(fileName);
		file.createNewFile();

		return file;
	}

	/**
	 * 
	 * 命令行删除文件夹或目录
	 * 
	 * @param fileOrFolderPath
	 *            待删除的文件或目录地址
	 * @return true成功删除，false 未能删掉
	 */
	public static boolean delFolderOrFileByCommand(String fileOrFolderPath) {
		boolean result = true;
		File file = new File(fileOrFolderPath);
		if (file.exists()) {
			String deleteCmd = "rm -r " + fileOrFolderPath;
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec(deleteCmd);
			} catch (IOException e) {
				result = false;
			}
		}
		return result;
	}

	/**
	 * 递归删除文件或文件夹
	 * 
	 * @param fileOrFolder
	 */
	public static boolean delFileOrFolderByRecursive(File fileOrFolder) {
		boolean result = true;
		try {
			if (fileOrFolder.isDirectory())
				for (File child : fileOrFolder.listFiles()) {
					delFileOrFolderByRecursive(child);
				}
			fileOrFolder.delete();
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 
	 * 将文本内容写入文件（如果不存在会创建文件）
	 * 
	 * @param filePath
	 *            路径
	 * @param content
	 *            文本
	 * @param encode
	 *            编码
	 * @param overwrite
	 *            true 覆盖原内容，false 在原内容的基础上追加数据
	 * @return true写入成功，false写入失败
	 */
	public static boolean writeContentIntoFile(String filePath, String content,
			String encode, boolean overwrite) {
		boolean result = true;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			result = writeContentIntoFileOutStream(content, encode, fos,
					overwrite);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();

			result = false;
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 
	 * 将内容写入私有文件（如果不存在会创建 data/packagename/files）
	 * 
	 * @param context
	 *            上下文，用于打开私有数据对应的流
	 * @param fileName
	 *            文件名
	 * @param content
	 *            待写入内容
	 * @param overwrite
	 *            true 覆盖原内容，false在原内容的基础上追加
	 * @return true写入成功，false写入失败
	 */
	public static boolean writeContentIntoPrivateFile(Context context,
			String fileName, String content, boolean overwrite) {
		// 初始化，如果调用FileOutputStream时指定的文件不存在，
		// Android会自动创建它（在data/包名/files/目录下会创建txtme.txt文件)
		// 在默认情况下，写入的时候会覆盖原文件内容，
		// 如果想把新写入的内容附加到原文件内容后，则应指定其模式为Context.MODE_APPEND
		// MODE_PRIVATE的文件是应用程序私有的 ，
		// MODE_WORLD_READABLE则所有应用程序都可以访问的，
		// MODE_WORLD_WRITEABLE所以应用程序都可以写
		boolean result = true;
		FileOutputStream fileOutStream = null;
		try {
			fileOutStream = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);

			result = writeContentIntoFileOutStream(content, fileOutStream,
					overwrite);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			result = false;
		} finally {
			try {
				if (fileOutStream != null) {
					fileOutStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 
	 * 将数据写入到文件流中（使用UTF-8的编码方式）
	 * 
	 * @param content
	 *            待写入的内容
	 * @param fileOutStream
	 *            文件输出流
	 * @param overwrite
	 *            true覆盖原内容，false在原内容的基础上追加
	 * @return true 写入成功，false写入失败
	 */
	protected static boolean writeContentIntoFileOutStream(String content,
			FileOutputStream fileOutStream, boolean overwrite) {
		return writeContentIntoFileOutStream(content, "UTF-8", fileOutStream,
				overwrite);
	}

	/**
	 * 
	 * 将内容写入到指定流中，按照指定的编码方式
	 * 
	 * @param content
	 * @param encode
	 * @param fos
	 * @param overwrite
	 *            true覆盖原内容 false在原来的基础上增加
	 * @return true 成功写入数据
	 */
	protected static boolean writeContentIntoFileOutStream(String content,
			String encode, FileOutputStream fos, boolean overwrite) {
		boolean result = true;
		OutputStreamWriter osw = null;
		try {
			osw = new OutputStreamWriter(fos, encode);
			if (overwrite) {
				osw.write(content);
			} else {
				osw.append(content);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			try {
				if (osw != null) {
					osw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 根据指定编码读取指定文件的内容，以字符串格式返回.要是读取出现异常会返回null
	 * 
	 * @param filePath
	 *            本地文件地址
	 * @param encode
	 *            编码格式
	 * @return 文件中包含的类容
	 */
	public static String readFileContent(String filePath, String encode) {
		String content = null;
		FileInputStream fileIns = null;
		try {
			fileIns = new FileInputStream(filePath);
			content = readFileContentFromStream(encode, fileIns);
		} catch (Exception e1) {
			e1.printStackTrace();
			content = null;
		} finally {
			try {
				if (fileIns != null) {
					fileIns.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return content;
	}

	/**
	 * 读取私有文件(/data/软件包名目录) 数据，要是读取发生异常会返回null
	 * 
	 * @param context
	 *            上下文，用户获取私有目录下文件对应的流
	 * @param fileName
	 *            私有目录下的文件名
	 * @param encoding
	 *            如： "UTF-8", "GBK", "GB2312"
	 * @return
	 */
	public static String readPrivateFileContent(Context context,
			String fileName, String encoding) {
		String content = null;
		FileInputStream fileInstream = null;
		try {
			fileInstream = context.openFileInput(fileName);
			content = readFileContentFromStream(encoding, fileInstream);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			content = null;
		} finally {
			if (fileInstream != null) {
				try {
					fileInstream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content;
	}

	/**
	 * 通过FileInputStream流来读取文件内容
	 * 
	 * @param encode
	 *            文件内容编码方式
	 * @param fileIns
	 *            文件对应的文件流
	 * @return 读取出来的文件内容
	 */
	protected static String readFileContentFromStream(String encode,
			FileInputStream fileIns) {
		String result = null;
		InputStreamReader isReader = null;
		BufferedReader bufReader = null;
		String readLine = null;
		try {
			isReader = new InputStreamReader(fileIns, encode);
			bufReader = new BufferedReader(isReader);
			while ((readLine = bufReader.readLine()) != null) {
				result = result + readLine;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			result = null;
		} catch (IOException e) {
			e.printStackTrace();
			result = null;
		} finally {
			try {
				if (bufReader != null) {
					bufReader.close();
				}
				if (isReader != null) {
					isReader.close();
				}
				readLine = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 从全路径中获取文件名
	 * 
	 * @param fullPath
	 *            文件对应的完整路径
	 * @return 文件名
	 */
	public static String getFileNameFromFullPath(String fullPath) {
		int last = fullPath.lastIndexOf("/");
		String fileName = fullPath.substring(last + 1);
		return fileName;
	}

	/**
	 * 获取文件MIME类型
	 * 
	 * @param fileName
	 *            文件名
	 * @return 文件对应的MIME类型
	 */
	public static String getFileMIMEType(File fileName) {
		String end = fileName
				.getName()
				.substring(fileName.getName().lastIndexOf(".") + 1,
						fileName.getName().length()).toLowerCase();
		String type = "";
		if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
				|| end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/**
	 * 通过管道快速复制文件
	 * 
	 * @param srcFile
	 *            源文件的路径
	 * @param dstFile
	 *            目标文件的路径
	 * @return 是否复制成功
	 */
	public static boolean transferFile(String srcFile, String dstFile) {
		return transferFile(srcFile, dstFile, 0);
	}

	/**
	 * 通过管道快速复制文件
	 * 
	 * @param srcFile
	 *            源文件的路径
	 * @param dstFile
	 *            目标文件的路径
	 * @param pos
	 *            起始位置[支持忽略头部一些字节]
	 * @return 是否复制成功
	 */
	public static boolean transferFile(String srcFile, String dstFile, long pos) {
		boolean result = true;

		FileInputStream fin = null;
		FileOutputStream fout = null;
		FileChannel fcin = null;
		FileChannel fcout = null;

		try {
			fin = new FileInputStream(srcFile);
			fout = new FileOutputStream(dstFile);
			fcin = fin.getChannel();
			fcout = fout.getChannel();

			long transferSize = fcin.size() - pos;
			fcin.transferTo(pos, transferSize, fcout);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fcout != null) {
					fcout.close();
				}
				if (fcin != null) {
					fcin.close();
				}
				if (fout != null) {
					fout.close();
				}
				if (fin != null) {
					fin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;
	}
}