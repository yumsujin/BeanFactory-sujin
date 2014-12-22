package com.coupang.c4.step14.beanfactory;


/*
*1. singleton instance 관리 - 생성된 bean 캐싱
*  1-1. 고려 내용을 추후 다른 scope 생성이 용이한 구조가 되도록.
* 2. 고려 내용  추후 다른 scope 생성이 용이한 구조가 되도록
* (step17의 3가지 Design Pattern에 맞게! )
* (step16에서 scope를 풀고 있으므로 그걸 참고하셔도 되요 )
*
* 이번 수업 취지는 자바문법이 아니예요.
* 자바에서 다루기 힘든 부분 위주로 하루를 다 썼는데
* C style 프로그래밍 방식을 벗어나고, OOP위주의 사고방식 방법을 알려드리고 싶었으므로
* 클래스 3개 만들어놨는데, 유연성과 확장성을 잘 고려하여 simple Bean Factory를 잘 나누고
* 추상화를 해주시면 됩니다.
*
* 감동을 받음 : 200점
* 스펙 완성 : 90점
* 일부 기능 구현 : 70점
* 하나도 못하면 : 30점
 */

/*
*1. singleton instance 관리 - 생성된 bean 캐싱
*  1-1. 고려 내용을 추후 다른 scope 생성이 용이한 구조가 되도록.
* 2. thread safe 하게 구서할 것
* 3. 계층 구조가 가능한 bean Factory
* (1-1과 관련성. 상속을 쓰라는게 아니라
* 전역으로 쓸 수 있는 빈팩토리가있고, 그 설정을 포함하여 추가할 수 있는는빈팩토리가 있다.
* )
*
 */
/*
각각의 빈팩토리들은 자신이 핸들링해야되는 빈의 생성 방법에대해서만 알면 된다.
이 중의 여러 빈팩토리가 뭘 만들수 이쓴지는 각깍의 빈팩토리에 물어보고 그걸 리턴해줄수 있는 거 ㄹ만들면 된다?
그래서 계층이 필요하다?
 */

import com.coupang.c4.ResourceUtil;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Properties;

public class SimpleBeanFactory implements BeanFactory{ // 이런 concrete 클래스가 아닌 Interface를 만들고 Interface를 기준으로 움직이는게 용이할 거예요.
	private String propertyPath;
	private HashMap<Scope, Singleton> map = new HashMap<Scope, Singleton>();

	public SimpleBeanFactory(String propertyPath){
		this.propertyPath = propertyPath;
	}
	
	public <T> Object getInstance(Singleton type) throws NoSuchMethodException {
		// TODO : 코드를 채워주세요
		Constructor constructor = type.getClass().getDeclaredConstructor();
		if (map.containsKey(type)) {
			return map.get(type);
		}

		constructor.setAccessible(true);
		Object ins = Singleton.getInstance();
		map.put(Scope.SINGLETON, (Singleton) ins);
		return ins;
	}
	
	public Object getInstance(String beanName) throws IOException, ClassNotFoundException, NoSuchMethodException {
		// TODO : 코드를 채워주세요
		Properties prop = new Properties();

		prop.load(ResourceUtil.resourceAsInputStream(propertyPath)); //properties 파일을 로드
		String clazzName = prop.getProperty(beanName); //beanName과 일치하는 값을 가져옴
		Class<?> forName = Class.forName(clazzName); //값에서 클래스이름을 추출함

		return getInstance(String.valueOf(forName)); //Overloading함수로 Instance 값을 넘겨줌
	}

}
