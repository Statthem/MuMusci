package com.mymusic.thief;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
  
public class ConnectionTest {
	
	HttpClient httpClient;
	HttpGet request;
	
	@Before
	public void configure() {
		String url = "http://myzuka.club/Artist";

		httpClient = HttpClientBuilder.create().build();
		request = new HttpGet(url);
	}
	
	@Test
	public void ifHTTPResponse_is200_thenOk() throws ClientProtocolException, IOException {
		 HttpResponse response = this.httpClient.execute(request);
		 Assert.assertThat(response, HttpResponseMatcher.hasStatus(200));
	}

}
