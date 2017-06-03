package net.nuttle.httpcomponents;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

/**
 * The LabApplication must be running before these tests are called.
 * @author Dan
 *
 */
public class SimpleTest {

  private static final String HOST = "http://localhost:8080";
  
  @Test
  public void get() throws Exception {
    HttpClient client = HttpClientBuilder.create()
      .build();
    HttpGet get = new HttpGet(HOST + "/testgetpost");
    HttpResponse resp = client.execute(get);
    assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
  }

  @Test
  public void post() throws Exception {
    HttpClient client = HttpClientBuilder.create()
      .build();
    HttpPost post = new HttpPost(HOST + "/testgetpost");
    HttpResponse resp = client.execute(post);
    assertThat(resp.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
  }
  
  /**
   * /testgetpost defines GET and POST; HEAD is also added on.
   * @throws Exception
   */
  @Test
  public void methodsSupported() throws Exception {
    HttpClient client = HttpClientBuilder.create().build();
    HttpOptions options = new HttpOptions(HOST + "/testgetpost");
    HttpResponse resp = client.execute(options);
    Set<String> methods = options.getAllowedMethods(resp);
    List<String> expected = new ArrayList<>();
    expected.add("HEAD");
    expected.add("GET");
    expected.add("POST");
    assertThat(methods).containsAll(expected);
    assertThat(methods.size()).isEqualTo(3);
  }
  
  /**
   * /testgetonly supports only HEAD and GET
   */
  @Test
  public void getSupported() throws Exception {
    HttpClient client = HttpClientBuilder.create().build();
    HttpOptions options = new HttpOptions(HOST + "/testget");
    HttpResponse resp = client.execute(options);
    Set<String> methods = options.getAllowedMethods(resp);
    List<String> expected = new ArrayList<>();
    expected.add("HEAD");
    expected.add("GET");
    assertThat(methods.containsAll(expected));
    assertThat(methods.size()).isEqualTo(2);
  }
}
