package web.ssm.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 面向切面编程AOP示例类
 */
@Aspect
@Component
public class TestAspect {

    // 切入点描述 这个是面向controller层的切入点
    @Pointcut(value = "execution(* web.ssm.controller..*(..))")
    // 签名，可以理解成这个切入点的一个名称
    public void controllerLog() {
    }

    // 在切入点的方法run之前要干的
    @Before("controllerLog()")
    public void doControllerBefore(JoinPoint joinPoint) {
        System.out.println("==========>面向切面编程AOP：Controller层切入 Before开始");
        System.out.println("方法名：" + joinPoint.getSignature().getName());
        System.out.println("==========>面向切面编程AOP：Controller层切入 Before结束");
    }

    // 切入点描述 这个是面向loginCheck方法的切入点
    @Pointcut(value = "execution(* web.ssm.service.LoginService.loginCheck(..))")
    // 签名，可以理解成这个切入点的一个名称
    public void loginCheck() {
    }

    // 在切入点的方法run之后要干的
    @After(value = "loginCheck()")
    public void doLoginCheckAfter(JoinPoint joinPoint) {
        System.out.println("==========>面向切面编程AOP：Service层loginCheck方法切入 After开始");
        System.out.println("方法名：" + joinPoint.getSignature().getName());
        System.out.println("参数值集合：" + Arrays.asList(joinPoint.getArgs()));
        System.out.println("参数值类型：" + joinPoint.getArgs()[0].getClass().getTypeName());
        System.out.println("==========>面向切面编程AOP：Service层loginCheck方法切入 After结束");
    }

    // 切入点描述 这个是面向list4Table方法的切入点
    @Pointcut(value = "execution(* web.ssm.service.UserService.list4Table(..))")
    // 签名，可以理解成这个切入点的一个名称
    public void list4Table() {
    }

    // 在切入点的方法run之后要干的
    @After(value = "list4Table()")
    public void doList4TableAfter(JoinPoint joinPoint) {
        System.out.println("==========>面向切面编程AOP：Service层list4Table方法切入 After开始");
        System.out.println("方法名：" + joinPoint.getSignature().getName());
        System.out.println("参数值集合：" + Arrays.asList(joinPoint.getArgs()));
        System.out.println("参数值类型：" + joinPoint.getArgs()[0].getClass().getTypeName());
        System.out.println("==========>面向切面编程AOP：Service层list4Table方法切入 After结束");
    }
}
