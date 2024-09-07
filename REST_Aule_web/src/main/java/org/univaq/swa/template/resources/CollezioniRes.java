package org.univaq.swa.template.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;
import java.sql.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.univaq.swa.template.exceptions.RESTWebApplicationException;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("collezioni")
public class CollezioniRes {

  private static final String DS_NAME = "java:comp/env/jdbc/myaule";
  private static final String S_EVENTI_TRE_ORE = "SELECT e.*\n"
      + "FROM Evento e\n"
      + " JOIN Aula a ON e.IDAula = a.ID\n"
      + " WHERE e.oraInizio BETWEEN CURRENT_TIME() AND ADDTIME(CURRENT_TIME(), '03:00:00')\n"
      + " AND e.Data = CURDATE()\n";
  private static final String S_EVENTI_SETTIMANALI_BY_AULA = "SELECT *FROM Evento AS ev"
      + " INNER JOIN Aula AS au ON ev.IDAula = au.ID"
      + " AND au.ID = ? AND ev.Data BETWEEN ? AND ?";

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

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("a/{aulaID: [0-9]+}/c/{data}")
  public Response testData(
      @PathParam("data") String data,
      @PathParam("aulaID") int aulaID) {

    return Response.ok(data + "---" + aulaID).build();

  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Path("aule/{aulaID: [0-9]+}/settimana/{data}")
  public Response getEventoByAulaAndSettimana(
      @Context UriInfo uriInfo,
      @PathParam("aulaID") int aulaID,
      @PathParam("data") String dataString) {

    // NOTE: Ottengo la settimana dalla data passata, e da li prendo la data del
    // lunedi e della domenica per la query
    LocalDate date = LocalDate.parse(dataString);

    LocalDate inizioSettimana = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    LocalDate fineSettimana = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

    Date inizioSettimanaSql = Date.valueOf(inizioSettimana);
    Date fineSettimanaSql = Date.valueOf(fineSettimana);

    try {
      List<String> eventiList = new ArrayList<>();

      try (Connection connection = getPooledConnection();
          PreparedStatement ps = connection.prepareStatement(S_EVENTI_SETTIMANALI_BY_AULA)) {
        ps.setInt(1, aulaID);
        ps.setDate(2, inizioSettimanaSql);
        ps.setDate(3, fineSettimanaSql);
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
