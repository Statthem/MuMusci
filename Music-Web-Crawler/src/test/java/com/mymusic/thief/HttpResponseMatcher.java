package com.mymusic.thief;

import org.apache.http.HttpResponse;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class HttpResponseMatcher extends TypeSafeMatcher<HttpResponse>{

	private int statusCode;
	
	private HttpResponseMatcher(){}
	
	private HttpResponseMatcher(int statusCode){
		this.statusCode = statusCode;
	}
	
	@Override
	public void describeTo(Description description) {
		
	}

	@Override
	protected boolean matchesSafely(HttpResponse response) {
		return (response.getStatusLine().getStatusCode() == this.statusCode) ? true : false;
	}
	
 
	public static Matcher<HttpResponse> hasStatus(int statusCode) {
		return new HttpResponseMatcher(statusCode);
	}

}
