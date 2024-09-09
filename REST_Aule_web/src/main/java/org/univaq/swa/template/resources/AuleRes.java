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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.univaq.swa.template.exceptions.RESTWebApplicationException;
import static org.univaq.swa.template.resources.EventiRes.createEvento;

@Path("aule")
public class AuleRes {

    private static final String DS_NAME = "java:comp/env/jdbc/myaule";
    private static final String SQL_SELECT_AULA_BY_ID = "SELECT * FROM Aula WHERE ID=?";
    private static final String SQL_SELECT_AUTHOR = "SELECT * FROM author WHERE ID=?";
    private static final String I_AULA = "INSERT INTO Aula "
            + "(nome, luogo, edificio, piano, capienza, preseElettriche,"
            + " preseRete, IDAttrezzatura, IDDipartimento, IDResponsabile ) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String U_AULA = "UPDATE Aula\n"
            + "SET IDDipartimento = ?\n"
            + "WHERE ID = ?";

    private static Connection getPooledConnection() throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup(DS_NAME);
        return ds.getConnection();
    }

    static HashMap<String, String> createAula(ResultSet rs) throws SQLException {
        // Creazione della HashMap per memorizzare i valori dell'aula
        HashMap<String, String> aula = new HashMap<>();

        // Estrazione dei valori dal ResultSet e popolamento della HashMap
        aula.put("ID", String.valueOf(rs.getInt("ID")));
        aula.put("nome", rs.getString("nome"));
        aula.put("edificio", rs.getString("edificio"));
        aula.put("luogo", rs.getString("luogo"));
        aula.put("piano", rs.getString("piano"));
        aula.put("capienza", String.valueOf(rs.getInt("capienza")));
        aula.put("preseElettriche", String.valueOf(rs.getInt("preseElettriche")));
        aula.put("preseRete", String.valueOf(rs.getInt("preseRete")));
        aula.put("attrezzaturaID", String.valueOf(rs.getInt("attrezzaturaID")));
        aula.put("dipartimentoID", String.valueOf(rs.getInt("dipartimentoID")));
        aula.put("responsabileID", String.valueOf(rs.getInt("responsabileID")));

        // Restituzione della HashMap popolata
        return aula;
    }

    @POST
    @Consumes("application/json")
    @Path("nuovo")
    public Response createAula(Map<String, Object> a_map) {

        try (
                // Preparo la connessione al DB
                Connection connection = getPooledConnection(); PreparedStatement ps = connection.prepareStatement(I_AULA)) {

            ps.setNull(1, Types.NULL);
            ps.setString(2, (String) a_map.get("nome"));
            ps.setString(3, (String) a_map.get("luogo"));
            ps.setString(4, (String) a_map.get("edificio"));
            ps.setString(5, (String) a_map.get("piano"));
            ps.setInt(6, Integer.valueOf((String) a_map.get("capienza")));
            ps.setInt(7, Integer.valueOf((String) a_map.get("preseElettriche")));
            ps.setInt(8, Integer.valueOf((String) a_map.get("preseRete")));
            ps.setInt(6, Integer.valueOf((String) a_map.get("attrezzaturaID")));
            ps.setInt(7, Integer.valueOf((String) a_map.get("dipartimentoID")));
            ps.setInt(8, Integer.valueOf((String) a_map.get("responsabileID")));

            ps.executeUpdate();
            return Response.ok("Operazione Riuscita").build();
        } catch (SQLException ex) {
            throw new RESTWebApplicationException("SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            throw new RESTWebApplicationException("DB: " + ex.getMessage());
        }
    }

    // Ritorna Un'Aula dato L'ID
    @GET
    @Path("{aID: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAulaByID(@PathParam("aID") int aID) throws RESTWebApplicationException {
        try {
            HashMap<String, String> a_map;
            try (
                    // Preparo la connessione al DB
                    Connection connection = getPooledConnection(); // Preparo la Query con i valori presi dalla GET
                     PreparedStatement ps = connection.prepareStatement(SQL_SELECT_AULA_BY_ID)) {
                ps.setInt(1, aID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        a_map = createEvento(rs);
                        return Response.ok(a_map).build();
                    }
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        } catch (SQLException ex) {
            throw new RESTWebApplicationException("SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            throw new RESTWebApplicationException("DB: " + ex.getMessage());
        }
    }

    @PUT
    @Consumes("application/json")
    @Path("modifica")
    public Response updateAula(Map<String, Object> a_map) {

        try (
                // Preparo la connessione al DB
                Connection connection = getPooledConnection(); PreparedStatement ps = connection.prepareStatement(U_AULA)) {

            ps.setInt(7, Integer.valueOf((String) a_map.get("dipartimentoID")));

            ps.executeUpdate();
            return Response.ok("Operazione Riuscita").build();
        } catch (SQLException ex) {
            throw new RESTWebApplicationException("SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            throw new RESTWebApplicationException("DB: " + ex.getMessage());
        }
    }

    //Ritorna L'attrezzatura presente in un'Aula dato L'ID dell'Aula
    @GET
    @Path("{aID: [0-9]+}/attrezzature")
    @Produces(MediaType.APPLICATION_JSON)
    public Response attrezzatureAula(@PathParam("aulaID") int aID) throws RESTWebApplicationException {
        try (
                Connection connection = getPooledConnection(); // Preparo la Query con i valori presi dalla GET
                 PreparedStatement ps = connection.prepareStatement(SQL_SELECT_AULA_BY_ID)) {
            ps.setInt(1, aID);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Map<String, Object> attrezzature = new LinkedHashMap<>();
                    attrezzature.put("attrezzature", rs.getString("attrezzature"));
                    return Response.ok(attrezzature).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).build();
                }
            }
        } catch (SQLException ex) {
            throw new RESTWebApplicationException("SQL: " + ex.getMessage());
        } catch (NamingException ex) {
            throw new RESTWebApplicationException("DB: " + ex.getMessage());
        }
    }
    
}
