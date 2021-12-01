package com.revature.banking.util;

import com.revature.banking.orm.utils.ConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactoryTest {

    @Test
    public void test_getConnection_returnsValidConnection_givenProvidedCredentials() {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            Assert.assertNotNull(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
