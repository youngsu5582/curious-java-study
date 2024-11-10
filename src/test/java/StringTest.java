import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringTest {
    String s;

    @BeforeEach
    void setUp() {
        s = "This String";
    }

    @Test
    void equal() {
        assertThat(s).isEqualTo("This String");
    }

    @Test
    void charAt() {
        assertThat(s.charAt(0)).isEqualTo('T');
        assertThat(s.charAt(s.length() - 1)).isEqualTo('g');
    }

    @Test
    void charArray() {
        assertThat(s.toCharArray()).containsExactly('T', 'h', 'i', 's', ' ', 'S', 't', 'r', 'i', 'n', 'g');
    }

    @Test
    void split() {
        final String[] ary = s.split(" ");
        assertThat(ary).hasSize(2)
                .contains("This", "String");
    }

    @Test
    void indexOf() {
        assertThat(s.indexOf('i')).isEqualTo(2);
        assertThat(s.lastIndexOf('i')).isEqualTo(8);

    }

    @Test
    void subString() {
        final String line = "line";
        final String result = line.substring(2);
        final String result1 = line.substring(2, 3);


        assertThat(result).isEqualTo("ne");
        assertThat(result1).isEqualTo("n");
    }
}
