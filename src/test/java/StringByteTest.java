import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class UtfEncodingTest {

    private final String ascii = "S";
    private final String korean = "ㄱ";

    @Test
    @DisplayName("UTF-8 인코딩에서 ASCII와 한글의 길이를 확인")
    void get_utf_8_length() {
        assertThat(ascii.getBytes(StandardCharsets.UTF_8)).hasSize(1);
        assertThat(korean.getBytes(StandardCharsets.UTF_8)).hasSize(3);
    }

    /**
     * BOM(Byte Order Mark):
     *
     * UTF-16에서는 데이터의 엔디안을 구분하기 위해 문자열의 시작에 BOM이 추가됩니다:
     * 빅 엔디안: 0xFE 0xFF.
     * 리틀 엔디안: 0xFF 0xFE.
     */
    @Test
    @DisplayName("UTF 16 에서 영어, 한글은 4바이트이다.")
    // BOM 때문에 2바이트가 추가된다.
    void get_utf_16_length() {
        assertThat(ascii.getBytes(StandardCharsets.UTF_16)).hasSize(4);
        assertThat(korean.getBytes(StandardCharsets.UTF_16)).hasSize(4);
    }

    @Test
    @DisplayName("엔디언을 지정하면 2바이트이다.")
    void get_utf_with_endian() {
        assertThat(ascii.getBytes(StandardCharsets.UTF_16BE)).hasSize(2);
        assertThat(korean.getBytes(StandardCharsets.UTF_16BE)).hasSize(2);

        assertThat(ascii.getBytes(StandardCharsets.UTF_16LE)).hasSize(2);
        assertThat(korean.getBytes(StandardCharsets.UTF_16LE)).hasSize(2);
    }
}
