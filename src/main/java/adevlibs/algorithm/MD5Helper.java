package adevlibs.algorithm;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author wangfan
 * @since 2014-02-14
 * 
 *        MD5 algorithm
 * 
 *        <ul>
 *        <li>
 *        <p>
 *        <b>MD5 简单理解如下：数据被MD5之后我们将会获得一个32位的16进制数，不同的数据被MD5之后获得的数据不同</b> --
 *        </p>
 *        <p>
 *        <b>MD5 简单理解如下：MD5不能想AES那么方便的解密。现在一般都是用大数据库的方式进行破解</b> --
 *        </p>
 *        <p>
 *        <b>wiki-exlain1</b> -- The MD5 message-digest algorithm is a widely
 *        used cryptographic hash function producing a 128-bit (16-byte) hash
 *        value, typically expressed in text format as a 32 digit hexadecimal
 *        number. MD5 has been utilized in a wide variety of cryptographic
 *        applications, and is also commonly used to verify data integrity.
 *        </p>
 *        </ul>
 * */
public class MD5Helper {

	/**
	 * 将String进行MD5,获得一个长度为32的16进制数
	 * 
	 * @param oriStr
	 *            原始的字符串
	 * @return 长度为32的16进制数
	 */
	public static String md5EncryptStr(String oriStr) {
		return md5EncryptByteArr(oriStr.getBytes());
	}

	/**
	 * 将String进行MD5,获得一个长度为32的16进制数
	 * 
	 * @param byteArr
	 *            原始的byte数组
	 * @return 长度为32的16进制数
	 */
	public static String md5EncryptByteArr(byte[] byteArr) {
		MessageDigest md = null;
		byte[] b = null;
		StringBuffer buf = null;
		int i = 0;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(byteArr);
			b = md.digest();
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		} finally {
			md = null;
			b = null;
		}

	}

	/**
	 * 将本地的一个文件进行MDT,获得一个长度为32的16进制数 例如 "/Users/wangfan/Desktop/123.txt"
	 * 
	 * @param localFilePath
	 *            本地文件路径
	 * @return 长度为32的16进制数
	 */
	public static String md5EncryptLocalFile(String localFilePath) {

		InputStream fis;

		byte[] buffer = new byte[1024];

		byte[] b = null;

		int numRead = 0;

		MessageDigest md5;

		int i = 0;

		StringBuffer buf = null;

		try {

			fis = new FileInputStream(localFilePath);

			md5 = MessageDigest.getInstance("MD5");

			while ((numRead = fis.read(buffer)) > 0) {

				md5.update(buffer, 0, numRead);

			}

			fis.close();

			b = md5.digest();
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将一个网络文件文件进行MDT,获得一个长度为32的16进制数 例如 "http://www.baidu.com/img/bdlogo.gif"
	 * 
	 * @param url
	 *            网络文件对应的URL
	 * @return 长度为32的16进制数
	 */
	public static String md5EncryptNetFile(URL url) {

		InputStream fis;

		byte[] buffer = new byte[1024];

		byte[] b = null;

		int numRead = 0;

		MessageDigest md5;

		int i = 0;

		StringBuffer buf = null;

		try {

			fis = url.openStream();

			md5 = MessageDigest.getInstance("MD5");

			while ((numRead = fis.read(buffer)) > 0) {

				md5.update(buffer, 0, numRead);

			}

			fis.close();

			b = md5.digest();
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
