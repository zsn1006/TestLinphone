package org.linphone.newphone.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;

/**
 * 1、apk中有两种资源文件，使用两种不同的方式进行打开使用。 raw使用InputStream in =
 * getResources().openRawResource(R.raw.test); asset使用InputStream in =
 * getResources().getAssets().open(fileName);
 * 这些数据只能读取，不能写入。更重要的是该目录下的文件大小不能超过1M。
 * 同时，需要注意的是，在使用InputStream的时候需要在函数名称后加上throws IOException。
 * 2、SD卡中的文件使用FileInputStream和FileOutputStream进行文件的操作。
 * 3、存放在数据区(/data/data/..)的文件只能使用openFileOutput和openFileInput进行操作。
 * 注意不能使用FileInputStream和FileOutputStream进行文件的操作。
 * 4、RandomAccessFile类仅限于文件的操作，不能访问其他IO设备。它可以跳转到文件的任意位置，从当前位置开始读写。
 * 5、InputStream和FileInputStream都可以使用skip和read
 * (buffre,offset,length)函数来实现文件的随机读取。
 * 6、BufferedReader按行读取文件
 **********************/
public class MothodFile {

	/**
	 * 判断SD卡是否存在，并返回SD卡根目录
	 * 
	 * @return
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		return sdDir.toString();
	}

	/**
	 * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            写入内容
	 * @param append
	 *            true追加，false不追加
	 */
	public static void method1(String file, String conent, Boolean append) {

		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(file, append));
			out.write(conent);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加文件：使用FileWriter
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            写入内容
	 * @param append
	 *            true追加，false不追加
	 */
	public static void method2(String fileName, String content, Boolean append) {

		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName, append);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 追加文件：使用RandomAccessFile
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            写入内容
	 * @param append
	 *            true追加，false不追加
	 */
	public static void method3(String fileName, String content, Boolean append) {

		RandomAccessFile randomFile = null;
		try {
			randomFile = new RandomAccessFile(fileName, "rw");
			if (append) {
				randomFile.seek(randomFile.length());
			} else {
				randomFile.seek(0);
				randomFile.setLength(0);
			}
			randomFile.writeBytes(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				randomFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读RAW下的文件
	 * 
	 * @param context
	 * @param fileId
	 * @return
	 */
	public String readRawFile(Context context, int fileId) throws IOException {

		String res = null;
		try {
			InputStream in = context.getResources().openRawResource(fileId);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			res = EncodingUtils.getString(buffer, "utf-8");
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 读Asset下的文件
	 * 
	 * @param context
	 * @param fileId
	 * @return
	 */
	public String readAssetFile(Context context, String fileName)
			throws IOException {

		String res = null;
		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 读data下的文件 文件只能使用openFileOutput和openFileInput进行操作
	 * 
	 * @param fileName
	 * @return
	 */
	public String readDataFile(Context context, String fileName)
			throws IOException {

		String res = null;
		try {
			FileInputStream fin = context.openFileInput(fileName);
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 得到String
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public String readFile(String fileName) throws IOException {

		String res = null;
		try {
			FileInputStream fin = new FileInputStream(fileName);
			byte[] buffer = new byte[fin.available()];
			fin.read(buffer);
			fin.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 得到String
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public String readFile(File file) throws IOException {

		String res = null;
		try {
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			res = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 得到InputStream
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public InputStream readFileStream(String fileName) throws IOException {

		try {
			FileInputStream fin = new FileInputStream(fileName);
			InputStream in = new BufferedInputStream(fin);
			return in;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
