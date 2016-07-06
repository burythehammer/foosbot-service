package com.foosbot.service.handlers;

import java.util.Map;

interface RequestHandler<V extends Validates> {

    Answer process(V value, Map<String, String> urlParams);

}