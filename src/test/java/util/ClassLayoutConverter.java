package util;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.FieldLayout;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClassLayoutConverter {
    public static Map<String, FieldLayout> converToMap(final ClassLayout layout){
        return layout.fields()
                .stream()
                .collect(Collectors.toMap(FieldLayout::name, Function.identity()));
    }
}
