import joyson.domain.*;
import joyson.domain.Sample;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

import static org.assertj.core.api.Assertions.assertThat;

class InternalTest {
    @Test
    @DisplayName("정렬 기준에 따라 자동으로 최적화 한다.")
    void sort_by_align(){
        final ClassLayout sampleLayout = ClassLayout.parseClass(Sample.class);
        final ClassLayout reverseSampleLayout = ClassLayout.parseClass(RevereSample.class);
        assertThat(sampleLayout.fields()).isEqualTo(reverseSampleLayout.fields());

        final ClassLayout sampleWithObjectLayout = ClassLayout.parseClass(SampleWithObject.class);
        final ClassLayout reverseSampleWithObjectLayout = ClassLayout.parseClass(RevereSampleWithObject.class);
        assertThat(sampleWithObjectLayout.fields()).isEqualTo(reverseSampleWithObjectLayout.fields());
        assertThat(sampleWithObjectLayout.getLossesInternal())
                .isEqualTo(reverseSampleWithObjectLayout.getLossesInternal())
                .isEqualTo(3);

        final ClassLayout interSampleOneLayout = ClassLayout.parseClass(InternalSampleOne.class);
        final ClassLayout interSampleTwoLayout = ClassLayout.parseClass(InternalSampleTwo.class);
        assertThat(interSampleOneLayout.fields()).isEqualTo(interSampleTwoLayout.fields());
        assertThat(interSampleOneLayout.getLossesInternal())
                .isEqualTo(interSampleTwoLayout.getLossesInternal())
                .isEqualTo(2);
    }
}
