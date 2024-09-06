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
import java.util.HashMap;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.template.exceptions.RESTWebApplicationException;

@Path("eventi")
public class EventiRes {

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String SQL_SELECT_AUTHOR = "SELECT * FROM author WHERE ID=?";
  private static final String SQL_SELECT_EVENTO_BY_ID = "SELECT * FROM Evento WHERE ID=?";

  private static Connection getPooledConnection() throws NamingException, SQLException {
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(DS_NAME);
    return ds.getConnection();
  }

  private HashMap<String, String> createEvento(ResultSet rs) throws SQLException {
    // Creazione della HashMap per memorizzare i valori dell'evento
    HashMap<String, String> evento = new HashMap<>();

    // Estrazione dei valori dal ResultSet e popolamento della HashMap
    evento.put("ID", String.valueOf(rs.getInt("ID")));
    evento.put("nome", rs.getString("nome"));
    evento.put("luogo", rs.getString("luogo"));
    evento.put("edificio", rs.getString("edificio"));
    evento.put("piano", String.valueOf(rs.getInt("piano")));
    evento.put("capienza", String.valueOf(rs.getInt("capienza")));
    evento.put("preseElettriche", String.valueOf(rs.getInt("preseElettriche")));
    evento.put("preseRete", String.valueOf(rs.getInt("preseRete")));
    evento.put("note", rs.getString("note"));
    evento.put("IDAttrezzatura", String.valueOf(rs.getInt("IDAttrezzatura")));
    evento.put("IDDipartimento", String.valueOf(rs.getInt("IDDipartimento")));
    evento.put("IDResponsabile", String.valueOf(rs.getInt("IDResponsabile")));

    // Restituzione della HashMap popolata
    return evento;
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response testEventi() {
    HashMap<String, String> testMap = new HashMap<>();
    testMap.put("myTest", "eventoTest");

    return Response.ok(testMap).build();
  }

  @GET
  @Path("{eID:[0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEventoByID(@PathParam("eID") int eID) throws RESTWebApplicationException {

    try {
      HashMap<String, String> e_map;
      try (
          // Preparo la connessione al DB
          Connection connection = getPooledConnection();
          // Preparo la Query con i valori presi dalla GET
          PreparedStatement ps = connection.prepareStatement(SQL_SELECT_EVENTO_BY_ID)) {
        ps.setInt(1, eID);
        try (ResultSet rs = ps.executeQuery()) {
          // Se ottengo almeno un risultato ritorno una mappa con i valori dell'oggetti
          if (rs.next()) {
            e_map = createEvento(rs);
            return Response.ok(e_map).build();
          } // Altrimenti ritorno non trovato
          return Response.status(Response.Status.NOT_FOUND).build();

        }

      }

    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }

  }

  /*
   * @GET
   * 
   * @Path("{name: [a-zA-Z]+}")
   * 
   * @Produces(MediaType.APPLICATION_JSON)
   * public Response getNamedItem(@PathParam("name") String name) throws
   * RESTWebApplicationException {
   * try {
   * List<String> l = new ArrayList<>();
   * try (Connection connection = getPooledConnection();
   * PreparedStatement ps = connection.prepareStatement(SQL_SELECT_AUTHOR)) {
   * ps.setString(1, "%" + name + "%");
   * try (ResultSet rs = ps.executeQuery()) {
   * while (rs.next()) {
   * l.add(rs.getString("surname") + " " + rs.getString("name"));
   * }
   * }
   * }
   * return Response.ok(l).build();
   * } catch (SQLException ex) {
   * throw new RESTWebApplicationException("SQL: " + ex.getMessage());
   * } catch (NamingException ex) {
   * throw new RESTWebApplicationException("DB: " + ex.getMessage());
   * }
   * }
   */
}
