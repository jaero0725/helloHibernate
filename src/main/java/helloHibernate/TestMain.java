package helloHibernate;



import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

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
		
		Category category1 = new Category();
		category1.setName("컴퓨터");
		
		Category category2 = new Category();
		category2.setName("자동차");
		
		Product product1 = new Product();
		product1.setName("Notebook1");
		product1.setPrice(2000);
		product1.setDescription("Awesome notebook");
		product1.setCategory(category1);
		
		Product product2 = new Product();
		product2.setName("Notebook2");
		product2.setPrice(3000);
		product2.setDescription("Powerful notebook");
		product2.setCategory(category1);
		
		Product product3 = new Product();
		product3.setName("Sonata");
		product3.setPrice(100000);
		product3.setDescription("Popular Car");
		product3.setCategory(category2);
		
		Session session = sessionFactory.openSession(); 		//세션을 만든다.
		Transaction tx = session.beginTransaction(); 			//트랜젝션 시작
	
		session.save(product1);	
		session.save(product2);	
		session.save(product3);	
		
		product1.setCategory(null);
		session.delete(product1);
		//바로 DB에 저장되지 않음. 
		//캐시에 잇음.
		/*
		 * Serializable id1 = session.save(product1);				//id를 기억함.
		 * Product savedProduct = session.get(Product.class, id1);
		 * System.out.println("saved product " + savedProduct); //캐시에 저장된걸 읽어옴.
		 * session.save(product1);
		 */								
		
		/*
		 * Query<Product> aQuery = session.createQuery("from Product order by name",
		 * Product.class); //HQL 사용 List <Product> products = aQuery.getResultList();
		 * //조회 System.out.println(products);
		 */
		
		tx.commit(); 				//트랜젝션 commit	- 이때 DB에 저장됨. 
		session.close();			//세션을 닫음.
		sessionFactory.close();		//세션 팩토리 닫음. 
	}

}
