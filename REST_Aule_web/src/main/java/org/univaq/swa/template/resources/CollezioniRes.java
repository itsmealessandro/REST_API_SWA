
package org.univaq.swa.template.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.template.exceptions.RESTWebApplicationException;

@Path("aule")
public class CollezioniRes {

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String SQL_SELECT_AUTHOR = "SELECT * FROM author WHERE ID=?";

  private static Connection getPooledConnection() throws NamingException, SQLException {
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(DS_NAME);
    return ds.getConnection();
  }

  @GET
  @Path("{name: [a-zA-Z]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNamedItem(@PathParam("name") String name) throws RESTWebApplicationException {
    try {
      List<String> l = new ArrayList<>();
      try (Connection connection = getPooledConnection();
          PreparedStatement ps = connection.prepareStatement(SQL_SELECT_AUTHOR)) {
        ps.setString(1, "%" + name + "%");
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            l.add(rs.getString("surname") + " " + rs.getString("name"));
          }
        }
      }
      return Response.ok(l).build();
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }
}
