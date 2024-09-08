package org.univaq.swa.template.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.template.exceptions.RESTWebApplicationException;

@Path("eventi")
public class EventiRes {

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String SQL_SELECT_EVENTO_BY_ID = "SELECT * FROM Evento WHERE ID=?";
  private static final String I_EVENTO = "INSERT INTO Evento "
      + "(IDMaster, nome, oraInizio, oraFine, descrizione, ricorrenza, Data,"
      + " dataFineRicorrenza, tipologiaEvento, IDResponsabile, IDCorso, IDAula) "
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
  private static final String U_EVENTO = "UPDATE Evento\n"
      + "SET nome = ?,\n"
      + "    oraInizio = ?,\n"
      + "    oraFine = ?,\n"
      + "    descrizione = ?,\n"
      + "    Data = ?,\n"
      + "    tipologiaEvento = ?,\n"
      + "    IDResponsabile = ?,\n"
      + "    IDCorso = ?,\n"
      + "    IDAula = ?\n"
      + "WHERE ID = ?";

  private static Connection getPooledConnection() throws NamingException, SQLException {
    InitialContext ctx = new InitialContext();
    DataSource ds = (DataSource) ctx.lookup(DS_NAME);
    return ds.getConnection();
  }

  HashMap<String, String> createEvento(ResultSet rs) throws SQLException {
    // Creazione della HashMap per memorizzare i valori dell'evento
    HashMap<String, String> evento = new HashMap<>();

    // Estrazione dei valori dal ResultSet e popolamento della HashMap
    // Estrazione dei valori dal ResultSet e popolamento della HashMap
    evento.put("ID", String.valueOf(rs.getInt("ID")));
    evento.put("IDMaster", String.valueOf(rs.getInt("IDMaster")));
    evento.put("nome", rs.getString("nome"));
    evento.put("oraInizio", rs.getString("oraInizio"));
    evento.put("oraFine", rs.getString("oraFine"));
    evento.put("descrizione", rs.getString("descrizione"));
    evento.put("ricorrenza", rs.getString("ricorrenza"));
    evento.put("Data", rs.getString("Data"));
    evento.put("dataFineRicorrenza", rs.getString("dataFineRicorrenza"));
    evento.put("tipologiaEvento", rs.getString("tipologiaEvento"));
    evento.put("IDResponsabile", String.valueOf(rs.getInt("IDResponsabile")));
    evento.put("IDCorso", String.valueOf(rs.getInt("IDCorso")));
    evento.put("IDAula", String.valueOf(rs.getInt("IDAula")));

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

  // Ritorna Un Evento dato L'ID
  @GET
  @Path("{eID:[0-9]+}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEventoByID(@PathParam("eID") int eID) throws RESTWebApplicationException {

    try {
      HashMap<String, String> e_map;
      try (
          // Preparo la connessione al DB
          Connection connection = getPooledConnection(); // Preparo la Query con i valori presi dalla GET
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

  @POST
  @Consumes("application/json")
  @Path("postTest")
  public Response testPost(
      Map<String, Object> hm) {
    return Response.ok(hm.get("a")).build();
  }

  @POST
  @Consumes("application/json")
  @Path("nuovo")
  public Response createEvento(Map<String, Object> e_map) {

    try (
        // Preparo la connessione al DB
        Connection connection = getPooledConnection();
        PreparedStatement ps = connection.prepareStatement(I_EVENTO)) {

      // NOTE: Conversione a SQL
      LocalTime oraInizioL = LocalTime.parse((String) e_map.get("oraInizio"));
      LocalTime oraFineL = LocalTime.parse((String) e_map.get("oraFine"));
      Time oraInizioSql = Time.valueOf(oraInizioL);
      Time oraFineSql = Time.valueOf(oraFineL);
      LocalDate localDate = LocalDate.parse((String) e_map.get("data"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      Date dateSql = Date.valueOf(localDate);

      ps.setNull(1, Types.NULL);
      ps.setString(2, (String) e_map.get("nome"));
      ps.setTime(3, oraInizioSql);
      ps.setTime(4, oraFineSql);
      ps.setString(5, (String) e_map.get("descrizione"));
      ps.setString(6, "NESSUNA");// ricorrenza
      ps.setDate(7, dateSql);
      ps.setNull(8, Types.NULL);// DATA FINE RICORRENZA
      ps.setString(9, (String) e_map.get("tipologia"));

      ps.setInt(10, Integer.valueOf((String) e_map.get("responsabileID")));

      ps.setInt(11, Integer.valueOf((String) e_map.get("corsoID")));

      ps.setInt(12, Integer.valueOf((String) e_map.get("aulaID")));

      ps.executeUpdate();
      return Response.ok("Operazione Riuscita").build();
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }

  @PUT
  @Consumes("application/json")
  @Path("modifica")
  public Response updateEvento(Map<String, Object> e_map) {

    try (
        // Preparo la connessione al DB
        Connection connection = getPooledConnection();
        PreparedStatement ps = connection.prepareStatement(U_EVENTO)) {

      // NOTE: Conversione a SQL
      LocalTime oraInizioL = LocalTime.parse((String) e_map.get("oraInizio"));
      LocalTime oraFineL = LocalTime.parse((String) e_map.get("oraFine"));
      Time oraInizioSql = Time.valueOf(oraInizioL);
      Time oraFineSql = Time.valueOf(oraFineL);
      LocalDate localDate = LocalDate.parse((String) e_map.get("data"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      Date dateSql = Date.valueOf(localDate);

      ps.setString(1, (String) e_map.get("nome"));
      ps.setTime(2, oraInizioSql);
      ps.setTime(3, oraFineSql);
      ps.setString(4, (String) e_map.get("descrizione"));
      ps.setDate(5, dateSql);
      ps.setString(6, (String) e_map.get("tipologia"));

      ps.setInt(7, Integer.valueOf((String) e_map.get("responsabileID")));

      ps.setInt(8, Integer.valueOf((String) e_map.get("corsoID")));

      ps.setInt(9, Integer.valueOf((String) e_map.get("aulaID")));

      ps.setInt(10, Integer.valueOf((String) e_map.get("id")));

      ps.executeUpdate();
      return Response.ok("Operazione Riuscita").build();
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }
}
