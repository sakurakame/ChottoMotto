package jp.co.sss.shop.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

/**
 * AOPを使ってコントローラーのメソッドの引数を表示
 */
@Aspect
@Component
public class SampleAOP {
	/**
	 * Controller配下のメソッドの引数をコンソール出力
	 * @param joinPoint
	 */
	@Before("execution(* jp.co.sss.shop.controller.*.*.*(..)) || execution(* jp.co.sss.shop.controller.*.*.*.*(..)) ")
	public void before(JoinPoint joinPoint) {
		String[] methodArgNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
		Object[] methodArgValues = joinPoint.getArgs();
		System.out.print("** " + joinPoint.getSignature().toShortString() + "[");

		for (int i = 0; i < methodArgNames.length; i++) {
			System.out.print(methodArgNames[i] + "=" + String.valueOf(methodArgValues[i]));
			if (i != methodArgNames.length - 1) {
				System.out.print(",");
			}
		}
		System.out.println("] **");
	}
}
