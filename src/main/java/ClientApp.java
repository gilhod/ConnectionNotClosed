import io.vertx.core.Vertx;
import io.vertx.core.http.*;

public class ClientApp {

  int port = 9000;
  String host = "localhost";
  String uri = "/hello";
  int idleTimeout = 0;
  HttpClient client;

  public ClientApp() {
    HttpClientOptions options = new HttpClientOptions();
    if (idleTimeout > 0)
      options.setIdleTimeout(idleTimeout);
    client = Vertx.vertx().createHttpClient(options);
  }

  public void run(int num) {

    client.request(HttpMethod.GET, port, host, uri, arRequest -> {
      if (arRequest.failed()) {
        System.out.println("request failure: " + arRequest.cause());
        return;
      }
      System.out.println("handle " + num);
      HttpClientRequest request = arRequest.result();
      request.connection().closeHandler(v -> {
        System.out.println("closing connection " + num);
      });
      request.response(arResponse -> {
        if (arResponse.failed()) {
          System.out.println("response failure: " + arResponse.cause());
          return;
        }
        HttpClientResponse response = arResponse.result();
        response.bodyHandler(body -> {
          System.out.println("Got body: " + body.toString());
        });
      }).send((String) null);  // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Add null body to trigger request exception
    });
  }

  public static void main(String[] args) {
    ClientApp app = new ClientApp();
    for (int i = 0; i < 10; i++) {
      app.run(i);
    }
  }
}
