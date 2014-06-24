public final class Base64
{
  private static final byte[] BASE64ALPHABET = new byte[1024];
  private static final int BASELENGTH = 128;
  private static final int EIGHTBIT = 8;
  private static final int FOURBYTE = 4;
  private static final char[] LOOKUPBASE64ALPHABET = new char[64];
  private static final int LOOKUPLENGTH = 64;
  private static final char PAD = '=';
  private static final int SIGN = -128;
  private static final int SIXTEENBIT = 16;
  private static final int TWENTYFOURBITGROUP = 24;
  
  static
  {
    int i = 0;
    int j;
    label27:
    int k;
    label36:
    int m;
    label45:
    int n;
    label70:
    int i1;
    int i2;
    label84:
    int i3;
    if (i >= 128)
    {
      j = 90;
      if (j >= 65) {
        break label134;
      }
      k = 122;
      if (k >= 97) {
        break label150;
      }
      m = 57;
      if (m >= 48) {
        break label169;
      }
      BASE64ALPHABET[43] = 62;
      BASE64ALPHABET[47] = 63;
      n = 0;
      if (n <= 25) {
        break label188;
      }
      i1 = 26;
      i2 = 0;
      if (i1 <= 51) {
        break label206;
      }
      i3 = 52;
    }
    for (int i4 = 0;; i4++)
    {
      if (i3 > 61)
      {
        LOOKUPBASE64ALPHABET[62] = '+';
        LOOKUPBASE64ALPHABET[63] = '/';
        return;
        BASE64ALPHABET[i] = -1;
        i++;
        break;
        label134:
        BASE64ALPHABET[j] = ((byte)(j - 65));
        j--;
        break label27;
        label150:
        BASE64ALPHABET[k] = ((byte)(26 + (k - 97)));
        k--;
        break label36;
        label169:
        BASE64ALPHABET[m] = ((byte)(52 + (m - 48)));
        m--;
        break label45;
        label188:
        LOOKUPBASE64ALPHABET[n] = ((char)(n + 65));
        n++;
        break label70;
        label206:
        LOOKUPBASE64ALPHABET[i1] = ((char)(i2 + 97));
        i1++;
        i2++;
        break label84;
      }
      LOOKUPBASE64ALPHABET[i3] = ((char)(i4 + 48));
      i3++;
    }
  }
  
  public static byte[] decode(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    char[] arrayOfChar = paramString.toCharArray();
    int i = removeWhiteSpace(arrayOfChar);
    if (i % 4 != 0) {
      return null;
    }
    int j = i / 4;
    if (j == 0) {
      return new byte[0];
    }
    ((byte[])null);
    int k = 0;
    byte[] arrayOfByte1 = new byte[j * 3];
    int m = 0;
    char c5;
    int i12;
    char c6;
    label194:
    label196:
    int i10;
    for (int n = 0;; n = i10)
    {
      int i1 = j - 1;
      if (k >= i1)
      {
        int i11 = m + 1;
        c5 = arrayOfChar[m];
        if (isData(c5))
        {
          i12 = i11 + 1;
          c6 = arrayOfChar[i11];
          if (isData(c6)) {
            break;
          }
        }
        return null;
      }
      int i2 = m + 1;
      char c1 = arrayOfChar[m];
      char c2;
      char c3;
      char c4;
      if (isData(c1))
      {
        m = i2 + 1;
        c2 = arrayOfChar[i2];
        if (isData(c2))
        {
          int i3 = m + 1;
          c3 = arrayOfChar[m];
          if (!isData(c3)) {
            break label194;
          }
          m = i3 + 1;
          c4 = arrayOfChar[i3];
          if (isData(c4)) {
            break label196;
          }
        }
      }
      return null;
      int i4 = BASE64ALPHABET[c1];
      int i5 = BASE64ALPHABET[c2];
      int i6 = BASE64ALPHABET[c3];
      int i7 = BASE64ALPHABET[c4];
      int i8 = n + 1;
      arrayOfByte1[n] = ((byte)(i4 << 2 | i5 >> 4));
      int i9 = i8 + 1;
      arrayOfByte1[i8] = ((byte)((i5 & 0xF) << 4 | 0xF & i6 >> 2));
      i10 = i9 + 1;
      arrayOfByte1[i9] = ((byte)(i7 | i6 << 6));
      k++;
    }
    int i13 = BASE64ALPHABET[c5];
    int i14 = BASE64ALPHABET[c6];
    int i15 = i12 + 1;
    char c7 = arrayOfChar[i12];
    (i15 + 1);
    char c8 = arrayOfChar[i15];
    if ((!isData(c7)) || (!isData(c8)))
    {
      if ((isPad(c7)) && (isPad(c8)))
      {
        if ((i14 & 0xF) != 0) {
          return null;
        }
        byte[] arrayOfByte3 = new byte[1 + k * 3];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, k * 3);
        arrayOfByte3[n] = ((byte)(i13 << 2 | i14 >> 4));
        return arrayOfByte3;
      }
      if ((!isPad(c7)) && (isPad(c8)))
      {
        int i16 = BASE64ALPHABET[c7];
        if ((i16 & 0x3) != 0) {
          return null;
        }
        byte[] arrayOfByte2 = new byte[2 + k * 3];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, k * 3);
        int i17 = n + 1;
        arrayOfByte2[n] = ((byte)(i13 << 2 | i14 >> 4));
        arrayOfByte2[i17] = ((byte)((i14 & 0xF) << 4 | 0xF & i16 >> 2));
        return arrayOfByte2;
      }
      return null;
    }
    int i18 = BASE64ALPHABET[c7];
    int i19 = BASE64ALPHABET[c8];
    int i20 = n + 1;
    arrayOfByte1[n] = ((byte)(i13 << 2 | i14 >> 4));
    int i21 = i20 + 1;
    arrayOfByte1[i20] = ((byte)((i14 & 0xF) << 4 | 0xF & i18 >> 2));
    (i21 + 1);
    arrayOfByte1[i21] = ((byte)(i19 | i18 << 6));
    return arrayOfByte1;
  }
  
  public static String encode(byte[] paramArrayOfByte)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    int i = 8 * paramArrayOfByte.length;
    if (i == 0) {
      return "";
    }
    int j = i % 24;
    int k = i / 24;
    int m;
    char[] arrayOfChar;
    int n;
    int i1;
    int i2;
    int i26;
    int i27;
    if (j != 0)
    {
      m = k + 1;
      ((char[])null);
      arrayOfChar = new char[m * 4];
      n = 0;
      i1 = 0;
      i2 = 0;
      if (n < k) {
        break label181;
      }
      if (j != 8) {
        break label426;
      }
      i26 = paramArrayOfByte[i1];
      i27 = (byte)(i26 & 0x3);
      if ((i26 & 0xFFFFFF80) != 0) {
        break label412;
      }
    }
    label262:
    label397:
    label412:
    for (int i28 = (byte)(i26 >> 2);; i28 = (byte)(0xC0 ^ i26 >> 2))
    {
      int i29 = i2 + 1;
      arrayOfChar[i2] = LOOKUPBASE64ALPHABET[i28];
      int i30 = i29 + 1;
      arrayOfChar[i29] = LOOKUPBASE64ALPHABET[(i27 << 4)];
      int i31 = i30 + 1;
      arrayOfChar[i30] = '=';
      (i31 + 1);
      arrayOfChar[i31] = '=';
      String str = new String(arrayOfChar);
      return str;
      m = k;
      break;
      label181:
      int i3 = i1 + 1;
      int i4 = paramArrayOfByte[i1];
      int i5 = i3 + 1;
      int i6 = paramArrayOfByte[i3];
      int i7 = i5 + 1;
      int i8 = paramArrayOfByte[i5];
      int i9 = (byte)(i6 & 0xF);
      int i10 = (byte)(i4 & 0x3);
      int i11;
      label247:
      int i12;
      if ((i4 & 0xFFFFFF80) == 0)
      {
        i11 = (byte)(i4 >> 2);
        if ((i6 & 0xFFFFFF80) != 0) {
          break label383;
        }
        i12 = (byte)(i6 >> 4);
        if ((i8 & 0xFFFFFF80) != 0) {
          break label397;
        }
      }
      for (int i13 = (byte)(i8 >> 6);; i13 = (byte)(0xFC ^ i8 >> 6))
      {
        int i14 = i2 + 1;
        arrayOfChar[i2] = LOOKUPBASE64ALPHABET[i11];
        int i15 = i14 + 1;
        arrayOfChar[i14] = LOOKUPBASE64ALPHABET[(i12 | i10 << 4)];
        int i16 = i15 + 1;
        arrayOfChar[i15] = LOOKUPBASE64ALPHABET[(i13 | i9 << 2)];
        i2 = i16 + 1;
        arrayOfChar[i16] = LOOKUPBASE64ALPHABET[(i8 & 0x3F)];
        n++;
        i1 = i7;
        break;
        i11 = (byte)(0xC0 ^ i4 >> 2);
        break label247;
        label383:
        i12 = (byte)(0xF0 ^ i6 >> 4);
        break label262;
      }
    }
    label426:
    int i17;
    int i18;
    int i19;
    int i20;
    int i21;
    if (j == 16)
    {
      i17 = paramArrayOfByte[i1];
      i18 = paramArrayOfByte[(i1 + 1)];
      i19 = (byte)(i18 & 0xF);
      i20 = (byte)(i17 & 0x3);
      if ((i17 & 0xFFFFFF80) != 0) {
        break label568;
      }
      i21 = (byte)(i17 >> 2);
      label476:
      if ((i18 & 0xFFFFFF80) != 0) {
        break label582;
      }
    }
    label568:
    label582:
    for (int i22 = (byte)(i18 >> 4);; i22 = (byte)(0xF0 ^ i18 >> 4))
    {
      int i23 = i2 + 1;
      arrayOfChar[i2] = LOOKUPBASE64ALPHABET[i21];
      int i24 = i23 + 1;
      arrayOfChar[i23] = LOOKUPBASE64ALPHABET[(i22 | i20 << 4)];
      int i25 = i24 + 1;
      arrayOfChar[i24] = LOOKUPBASE64ALPHABET[(i19 << 2)];
      i2 = i25 + 1;
      arrayOfChar[i25] = '=';
      break;
      i21 = (byte)(0xC0 ^ i17 >> 2);
      break label476;
    }
  }
  
  private static boolean isData(char paramChar)
  {
    return (paramChar < '') && (BASE64ALPHABET[paramChar] != -1);
  }
  
  private static boolean isPad(char paramChar)
  {
    return paramChar == '=';
  }
  
  private static boolean isWhiteSpace(char paramChar)
  {
    return (paramChar == ' ') || (paramChar == '\r') || (paramChar == '\n') || (paramChar == '\t');
  }
  
  private static int removeWhiteSpace(char[] paramArrayOfChar)
  {
    if (paramArrayOfChar == null)
    {
      k = 0;
      return k;
    }
    int i = paramArrayOfChar.length;
    int j = 0;
    int k = 0;
    label15:
    int m;
    if (j < i)
    {
      if (isWhiteSpace(paramArrayOfChar[j])) {
        break label49;
      }
      m = k + 1;
      paramArrayOfChar[k] = paramArrayOfChar[j];
    }
    for (;;)
    {
      j++;
      k = m;
      break label15;
      break;
      label49:
      m = k;
    }
  }
}



/* Location:           C:\Development\Client\tmg-agent\wrapper\work_GudangApp_1.3.5\classes-injected\

 * Qualified Name:     com.huawei.softclient.xl.megastore.logic.account.AES.Base64

 * JD-Core Version:    0.7.0.1

 */