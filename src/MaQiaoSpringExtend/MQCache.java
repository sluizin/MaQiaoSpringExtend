/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import MaQiaoSpringExtend.Element.ElementMethod;

/**
 * <code>MQparameter List&lt;ElementMethod&gt;</code> 缓存
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQCache {
	//private final Map<MQparameter, List<ElementMethod>> cacheParam = new HashMap<MQparameter, List<ElementMethod>>();
	private final List<cacheParamClass> cacheList = new ArrayList<cacheParamClass>();

	List<ElementMethod> getList(MQparameter parameter) {
		if (parameter == null) return null;
		String json = parameter.tojson();
		cacheParamClass e = null;
		for (int i = 0, len = cacheList.size(); i < len; i++)
			if ((e = cacheList.get(i)).parameter.tojson().equals(json)) return e.list;
		return null;
	}

	void put(MQparameter parameter, List<ElementMethod> list) {
		cacheList.add(new cacheParamClass(parameter, list));
	}

	/** States:状态 0:未占用 2:已独占锁定(与this.Groups相对应)<br/> */
	int state;

	/**
	 * 锁定对象
	 */
	public final void lock() {
		while (!MQConsts.UNSAFE.compareAndSwapInt(this, MQConsts.lockedMQCacheStateOffset, 0, 1)) {
		}
	}

	/**
	 * 释放对象锁
	 */
	public final void unLock() {
		while (!MQConsts.UNSAFE.compareAndSwapInt(this, MQConsts.lockedMQCacheStateOffset, 1, 0)) {
		}
	}

	/**
	 * 缓存List&lt;ElementMethod&gt;
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	static class cacheParamClass {
		/** 参数 */
		MQparameter parameter = null;
		/** 缓存对象 */
		List<ElementMethod> list = null;
		/** 记录生成对象当前时间 */
		long currentTime = System.currentTimeMillis();

		public cacheParamClass(MQparameter parameter, List<ElementMethod> list) {
			this.parameter = parameter;
			this.list = list;
		}

		/**
		 * 判断是否过期
		 * @return boolean
		 */
		boolean isExpire() {
			long currentTime = System.currentTimeMillis();
			return (currentTime - this.currentTime) > MQConsts.cacheParamSize;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			cacheParamClass other = (cacheParamClass) obj;
			if (parameter == null) {
				if (other.parameter != null) return false;
			} else if (!parameter.equals(other.parameter)) return false;
			return true;
		}

	}
}
