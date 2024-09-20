import Entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {

            DBContext db = new DBContext("jdbc:mysql://localhost:3306");
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/App/Users/GetUsers", (exchange -> {
                String response = "";
                if ("GET".equals(exchange.getRequestMethod())) {
                    if (!db.isDBExists("cafedb")) {
                        var t = db.dataBaseCreate();
                    }
                    db.useDB("cafedb");
                    var users = db.queryAny("Select * From Users", new User());
                    if (users == null) {
                        response = "Table has 0 rows";
                    }
                    ObjectMapper om = new ObjectMapper();
                    response = om.writeValueAsString(users);
                    exchange.sendResponseHeaders(200, response.getBytes().length);     // curl localhost:8000/App/Users/GetUsers
                    OutputStream output = exchange.getResponseBody();
                    output.write(response.getBytes());
                    output.flush();
                } else {
                    exchange.sendResponseHeaders(405, -1);
                }
                exchange.close();
            }));
            server.getExecutor();
            server.start();
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }
}