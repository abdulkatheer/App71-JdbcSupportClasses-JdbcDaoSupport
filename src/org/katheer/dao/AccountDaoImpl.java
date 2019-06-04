package org.katheer.dao;

import org.katheer.dto.Account;
import org.katheer.mapper.AccountRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("accountDao")
public class AccountDaoImpl extends JdbcDaoSupport implements AccountDao {
    private String query;

    @Override
    public int createAccount(Account account) {
        try {
            query = "INSERT INTO account(name, mobile, email, branch, " +
                    "balance) VALUES(?, ?, ?, ?, ?)";

            getJdbcTemplate().update(query, new Object[] {account.getName(),
                    account.getMobile(), account.getEmail(),
                    account.getBranch(), account.getBalance()});
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

        //returning account number to user
        query = "SELECT * FROM account WHERE name=? AND mobile=? " +
                "AND email=?";

        return getJdbcTemplate().query(query, new Object[]{account.getName(),
                        account.getMobile(), account.getEmail()},
                new AccountRowMapper()).get(0).getAccNo();
    }

    @Override
    public Account getAccount(int accNo) {
        Account account = null;
        try {
            query = "SELECT * FROM account WHERE accNo=?";

            List<Account> accounts = getJdbcTemplate().query(query,
                    new Object[] {accNo}, new AccountRowMapper());
            account = accounts.size() == 0 ? null : accounts.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return account;
        }
        return account;
    }

    @Override
    public double deposit(int accNo, double amount) {
        try {
            query = "UPDATE account SET balance=? WHERE accNo=?";

            int rowCount = getJdbcTemplate().update(query, new Object[] {amount,
                    accNo});

            if (rowCount == 1) {
                return amount;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public double withdraw(int accNo, double amount) {
        try {
            query = "UPDATE account SET balance=? WHERE accNo=?";

            int rowCount = getJdbcTemplate().update(query, new Object[]{amount,
                    accNo});

            if (rowCount == 1) {
                return amount;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
