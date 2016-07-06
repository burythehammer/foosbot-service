package com.foosbot.service.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.foosbot.service.model.Model;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRequestHandler<V extends Validable> implements RequestHandler<V>, Route {

    private Class<V> valueClass;
    protected Model model;

    private static final int HTTP_BAD_REQUEST = 400;

    public AbstractRequestHandler(final Class<V> valueClass, final Model model) {
        this.valueClass = valueClass;
        this.model = model;
    }

    private static boolean shouldReturnHtml(final Request request) {
        final String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
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

    public final Answer process(final V value, final Map<String, String> queryParams, final boolean shouldReturnHtml) {
        if (!value.isValid()) {
            return new Answer(HTTP_BAD_REQUEST);
        } else {
            return processImpl(value, queryParams, shouldReturnHtml);
        }
    }

    protected abstract Answer processImpl(V value, Map<String, String> queryParams, boolean shouldReturnHtml);


    @Override
    public Object handle(final Request request, final Response response) throws Exception {
        final ObjectMapper objectMapper = new ObjectMapper();
        final V value = objectMapper.readValue(request.body(), valueClass);
        final Map<String, String> queryParams = new HashMap<>();
        final Answer answer = process(value, queryParams, shouldReturnHtml(request));
        response.status(answer.getCode());
        if (shouldReturnHtml(request)) {
            response.type("text/html");
        } else {
            response.type("application/json");
        }
        response.body(answer.getBody());
        return answer.getBody();
    }

}
