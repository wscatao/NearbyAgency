package br.com.santander.nearagencyapi.infrastructure.configuration.geoapicontext;

import com.google.maps.GeoApiContext;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class GeoApiContextShutdown {

    /**
     * A classe GeoApiContextShutdown é responsável por garantir que o
     * GeoApiContext seja corretamente encerrado quando a aplicação
     * Spring Boot for desligada.
     * A anotação @PreDestroy é usada para marcar um
     * método que deve ser executado antes que o bean seja destruído
     * pelo contêiner Spring.
     */

    private final GeoApiContext geoApiContext;

    public GeoApiContextShutdown(GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }


    /**
     * Método shutdown: Este método é anotado com @PreDestroy,
     * o que significa que ele será chamado automaticamente pelo contêiner Spring antes de destruir o bean.
     * Dentro deste método, o geoApiContext.shutdown() é chamado para
     * liberar recursos e encerrar corretamente o contexto.
     */
    @PreDestroy
    public void shutdown() {
        geoApiContext.shutdown();
    }
}
