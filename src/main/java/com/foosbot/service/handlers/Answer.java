package com.foosbot.service.handlers;

import org.eclipse.jetty.http.HttpStatus;

public class Answer {

    private int code;
    private String body;

    public Answer(int code) {
        this.code = code;
        this.body = "";
    }

    public Answer(int code, String body) {
        this.code = code;
        this.body = body;
    }

    public static Answer ok(String body) {
        return new Answer(HttpStatus.OK_200, body);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (code != answer.code) return false;
        return body != null ? body.equals(answer.body) : answer.body == null;

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

    public String getBody() {
        return body;
    }

    public int getCode() {
        return code;
    }
}