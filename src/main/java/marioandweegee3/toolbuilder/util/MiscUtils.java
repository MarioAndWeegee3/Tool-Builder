package marioandweegee3.toolbuilder.util;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MiscUtils {
    @SuppressWarnings("unchecked")
    public static <key, value> Map<key, value> toMap(Object[][] objects){
        return Stream.of(objects).collect(Collectors.toMap(data -> (key) data[0], data -> (value) data[1]));
    }
}