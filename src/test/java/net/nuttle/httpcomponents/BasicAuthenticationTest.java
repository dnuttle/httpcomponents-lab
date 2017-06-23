package net.nuttle.httpcomponents;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;

public class BasicAuthenticationTest {

  @Test
  /*
   * The following (as can be seen if logging is enabled) will show that first
   * a GET is sent, followed by negotiation for a method of authentication,
   * and then a second request, this time with basic authentication
   */
  public void testBasicAuthenntication() throws ClientProtocolException, IOException {
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = 
        new UsernamePasswordCredentials("user1", "userpassword");
    //AuthScoppe represents an authentication scope consisting of a host name,
    //a port number, a realm name and an authentication scheme name.
    provider.setCredentials(AuthScope.ANY, credentials);
    HttpClient client = HttpClientBuilder.create()
        .setDefaultCredentialsProvider(provider)
        .build();
    HttpResponse response = client.execute(new HttpGet("https://www.google.com"));
    int statusCode = response.getStatusLine().getStatusCode();
    Assert.assertEquals(HttpStatus.SC_OK, statusCode);
  }

  @Test
  /*
   * The following ensures that there is no negotiation over authentication.
   * It's preemptively basic authentication
   */
  public void testPreemptiveBasicAuthenntication() throws IOException {
    HttpHost targetHost = new HttpHost("www.google.com", 80, "https");
    CredentialsProvider provider = new BasicCredentialsProvider();
    UsernamePasswordCredentials credentials = 
        new UsernamePasswordCredentials("user1", "userpassword");
    provider.setCredentials(AuthScope.ANY, credentials);
    AuthCache authCache = new BasicAuthCache();
    authCache.put(targetHost, new BasicScheme());
    
    final HttpClientContext context = HttpClientContext.create();
    context.setCredentialsProvider(provider);
    context.setAuthCache(authCache);
    
    HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(new HttpGet("https://www.google.com"), context);
    int statusCode = response.getStatusLine().getStatusCode();
    Assert.assertEquals(HttpStatus.SC_OK, statusCode);
  }
  
  /*
   * Preemptive basic authentication but using raw http header
   * instead of logic in httpcomponents
   */
  @Test
  public void testRawHttpHeaders() throws IOException {
    HttpGet request = new HttpGet("https://www.google.com");
    String auth = "user:password";
    byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("ISO-8859-1")));
    String authHeader = "Basic " + new String(encodedAuth);
    request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
    HttpClient client = HttpClientBuilder.create().build();
    HttpResponse response = client.execute(request);
    int statusCode = response.getStatusLine().getStatusCode();
    Assert.assertEquals(HttpStatus.SC_OK, statusCode);
  }
}
