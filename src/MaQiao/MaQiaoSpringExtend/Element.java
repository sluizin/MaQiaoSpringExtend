/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;
import MaQiao.MaQiaoSpringExtend.basicTypes.BasicTypesMatching;
import MaQiao.MaQiaoSpringExtend.parameter.AbstractMQparameter;
import MaQiao.MaQiaoSpringExtend.warning.Warning;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class Element {
	static class MQparameterMethod {
		MQparameter parameter;
		List<ElementMethod> commondList;
	}

	/**
	 * 方法对象
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ElementMethod implements Comparable<Object> {
		String value = "";
		String explain = "";
		Method method = null;
		int groupid = 0;
		List<ElementParameter> parameterList = new ArrayList<ElementParameter>();
		Class<?> classzz = null;

		String getMethodName() {
			return method.getName();
		}

		void Run(final AbstractMQparameter absMQparameter) {
			Run(absMQparameter.getParameter());

		}

		/**
		 * ElementMethod的运行
		 * @param parameter MQparameter
		 */
		void Run(final MQparameter parameter) {
			if (method == null || parameter == null) return;
			Object[] arrayObject = MQparameterMatching.matching(method, this, parameter);
			for (int i = 0; i < arrayObject.length; i++) {
				Warning.show("MethodParameterObject{" + parameterList.get(i).p_name + "}[" + i + "]", (arrayObject[i] != null) ? arrayObject[i].toString() : "null");
			}
			if (!method.isAccessible()) method.setAccessible(true);
			List<Object> list = MQContainer.MQSpringExtendObjectMap.get(classzz);
			if (list == null || list.size() == 0) return;
			if (parameter.setupSystem.allowMulti) {
				Run(arrayObject, list.toArray());
			} else {
				Run(arrayObject, list.get(0));
			}
		}

		/**
		 * 搂Object数组，依次运行方法
		 * @param args Object[]
		 * @param objArray Object[]
		 */
		private void Run(Object[] args, Object... objArray) {
			if (objArray.length == 0) return;
			try {
				for (int i = 0, len = objArray.length; i < len; i++) {
					Warning.show("Private Run", objArray[i].toString());
					//for (int ii = 0, lena = args.length; ii < lena; ii++)
					//if (args[ii] != null) MQWarning.show("Private Run", args[ii].toString());
					method.invoke(objArray[i], args);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 得到此方法的参数名称数组
		 * @return String[]
		 */
		String[] getParameterNameArray() {
			int len = parameterList.size();
			String[] paraNameArray = new String[len];
			for (int i = 0; i < len; i++)
				paraNameArray[i] = parameterList.get(i).p_name;
			return paraNameArray;
		}

		/**
		 * 按关键字与系统参数判断这个方法合格[value]
		 * @param key String
		 * @param parameter MQparameter
		 * @return boolean
		 */
		boolean checkKey(final String key, final MQparameter parameter) {
			if (parameter.value.matching) {
				if (value.indexOf(key) == -1) return false;
			} else {
				if (!value.equals(key)) return false;
			}
			return checkParameter(parameter);
		}

		/**
		 * 匹配参数，如果为真，则输出<br/>
		 * 先按参数名称进行比较<br/>
		 * 再对参数是否允许null进行比较</br>
		 * @param parameter MQparameter
		 * @return boolean
		 */
		boolean checkParameter(MQparameter parameter) {
			//MQWarning.show("checkParameter", true);
			if (parameter == null) return false;
			List<ClassParameterObject> parameterObjList = parameter.parameterObjList;
			System.out.println("parameterObjList:"+parameterObjList.size());
			/* 匹配参数名 */
			if (!checkParameterName(parameterList, parameterObjList)) {
				System.out.println("参数名 error:" + this.value);
				return false;
			}
			//MQWarning.show("匹配参数名", true);
			/* 匹配参数是否允许Null */
			if (!checkParameterNull(parameterList, parameter)) {
				System.out.println("Null error:" + this.value);
				return false;
			}
			//MQWarning.show("匹配参数是否允许Null", true);
			return true;
		}

		/**
		 * 判断class是否相同
		 * @param classzz Class&lt;?>
		 * @return boolean
		 */
		boolean checkClass(final Class<?> classzz) {
			if (classzz == null) return false;
			if (this.classzz == classzz || classzz.equals(classzz)) return true;
			return false;
		}

		/**
		 * 依照参数对象得到此方法的实际调用使用的参数数组
		 * @param parameter MQparameter
		 * @return Object[]
		 */
		Object[] getPara(final MQparameter parameter) {
			final int len = parameterList.size();
			Object[] paraArray = new Object[len];
			return paraArray;
		}

		/**
		 * 匹配参数是否允许Null<br/>
		 * 依次判断此方法的各个参数是否允许为空:<br/>
		 * 设置参数ParameterClass.allowNull=true， 不允许出现@MQExtend(Null = false) <br/>
		 * 设置参数ParameterClass.allowNull=false，不允许出现@MQExtend(Null = true)<br/>
		 * @param parameterList List< ElementParameter >
		 * @param parameter MQparameter
		 * @return boolean
		 */
		public static boolean checkParameterNull(List<ElementParameter> parameterList, MQparameter parameter) {
			if (parameterList == null || parameter == null) return false;
			ElementParameter e;
			for (int i = 0, len = parameterList.size(); i < len; i++) {
				e = parameterList.get(i);
				if (parameter.setupParameter.allowNull) {
					/* 设置参数允许为空，则依次判断此方法的各个参数是否允许为空 不允许出现@MQExtend(Null = false) */
					if (e.p_useAnnotation && !e.p_allowNull) {
						//MQWarning.show("匹配参数是否允许Null", "设置参数允许为空");
						return false;
					}
				} else {
					/* 设置参数不允许为空，则依次判断此方法的各个参数是否允许为空 不允许出现@MQExtend(Null = true) */
					if (e.p_useAnnotation && e.p_allowNull) {
						//MQWarning.show("匹配参数是否允许Null", "设置参数不允许为空");
						return false;
					}
				}
			}

			/*
			 * if(parameter.parameterSet.allowNull){
			 * //设置参数允许为空，则依次判断此方法的各个参数是否允许为空 不允许出现@MQExtend(Null = false)
			 * for(int i=0,len=parameterList.size();i<len;i++){
			 * e=parameterList.get(i);
			 * if(e.p_useAnnotation && !e.p_allowNull)return false;
			 * }
			 * }else{
			 * //设置参数不允许为空，则依次判断此方法的各个参数是否允许为空 不允许出现@MQExtend(Null = true)
			 * for(int i=0,len=parameterList.size();i<len;i++){
			 * e=parameterList.get(i);
			 * if(e.p_useAnnotation && e.p_allowNull)return false;
			 * }
			 * }
			 */
			return true;
		}

		/**
		 * 匹配参数名如果出现强制要求参数名，则检验合法性
		 * @param parameterList List< ElementParameter >
		 * @param parameterSetList List< ParameterObject >
		 * @return boolean
		 */
		static boolean checkParameterName(List<ElementParameter> parameterList, List<ClassParameterObject> parameterSetList) {
			if (parameterList == null || parameterSetList == null) return false;
			for (int i = 0,len = parameterList.size(); i < len; i++) 
				if(!parameterList.get(i).checkSafeParameterSetList(parameterSetList))return false;
			return true;
		}

		/**
		 * 匹配参数名如果出现强制要求参数名，则检验合法性
		 * @param parameterList List< ElementParameter >
		 * @param parameterSetList List< ParameterObject >
		 * @return boolean
		 */
		boolean checkSafeParameterName(ElementParameter e, final List<ClassParameterObject> parameterSetList) {
			if (e == null || parameterSetList == null) return false;
			ClassParameterObject f;
			if (e.p_useAnnotation) {
				for (int i = 0, len = parameterSetList.size(); i < len; i++) {
					f = parameterSetList.get(i);
					if (!(f.variable.equals(e.p_name) && BasicTypesMatching.isMatching(e.p_class, f.obj.getClass()))) {
						return false;
					}
				}
				return false;
			}
			return true;
		}

		/*
		 * (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Object o) {
			if (o instanceof ElementMethod) {
				ElementMethod s = (ElementMethod) o;
				if (this.groupid > s.groupid) return 1;
				else return 0;
			}
			return -1;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("[groupid=");
			builder.append(String.format("%-3d", groupid));
			builder.append(",value=");
			builder.append(String.format("%-30s", value));
			builder.append(",method=");
			builder.append(String.format("%-38s", method.getName()));
			builder.append(",texplain=");
			builder.append(String.format("%-35s", explain));
			builder.append(",classzz=");
			builder.append(classzz.getName());
			builder.append(",\tparameterList=");
			builder.append(parameterList);
			builder.append("]");
			return builder.toString();
		}

		/**
		 * 导出ElementMethod的Json串，结果为对象:{}
		 * @return String
		 */
		String toJson() {
			StringBuilder sb = new StringBuilder();
			sb.append('{');
			sb.append("\"value\":\"");
			sb.append(value);
			sb.append("\",");
			sb.append("\"groupid\":");
			sb.append(groupid);
			sb.append(",");
			sb.append("\"explain\":\"");
			sb.append(explain);
			sb.append("\",");
			sb.append("\"method\":\"");
			sb.append(method.getName());
			sb.append("\",");
			sb.append("\"parameterList\":[");
			for (int i = 0, len = parameterList.size(); i < len; i++) {
				sb.append(parameterList.get(i).toJson());
				if (i < len - 1) sb.append(',');
			}
			sb.append("]");
			sb.append('}');
			return sb.toString();
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((classzz == null) ? 0 : classzz.hashCode());
			result = prime * result + ((explain == null) ? 0 : explain.hashCode());
			result = prime * result + groupid;
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((parameterList == null) ? 0 : parameterList.hashCode());
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			ElementMethod other = (ElementMethod) obj;
			return this.toString().equals(other.toString());
		}

	}

	/**
	 * 参数对象
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class ElementParameter {
		/** 是否使用的注解 */
		boolean p_useAnnotation = false;
		/** 注解值 */
		boolean p_allowNull = true;
		/** 参数名称 */
		String p_name;
		/** 参数类型 */
		Class<?> p_class;
		/** 参数详细类型Type */
		Type p_type;

		/**
		 * 匹配参数名如果出现强制要求参数名，则检验合法性
		 * @param parameterSetList List< ParameterObject >
		 * @return boolean
		 */
		boolean checkSafeParameterSetList(final List<ClassParameterObject> parameterSetList) {
			if (parameterSetList == null) return false;
			ClassParameterObject f;
			if (p_useAnnotation) {
				for (int i = 0, len = parameterSetList.size(); i < len; i++) {
					System.out.println(i+"/"+len);
					f = parameterSetList.get(i);
					boolean t=f.variable.equals(p_name);
					boolean tt=BasicTypesMatching.isMatching(p_class, f.obj.getClass());
					System.out.println("t:"+t);
					System.out.println("tt:"+tt);
					System.out.println("p_class:"+p_class.getName());
					System.out.println("f.obj.getClass():"+f.obj.getClass().getName());
					if (f.variable.equals(p_name) && BasicTypesMatching.isMatching(p_class, f.obj.getClass())) {
						return true;
					}
				}
				return false;
			}
			return true;
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("ElementParameter [p_useAnnotation=");
			builder.append(p_useAnnotation);
			builder.append(", p_allowNull=");
			builder.append(p_allowNull);
			builder.append(", p_name=");
			builder.append(p_name);
			builder.append(", p_class=");
			builder.append(p_class);
			builder.append(", p_type=");
			builder.append(p_type);
			builder.append("]");
			return builder.toString();
		}

		/**
		 * 导出 ElementParameter 的Json串，结果为对象:{}
		 * @return String
		 */
		String toJson() {
			StringBuilder sb = new StringBuilder(200);
			sb.append('{');
			sb.append("\"p_useAnnotation\":");
			sb.append(p_useAnnotation);
			sb.append(",");
			sb.append("\"p_allowNull\":");
			sb.append(p_allowNull);
			sb.append(",");
			sb.append("\"p_name\":\"");
			sb.append(p_name);
			sb.append("\",");
			sb.append("\"p_class\":\"");
			sb.append(p_class);
			sb.append("\",");
			sb.append("\"p_type\":\"");
			sb.append(p_type);
			sb.append("\"");
			sb.append('}');
			return sb.toString();
		}

	}

	public static final class ElementT<T> {
		private Map<T, List<ElementMethod>> map = Collections.synchronizedMap(new HashMap<T, List<ElementMethod>>());
		/** 0:String 1:Integer */
		private int paradigmType = -1;

		boolean isString() {
			return paradigmType == 0;
		}

		boolean isInteger() {
			return paradigmType == 1;
		}

		/**
		 * 导出ElementMethod的Json串，结果为数组:[]
		 * @return String
		 */
		String toJson() {
			StringBuilder sb = new StringBuilder(2000);
			sb.append('[');
			final int len = map.keySet().size();
			int point = 0;
			for (T key : map.keySet()) {
				sb.append("{\"");
				sb.append(key);
				sb.append("\":[");
				List<ElementMethod> list = map.get(key);
				for (int i = 0, lenList = list.size(); i < lenList; i++) {
					sb.append(list.get(i).toJson());
					if (i < lenList - 1) sb.append(',');
				}
				sb.append("]}");
				if (++point < len) sb.append(',');
			}
			sb.append(']');
			return sb.toString();
		}

		public final Map<T, List<ElementMethod>> getMap() {
			return map;
		}

		public final void setMap(Map<T, List<ElementMethod>> map) {
			this.map = map;
		}

		/**
		 * 合集使用map.get(key)
		 * @param ObjArray String[]
		 * @return List< ElementMethod >
		 */
		@Deprecated
		List<ElementMethod> getListByObjArray(String... ObjArray) {
			if (ObjArray.length == 0) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> listnew = new ArrayList<ElementMethod>(0);
			for (int i = 0, len = ObjArray.length; i < len; i++)
				listnew.addAll(getListByKey(ObjArray[i], false, false));
			return listnew;
		}

		/**
		 * 合集(String使用indexOf或equals))
		 * @param ObjArray String[]
		 * @return List< ElementMethod >
		 */
		List<ElementMethod> getListByMQparameter(final MQparameter parameter) {
			if (parameter == null || !parameter.islegitimate()) return new ArrayList<ElementMethod>(0);
			return getListByObjArray(parameter.value.matching, parameter.setupSystem.ignoreCase, parameter.value.toKeyArray());
		}

		/**
		 * 合集(String使用indexOf或equals))[随机，按Map依次检索关键字数组]
		 * @param ObjArray String[]
		 * @return List< ElementMethod >
		 */
		List<ElementMethod> getListByMQparameterRnd(final MQparameter parameter) {
			//MQWarning.show("matching", parameter.value.matching);
			//MQWarning.show("islegitimate", parameter.islegitimate());
			if (parameter == null || !parameter.islegitimate()) return new ArrayList<ElementMethod>(0);
			return getListStringByKeyRnd(parameter.value.matching, parameter.setupSystem.ignoreCase, parameter.value.toKeyArray());
		}

		/**
		 * 合集(String使用indexOf或equals)
		 * @param matching boolean
		 * @param ObjArray String[]
		 * @return List< ElementMethod >
		 */
		List<ElementMethod> getListByObjArray(final boolean matching, final boolean ignoreCase, String... ObjArray) {
			if (ObjArray.length == 0) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> listnew = new ArrayList<ElementMethod>(0);
			for (int i = 0, len = ObjArray.length; i < len; i++)
				listnew.addAll(getListByKey(ObjArray[i], matching, ignoreCase));
			System.out.println("listnew:" + listnew.size());
			return listnew;
		}

		@Deprecated
		List<ElementMethod> getList(Object key) {
			if (paradigmType == 0 && (key instanceof String)) {
				return map.get(key);
			} else {
				if (paradigmType == 1 && (key instanceof Integer)) {
					return map.get(key);
				} else {
					return new ArrayList<ElementMethod>(0);
				}
			}
		}

		/**
		 * 直接使用map.get(key)
		 * @param key Object
		 * @return List< ElementMethod >
		 */
		@Deprecated
		List<ElementMethod> getListByKey(Object key) {
			if (key == null) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> list = map.get(key);
			return (list == null) ? new ArrayList<ElementMethod>(0) : list;
		}

		/**
		 * 如果是String则使用indexOf
		 * @param key Object
		 * @return List< ElementMethod >
		 */
		@Deprecated
		List<ElementMethod> getListByKeyMatchingDeprecated(Object key) {
			if (key == null) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> list = map.get(key);
			if (paradigmType == 0) {
				String key9 = key.toString();
				for (T key1 : map.keySet())
					if (((String) key1).indexOf(key9) > -1) list.addAll(map.get(key1));
			}
			if (paradigmType == 1) list = map.get(key);
			return (list == null) ? new ArrayList<ElementMethod>(0) : list;
		}

		/**
		 * 是否需要matching[字符串匹配] ignoreCase[是否忽略大小写]
		 * @param key Object
		 * @param matching boolean
		 * @param ignoreCase boolean
		 * @return List< ElementMethod >
		 */
		List<ElementMethod> getListByKey(final Object key, final boolean matching, final boolean ignoreCase) {
			if (key == null) return new ArrayList<ElementMethod>(0);
			//MQWarning.show("key", key);
			/* 字符串 */
			if (paradigmType == 0) { return getListStringByKey(key.toString(), matching, ignoreCase); }
			/* Integer */
			if (paradigmType == 1) { return map.get(key); }
			return null;
		}

		/**
		 * 字符串型Map 查到关键字，是否使用匹配和忽略大小写
		 * @param key String
		 * @param matching boolean
		 * @param ignoreCase boolean
		 * @return List< ElementMethod >
		 */
		private List<ElementMethod> getListStringByKey(final String key, final boolean matching, final boolean ignoreCase) {
			if (!isString() || key == null) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> list = new ArrayList<ElementMethod>(0);
			String key2 = "";
			//MQWarning.show("========================================", "--------------------------------");
			//MQWarning.show("ignoreCase", ignoreCase);
			//MQWarning.show("search key", key);
			for (T key1 : map.keySet()) {
				key2 = (String) key1;
				if (MQUtils.checkStandard(matching, ignoreCase, key2, key)) {
					//MQWarning.show("Search->map.key", key2 + " --- keyfind:" + key);
					list.addAll(map.get(key1));
				}
			}
			//MQWarning.show("getListStringByKey--size", list.size());
			//MQWarning.show("========================================", "--------------------------------");
			return (list == null) ? new ArrayList<ElementMethod>(0) : list;
		}

		/**
		 * 随机从Map中检索出符合关键字串的记录
		 * @param matching boolean
		 * @param ignoreCase boolean
		 * @param arrayKey String[]
		 * @return List< ElementMethod >
		 */
		public List<ElementMethod> getListStringByKeyRnd(final boolean matching, final boolean ignoreCase, final String... arrayKey) {
			if (arrayKey.length == 0) return new ArrayList<ElementMethod>(0);
			List<ElementMethod> list = new ArrayList<ElementMethod>(0);
			String key2 = "";
			for (T key1 : map.keySet()) {
				key2 = (String) key1;
				if (MQUtils.checkStandard(matching, ignoreCase, key2, arrayKey)) {
					//MQWarning.show("Search Result->map.key", key2);
					list.addAll(map.get(key1));
				}
			}
			return list;
		}

		/**
		 * 插入方法
		 * @param key T
		 * @param e ElementMethod
		 */
		void insertMap(T key, ElementMethod e) {
			if (!map.containsKey(key)) map.put(key, new ArrayList<ElementMethod>(1));
			List<ElementMethod> list = map.get(key);
			if (list != null && !list.contains(e)) list.add(e);
		}

		/**
		 * 移除方法
		 * @param key T
		 * @param e ElementMethod
		 */
		void removeMap(T key, ElementMethod e) {
			if (map.containsKey(key)) {
				List<ElementMethod> f = map.get(key);
				f.remove(e);
				if (f.isEmpty()) {
					map.remove(key);
					return;
				}
			}
		}

		void removeMap(final Class<?> classzz) {
			for (Entry<T, List<ElementMethod>> entry : map.entrySet()) {
				T key = entry.getKey();
				List<ElementMethod> list = entry.getValue();
				for (int i = 0; i < list.size(); i++) {
					ElementMethod e = list.get(i);
					if (e.classzz == classzz || e.classzz.equals(classzz)) list.remove(i--);
				}
				if (list.isEmpty()) map.remove(key);
			}
		}

		/*
		 * @Deprecated
		 * boolean isString() {
		 * ParameterizedType ptType;
		 * try {
		 * // 得到泛型的类型 擦除后类型
		 * ptType = (ParameterizedType) getClass().getDeclaredField("map").getGenericType();
		 * Type type = ptType.getActualTypeArguments()[0];
		 * System.out.println("Type type:" + type);
		 * System.out.println("Type type:" + type.toString());
		 * System.out.println("Type type:" + type.getClass());
		 * System.out.println("Type type:" + type.getClass().getName());
		 * } catch (NoSuchFieldException e) {
		 * e.printStackTrace();
		 * } catch (SecurityException e) {
		 * e.printStackTrace();
		 * }
		 * return false;
		 * }
		 */

		ElementT(Class<T> type) {
			T t = newTclass(type);
			if (t != null) {
				if (String.class.equals(t.getClass())) {
					this.paradigmType = 0;
				} else {
					if (Integer.class.equals(t.getClass())) {
						this.paradigmType = 1;
					} else {
						this.paradigmType = -1;
					}
				}
			}
		}

		/*
		 * @Deprecated
		 * void init() {
		 * try {
		 * Type typeClass1 = this.getClass().getGenericSuperclass();
		 * System.out.println("typeClass1:" + typeClass1);
		 * System.out.println("typeClass1:" + typeClass1.getTypeName());
		 * System.out.println("typeClass1:" + typeClass1.toString());
		 * ParameterizedType ptType = (ParameterizedType) getClass().getDeclaredField("map").getGenericType();
		 * Type type = ptType.getActualTypeArguments()[0];
		 * System.out.println("Type type4:" + type);
		 * System.out.println("Type type4:" + type.toString());
		 * System.out.println("Type type4:" + type.getClass());
		 * System.out.println("Type type4:" + type.getClass().getName());
		 * } catch (Exception e) {
		 * e.printStackTrace();
		 * }
		 * }
		 */
		/**
		 * 生成新的对象
		 * @param clazz Class< T >
		 * @return T
		 */
		private static <T> T newTclass(Class<T> clazz) {
			try {
				T a = clazz.newInstance();
				return a;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
