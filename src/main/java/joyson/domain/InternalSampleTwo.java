package joyson.domain;

public class InternalSampleTwo {
    String s1;
    String s2;
    byte status1;       // 1 byte
    int id1;             // 4 bytes (정렬 기준으로 4 bytes 이후 시작하려면 padding 필요)
    byte status2;       // 1 byte
    int id2;
    long timestamp;     // 8 bytes (정렬 기준으로 8 bytes 이후 시작하려면 padding 필요)
}
