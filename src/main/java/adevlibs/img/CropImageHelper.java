/**
 *ClassName : CropImageHelper</br>
 * 
 * <p>2013© e-future.com.cn 版权所有 翻版必究</p>
 * <p>未经允许不得使用</p>
 *
 */
package adevlibs.img;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * ClassName : CropImageHelper
 * <p>
 * 功能描述：
 * </p>
 * <p>
 * History
 * </p>
 * <p>
 * Create User: 
 * </p>
 * <p>
 * Create Date: 2013-9-24 下午12:30:39
 * </p>
 * <p>
 * Update User:
 * </p>
 * <p>
 * Update Date:
 * </p>
 */

/**
 * android开发文件系统常用方法
 * 
 * @author An Zewei
 *         <p>
 *         Create Date: 2013-9-24 下午12:30:39
 *         </p>
 *         <p>
 *         本工具类中覆盖的范围为:
 *         </p>
 *         <ul>
 *         <li>--选择图片--</li>
 *         <li>，跳转到相机选择，跳转到图片的裁剪，根据路径获取图片（包含了对图片大小的控制）</li>
 *         <li>--图库--</li>
 *         <li>跳转到图库选择，添加图片到图库</li>
 *         <li>--图片获取--</li>
 *         <li>通过本地Url获取图片，通过网络Url获取图片</li>
 *         <li>--图片压缩--</li>
 *         <li>极限压缩图片算法，图片缩放时SampleSize值的计算</li>
 *         <li>--图片属性--</li>
 *         <li>获取图片文件的大小</li>
 *         <li>--Uri 类型判断--</li>
 *         <li>
 *         支持的类型为ExternalStorageProvider，DownloadsProvider，MediaProvider，Google
 *         Photos</li>
 *         </ul>
 * 
 */
public class CropImageHelper {
	private Activity mActivity;
	public final static int REQUEST_IMAGE_CHOICE = 10;
	public final static int REQUEST_IMAGE_CAPTURE = 20;
	public final static int REQUEST_IMAGE_CROP = 2000;

	/**
     * 
     */
	public CropImageHelper(Activity activity) {
		mActivity = activity;
	}

