/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.cache;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import MaQiao.MaQiaoSpringExtend.MQConsts;
import MaQiao.MaQiaoSpringExtend.MQparameter;
import MaQiao.MaQiaoSpringExtend.Element.ElementMethod;

/**
 * <code>MQparameter List&lt;ElementMethod&gt;</code> 缓存
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQCache {
	/** 缓存对象放入在List中 */
	private final List<cacheParamClass> cacheList = Collections.synchronizedList(new LinkedList<cacheParamClass>());

	/** Cache中state属性所在类中的偏移量 */
	static long lockedMQCacheStateOffset = 0L;
	/**
	 * 从缓存中提出List
	 * @param parameter MQparameter
	 * @return List&lt;ElementMethod>
	 */
	public List<ElementMethod> getList(MQparameter parameter) {
		if (parameter == null) return null;
		String jsonZip=getKey(parameter);
		cacheParamClass e = null;
		clean();
		for (int i = 0, len = cacheList.size(); i < len; i++)
			if ((e = cacheList.get(i)).jsonZip.equals(jsonZip)){
				System.out.println("searchCache:"+e.currentTime);
				return e.getList();
			}
		return null;
	}

	/**
	 * 清除过时缓存
	 */
	void clean() {
		for (int i = 0; i < cacheList.size(); i++) {
			if (cacheList.get(i).isExpire()) {
				cacheList.remove(i--);
			}
		}
	}

	/**
	 * 插入参数对象
	 * @param parameter MQparameter
	 * @param list List&lt;ElementMethod>
	 */
	public void put(MQparameter parameter, List<ElementMethod> list) {
		put(getKey(parameter), list);
	}
	/**
	 * 得到参数的压缩结果值成为key
	 * @param parameter MQparameter
	 * @return String
	 */
	private String getKey(MQparameter parameter){
		return parameter.toJsonZip();
	}
	/**
	 * 插入缓存对象
	 * @param jsonZip String
	 * @param list List&lt;ElementMethod>
	 */
	void put(String jsonZip, List<ElementMethod> list) {
		cacheList.add(new cacheParamClass(jsonZip, list));
	}

	/** States:状态 0:未占用 2:已独占锁定<br/> */
	int state;

	/**
	 * 锁定对象
	 */
	final void lock() {
		while (!MQConsts.UNSAFE.compareAndSwapInt(this, lockedMQCacheStateOffset, 0, 1)) {
		}
	}

	/**
	 * 释放对象锁
	 */
	final void unLock() {
		while (!MQConsts.UNSAFE.compareAndSwapInt(this, lockedMQCacheStateOffset, 1, 0)) {
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
		String jsonZip="";
		//MQparameter parameter = null;
		/** 缓存对象 */
		List<ElementMethod> list = null;
		/** 记录生成对象当前时间 */
		long currentTime = System.currentTimeMillis();

		public cacheParamClass(String jsonZip, List<ElementMethod> list) {
			this.jsonZip = jsonZip;
			this.list = list;
		}
		/**
		 * 得到List并且更新时间
		 * @return List&lt;ElementMethod> 
		 */
		List<ElementMethod> getList(){
			currentTime = System.currentTimeMillis();
			return list;
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
			result = prime * result + ((jsonZip == null) ? 0 : jsonZip.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			cacheParamClass other = (cacheParamClass) obj;
			if (jsonZip == null) {
				if (other.jsonZip != null) return false;
			} else if (!jsonZip.equals(other.jsonZip)) return false;
			return true;
		}

	}
	static {
		try {
			lockedMQCacheStateOffset = MQConsts.UNSAFE.objectFieldOffset(MQCache.class.getDeclaredField("state"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
