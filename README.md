 * 注解方法，以确定哪些类的哪些方法可以向外提供服务以及参数的限制<br/>
 * <font color='red'>支持类的注解:@Component;@Scope("singleton");@Service</font><br/>
 * 必须先注解类，再注解方法，否则无法找到此类的方法[先检索类是否含有注解，舍弃接口]<br/>
 * 因为使用asm，所以暂时不支持继承注解<br/>
 * 如果两个方法值相同，具体哪个先运行[上帝安排]<br/>
 * <font color='red'>注意：严禁在此注解下的方法里使用MQExtendSystem，为了防止嵌套死循环[初步判断StackTraceElement]</font><br/>
 * 复杂度加高，把方法名和groupid的检索暂缓开发<br/><br/>