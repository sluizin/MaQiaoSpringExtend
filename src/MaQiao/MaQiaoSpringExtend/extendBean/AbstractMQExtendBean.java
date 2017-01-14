/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.extendBean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import MaQiao.MaQiaoSpringExtend.MQRUN;
import MaQiao.MaQiaoSpringExtend.annotation.MQExtenState;
import MaQiao.MaQiaoSpringExtend.annotation.MQExtendClass;

/**
 * 尝试使用Spring 中的InitializingBean，自动注解本对象，继承关系
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@MQExtenState(author = "Sunjian", start = "", end = "", invalid = false)
@MQExtendClass(value = "AbstractMQExtendBean 类", explain = "专题扩展父类[来源于继承]", groupid = 0, version = "1.0")
public abstract class AbstractMQExtendBean implements InitializingBean,DisposableBean{
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("register this...");
		MQRUN.register(this);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 */
	@Override
	public void destroy() throws Exception {
		MQRUN.unregister(this.getClass());
	}

}
