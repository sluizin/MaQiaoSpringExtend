/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MaQiaoSpringExtend.Element.ElementMethod;
import MaQiaoSpringExtend.Element.ElementT;

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
	static final Map<Class<?>, List<Object>> MQSpringExtendObjectMap = new HashMap<Class<?>, List<Object>>();

	static final List<ElementMethod> MQSpringExtendMethodList = new ArrayList<ElementMethod>();

	//static final ElementT<Class<?>> mapClass = new ElementT<Class<?>>();
	static final ElementT<String> mapIdentifier = new ElementT<String>(String.class);

	//static final ElementT<String> mapMethodname = new ElementT<String>();
	//static final ElementT<Integer> mapGroupid = new ElementT<Integer>();

	static final MQCache cacheParam = new MQCache();

	List<ElementMethod> getListByIdentifier(boolean matching, boolean ignoreCase, String... ObjArray) {
		if (ObjArray.length == 0) return new ArrayList<ElementMethod>(0);
		List<ElementMethod> listnew = mapIdentifier.getListByKey(ObjArray[0], matching, ignoreCase);
		for (int i = 0, len = ObjArray.length; i < len; i++)
			listnew.retainAll(mapIdentifier.getListByKey(ObjArray[i], matching, ignoreCase));
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
	 * 0:Identifier<br/>
	 * 1:Methodname<br/>
	 * 2:Groupid<br/>
	 * @param from int
	 * @param key Object
	 * @return List< ElementMethod >
	 */
	public List<ElementMethod> getKeyBy(int from, boolean matching, boolean ignoreCase, Object key) {
		//if (from == 0) return mapClass.map.get(key);
		if (from == 0) return mapIdentifier.getListByKey(key, matching, ignoreCase);
		//if (from == 1) return mapMethodname.getListByKey(key);
		//if (from == 2) return mapGroupid.getListByKey(key);

		return null;
	}

	/**
	 * 把方法集插入到容器里
	 * @param e ElementMethod
	 */
	static void setEM(ElementMethod e) {
		//mapClass.insertMap(e.classzz, e);
		mapIdentifier.insertMap(e.identifier, e);
		//mapMethodname.insertMap(e.getMethodName(), e);
		//mapGroupid.insertMap(e.groupid, e);
	}

	static final void runElementMethod(final List<ElementMethod> list, final MQparameter parameter) {
		if (parameter.identifier.order) {
			/* 顺序运行 以参数为标准顺序读取 */
			List<String> keyList = parameter.identifier.array;
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
}
