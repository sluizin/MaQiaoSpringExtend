/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.zip.GZIPOutputStream;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQUtils {

	/**
	 * 通过RandomAccessFile读文件 按行读 randomFile.readLine<br/>
	 * 是否过滤#右侧数据
	 * @param filenamepath String
	 * @param enterStr String
	 * @param delnotes boolean
	 * @return StringBuilder
	 */
	static final StringBuilder readFile(String filenamepath, String enterStr, boolean delnotes) {
		StringBuilder sb = new StringBuilder(400);
		try {
			File file = new File(filenamepath);
			boolean bool = file.exists();
			file = null;
			if (bool) {
				RandomAccessFile randomFile = new RandomAccessFile(new File(filenamepath), "r");
				FileChannel filechannel = randomFile.getChannel();
				// 将写文件指针移到文件头。
				randomFile.seek(0);
				FileLock lock;// = fc.tryLock();// lock
				do {
					lock = filechannel.tryLock(0L, Long.MAX_VALUE, true);
				} while (null == lock);
				if (null != lock) {
					Thread.sleep(10);// 本线程锁定10(lockTime)毫秒。过后任何程序对该文件的写操作将被禁止
					String str = null;
					while (randomFile.getFilePointer() < randomFile.length()) {
						str = changedLine(randomFile.readLine());
						if (str != null) {
							str = str.trim();
							if (delnotes && str.indexOf('#') >= 0) str = str.substring(0, str.indexOf('#'));
						}
						if (str.length() == 0) continue;
						sb.append(str);
						if (randomFile.getFilePointer() < randomFile.length()) sb.append(enterStr);
					}
					lock.release();// lock release
				}
				randomFile.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR :FileNotFoundException");
		} catch (IOException e) {
			System.out.println("ERROR :IOException");
		} catch (Exception e) {
			System.out.println("ERROR :Exception");
		}
		return sb;
	}

	/**
	 * 通过URL得到文件内容<br/>
	 * 是否过滤#右侧数据
	 * @param url URL
	 * @param enterStr String
	 * @param delnotes boolean
	 * @return StringBuilder
	 */
	static final StringBuilder readFile(final URL url, String enterStr, boolean delnotes) {
		StringBuilder sb = new StringBuilder(20);
		try {
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setConnectTimeout(30000);
			urlcon.setReadTimeout(30000);
			urlcon.connect(); // 获取连接
			String returnCode = new Integer(urlcon.getResponseCode()).toString();
			if (!returnCode.startsWith("2")) return null;
			InputStream is = urlcon.getInputStream();
			BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
			String l = null;
			while ((l = buffer.readLine()) != null) {
				if (delnotes && l.indexOf('#') >= 0) l = l.substring(0, l.indexOf('#'));
				if (l.length() == 0) continue;
				sb.append(l);
				sb.append(enterStr);
			}
			buffer.close();
			is.close();
			return sb;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			return sb;
		}
	}

	/**
	 * RandomAccessFile RandomAccessFile读出时，转换成UTF-8
	 * @param line String
	 * @return String
	 */
	private static final String changedLine(final String line) {
		if (line == null) return null;
		try {
			byte[] byteArray = new byte[line.length()];
			StringReader aStringReader = new StringReader(line);
			int character, i = 0;
			while ((character = aStringReader.read()) != -1)
				byteArray[i++] = (byte) character;
			return new String(byteArray, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把字符串按规则进行转化<br/>
	 * autoChange: "iso-8859-1 to utf-8"
	 * @param string String
	 * @param autoChange String
	 * @return String
	 */
	static final String autoChange(final String string, final String autoChange) {
		if (string == null || string.equals("") || autoChange == null || autoChange.equals("") || autoChange.trim().toLowerCase().indexOf("to") == -1) return string;
		return autoChange(string, autoChange.trim().toLowerCase().split("to"));
	}

	/**
	 * 把字符串按规则进行转化<br/>
	 * autoArray: {"iso-8859-1","utf-8"} <br/>
	 * 注意:autoArray数组必须是2个单元
	 * @param string String
	 * @param autoArray String[]
	 * @return String
	 */
	static final String autoChange(String string, String... autoArray) {
		if (autoArray.length != 2) return string;
		try {
			return new String(string.getBytes(autoArray[0].trim()), autoArray[1].trim());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return string;
	}

	/**
	 * 移位判断
	 * @param range int
	 * @param point int
	 * @return boolean
	 */
	static final boolean shift(final int range, final int point) {
		return (range & point) > 0;
	}

	/**
	 * 判断此类是否含有接口，允许向父类查和接口的父接口查
	 * @param c Class
	 * @param interfaceClass Class< ? >
	 * @return boolean
	 */
	public static final boolean isInterface(final Class<?> c, final Class<?> interfaceClass) {
		if (!interfaceClass.isInterface()) return false;
		Class<?>[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++)
			if (face[i].getName().equals(interfaceClass.getName())) {
				return true;
			} else {
				Class<?>[] face1 = face[i].getInterfaces();
				for (int x = 0, len2 = face1.length; x < len2; x++)
					if (face1[x].getName().equals(interfaceClass.getName())) return true;
					else if (isInterface(face1[x], interfaceClass)) return true;
			}
		if (null != c.getSuperclass()) return isInterface(c.getSuperclass(), interfaceClass);
		return false;
	}

	/**
	 * 判断对象是否含此接口
	 * @param obj Object
	 * @param interfaceClass Class< ? >
	 * @return boolean
	 */
	static final boolean isInterface(final Object obj, final Class<?> interfaceClass) {
		return isInterface(obj.getClass(), interfaceClass);
	}

	/**
	 * 让源串与数组各个相比较，有True，则返回True<br/>
	 * key1:源串<br/>
	 * keyArray:比较串数组<br/>
	 * @param matching boolean
	 * @param ignoreCase boolean
	 * @param key1 String
	 * @param keyArray String[]
	 * @return boolean
	 */
	static boolean checkStandard(final boolean matching, final boolean ignoreCase, final String key1, final String... keyArray) {
		for (int i = 0, len = keyArray.length; i < len; i++)
			if (checkStandard(matching, ignoreCase, key1, keyArray[i])) return true;
		return false;
	}

	/**
	 * 两个字符串进行比较<br/>
	 * key1:源串<br/>
	 * key2:比较串<br/>
	 * @param matching boolean
	 * @param ignoreCase boolean
	 * @param key1 String
	 * @param key2 String
	 * @return boolean
	 */
	static boolean checkStandard(final boolean matching, final boolean ignoreCase, final String key1, final String key2) {
		if (key1 == null || key2 == null) return false;
		if (matching) {
			if (ignoreCase) {
				if (key1.toLowerCase().indexOf(key2.toLowerCase()) > -1) return true;
			} else {
				if (key1.indexOf(key2) > -1) return true;
			}
		} else {
			if (ignoreCase) {
				if (key1.equalsIgnoreCase(key2)) return true;
			} else {
				if (key1.equals(key2)) return true;
			}
		}
		return false;
	}
	/**
	 * 使用gzip进行压缩
	 * @param str String
	 * @return String
	 */
	public final static String gzip(final String str) {
		if (str == null || str.length() == 0) return str;
		try (final ByteArrayOutputStream out = new ByteArrayOutputStream(); final GZIPOutputStream gzip = new GZIPOutputStream(out);) {
			gzip.write(str.getBytes());
			return new sun.misc.BASE64Encoder().encode(out.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return str;
		}
	}

}
