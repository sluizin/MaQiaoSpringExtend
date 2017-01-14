/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.listener;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import MaQiao.MaQiaoSpringExtend.MQApplicationcontext;
import MaQiao.MaQiaoSpringExtend.MQConsts;
import MaQiao.MaQiaoSpringExtend.MQUtilsInit;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Service
@Scope("singleton")
public final class MQApplicationListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware, ServletContextListener {

	private static ServletContext sc;
	private static ApplicationContext ac;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		boolean openMQSpringExtend = true;
		if (openMQSpringExtend) {
			System.out.println("MQ[Listener]putMQSpringExtendSystem --------------Start");
			ApplicationContext  ac2= event.getApplicationContext();
			MQUtilsInit.setApplicationContextClassAllow(ac2);			
			/* MQSpringExtendSystem */
			MQApplicationcontext mqapplication = new MQApplicationcontext(ac,sc);
			/*
			mqspring.setApplicationcontextIOC(ac);//设置IOC容器
			mqspring.setServletContext(sc);//设置Servlet容器提供的上下文
			mqspring.setSearchRange(MQSpringConsts.ACC_Search_IOC+MQSpringConsts.ACC_Search_Base);
			*/
			mqapplication.setSearchRange(MQConsts.ACC_Search_IOC);
			mqapplication.initialization();
			System.out.println("MQ[Listener]putMQSpringExtendSystem --------------E N D");
		}

	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sc = sce.getServletContext();
		System.out.println("ServletContextEvent Init");

	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ac = applicationContext;
	}

	/**
	 * 从当前IOC获取bean
	 * @param id
	 *            bean的id
	 * @return
	 */
	public static Object getObject(String id) {
		Object object = null;
		object = ac.getBean(id);
		return object;
	}
}
