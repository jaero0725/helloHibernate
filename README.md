# helloHibernate
> Hibernate 연습

## Benefits of Hibernate
> 1) Hiberante provides the Object-to-Relational Mapping(ORM)
> 2) Hibernate handles all of the low-level SQL
> - JDBC 코드의 양을 최소화 해준다. 
<br>

## Application Architecture

![1](https://user-images.githubusercontent.com/55049159/111074758-ef4dbc80-8527-11eb-8d0a-220adfd652b7.png)

## Ex. Spring 없이 Java Application을 이용해서 Hibernate 사용 

#### pom.xml
> <b>의존성 추가</b>
> 1) Hibernate-core <br>
> 2) MySQL Connector<br>
> 3) lombok<br>
> 4) logback<br><br>

~~~xml
		<!-- Hibernate Dependency 추가. -->
		<!-- Hibernate-core -->
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-core</artifactId>
			<version>5.4.29.Final</version>
		</dependency>
		<!-- MySQL Connector -->
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<version>8.0.20</version>
		</dependency>

		<!-- lombok -->
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.12</version>
			<scope>provided</scope>
		</dependency>

		<!-- logback -->
		<!-- logback-classic만 넣어주면, core도 들어감 -->
		<dependency>
			<groupId>ch.qos.logback</groupId>
			<artifactId>logback-classic</artifactId>
			<version>1.2.3</version>
			<scope>runtime</scope>
		</dependency>
~~~

### src/main/resources
#### 1. hibernate.cfg.xml 파일 생성

~~~xml
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>

	<session-factory>
		<!-- JDBC Database connection settings -->
		<property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
		<property name="connection.url">jdbc:mysql://localhost:3306/testdb?useSSL=false&amp;characterEncoding=UTF-8&amp;serverTimezone=Asia/Seoul</property>
		<property name="connection.username">root</property>
		<property name="connection.password">hansung</property>
		
		<!-- Select our SQL dialect 방언..-->
		<property name="dialect">org.hibernate.dialect.MySQL8Dialect</property>
		
		<property name="show_sql">true</property>
		<property name="current_session_context_class">thread</property>
		<property name="hbm2ddl.auto">create</property>	<!-- create 테이블을 자동으로 생성해 준다.  drop : 종료되면 삭제  -->
		
		<mapping class="helloHibernate.Product"/>	<!-- mapping 해서 바로 만들어 준다.  -->
	</session-factory>
</hibernate-configuration>
~~~

#### 2. logback.xml
~~~
<?xml version="1.0" encoding="UTF-8"?>
    <configuration>
    
    	<!-- console Appender -->
    	<appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
    		<encoder>
    			<Pattern>.%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg %n
    			</Pattern>
    		</encoder>
    		<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
    			<level>TRACE</level>
    		</filter>
    	</appender>
    
    	<logger name="kr.ac.hansung.cse">
    	    <level value="DEBUG" />
    	</logger>
    	
    	<!-- org.hibernate에 대해서 자세히 보기 위해 추가. -->
    	<logger name="org.hibernate.type.descriptor.sql">
    	    <level value="Trace" />
    	</logger>
    	
    	<root>
    		<level value="INFO" />
    		<appender-ref ref="consoleAppender" />
    	</root>
    </configuration>
~~~

###  src/main/java
####  3. Product.java

~~~java
package helloHibernate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
 * Entity class : Java class that is mapped to a database table
 * 
 * Java Annotations 
 * - map class to database table
 * - map fields to database columns
 * 
 */

@Getter
@Setter
@ToString
@Entity
@Table(name="product")
public class Product {
	
	@Id							//id로 사용. primary key
	@GeneratedValue						//키를 생성할때는, 자동으로 생성한다. 
	@Column(name="product_id")				//컬럼 내용을 지정해줄 수 있다.  - 만약에 name을 지정하지 않는다면, field이름과 같아 진다. 
	private int id;
	
	private String name;
	
	private int price;
	
	private String description;
}

~~~

#### 4. TestMain.java

~~~java
package helloHibernate;



import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

//JAVA Application에서 사용하는 법. 
public class TestMain {
	
	private static SessionFactory sessionFactory;		//Spring 에서는 DI 
	
	public static void main(String[] args) {
		
		//세션 펙토리 얻어 오는 과정. 
		/*
		 * Configuration conf = new Configuration(); conf.configure();
		 * 
		 * sessionFactory = conf.buildSessionFactory();					//설정파일 명시 = Default 이름 : hibernate.cfg.xml
		 */
		
		sessionFactory = new Configuration().configure().buildSessionFactory();		//chained method 
		
		Product product1 = new Product();
		product1.setName("Notebook");
		product1.setPrice(2000);
		product1.setDescription("Awesome notebook");
		
		Session session = sessionFactory.openSession(); 		//세션을 만든다.
		Transaction tx = session.beginTransaction(); 			//트랜젝션 시작
		
		session.save(product1); 					//자동적으로 데이터베이스에 저장됨.
		
		tx.commit(); 							//트랜젝션 commit
		session.close();						//세션을 닫음.
		sessionFactory.close();						//세션 팩토리 닫음. 
		
	}

}
~~~
