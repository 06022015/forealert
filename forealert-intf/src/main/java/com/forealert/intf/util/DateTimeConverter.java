package com.forealert.intf.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 9/10/17
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class DateTimeConverter implements JsonSerializer<Date>, JsonDeserializer {

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(date.toString());
    }

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return new Date(jsonElement.getAsLong());
    }
}
