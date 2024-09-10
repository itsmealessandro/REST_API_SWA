package org.univaq.swa.template.resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.univaq.swa.template.exceptions.RESTWebApplicationException;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
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

  private static final String S_EVENTI_BY_DATES = "SELECT *\n"
      + "FROM Evento\n"
      + "WHERE Data BETWEEN ? AND ?";

  private static final String S_AULE_BY_ID = "SELECT *\n"
      + " FROM Aula";

  private static final String INSERT_AULA_SQL = "INSERT INTO aula"
      + "(nome, luogo, edificio, piano, capienza, preseElettriche, preseDiRete, note, attrezzaturaID, dipartimentoID, responsabileID)"
      + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

  // Esporta eventi in csv
  @GET
  @Path("esporta/{dataInizio}/{dataFine}")
  public Response esportaEventiCSV(@Context UriInfo uriinfo,
      @PathParam("dataInizio") String dataI,
      @PathParam("dataFine") String dataF) throws IOException {
    try (Connection connection = getPooledConnection();
        PreparedStatement ps = connection.prepareStatement(S_EVENTI_BY_DATES)) {
      // NOTE: Conversione date
      LocalDate dataInizio = LocalDate.parse(dataI);
      LocalDate dataFine = LocalDate.parse(dataF);
      Date dataISql = Date.valueOf(dataInizio);
      Date dataFSql = Date.valueOf(dataFine);
      ps.setDate(1, dataISql);
      ps.setDate(2, dataFSql);

      ArrayList<Map<String, String>> listaEventi = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, String> evento = EventiRes.createEvento(rs);
          listaEventi.add(evento);
        }
        return esportaEventi(listaEventi);
      }
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }

  // Creazione del file CSV (Eventi)
  private Response esportaEventi(List<Map<String, String>> listaEventi) throws IOException {
    StringWriter csvWriter = new StringWriter();
    csvWriter.append("nome,descrizione,data,oraInizio,oraFine,tipologia,responsabileID,aulaID,corsoID\n");
    for (Map<String, String> evento : listaEventi) {
      csvWriter.append(evento.get("nome")).append(",");
      csvWriter.append(evento.get("descrizione")).append(",");
      csvWriter.append(evento.get("data")).append(",");
      csvWriter.append(evento.get("oraInizio")).append(",");
      csvWriter.append(evento.get("oraFine")).append(",");
      csvWriter.append(evento.get("tipologia")).append(",");
      csvWriter.append(evento.get("responsabileID")).append(",");
      csvWriter.append(evento.get("aulaID")).append(",");
      csvWriter.append(evento.get("corsoID")).append("\n");
    }

    String csvOutput = csvWriter.toString();

    return Response.ok(csvOutput)
        .header("Content-Disposition", "attachment; filename=eventi.csv")
        .header("Content-Type", "text/csv")
        .build();

  }

  // Esporta aule in csv
  @GET
  @Path("esporta")
  public Response esprtaAuleCSV(@Context UriInfo uriinfo) throws IOException {
    try (Connection connection = getPooledConnection();
        PreparedStatement ps = connection.prepareStatement(S_AULE_BY_ID)) {

      ArrayList<Map<String, String>> listaAule = new ArrayList<>();
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          Map<String, String> aula = AuleRes.createAula(rs);
          listaAule.add(aula);
        }
        return esportaAule(listaAule);
      }
    } catch (SQLException ex) {
      throw new RESTWebApplicationException("SQL: " + ex.getMessage());
    } catch (NamingException ex) {
      throw new RESTWebApplicationException("DB: " + ex.getMessage());
    }
  }

  // Creazione del file CSV (Aule)
  private Response esportaAule(List<Map<String, String>> listaAule) throws IOException {
    StringWriter csvWriter = new StringWriter();
    csvWriter.append(
        "nome,luogo,edificio,piano,capienza,preseElettriche,preseRete,note,attrezzaturaID,dipartimentoID,responsabileID\n");
    for (Map<String, String> aula : listaAule) {
      csvWriter.append(aula.get("nome")).append(",");
      csvWriter.append(aula.get("luogo")).append(",");
      csvWriter.append(aula.get("edificio")).append(",");
      csvWriter.append(aula.get("piano")).append(",");
      csvWriter.append(aula.get("capienza")).append(",");
      csvWriter.append(aula.get("preseElettriche")).append(",");
      csvWriter.append(aula.get("preseRete")).append(",");
      csvWriter.append(aula.get("note")).append(",");
      csvWriter.append(aula.get("attrezzaturaID")).append(",");
      csvWriter.append(aula.get("dipartimentoID")).append(",");
      csvWriter.append(aula.get("responsabileID")).append("\n");
    }

    String csvOutput = csvWriter.toString();

    return Response.ok(csvOutput)
        .header("Content-Disposition", "attachment; filename=eventi.csv")
        .header("Content-Type", "text/csv")
        .build();
  }

  @POST
  @Path("importazione")
  @Consumes("text/csv")
  public Response importaAuleCSV(@Context SecurityContext securityContext, InputStream csvFile) {
    try (Reader reader = new InputStreamReader(csvFile);
        BufferedReader bufferedReader = new BufferedReader(reader)) {

      String line;
      boolean firstLine = true; // Per saltare l'intestazione
      try (Connection connection = getPooledConnection()) {
        connection.setAutoCommit(false); // Disabilita l'auto commit per gestire la transazione

        try (PreparedStatement ps = connection.prepareStatement(INSERT_AULA_SQL)) {
          while ((line = bufferedReader.readLine()) != null) {
            if (firstLine) {
              firstLine = false; // Salta la prima riga di intestazione
              continue;
            }
            String[] values = line.split(",");

            ps.setString(1, values[0]); // Nome
            ps.setString(2, values[1]); // Luogo
            ps.setString(3, values[2]); // Edificio
            ps.setString(4, values[3]); // Piano
            ps.setInt(5, Integer.parseInt(values[4])); // Capienza
            ps.setInt(6, Integer.parseInt(values[5])); // PreseElettriche
            ps.setInt(7, Integer.parseInt(values[6])); // PreseDiRete
            ps.setString(8, values[7]); // Note
            ps.setInt(9, Integer.parseInt(values[8])); // AttrezzaturaID
            ps.setInt(10, Integer.parseInt(values[9])); // DipartimentoID
            ps.setInt(11, Integer.parseInt(values[10])); // ResponsabileID

          }

          ps.executeUpdate();
          connection.commit(); // Conferma la transazione
        } catch (SQLException ex) {
          connection.rollback(); // Rollback in caso di errore
          return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
              .entity("Errore durante l'importazione dei dati.").build();
        }
      } catch (SQLException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Errore nella connessione al database.")
            .build();
      }

      return Response.ok("Aule importate con successo.").build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST).entity("Errore durante l'importazione del file CSV.").build();
    }
  }

}
