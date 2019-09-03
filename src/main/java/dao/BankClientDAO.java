package dao;

import model.BankClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {
    private Connection connection;

    // REFACTORED!
    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    // REFACTORED!
    public BankClient getClientById(long id) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bank_client WHERE id=?")) {
            stmt.setLong(1, id);
            ResultSet result = stmt.executeQuery();

            if (!result.isLast()) {  // if there are NOT only headers of the table
                result.next();
                String name = result.getString("name");
                String pass = result.getString("password");
                long money = result.getLong("money");

                return new BankClient(id, name, pass, money);
            } else {
                return null;
            }
        }
    }

    // REFACTORED!
    public BankClient getClientByName(String name) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bank_client WHERE name=?")) {
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();

            result.next();
            long id = result.getLong("id");
            long money = result.getLong("money");
            String pass = result.getString("password");

            return new BankClient(id, name, pass, money);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // REFACTORED!
    public List<BankClient> getAllBankClient() {
        List<BankClient> allClients = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bank_client")) {
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                long id = result.getLong("id");
                long money = result.getLong("money");
                String name = result.getString("name");
                String pass = result.getString("password");
                allClients.add(new BankClient(id, name, pass, money));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return allClients;
    }

    // REFACTORED!
    public void deleteClient(String name) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM bank_client WHERE name=?")) {
            stmt.setString(1, name);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // REFACTORED!
    public boolean validateClient(String name, String password) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT COUNT(*) AS val FROM bank_client WHERE name=? AND password=?")) {
            stmt.setString(1, name);
            stmt.setString(2, password);
            ResultSet result = stmt.executeQuery();

            result.next();
            return result.getInt("val") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // REFACTORED!
    public void addClient(BankClient client) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO bank_client (name, password, money) VALUES (?, ?, ?)")) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getPassword());
            stmt.setLong(3, client.getMoney());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // REFACTORED!
    public void updateClientsMoney(String name, String password, long transactValue) throws SQLException {
        if (transactValue < 0 && isClientHasSum(name, Math.abs(transactValue))) {
            withdraw(name, password, Math.abs(transactValue));
        } else if (transactValue > 0) {
            enrollment(name, password, transactValue);
        } else
            throw new SQLException("insufficient funds");
    }

    private void withdraw(String name, String password, long toWithdraw) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE bank_client SET money = money - ? WHERE name=? AND password=?")) {
            stmt.setLong(1, toWithdraw);
            stmt.setString(2, name);
            stmt.setString(3, password);

            stmt.executeUpdate();
        }
    }

    private void enrollment(String name, String password, long toWithdraw) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE bank_client SET money = money + ? WHERE name=? AND password=?")) {
            stmt.setLong(1, toWithdraw);
            stmt.setString(2, name);
            stmt.setString(3, password);

            stmt.executeUpdate();
        }
    }

    // REFACTORED!
    public boolean isClientHasSum(String name, long expectedSum) throws SQLException {
        BankClient client = getClientByName(name);  // existence of clients checked in getClientByName()
        return client.getMoney() >= expectedSum;
    }

    // REFACTORED!
    public long getClientIdByName(String name) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM bank_client WHERE NAME=?")) {
            stmt.setString(1, name);
            ResultSet result = stmt.executeQuery();

            result.next();
            return result.getLong(1);
        }
    }

    // REFACTORED!
    public void createTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("create table if not exists bank_client (id bigint auto_increment, name varchar(256), password varchar(256), money bigint, primary key (id))");
        stmt.close();
    }

    // REFACTORED!
    public void dropTable() throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE IF EXISTS bank_client");
        stmt.close();
    }
}
