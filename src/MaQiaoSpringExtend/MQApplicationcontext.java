/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


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
		System.out.println("servletContext:" + servletContext.toString());
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
		Class<?> classzz = null;
		Object obj = null;
		for (String name : applicationcontextIOC.getBeanDefinitionNames()) {
			MQWarning.show(0,"Search Class from ioc", name);
			obj = applicationcontextIOC.getBean(name);
			classzz = obj.getClass();
			/* 检索这个对象的接口 */
			if (MQUtilsAnno.isMQExtendClass(classzz)){
				MQWarning.show(0,"Search Class from ioc[Exist]", name);
				insertSpringExtendMapClass(classzz);
				if (MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) {
					insertSpringExtendMapObject(obj);
				}
			}
		}
	}

	/**
	 * 从Base中寻找对象
	 */
	void searchBase() {
		if (applicationcontextBase == null) return;
		Class<?> classzz = null;
		Object obj = null;
		try {
			for (String name : applicationcontextBase.getBeanDefinitionNames()) {
				MQWarning.show(0,"Search Class from base", name);
				obj = applicationcontextBase.getBean(name);
				classzz = obj.getClass();
				if (MQUtilsAnno.isMQExtendClass(classzz)) {
					MQWarning.show(0,"Search Class from base[Exist]", name);
					//insertSpringExtendMapClass(classzz);
					if (MQContainer.MQSpringExtendObjectMap.containsKey(classzz)){
						insertSpringExtendMapObject(obj);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 插入对象，如果发现此对象的类重复跳出
	 * @param obj Object
	 */
	static void insertSpringExtendMapClass(final Object obj) {
		insertSpringExtendMapClass(obj.getClass());
	}
	/**
	 * 插入类，如果发现重复跳出
	 * @param classzz Class&lt;?&gt;
	 */
	static void insertSpringExtendMapClass(final Class<?> classzz) {
		/* 判断这个类是否含有本系统的扩展注解，没有，则退出 */
		if(!MQUtilsAnno.isMQExtendClass(classzz))return;
		/* 如果已经存在这个类，则退出 */
		if (MQContainer.MQSpringExtendObjectMap.containsKey(classzz))return;
		/* 判断这个类是否含有过滤注解的类，如果有，则退出 */
		if(MQUtilsAnno.isAnnotationfilterClass(classzz)) return;
		/* 判断这个是否含有指定允许的注解，如果没有，则退出 */
		if(!MQUtilsAnno.isAnnotationAllowClass(classzz))return;
		if (MQConsts.ACC_MQInitViewwarning)System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
		MQWarning.show(0,"insert Class", classzz.getName());
		MQContainer.MQSpringExtendObjectMap.put(classzz,new ArrayList<Object>());
		MQUtils.splitclassMethod(classzz);
		if (MQConsts.ACC_MQInitViewwarning)System.out.println("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
	}


	/**
	 * 先检索是否含有类，如果没有类则退出，如果有的话记录
	 * @param obj Object
	 */
	static void insertSpringExtendMapObject(Object obj) {
		if (obj == null) return;
		Class<?> classzz = obj.getClass();
		if (!MQContainer.MQSpringExtendObjectMap.containsKey(classzz)) return;
		MQWarning.show(0,"insert Object", obj.toString());
		MQUtils.add(obj);
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
