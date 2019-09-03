package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class UtilityServlet extends HttpServlet {
    private BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        try {
            service.cleanUp();      // drop table
            out.println("<br><h3>Table dropped!</h3></br>");
            service.createTable();  // create new table
            out.println("<br><h3>Table created!</h3></br>");

            // add clients for test
            service.addClient(new BankClient("fatumepta", "1", 1000L));
            service.addClient(new BankClient("kos", "2", 25L));
            service.addClient(new BankClient("dura4ek", "3", 0L));
            out.println("<br><h3>Clients added!</h3></br>");

            out.println("<hr><br><h3>List of clients:</h3></br>");
            service.getAllClient()
                    .forEach(c -> out.printf("<br>Name: %s Pass: %s Money: %d<br>", c.getName(), c.getPassword(), c.getMoney()));

            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
