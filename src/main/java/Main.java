import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlet.*;

public class Main {
    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(new ApiServlet()), "/api/*");
        context.addServlet(new ServletHolder(new MoneyTransactionServlet()), "/transaction");
        context.addServlet(new ServletHolder(new RegistrationServlet()), "/registration");
        context.addServlet(new ServletHolder(new ResultServlet()), "/result");
        context.addServlet(new ServletHolder(new UtilityServlet()), "/renew");
        context.addServlet(new ServletHolder(new AllUsers()), "/users");

        Server server = new Server(8080);
        server.setHandler(context);

        server.start();
        server.join();
    }
}
