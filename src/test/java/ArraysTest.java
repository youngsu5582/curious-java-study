import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ArraysTest {
    int[] ary;

    @BeforeEach
    void setUp() {
        ary = new int[]{1, 2, 4, 5, 3};
    }

    @Test
    @DisplayName("from 인덱스부터 to 인덱스 전까지 정렬한다.")
    void some() {

        Arrays.sort(ary, 3, 5);
        assertThat(ary).containsExactly(1, 2, 4, 3, 5);
    }

    @Test
    @DisplayName("원시 배열을 문자열로 출력한다.")
    void Arrays_toString() {
        final String line = Arrays.toString(ary);
        assertThat(line).isEqualTo("[1, 2, 4, 5, 3]");
    }

    @Test
    @DisplayName("정렬이 되어 있지 않으므로, 음수를 반환한다.")
    void binary_search_with_not_sort() {
        final int index = Arrays.binarySearch(ary, 3);
        assertThat(index).isNotEqualTo(4)
                .isNegative();
    }

    @Test
    @DisplayName("정렬이 되어 있으면, 올바른 값을 반환한다.")
    void binary_search_with_sort() {
        Arrays.sort(ary);
        final int index = Arrays.binarySearch(ary, 3);
        assertThat(index).isEqualTo(2);
    }

    @Test
    void not_equal_memory_address() {
        final int[] copy = Arrays.copyOf(ary, ary.length);
        assertAll(() -> {
            assertThat(copy).isEqualTo(ary);
            assertThat(copy).isNotSameAs(ary);
        });
    }

    @Test
    @DisplayName("compare 은 앞의 숫자가 크면 1, 동일하면 0,작으면 -1을 반환한다.")
    void compare() {
        // 1, 2, 4, 5, 3
        assertThat(Arrays.compare(ary, new int[]{1, 2, 3, 4, 5})).isOne();
        assertThat(Arrays.compare(ary, new int[]{1, 2, 4, 5, 3})).isZero();
        assertThat(Arrays.compare(ary, new int[]{1, 2, 5, 5, 3})).isEqualTo(-1);
    }
}
