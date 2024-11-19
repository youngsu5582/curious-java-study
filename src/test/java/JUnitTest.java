import joyson.dto.SampleDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JUnitTest {

    /**
     * List<Integer> 은 유사하나 `IndexedObjectEnumerableAssert` 를 통해 검증한다.
     * int[] 은 `AbstractIntArrayAssert` 를 통해 검증한다.
     */
    @Test
    @DisplayName("배열 관련된 함수를 정리한다.")
    void ary_test() {
        final int[] ary = {1, 2, 3, 1};
        // 동일하게 작동한다.
//        final List<Integer> arys = List.of(1, 2, 3, 1);
        assertThat(ary)
                .isEqualTo(new int[]{1, 2, 3, 1})
                .isNotSameAs(new int[]{1, 2, 3, 1})
                .doesNotContain(5)
                .contains(2, 3)
                .containsAnyOf(1, 2, 3, 4, 5)
                .containsOnlyOnce(2, 3)
                .containsExactlyInAnyOrder(3, 2, 1, 1)
                .containsExactly(1, 2, 3, 1)
                .containsAnyOf(4, 5, 1)
                .containsSequence(2, 3);
    }

    @Test
    @DisplayName("데이터를 필터링해서 넘긴다.")
    void filtered_on() {
        final List<SampleDto> ary = List.of(
                new SampleDto(1, "first"),
                new SampleDto(2, "second"),
                new SampleDto(3, "third")
        );
        assertThat(ary)
                .filteredOn(sampleDto -> sampleDto.id() == 1)
                .hasSize(1)
                .containsExactly(new SampleDto(1, "first"));
    }

    @Nested
    @DisplayName("파라미터 테스트를 한다.")
    class ParameterizedTestClass {

        // ValueSource 는 매개변수 하나만 주입이 된다.
        @ParameterizedTest(name = "{0} 은 양수이다.")
        @DisplayName("양수 숫자 검사")
        @ValueSource(ints = {1, 2, 3})
        void some(final int x) {
            assertThat(x).isPositive();
        }

        @ParameterizedTest(name = "{1} 을 버림시, {0} 과 같다. {index} , {argumentsWithNames}")
        @DisplayName("버림 검사")
        @CsvSource(value = {
                "1, 1.1",
                "2, 2.9",
                "3, 3.5"
        })
        void some1(final double integerText, final double doubleText){
            assertThat(integerText).isEqualTo(Math.floor(doubleText));
        }
    }
}
