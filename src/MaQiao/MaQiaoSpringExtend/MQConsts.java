/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.lang.reflect.Field;
import sun.misc.Unsafe;

/**
 * 常量池
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQConsts {
	/** 是否打开监控 */
	public static boolean ACC_MonitorMap = true;
	/** 是否允许调用static方法[暂时不开发静态方法的调用] */
	static final boolean ACC_allowStatic = false;
	/** 是否允许调用public方法 */
	public static boolean ACC_allowPublic = true;
	/** 是否允许调用private方法 */
	public static boolean ACC_allowPrivate = false;
	/** 是否允许调用protected方法 */
	public static boolean ACC_allowProtected = false;
	/** 是否允许调用带有可变数量的参数 */
	static final boolean ACC_allowVarArgs = false;
	/** 是否使用缓存 */
	public static boolean ACC_useCache = true;
	/** 缓存时间[60*1000] */
	public static long cacheParamSize = 60 * 1000;//1份钟
	/** 指定路径数组中检索接口 */
	@Deprecated
	public static final int ACC_Search_Appoint = 1;
	/** 在ioc中检索接口和对象 */
	public static final int ACC_Search_IOC = 2;
	/** 在base中检索接口和对象 */
	public static final int ACC_Search_Base = 4;
	public static final Unsafe UNSAFE;
	/**
	 * String对象中value(char[])地址偏移量
	 */
	public static long StringArrayOffset = 0L;
	static {
		try {
			final Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
			StringArrayOffset = UNSAFE.objectFieldOffset(String.class.getDeclaredField("value"));/* 得到String对象中数组的偏移量 */
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
