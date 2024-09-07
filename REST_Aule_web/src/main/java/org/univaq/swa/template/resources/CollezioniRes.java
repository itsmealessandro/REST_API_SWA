
package org.univaq.swa.template.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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

@Path("collezioni")
public class CollezioniRes {

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String S_EVENTI_TRE_ORE = "SELECT e.*\n" +
      "FROM Evento e\n" +
      " JOIN Aula a ON e.IDAula = a.ID\n" +
      " WHERE e.oraInizio BETWEEN CURRENT_TIME() AND ADDTIME(CURRENT_TIME(), '03:00:00')\n" +
      " AND e.Data = CURDATE()\n";

  private static Connection getPooledConnection() throws NamingException, SQLException {
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(DS_NAME);
    return ds.getConnection();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response testCollezioni(@Context UriInfo uriInfo) {
    ArrayList<String> eventi = new ArrayList<>();
    eventi.add(uriInfo.getBaseUriBuilder()
        .path(EventiRes.class)
        .path("1").build().toString());
    eventi.add(uriInfo.getBaseUriBuilder()
        .path(EventiRes.class)
        .path("2").build().toString());

    return Response.ok(eventi).build();
  }

  @GET
  @Path("eventiTreOre")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNamedItem(@Context UriInfo uriInfo) throws RESTWebApplicationException {
    try {
      List<String> eventiList = new ArrayList<>();
      try (Connection connection = getPooledConnection();
          PreparedStatement ps = connection.prepareStatement(S_EVENTI_TRE_ORE)) {
        try (ResultSet rs = ps.executeQuery()) {
          while (rs.next()) {
            eventiList.add(uriInfo.getBaseUriBuilder()
                .path(EventiRes.class)
                .path(Integer.toString(rs.getInt("ID")))
                .build().toString());
          }
        }
      }
      return Response.ok(eventiList).build();
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }
}
