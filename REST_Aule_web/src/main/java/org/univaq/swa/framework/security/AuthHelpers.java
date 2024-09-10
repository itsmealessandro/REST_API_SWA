package org.univaq.swa.framework.security;

import jakarta.ws.rs.core.UriInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.template.exceptions.RESTWebApplicationException;

/**
 *
 * Una classe di utilità di supporto all'autenticazione
 * qui tutto è finto, non usiamo JWT o altre tecnologie
 *
 */
public class AuthHelpers {

  private static AuthHelpers instance = null;

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String AUTH_SQL = "SELECT * FROM Amministratore WHERE username=? and password=?";

  private static Connection getPooledConnection() throws NamingException, SQLException {
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(DS_NAME);
    return ds.getConnection();
  }

  public AuthHelpers() {

  }

  public boolean authenticateUser(String username, String password) {
    try (
        // Preparo la connessione al DB
        Connection connection = getPooledConnection(); // Preparo la Query con i valori presi dalla GET
        PreparedStatement ps = connection.prepareStatement(AUTH_SQL)) {
      ps.setString(1, username);
      ps.setString(2, password);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next())
          return true;
        return false;
      }

    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }

  // Se ottengo almeno un risultato ritorno una mappa con i valori dell'oggetti

  public String issueToken(UriInfo context, String username) {
    String token = username + UUID.randomUUID().toString();
    return token;
  }

  public void revokeToken(String token) {
    /* invalidate il token */
  }

  public String validateToken(String token) {
    return "pippo"; // lo username andrebbe derivato dal token!
  }

  public static AuthHelpers getInstance() {
    if (instance == null) {
      instance = new AuthHelpers();
    }
    return instance;
  }

}
