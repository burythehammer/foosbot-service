package com.foosbot.service.handlers;

import org.eclipse.jetty.http.HttpStatus;

public class Answer {

    private int code;
    private String body;

    public Answer(final int code) {
        this.code = code;
        this.body = "";
    }

    public Answer(final int code, final String body) {
        this.code = code;
        this.body = body;
    }

    static Answer ok(final String body) {
        return new Answer(HttpStatus.OK_200, body);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Answer answer = (Answer) o;

        return code == answer.code && (body != null ? body.equals(answer.body) : answer.body == null);

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Answer(code=" + code + ", body=" + body + ")";
    }

    String getBody() {
        return body;
    }

    int getCode() {
        return code;
    }
}