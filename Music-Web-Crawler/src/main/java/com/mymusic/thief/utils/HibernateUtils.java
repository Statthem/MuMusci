package com.mymusic.thief.utils;

import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.collection.internal.PersistentBag;

import com.mymusic.thief.entities.Album;
import com.mymusic.thief.entities.Artist;
import com.mymusic.thief.entities.Song;
import com.mymusic.thief.entities.SoundTrack;

import org.hibernate.cfg.Configuration;


public class HibernateUtils {
	private static final SessionFactory concreteSessionFactory;
	static {
		try {
			Properties prop= new Properties();
			prop.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/music");
			prop.setProperty("hibernate.connection.username", "root");
			prop.setProperty("hibernate.connection.password", "root");
			prop.setProperty("driverClassName", "com.mysql.jdbc.Driver");
			prop.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
			//prop.setProperty("hibernate.hbm2ddl.auto", "update");
			prop.setProperty("hibernate.show_sql", "true");
			prop.setProperty("hibernate.format_sql","true");
			
			concreteSessionFactory = new Configuration()
		   
					.addAnnotatedClass(Album.class)
					.addAnnotatedClass(Song.class)
					.addAnnotatedClass(Artist.class)
					.addAnnotatedClass(SoundTrack.class)
					.addProperties(prop)
					.buildSessionFactory();		
			} catch (Throwable ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}
	public static Session getSession()
			throws HibernateException {
		return concreteSessionFactory.openSession();
	}
	
	public static void addArtist(Artist artist) {
		Session session = getSession();
		session.save(artist);
		
	}
	
	
	}