# helloHibernate
> Hibernate 연습

### pom.xml
> Hiberante-core <br>
> MySQL Connector<br>
> lombok<br>
> logback<br>
<br>
~~~ 
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
### 1. hibernate.cfg.xml 파일 생성

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

### src/main/java
2. Product.java

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
