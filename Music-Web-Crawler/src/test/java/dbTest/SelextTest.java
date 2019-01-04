package dbTest;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.Test;

import enities.Album;
import utils.HibernateUtils;

public class SelextTest {
	
	int time = 0;
	static int staticTime = 10;
	
	public static void setTime() {
		staticTime = 20;
		
	}
	
	@Test
	public void albumCheck() {
		Session session =  HibernateUtils.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Album> query = builder.createQuery(Album.class);
		Root<Album> root = query.from(Album.class);
		query.select(root).where(builder.equal(root.get("albumTitle"), "TestAlbum1"));
		Query<Album> q = session.createQuery(query);
		List<Album> list = q.getResultList();
		System.out.println(list.size());
	}
	
	

}
