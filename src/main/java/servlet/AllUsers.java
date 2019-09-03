package servlet;

import service.BankClientService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AllUsers extends HttpServlet {
    private BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        out.println("<br><h3>List of clients:</h3></br>");
        service.getAllClient()
                .forEach(c -> out.printf("<br>Name: %s Pass: %s Money: %d<br>", c.getName(), c.getPassword(), c.getMoney()));

        resp.setStatus(HttpServletResponse.SC_OK);

    }
}
