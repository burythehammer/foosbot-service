package com.foosbot.service.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.foosbot.service.handlers.payloads.EmptyPayload;
import com.foosbot.service.model.Model;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequestHandler<V extends Validates> implements RequestHandler<V>, Route {

    protected Model model;
    private Class<V> valueClass;

    public AbstractRequestHandler(final Class<V> valueClass, final Model model) {
        this.valueClass = valueClass;
        this.model = model;
    }


    public static String dataToJson(final Object data) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            final StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (final IOException e) {
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }

    public final Answer process(final V value, final Map<String, String> urlParams) {
        if (value != null && !value.isValid()) {
            return new Answer(HttpStatus.BAD_REQUEST_400);
        } else {
            return processImpl(value, urlParams);
        }
    }

    protected abstract Answer processImpl(V value, Map<String, String> urlParams);


    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();

        V value = null;
        if (valueClass != EmptyPayload.class) {
            value = objectMapper.readValue(request.body(), valueClass);
        }

        final Map<String, String> queryParams = new HashMap<>();
        final Answer answer = process(value, queryParams);
        response.status(answer.getCode());
        response.type("application/json");
        response.body(answer.getBody());
        return answer.getBody();
    }

}
