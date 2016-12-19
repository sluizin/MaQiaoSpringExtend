/**
 * 
 */
package MaQiaoSpringExtend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import MaQiaoSpringExtend.Element.ElementMethod;
import MaQiaoSpringExtend.Element.ElementParameter;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQUtils {

	/**
	 * 通过上下文按允许的注解组进行检索。含有扩展注解的，则提出保存
	 * @param ac ApplicationContext
	 */
	static void setApplicationContextClassAllow(ApplicationContext ac) {
		if (ac == null) return;
		for (int i = 0, len = MQConsts.classAnnotationAllow.size(); i < len; i++) {
			Map<String, Object> map = ac.getBeansWithAnnotation(MQConsts.classAnnotationAllow.get(i));
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				MQWarning.show(0, MQConsts.classAnnotationAllow.get(i).getName() + "[key:" + key + "]", obj.toString());
				MQApplicationcontext.insertSpringExtendMapClass(obj);
			}
		}
	}

	/**
	 * 通过上下文按 <code>@Scope("singleton")</code> 进行检索。含有扩展注解的，则提出保存
	 * @param ac ApplicationContext
	 */
	@Deprecated
	static void setApplicationContextClassScopeSingleton(ApplicationContext ac) {
		if (ac == null) return;
		Map<String, Object> map = ac.getBeansWithAnnotation(Scope.class);
		for (String key : map.keySet()) {
			Object obj = map.get(key);
			MQWarning.show(0, "Scope.class[key:" + key + "]", obj.toString());
			if (!MQUtilsAnno.isMQExtendClass(obj)) continue;
			MQWarning.show(0, "Scope.class[key:" + key + "]", obj.toString());
			MQApplicationcontext.insertSpringExtendMapClass(obj);
		}

	}

	/**
	 * 向容器内添加对象，自动按照类寻找类组
	 * @param obj Object
	 * @return boolean
	 */
	static final boolean add(final Object obj) {
		Class<?> clazz = obj.getClass();
		if (!MQContainer.MQSpringExtendObjectMap.containsKey(clazz)) return false;
		if (MQContainer.MQSpringExtendObjectMap.get(clazz).contains(obj)) return false;
		MQContainer.MQSpringExtendObjectMap.get(clazz).add(obj);
		return true;
	}

	/**
	 * 把某个类分解放在静态列表中MQSpringExtendContainer.MQSpringExtendMethodList
	 * @param classzz Class< ? >
	 */
	static final void splitclassMethod(final Class<?> classzz) {
		if (classzz == null) return;
		Method[] methods = classzz.getDeclaredMethods();
		try {
			for (Method method : methods) {
				if (!method.isAccessible()) method.setAccessible(true);
				if (MQUtilsAnno.isMQExtendMethod(method)) {
					if (!MQConsts.allowInputMethod(method)) continue;
					ElementMethod e = getEM(classzz, method);
					if (MQConsts.ACC_MQInitViewwarning) System.out.println("MQExtend->ElementMethod:" + e.toString());
					MQContainer.MQSpringExtendMethodList.add(e);
					MQContainer.setEM(e);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 把类中的某个方法转成ElementMethod
	 * @param classzz Class< ? >
	 * @param method Method
	 * @return ElementMethod
	 */
	static final ElementMethod getEM(final Class<?> classzz, final Method method) {
		final ElementMethod e = new ElementMethod();
		e.classzz = classzz;
		e.method = method;
		try {
			Annotation p = method.getAnnotation(MQConsts.ACC_AnnotationMethod);
			e.identifier = MQAnnotation.getString(p, MQConsts.ACC_annotion.identifier);
			e.explain = MQAnnotation.getString(p, MQConsts.ACC_annotion.explain);
			e.groupid = MQAnnotation.getInteger(p, MQConsts.ACC_annotion.groupid, 0);
			//e.explain = (String) p.getClass().getDeclaredMethod(MQConsts.ACC_annotion.explain).invoke(p);
			//e.groupid = (Integer) p.getClass().getDeclaredMethod(MQConsts.ACC_annotion.groupid).invoke(p);
			Annotation[][] paraAnno = method.getParameterAnnotations();
			String[] paraName = asmReadClass.getParaName(classzz, method);
			Class<?>[] paraClassArray = method.getParameterTypes();
			for (int i = 0, len = paraName.length; i < len; i++) {
				ElementParameter f = getEP(paraName[i], paraAnno[i]);
				if (f == null) continue;
				//System.out.println("f:" + f.toString());
				f.p_class = paraClassArray[i];
				e.parameterList.add(f);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return e;
	}

	/**
	 * 把某个参数与相应的扩展注解结合得 ElementParameter<br/>
	 * 注意：这里参数对象里没有此参数的类，需要返回到上升通过Method得到
	 * @param panaName String
	 * @param annoArray Annotation[]
	 * @return ElementParameter
	 */
	static final ElementParameter getEP(final String panaName, final Annotation[] annoArray) {
		if (panaName == null || panaName.equals("")) return null;
		ElementParameter f = new ElementParameter();
		try {
			for (int i = 0, len = annoArray.length; i < len; i++)
				if (MQConsts.ACC_AnnotationParameter.isInstance(annoArray[i])) {
					f.p_useAnnotation = true;
					f.p_allowNull = MQAnnotation.getBoolean(annoArray[i], MQConsts.ACC_annotion.Null, true);
					break;
				}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		f.p_name = panaName;
		return f;
	}

	/**
	 * 分解类
	 * @param classzz
	 */
	@SuppressWarnings("unused")
	static final void splitClass(final Class<?> classzz) {
		if (classzz == null) return;
		try {

			String classfile = classzz.getName();
			classfile = classfile.replaceAll("\\.", "/");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(classfile + ".class");
			InputStream is = url.openStream();
			ClassReader cr = new ClassReader(is);
			asmReadClass eac = new asmReadClass();
			//cr.accept(eac, 0);
			cr = null;
			is.close();
			is = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	static final String[] getParaName(final InputStream is, Method m) {
		final String[] paramNames = new String[m.getParameterTypes().length];
		try {
			ClassReader cr = new ClassReader(is);
			asmReadClass eac = new asmReadClass();
			/*
			 * cr.accept(new ClassVisitor{
			 * }, 0);
			 */
			cr = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paramNames;
	}

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
	static final String changedLine(final String line) {
		if (line == null) {
			return null;
		} else {
			// System.out.println("beforechangeline:"+line);
			try {
				byte buf[] = new byte[1];
				byte[] byteArray = new byte[line.length()];
				StringReader aStringReader = new StringReader(line);
				int character, i = 0;
				try {
					while ((character = aStringReader.read()) != -1) {
						buf[0] = (byte) character;
						byteArray[i++] = buf[0];
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return new String(byteArray, "UTF-8");
			} catch (UnsupportedEncodingException e) {
			}
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
	 * 对ElementMethod List进行过滤，判断参数是否合格，返回List
	 * @param list List< ElementMethod >
	 * @param parameter MQparameter
	 * @return List< ElementMethod >
	 */
	static final List<ElementMethod> filter(List<ElementMethod> list, final MQparameter parameter) {
		if (list == null || parameter == null || list.size() == 0) return list;
		final int len = list.size();
		//MQWarning.show("filter", len);
		List<ElementMethod> newList = new ArrayList<ElementMethod>(0);
		for (int i = 0; i < len; i++)
			if (list.get(i).checkParameter(parameter)) {
				//MQWarning.show("发现参数正确项", list.get(i).toString());
				newList.add(list.get(i));
			}
		return newList;
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
	static final boolean isInterface(final Class<?> c, final Class<?> interfaceClass) {
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

	@Deprecated
	static final void showWarningRun(final String key, final Object value) {
		if (MQConsts.ACC_MQRunViewwarning) System.out.println(MQConsts.ACC_MQRunHead + key + ":" + value.toString());
	}

	@Deprecated
	static final void showWarningInit(final String key, final Object value) {
		if (MQConsts.ACC_MQInitViewwarning) System.out.println(MQConsts.ACC_MQInitHead + key + ":" + value.toString());
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

}