	/**
	 * 跳转到图库
	 */
	public void goImageChoice() {
		try {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			intent.setType("image/*"); // 设置文件类型
			mActivity.startActivityForResult(intent, REQUEST_IMAGE_CHOICE);// 转到图片
		} catch (Exception e) {
			Toast.makeText(mActivity, "请先安装图库", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 跳转到相机
	 */
	public Uri goCamera() {
		Intent captrueIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		// camerSaveUri = getUri();
		// if (camerSaveUri == null) {
		// return camerSaveUri;
		// }
		File picFile = getUri();
		if (picFile == null) {
			return null;
		}
		captrueIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
		mActivity.startActivityForResult(captrueIntent, REQUEST_IMAGE_CAPTURE);
		return Uri.fromFile(picFile);
	}

	/**
	 * 添加到图库
	 */
	public static void galleryAddPic(Context context, String path) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(path);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		context.sendBroadcast(mediaScanIntent);
	}

	/**
	 * 跳转到图片裁剪
	 * 
	 * @param uri
	 *            原图的Uri
	 * @param width
	 *            想要获得的宽度
	 * @param height
	 *            想要获得的高度
	 */
	public void goZoomImage(Uri uri, int width, int height) {
		try {

			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 设置裁剪
			intent.putExtra("crop", "true");
			intent.putExtra("outputX", width);
			intent.putExtra("outputY", height);
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", width);
			intent.putExtra("aspectY", height);
			intent.putExtra("return-data", true);
			mActivity.startActivityForResult(intent, REQUEST_IMAGE_CROP);
		} catch (Exception e) {
			Toast.makeText(mActivity, "请先安装图库", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 创建一个文件并返回他的Uri （创建的时候文件名是按照当前时间生产的）
	 * 
	 * @return 文件对应的Uri
	 */
	private File getUri() {
		File dcimFile = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
		if (!dcimFile.exists()) {
			Toast.makeText(mActivity, "使用相机前请插入sd卡！", Toast.LENGTH_SHORT)
					.show();
			return null;
		}
		File file = new File(dcimFile.getAbsolutePath() + "/dcim"
				+ System.currentTimeMillis() + ".jpg");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 查询给定的URI，获取图片，并按给定的最大高度和宽度保持长宽比压缩 <br>
	 * <b>注： maxwidth或者maxheight小于等于0时，保持原图片大小
	 * 
	 * @param contentResolver
	 *            用于查询
	 * @param localUri
	 *            需要查询的URI
	 * @param maxwidth
	 *            图片的最大宽度
	 * @param maxheight
	 *            图片的最大高度
	 * @return
	 */
	public static Bitmap getBmpFromLocalUrl(String localUri, int maxwidth,
			int maxheight) {
		try {
			ExifInterface exifInterface = new ExifInterface(localUri);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				orientation = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				orientation = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				orientation = 270;
				break;
			default:
				orientation = 0;
			}
			return compressBitmap(localUri, orientation, maxwidth, maxheight);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 极限压缩
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static Bitmap compressBitmap(String filepath, int degrees,
			int maxwidth, int maxheight) {
		if (degrees == 90 || degrees == 270) {// 经过旋转
			int tmp = maxheight;
			maxheight = maxwidth;
			maxwidth = tmp;
		}
		return compressBitmap(filepath, degrees,
				getSimplesize(filepath, maxwidth, maxheight));
	}

	/**
	 * 图片压缩
	 * 
	 * @param filepath
	 *            图片路径
	 * @param rotateDegrees
	 *            旋转角度
	 * @param scale
	 *            缩放时用到的缩放值，x方向和y方向都使用这个值进行缩放
	 * @return 处理之后的图片
	 */
	private static Bitmap compressBitmap(String filepath, int rotateDegrees,
			float scale) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(filepath, options);
			if (rotateDegrees != 0 || scale < 1) {
				Matrix matrix = new Matrix();
				matrix.setRotate(rotateDegrees);
				matrix.setScale(scale, scale);
				Bitmap nbitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				bitmap.recycle();
				bitmap = nbitmap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 计算图片缩放是合理的SampleSize（按照我们的预期宽高压缩图片）
	 * 
	 * @param options
	 *            BitmapFactory使用的当前 options
	 * @param reqWidth
	 *            期望的宽
	 * @param reqHeight
	 *            期望的高
	 * @return 建议的压缩比
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 获取图片文件的大小
	 * 
	 * @param filepath
	 *            图片的路径
	 * @param maxwidth
	 *            限宽
	 * @param maxheight
	 *            限高
	 * @return 图片文件的大小 [0]为宽，[1]为高
	 */
	public static int[] getPicFileSize(String filepath, int maxwidth,
			int maxheight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		int width = options.outWidth;
		int height = options.outHeight;
		try {
			ExifInterface exifInterface = new ExifInterface(filepath);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
			case ExifInterface.ORIENTATION_ROTATE_270:
				width = options.outHeight;
				height = options.outWidth;
			default:
			}
		} catch (Exception e) {
		}
		float rate = getSimplesize(filepath, maxwidth, maxheight);
		return new int[] { (int) (width * rate), (int) (height * rate) };
	}

	private static float getSimplesize(String filepath, int maxwidth,
			int maxheight) {
		if (maxheight <= 0 || maxwidth <= 0) {
			return 1;
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		int width = options.outWidth;
		int height = options.outHeight;
		float rate = 1;
		if (maxheight < height) {
			rate = (float) maxheight / (float) height;
		}
		if (maxwidth < width) {
			rate = Math.min(rate, (float) maxwidth / (float) width);
		}
		return rate;
	}

	/**
	 * 获取Uri对应的column，这个对于基于Uris和基于文件的ContentProviders很有用
	 * 
	 * @param context
	 *            上下文，用来获取ContentProvider
	 * @param uri
	 *            待执行的Uri
	 * @param selection
	 *            （可选）执行selection时候使用的过滤条件
	 * @param selectionArgs
	 *            （可选）执行selection的时候使用的参数
	 * @return 文件路径对应的一些典型值（_data）
	 * 
	 */
	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            待检查的Uri
	 * @return Uri的authority是否符合ExternalStorageProvider
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            待检查的Uri
	 * @return Uri的authority是否符合DownloadsProvider
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            待检查的Uri
	 * @return Uri的authority是否符合MediaProvider
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            待检查的Uri
	 * @return Uri的authority是否符合Google Photos
	 */
	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}

	/**
	 * 压缩原始图片到指定大小之下（本方法启动一个单独的线程对原始图片进行压缩，压缩完成之后会通过回调接口通知）
	 * 
	 * @param oriImage
	 *            原始图片
	 * @param listener
	 *            压缩完成之后的回调接口
	 */
	public synchronized static void compressImage(final Bitmap oriImage,
			final ImageProcessListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				oriImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
				int options = 100;
				while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
					baos.reset();// 重置baos即清空baos
					oriImage.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
					options -= 5;// 每次都减少5
				}
				ByteArrayInputStream isBm = new ByteArrayInputStream(baos
						.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
				Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
				if (listener != null) {
					listener.onProcessed(bitmap);
				}
			}
		}).start();
	}

	/**
	 * 回调接口 {@link compressImage}
	 * 
	 * @author wangfan
	 * 
	 */
	public interface ImageProcessListener {
		public void onProcessed(Bitmap bitmap);
	}
}
