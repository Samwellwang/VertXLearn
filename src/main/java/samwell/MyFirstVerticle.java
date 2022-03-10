package samwell;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.Launcher;

/**
 * Created by chengen on 26/04/2017.
 */
public class MyFirstVerticle extends AbstractVerticle {
    public void start() {
//        vertx.createHttpServer().requestHandler(req -> {
//            req.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("Hello World!");
//        }).listen(8080);
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);

        server.requestHandler(routingContext -> {

            MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                    .setPort(3306)
                    .setHost("localhost")
                    .setDatabase("samwell")
                    .setUser("root")
                    .setPassword("root");
// Pool options
            PoolOptions poolOptions = new PoolOptions().setMaxSize(5);

// Create the client pool
            MySQLPool client = MySQLPool.pool(connectOptions, poolOptions);
// A simple query
            client.query("SELECT * FROM sms_his").execute(ar -> {
                        if (ar.succeeded()) {
                            RowSet<Row> result = ar.result();
                            System.out.println("Got " + result.size() + " rows ");
                        } else {
                            System.out.println("Failure: " + ar.cause().getMessage());
                        }

                        // Now close the pool
                        client.close();
                    });
            // 所有的请求都会调用这个处理器处理
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            // 写入响应并结束处理
            response.end("Hello World from Vert.x-Web!");
        });

        server.listen(8080);

    }
    public static void main(final String[] args) {
        Launcher.executeCommand("run", MyFirstVerticle.class.getName());
    }

}