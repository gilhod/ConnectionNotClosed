import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class ServerApp {

  int port = 9000;

  public void run() {
    Vertx vertx = Vertx.vertx();
    HttpServer server = Vertx.vertx().createHttpServer();
    Router router = Router.router(vertx);
    Route route = router.route("/hello");
    route.handler(BodyHandler.create());
    route.handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.putHeader("content-type", "text/plain");
      response.end("Hello from server");
    });
    server.requestHandler(router).listen(port);
  }

  public static void main(String[] args) {
    ServerApp app = new ServerApp();
    app.run();
  }

}