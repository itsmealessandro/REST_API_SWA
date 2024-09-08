package org.univaq.swa.template.base;

import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.univaq.swa.framework.jackson.ObjectMapperContextResolver;
import org.univaq.swa.framework.security.AuthLoggedFilter;
import org.univaq.swa.framework.security.AuthenticationRes;
import org.univaq.swa.framework.security.CORSFilter;
import org.univaq.swa.template.resources.AuleRes;
import org.univaq.swa.template.resources.CollezioniRes;
import org.univaq.swa.template.resources.EventiRes;
import org.univaq.swa.template.exceptions.JacksonExceptionMapper;

@ApplicationPath("rest")
public class RESTApp extends Application {

  private final Set<Class<?>> classes;

  public RESTApp() {
    HashSet<Class<?>> c = new HashSet<Class<?>>();
    // aggiungiamo tutte le *root resurces* (cioè quelle
    // con l'annotazione Path) che vogliamo pubblicare
    c.add(AuleRes.class);
    c.add(CollezioniRes.class);
    c.add(EventiRes.class);

    // aggiungiamo il provider Jackson per poter
    // usare i suoi servizi di serializzazione e
    // deserializzazione JSON
    c.add(JacksonJsonProvider.class);

    c.add(AuthenticationRes.class);
    // esempio di autenticazione
    c.add(AuthLoggedFilter.class);

    // necessario se vogliamo una (de)serializzazione custom di qualche classe
    c.add(ObjectMapperContextResolver.class);

    // aggiungiamo il filtro che gestisce gli header CORS
    c.add(CORSFilter.class);

    // esempi di exception mapper, che mappano in Response eccezioni non già
    // derivanti da WebApplicationException
    c.add(JacksonExceptionMapper.class);

    classes = Collections.unmodifiableSet(c);
  }

  // l'override di questo metodo deve restituire il set
  // di classi che Jersey utilizzerà per pubblicare il
  // servizio. Tutte le altre, anche se annotate, verranno
  // IGNORATE
  @Override
  public Set<Class<?>> getClasses() {
    return classes;
  }
}
