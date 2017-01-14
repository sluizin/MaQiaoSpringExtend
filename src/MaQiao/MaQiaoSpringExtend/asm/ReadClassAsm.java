/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.asm;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import aj.org.objectweb.asm.Type;

/**
 * 通过asm分解类
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class ReadClassAsm {

	/**
	 * 得到某类中某个方法的参数数组
	 * @param classzz Class&lt;?>
	 * @param m Method
	 * @return String[]
	 */
	public static final String[] getParaName(final Class<?> classzz, final Method m) {
		if (classzz == null || m == null) return new String[0];
		int len = m.getParameterTypes().length;
		if (len == 0) return new String[0];
		final String[] paramNames = new String[len];
		try {
			String classfile = classzz.getName().replaceAll("\\.", "/");
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(classfile + ".class");
			InputStream is = url.openStream();
			ClassReader cr = new ClassReader(is);
			cr.accept(new MQClassVisitor(paramNames,m), 0);
			cr = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paramNames;
	}


}
