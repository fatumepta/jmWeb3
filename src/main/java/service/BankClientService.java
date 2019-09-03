package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientService {
    // REFACTORED!
    public BankClientService() {
    }

    // REFACTORED!
    public BankClient getClientById(long id) throws DBException {
        try {
            return getBankClientDAO().getClientById(id);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    // REFACTORED!
    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    // REFACTORED!
    public List<BankClient> getAllClient() {
        return getBankClientDAO().getAllBankClient();
    }

    // REFACTORED!
    public boolean deleteClient(String name) {
        if (getClientByName(name) != null) {
            getBankClientDAO().deleteClient(name);
            return true;
        }

        return false;
    }

    // REFACTORED!
    public boolean validateUser(String name, String password) {
        return getBankClientDAO().validateClient(name, password);
    }

    // REFACTORED!
    public boolean addClient(BankClient client) {
        if (getClientByName(client.getName()) == null) {
            getBankClientDAO().addClient(client);
            return true;
        }

        return false;
    }

    // REFACTORED!
    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        BankClientDAO dao = getBankClientDAO();
        try {
            if (!validateUser(sender.getName(), sender.getPassword())) {
                throw new SQLException("Invalid user or password!");
            }

            BankClient receiver = dao.getClientByName(name);
            if (receiver == null) {
                throw new SQLException("There is no such client!");
            }

            dao.updateClientsMoney(sender.getName(), sender.getPassword(), -value); // withdraw
            dao.updateClientsMoney(name, receiver.getPassword(), value);    // enroll

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // REFACTORED!
    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.dropTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    // REFACTORED!
    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        try {
            dao.createTable();
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    // REFACTORED!
    private static Connection getMysqlConnection() {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.cj.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").        //db type
                    append("localhost:").           //host name
                    append("3306/").                //port
                    append("db_example?").          //db name
                    append("user=fatumepta&").      //login
                    append("password=jm&").         //password
                    append("serverTimezone=UTC");   //time zone

            System.out.println("URL: " + url + "\n");
            return DriverManager.getConnection(url.toString());
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalStateException();
        }
    }

    // REFACTORED!
    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
