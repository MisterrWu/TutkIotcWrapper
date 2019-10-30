package com.tutk.IOTC;

public class Packet {
    public static int byteArrayToInt_Big(byte[] paramArrayOfByte) {
        int j;
        if (paramArrayOfByte.length == 1)
            j = 0xFF & paramArrayOfByte[0];
        int i;
        do {
            // return j;
            if (paramArrayOfByte.length == 2)
                return (0xFF & paramArrayOfByte[0]) << 8 | 0xFF & paramArrayOfByte[1];
            i = paramArrayOfByte.length;
            j = 0;
        }
        while (i != 4);
        return (0xFF & paramArrayOfByte[0]) << 24 | (0xFF & paramArrayOfByte[1]) << 16 | (0xFF & paramArrayOfByte[2]) << 8 | 0xFF & paramArrayOfByte[3];
    }

    public static int byteArrayToInt_Little(byte[] paramArrayOfByte) {
        int j;
        if (paramArrayOfByte.length == 1)
            j = 0xFF & paramArrayOfByte[0];
        int i;
        do {
            // return j;
            if (paramArrayOfByte.length == 2)
                return 0xFF & paramArrayOfByte[0] | (0xFF & paramArrayOfByte[1]) << 8;
            i = paramArrayOfByte.length;
            j = 0;
        }
        while (i != 4);
        return 0xFF & paramArrayOfByte[0] | (0xFF & paramArrayOfByte[1]) << 8 | (0xFF & paramArrayOfByte[2]) << 16 | (0xFF & paramArrayOfByte[3]) << 24;
    }

    public static int byteArrayToInt_Little(byte[] paramArrayOfByte, int paramInt) {
        return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
    }

    public static long byteArrayToLong_Little(byte[] paramArrayOfByte, int paramInt) {
        return (long)0xFF & paramArrayOfByte[paramInt] | ((long)0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | ((long)0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | ((long)0xFF & paramArrayOfByte[(paramInt + 3)]) << 24 ;
    }

    public static short byteArrayToShort_Little(byte[] paramArrayOfByte, int paramInt) {
        return (short) (0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8);
    }

    public static byte[] intToByteArray_Big(int paramInt) {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte) (paramInt >>> 24));
        arrayOfByte[1] = ((byte) (paramInt >>> 16));
        arrayOfByte[2] = ((byte) (paramInt >>> 8));
        arrayOfByte[3] = ((byte) paramInt);
        return arrayOfByte;
    }

    public static byte[] intToByteArray_Little(int paramInt) {
        byte[] arrayOfByte = new byte[4];
        arrayOfByte[0] = ((byte) paramInt);
        arrayOfByte[1] = ((byte) (paramInt >>> 8));
        arrayOfByte[2] = ((byte) (paramInt >>> 16));
        arrayOfByte[3] = ((byte) (paramInt >>> 24));
        return arrayOfByte;
    }

    public static byte[] longToByteArray_Little(long paramLong) {
        byte[] arrayOfByte = new byte[8];
        arrayOfByte[0] = ((byte) (int) paramLong);
        arrayOfByte[1] = ((byte) (int) (paramLong >>> 8));
        arrayOfByte[2] = ((byte) (int) (paramLong >>> 16));
        arrayOfByte[3] = ((byte) (int) (paramLong >>> 24));
        arrayOfByte[4] = ((byte) (int) (paramLong >>> 32));
        arrayOfByte[5] = ((byte) (int) (paramLong >>> 40));
        arrayOfByte[6] = ((byte) (int) (paramLong >>> 48));
        arrayOfByte[7] = ((byte) (int) (paramLong >>> 56));
        return arrayOfByte;
    }

    public static byte[] shortToByteArray_Big(short paramShort) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[0] = ((byte) (paramShort >>> 8));
        arrayOfByte[1] = ((byte) paramShort);
        return arrayOfByte;
    }

    public static byte[] shortToByteArray_Little(short paramShort) {
        byte[] arrayOfByte = new byte[2];
        arrayOfByte[0] = ((byte) paramShort);
        arrayOfByte[1] = ((byte) (paramShort >>> 8));
        return arrayOfByte;
    }

    public static int byteArrayToInt_Little(byte[] paramArrayOfByte, int paramInt,int move) {
        int result = 0xFF & paramArrayOfByte[paramInt];
        for (int i = 1; i < move; i++) {
            result = result | (0xFF & paramArrayOfByte[(paramInt + i)]) << i*8;
        }
        return result;
    }
}