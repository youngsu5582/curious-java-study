package joyson;

public enum SampleEnumWithValue {
    ONE(1),
    TWO(2),
    THREE(3);

    private final int value;

    SampleEnumWithValue(final int value) {
        this.value = value;
    }
}
