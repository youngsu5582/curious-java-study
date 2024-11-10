import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.Extracter;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {

    // 넣는 순서가 아니라, 값의 순서대로 출력이 된다.

    /**
     * if ((p = tab[i = (n - 1) & hash]) == null)
     * tab[i] = newNode(hash, key, value, null);
     * hashMap 은 `i = (n - 1) & hash` 와 같이 HASH 크기를 통해 나머지 계산을 한다.
     * 기본값은 16이다.
     */
    @Test
    @DisplayName("순서대로 값이 저장된다.")
    void order_by_int_value() {
        final Map<Integer, String> mp = new HashMap<>();
        mp.put(3, "first");
        mp.put(5, "second");
        mp.put(1, "third");
        mp.put(9, "fourth");
        mp.put(7, "fifth");
        mp.put(3, "sixth");
        mp.put(15, "last");
        assertThat(mp.keySet()).containsExactly(1, 3, 5, 7, 9, 15);
    }

    /**
     * ORDINAL 을 통해 초기화 된다.
     */
    @Test
    @DisplayName("EnumMap 내부에 배열을 가지고 있다.")
    void enum_map_have_array() {
        final Map<Sample, String> mp = new EnumMap<>(Sample.class);
        mp.put(Sample.ONE, "1");
        mp.put(Sample.TWO, "2");
        final var vals = (Object[]) Extracter.extract(mp, "vals");
        assertThat(vals).isEqualTo(new Object[]{"1","2"});
    }

    @Test
    @DisplayName("computeIfPresent 는 값이 있거나 null 이 아닐때 작동한다.")
    void computeIfPresent(){
        final Map<Integer, Integer> mp = new HashMap<>();

        mp.put(1,null);
        mp.put(2,1);

        // key,value 제공
        mp.computeIfPresent(1,(int1, int2) -> int2+1);
        mp.computeIfPresent(2,(int1, int2) -> int2+1);
        mp.computeIfPresent(3,(int1, int2) -> int2+1);
        assertThat(mp.values()).containsExactly(null,2);
    }

    @Test
    @DisplayName("computeIfAbsent 는 값이 없거나 null 일때 작동한다.")
    void computeIfAbsent(){
        final Map<Integer, Integer> mp = new HashMap<>();

        mp.put(1,null);
        mp.put(2,1);

        // key 만 제공
        mp.computeIfAbsent(1,int1 -> int1+1);
        mp.computeIfAbsent(2,int1 -> int1+1);
        mp.computeIfAbsent(3,int1 -> int1+1);
        assertThat(mp.values()).containsExactly(2,1,4);
    }
}

enum Sample {
    ONE,
    TWO
}
