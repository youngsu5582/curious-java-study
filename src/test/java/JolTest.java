import joyson.SampleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.FieldLayout;
import util.ClassLayoutConverter;
import util.Extracter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class JolTest {

    /**
     * 0   8    (object header: mark)     N/A
     * 8   4    (object header: class)    N/A
     * 12   4   int Enum.ordinal              N/A
     * 16   4   java.lang.String Enum.name                 N/A
     * 20   4                    (object alignment gap)
     * Instance size: 24 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     * 8 바이트, 4 바이트, 4 바이트 4 바이트가 된다.
     * 그 후, 8 바이트(64비트) 를 맞추기 위해 4 바이트를 패딩한다.
     */
    // 4 바이트 객체 즉 (int,객체 주소) 등이 들어와도 24 바이트이다.
    @Test
    @DisplayName("ENUM 객체는 기본적으로 24바이트이다.")
    void enum_object_layout() {
        final ClassLayout layout = ClassLayout.parseClass(SampleEnum.class);
        final var fields = ClassLayoutConverter.converToMap(layout);

        final FieldLayout nameField = fields.get("name");
        assertThat(nameField.size()).isEqualTo(4);
        assertThat(nameField.typeClass()).isEqualTo(String.class.getName());

        assertThat(layout.headerSize()).isEqualTo(12);
        assertThat(layout.instanceSize()).isEqualTo(24);
        assertThat(layout.getLossesTotal()).isEqualTo(4);
    }

    /**
     * 객체 포인터는 4바이트이다.
     * 왜 4 바이트인가?
     * 64-bit JVM에서 객체 포인터를 4바이트로 압축하여 메모리 사용량을 줄이는 기술입니다.
     * Oops는 "Ordinary Object Pointers"의 약자로, Java 힙에 있는 객체를 참조하기 위한 포인터를 의미합니다.
     * java 는 포인터가 없다. 객체는 자기가 스스로의 포인터를 가진다.
     * <p>
     * 즉,JVM Heap 크기가 32GB 이상이면 OOP 가 자동으로 비활성화 된다.
     * ( 4바이트로 Heap 주소를 나타낼 수 없으므로 )
     * -XX:+UseCompressedOops : 활성화
     * -XX:+UseCompressedOops : 비활성화
     */
    @Test
    @DisplayName("객체 포인터는 4바이트이다.")
    void pointer_is_4byte() {
        final LocalDateTime localDateTime = LocalDateTime.now()
                .plusDays(1);
        final ClassLayout localDateTimeLayout = ClassLayout.parseInstance(localDateTime);
        final LocalDate date = Extracter.extract(localDateTime, "date");

        long identityHashCode = System.identityHashCode(date);
        String hashInHex = String.format("0x%08x", identityHashCode);
        String markWord = "0x00000004" + hashInHex.substring(2) + "01";
//        System.out.println("Mark Word: " + markWord);
//        System.out.println(ClassLayout.parseInstance(date).toPrintable());

        final LocalTime time = Extracter.extract(localDateTime, "time");

        final ClassLayout localDateLayout = ClassLayout.parseInstance(date);
        final ClassLayout localTimeLayout = ClassLayout.parseInstance(time);

        final var fields = ClassLayoutConverter.converToMap(localDateTimeLayout);

        assertThat(fields.get("date").size()).isEqualTo(4);
        assertThat(fields.get("time").size()).isEqualTo(4);

        assertThat(localDateLayout.instanceSize()).isEqualTo(24);
        assertThat(localTimeLayout.instanceSize()).isEqualTo(24);
    }
}
