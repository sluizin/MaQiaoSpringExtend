/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//import MaQiao.MaQiaoSpringExtend.AnnotationCheck.AnnotationUtils;




import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationClassUtils;
import MaQiao.MaQiaoSpringExtend.warning.Warning;
import MaQiao.MaQiaoSpringExtend.warning.WarningConsts;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQApplicationcontext {
	int searchRange = 0;
	/**
	 * 本地运行环境容器
	 */
	ApplicationContext applicationcontextBase = null;
	/**
	 * IOC容器
	 */
	ApplicationContext applicationcontextIOC = null;
	/**
	 * Servlet容器提供的上下文
	 */
	ServletContext servletContext = null;

	/**
	 * 构造函数
	 */
	public MQApplicationcontext() {
		System.out.println("MQSpringExtend:structure");
		structureInit();
	}

	/**
	 * 构造函数
	 */
	public MQApplicationcontext(ApplicationContext ac, ServletContext sc) {
		System.out.println("MQSpringExtend:structure");
		structureInit();
		if (ac != null) {
			System.out.println("ac:" + ac.toString());
			this.applicationcontextIOC = ac;
		}
		if (sc != null) {
			System.out.println("sc:" + sc.toString());
			this.servletContext = sc;
		}
	}

	/**
	 * 构造时进行初始化
	 */
	private void structureInit() {
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		this.servletContext = webApplicationContext.getServletContext();
		if(servletContext!=null){
			String active = servletContext.getInitParameter("spring.profiles.active");
			System.setProperty("spring.profiles.active", active);
		}
		applicationcontextBase = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		System.out.println("applicationcontextBase:" + applicationcontextBase.toString());
	}

	/**
	 * 启动检索
	 */
	public void initialization() {
		if (MQContainer.MQSpringExtendMethodList.size() > 0) return;//如果输出的结果集非空，则跳出
		System.out.println("MQSpring:init()--START");
		/* 通过注解检索所有范围内的类，并存入容器中 */
		search();
		//searchClassAnnotation();

		Collections.sort(MQContainer.MQSpringExtendMethodList);  //按照升序排序
		/* 通过范围检索所有对象，并按照注解类存入容器中 */
		//searchObject();
		//启动守护线程

		System.out.println("MQSpring:init()--E N D");
	}

	/**
	 * 检索所有对象
	 */
	void search() {
		/*
		 * 在ioc中检索对象
		 */
		if (MQUtils.shift(searchRange, MQConsts.ACC_Search_IOC)) {
			searchIOC();
		}
		/*
		 * 在base中检索对象
		 */
		if (MQUtils.shift(searchRange, MQConsts.ACC_Search_Base)) {
			searchBase();
		}
	}

	/**
	 * 从IOC中寻找对象
	 */
	void searchIOC() {
		if (applicationcontextIOC == null) return;
		Object obj = null;
		try {
			for (String name : applicationcontextIOC.getBeanDefinitionNames()) {
				Warning.show(0, "Search Class from ioc", name);
				obj = applicationcontextIOC.getBean(name);
				put("ioc", name, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 放入对象
	 * @param from String
	 * @param name String
	 * @param obj Object
	 */
	private void put(String from, String name, Object obj) {
		if (obj == null) return;
		/* 检索这个对象的接口 */
		if (AnnotationClassUtils.isCorrectClass(obj.getClass())) {
			Warning.show(0, "Search Class from " + from + "[Exist]", name);
			register(obj);
			//if (MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) register(obj);
		}

	}

	/**
	 * 从Base中寻找对象
	 */
	void searchBase() {
		if (applicationcontextBase == null) return;
		Object obj = null;
		try {
			for (String name : applicationcontextBase.getBeanDefinitionNames()) {
				Warning.show(0, "Search Class from base", name);
				obj = applicationcontextBase.getBean(name);
				put("base", name, obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 插入类，如果发现重复跳出
	 * @param classzz Class&lt;?&gt;
	 */
	static boolean register(final Class<?> classzz) {
		/* 判断这个类是否含有本系统的扩展注解，没有，则退出 */
		if (!AnnotationClassUtils.isCorrectClass(classzz)) return false;
		/* 如果已经存在这个类，则退出 */
		if (MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) return false;
		if (WarningConsts.ACC_MQInitViewwarning) System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
		Warning.show(0, "insert Class", classzz.getName());
		MQContainer.MQSpringExtendObjectMap.put(classzz, new ArrayList<Object>());
		MQContainer.register(classzz);
		if (WarningConsts.ACC_MQInitViewwarning) System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
		return true;
	}
	/**
	 * 移除类
	 * @param classzz Class&lt;?&gt;
	 */
	static boolean unregister(final Class<?> classzz) {
		if (!AnnotationClassUtils.isCorrectClass(classzz)) return false;
		Warning.show(0, "unregister Class", classzz.getName());
		MQContainer.unregister(classzz);
		return true;
	}
	/**
	 * 先注册此对象的类，如果不能注册此类，则退出，反之记录某对象
	 * @param obj Object
	 */
	static boolean unregister(Object obj) {
		if (obj == null) return false;
		if(!AnnotationClassUtils.isCorrectClass(obj.getClass()))return false;
		Warning.show(0, "unregister Object", obj.toString());
		MQContainer.unregister(obj);
		return true;
	}
	/**
	 * 先注册此对象的类，如果不能注册此类，则退出，反之记录某对象
	 * @param obj Object
	 */
	static boolean register(Object obj) {
		if (obj == null) return false;
		if(!AnnotationClassUtils.isCorrectClass(obj.getClass()))return false;
		MQContainer.register(obj);
		return true;
	}
	/**
	 * 判断此类是否存在系统中
	 * @param obj
	 * @return
	 */
	static boolean isExist(Object obj){
		if (obj == null) return false;
		Class<?> clazz = obj.getClass();
		if (!isExist(clazz)) return false;
		if (MQContainer.MQSpringExtendObjectMap.get(clazz).contains(obj)) return true;
		return false;		
	}
	/**
	 * 判断此类是否存在系统中
	 * @param obj
	 * @return
	 */
	static boolean isExist(final Class<?> classzz){
		if (classzz == null) return false;
		if (!MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) return true;
		return false;		
	}

	public final ApplicationContext getApplicationcontextIOC() {
		return applicationcontextIOC;
	}

	/**
	 * 设置IOC容器
	 * @param ac ApplicationContext
	 */
	public final void setApplicationcontextIOC(ApplicationContext ac) {
		this.applicationcontextIOC = ac;
	}

	public final ServletContext getServletContext() {
		return servletContext;
	}

	public final void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public final int getSearchRange() {
		return searchRange;
	}

	/**
	 * 设置检索接口范围<br/>
	 * 例：MQSpringConsts.ACC_Search_IOC+MQSpringConsts.ACC_Search_Base
	 * @param searchRange int
	 */
	public final void setSearchRange(int searchRange) {
		this.searchRange = searchRange;
	}

}
