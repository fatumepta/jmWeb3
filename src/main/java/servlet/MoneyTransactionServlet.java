package servlet;

import exception.DBException;
import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class MoneyTransactionServlet extends HttpServlet {

    private BankClientService bankClientService = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().println(
                PageGenerator.getInstance().getPage("moneyTransactionPage.html", null));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        Map<String, Object> messages = new HashMap<>();

        // prepared sender data
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        long count = Long.parseLong(req.getParameter("count"));
        // client-sender
        BankClient sender = new BankClient(senderName, senderPass, count);
        // client-receiver (we getting ONLY the name from the form)
        String receiverName = req.getParameter("nameTo");

        if (bankClientService.sendMoneyToClient(sender, receiverName, count)) {
            messages.put("message", "The transaction was successful");
        } else {
            messages.put("message", "transaction rejected");
        }

        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", messages));
        resp.setStatus(HttpServletResponse.SC_OK);


    }
}
