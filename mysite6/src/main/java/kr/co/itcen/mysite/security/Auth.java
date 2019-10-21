package kr.co.itcen.mysite.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//1.Annotation은 Target의 범위를 지정해줘야한다.
//ElementType.Type : 모든 범위
//2.Retention : Annotation을 Runtime시 확인하는지 Compile시 확인하는지 지정
//3.Variable : 변수를 해당 Anntotation Interface안에 선언해주면된다. 
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auth {
   //public enum Role{USER, ADMIN};
   //public Role role() default Role.USER;
	
   public String value() default "USER";
//   public int test() default 1;
//   public String abcd() default "abcd";
}