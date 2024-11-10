package util;

import java.lang.reflect.Field;

/**
 * 자바는 기본적으로, 기본 모듈 (java.*) 을 열지 않게 막아놓음.
 *'--add-opens', 'java.base/java.util=ALL-UNNAMED' 해당 옵션을 통해 열수있게 허용해야 한다.
 */
public class Extracter {
    public static <T> T extract(final Object obj, final String key) {
        try {
            final Field field = obj.getClass().getDeclaredField(key);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException("Unable to access field: " + key, e);
        } catch (final NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + key, e);
        }
    }
}
