/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import MaQiao.MaQiaoSpringExtend.Element.ElementMethod;
import MaQiao.MaQiaoSpringExtend.Element.ElementT;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationClassUtils;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationMethodUtils;
import MaQiao.MaQiaoSpringExtend.cache.MQCache;
import MaQiao.MaQiaoSpringExtend.warning.Warning;

/**
 * Spring方法扩展容器
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQContainer {
	/**
	 * 存放类对应多个对象的记录基础表
	 */
	static final Map<Class<?>, List<Object>> MQSpringExtendObjectMap = Collections.synchronizedMap(new HashMap<Class<?>, List<Object>>());

	static final List<ElementMethod> MQSpringExtendMethodList = Collections.synchronizedList(new ArrayList<ElementMethod>());

	static final ElementT<String> mapValue = new ElementT<String>(String.class);

	//static final ElementT<Class<?>> mapClass = new ElementT<Class<?>>();
	//static final ElementT<String> mapMethodname = new ElementT<String>();
	//static final ElementT<Integer> mapGroupid = new ElementT<Integer>();

	static final MQCache cacheParam = new MQCache();

	List<ElementMethod> getListByValue(boolean matching, boolean ignoreCase, String... ObjArray) {
		if (ObjArray.length == 0) return new ArrayList<ElementMethod>(0);
		List<ElementMethod> listnew = mapValue.getListByKey(ObjArray[0], matching, ignoreCase);
		for (int i = 0, len = ObjArray.length; i < len; i++)
			listnew.retainAll(mapValue.getListByKey(ObjArray[i], matching, ignoreCase));
		return listnew;
	}

	/*
	 * List<ElementMethod> getListByMethodname (String... ObjArray){
	 * if (ObjArray.length == 0) return MQSpringConsts.ACC_defautlNullList;
	 * List<ElementMethod> listnew = mapMethodname.getListByKey(ObjArray[0]);
	 * for(int i=0,len=ObjArray.length;i<len;i++)
	 * listnew.retainAll(mapMethodname.getListByKey(ObjArray[i]));
	 * return listnew;
	 * }
	 * List<ElementMethod> getListByGroupid (String... ObjArray){
	 * if (ObjArray.length == 0) return MQSpringConsts.ACC_defautlNullList;
	 * List<ElementMethod> listnew = mapGroupid.getListByKey(ObjArray[0]);
	 * for(int i=0,len=ObjArray.length;i<len;i++)
	 * listnew.retainAll(mapGroupid.getListByKey(ObjArray[i]));
	 * return listnew;
	 * }
	 */
	/**
	 * 按关键对象查找<br/>
	 * from:<br/>
	 * 0:Value<br/>
	 * 1:Methodname<br/>
	 * 2:Groupid<br/>
	 * @param from int
	 * @param key Object
	 * @return List< ElementMethod >
	 */
	public List<ElementMethod> getKeyBy(int from, boolean matching, boolean ignoreCase, Object key) {
		//if (from == 0) return mapClass.map.get(key);
		if (from == 0) return mapValue.getListByKey(key, matching, ignoreCase);
		//if (from == 1) return mapMethodname.getListByKey(key);
		//if (from == 2) return mapGroupid.getListByKey(key);
		return null;
	}

	/**
	 * 把方法集从容器里移除
	 * @param e ElementMethod
	 */
	static void removeEM(ElementMethod e) {
		mapValue.removeMap(e.value, e);
	}

	/*
	 * ==================================================================================================
	 * 注册类
	 * ==================================================================================================
	 */
	/**
	 * 把某个类分解放在静态列表中MQSpringExtendContainer.MQSpringExtendMethodList
	 * @param classzz Class< ? >
	 */
	public static final void register(final Class<?> classzz) {
		if (classzz == null) return;
		if (!AnnotationClassUtils.isCorrectClass(classzz)) return;
		if (!MQSpringExtendObjectMap.containsKey(classzz)) MQSpringExtendObjectMap.put(classzz, new ArrayList<Object>());
		Method[] methods = classzz.getDeclaredMethods();
		try {
			for (Method method : methods) {
				if (!method.isAccessible()) method.setAccessible(true);
				if (AnnotationMethodUtils.isCorrectMethod(method)) {
					if (!MQUtilsInit.allowInputMethod(method)) continue;
					ElementMethod e = MQUtilsInit.getEM(classzz, method);
					Warning.show("ElementMethod:", e.toString());
					//MQContainer.MQSpringExtendMethodList.add(e);
					MQContainer.registerMapValue(e);
					MQContainer.registerMethodList(e);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 从容器[mapValue]里添加ElementMethod，按类
	 * @param e ElementMethod
	 */
	private static final void registerMapValue(ElementMethod e) {
		mapValue.insertMap(e.value, e);
	}

	/**
	 * 从容器[MQSpringExtendMethodList]里移除，按类移除
	 * @param classzz Class&lt;?>
	 */
	private static final void registerMethodList(ElementMethod e) {
		if (!MQSpringExtendMethodList.contains(e)) MQSpringExtendMethodList.add(e);

	}

	/*
	 * ==================================================================================================
	 * 移除类
	 * ==================================================================================================
	 */
	/**
	 * 把某个类移除静态列表
	 * @param classzz Class< ? >
	 */
	public static final void unregister(final Class<?> classzz) {
		if (classzz == null) return;
		removeMapValue(classzz);
		removeMethodList(classzz);
		removeObjectMap(classzz);
	}

	/**
	 * 向容器内添加对象，自动按照类寻找类组
	 * @param obj Object
	 * @return boolean
	 */
	static final void unregister(final Object obj) {
		if (obj == null) return;
		Class<?> clazz = obj.getClass();
		if (!AnnotationClassUtils.isCorrectClass(clazz)) return;
		Warning.show(0, "unregister Object", obj.toString());
		MQContainer.removeObjectMap(obj);
	}

	/**
	 * 从容器[mapValue]里移除，按类移除
	 * @param classzz Class&lt;?>
	 */
	private static final void removeMapValue(final Class<?> classzz) {
		mapValue.removeMap(classzz);
	}

	/**
	 * 从容器[MQSpringExtendMethodList]里移除，按类移除
	 * @param classzz Class&lt;?>
	 */
	private static final void removeMethodList(final Class<?> classzz) {
		for (int i = 0; i < MQSpringExtendMethodList.size(); i++) {
			ElementMethod e = MQSpringExtendMethodList.get(i);
			if (e.checkClass(classzz)) MQSpringExtendMethodList.remove(i--);
		}
	}

	/**
	 * 从容器[MQSpringExtendObjectMap]里移除，按类移除
	 * @param classzz Class&lt;?>
	 */
	private static final void removeObjectMap(final Class<?> classzz) {
		MQSpringExtendObjectMap.remove(classzz);
	}

	/**
	 * 从容器[MQSpringExtendObjectMap]里移除，按对象移除
	 * @param classzz Class&lt;?>
	 */
	private static final void removeObjectMap(final Object obj) {
		if (obj == null) return;
		List<Object> list = MQSpringExtendObjectMap.get(obj.getClass());
		list.remove(obj);
		/* 如果对象为空，则移除类 */
		if (list.isEmpty()) unregister(obj.getClass());
	}

	/*
	 * ==================================================================================================
	 * 注册对象
	 * ==================================================================================================
	 */
	/**
	 * 向容器内添加对象，自动按照类寻找类组
	 * @param obj Object
	 * @return boolean
	 */
	static final void register(final Object obj) {
		if (obj == null) return;
		Class<?> clazz = obj.getClass();
		if (!AnnotationClassUtils.isCorrectClass(clazz)) return;
		register(clazz);
		//if (!MQContainer.MQSpringExtendObjectMap.containsKey(clazz)) return;
		//if (MQContainer.MQSpringExtendObjectMap.get(clazz).contains(obj)) return;
		Warning.show(0, "insert Object", obj.toString());
		//MQContainer.MQSpringExtendObjectMap.get(clazz).add(obj);
		MQContainer.registerObjectMap(clazz, obj);
	}

	/**
	 * 从容器[MQSpringExtendObjectMap]里移除，按类移除
	 * @param classzz Class&lt;?>
	 * @param array Object[]
	 */
	private static final void registerObjectMap(final Class<?> classzz, final Object... array) {
		if (!MQSpringExtendObjectMap.containsKey(classzz)) MQSpringExtendObjectMap.put(classzz, new ArrayList<Object>());
		List<Object> list = MQSpringExtendObjectMap.get(classzz);
		if (list != null) for (int i = 0, len = array.length; i < len; i++)
			if (!list.contains(array[i])) list.add(array[i]);
	}

	/*
	 * ==================================================================================================
	 * 其它
	 * ==================================================================================================
	 */
	@Deprecated
	static final void runElementMethod(final List<ElementMethod> list, final MQparameter parameter) {
		if (parameter.value.order) {
			/* 顺序运行 以参数为标准顺序读取 */
			List<String> keyList = parameter.value.array;
			for (int i = 0, len = keyList.size(); i < len; i++) {
			}
		} else {
			/* 随机运行 */

		}
	}

	/**
	 * 判断类名是否存在于容器基础表中
	 * @param ClassName String
	 * @return boolean
	 */
	static final boolean isExistClass(final String ClassName) {
		if (ClassName == null || ClassName.equals("")) return false;
		for (Class<?> key : MQSpringExtendObjectMap.keySet())
			if (key.getName().equals(ClassName)) return true;
		return false;
	}

	/**
	 * 判断此类是否存在系统中
	 * @param obj
	 * @return
	 */
	static boolean isExist(final Class<?> classzz) {
		if (classzz == null) return false;
		if (!MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) return true;
		return false;
	}

	/**
	 * 把所有静态存储单元列表状态输出[Html]
	 * @return List&lt;String>
	 */
	public static List<String> outputList() {
		List<String> outList = new ArrayList<String>();
		for (Class<?> key : MQContainer.MQSpringExtendObjectMap.keySet()) {
			List<Object> list = MQContainer.MQSpringExtendObjectMap.get(key);
			for (int i = 0; i < list.size(); i++)
				outList.add("MQSpringExtendObjectMap[" + key.toString() + "[" + i + "]]:" + list.get(i).toString());
		}
		for (int i = 0; i < MQContainer.MQSpringExtendMethodList.size(); i++)
			outList.add("MQSpringExtendMethodList[" + i + "]:" + MQContainer.MQSpringExtendMethodList.get(i).toString() + "\n");
		for (String key : MQContainer.mapValue.getMap().keySet()) {
			List<ElementMethod> list = MQContainer.mapValue.getMap().get(key);
			for (int i = 0; i < list.size(); i++)
				outList.add("mapValue[" + key + ":" + i + "]:" + list.get(i).toString() + "\n");
		}
		return outList;
	}

	/**
	 * 把所有静态存储单元列表状态输出[Json]
	 * @return List&lt;String>
	 */
	public static String outputJson() {
		StringBuilder sb = new StringBuilder();
		sb.append('{');
		sb.append("\"MQSpringExtendObjectMap\":");
		sb.append(outputJsonObjectMap());
		sb.append(",");
		sb.append("\"MQSpringExtendMethodList\":");
		sb.append(outputJsonMethodList());
		sb.append(",");
		sb.append("\"mapValue\":");
		sb.append(MQContainer.mapValue.toJson());
		sb.append('}');
		return sb.toString();
	}

	/**
	 * 输出 MQContainer.MQSpringExtendObjectMap 的Json串，结果为数组:[]
	 * @return String
	 */
	private static final String outputJsonObjectMap() {
		StringBuilder sb = new StringBuilder(1000);
		sb.append('[');
		Set<Class<?>> objectMapSet = MQContainer.MQSpringExtendObjectMap.keySet();
		final int lenMapKey = objectMapSet.size();
		int point = 0;
		for (Class<?> key : objectMapSet) {
			List<Object> list = MQContainer.MQSpringExtendObjectMap.get(key);
			sb.append("{\"");
			sb.append(key);
			sb.append("\":[");
			for (int i = 0, len = list.size(); i < len; i++) {
				sb.append("\"" + list.get(i).toString() + "\"");
				if (i < len - 1) sb.append(',');
			}
			sb.append("]}");
			if (++point < lenMapKey) sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}

	/**
	 * 输出 MQContainer.MQSpringExtendMethodList 的Json串 ，结果为数组:[]
	 * @return String
	 */
	private static final String outputJsonMethodList() {
		StringBuilder sb = new StringBuilder(2000);
		sb.append('[');
		for (int i = 0, len = MQContainer.MQSpringExtendMethodList.size(); i < len; i++) {
			sb.append(MQContainer.MQSpringExtendMethodList.get(i).toJson());
			if (i < len - 1) sb.append(',');
		}
		sb.append(']');
		return sb.toString();
	}
}
