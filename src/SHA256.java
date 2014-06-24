import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256
{
  private static final String TAG = SHA256.class.getSimpleName();
  
  public static String Encrypt(String paramString)
  {
    if (paramString != null) {}
    try
    {
      String str = bytes2Hex(MessageDigest.getInstance("SHA-256").digest(paramString.getBytes("UTF-8")));
      return str;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      return "";
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return "";
  }
  
  public static String EncryptBase64(String paramString)
  {
    if (paramString != null) {}
    try
    {
      String str = Base64.encode(MessageDigest.getInstance("SHA-256").digest(paramString.getBytes("UTF-8")));
      return str;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
       return "";
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
    }
    return "";
  }
  
  private static String bytes2Hex(byte[] paramArrayOfByte)
  {
    String str1 = "";
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfByte.length) {
        return str1;
      }
      String str2 = Integer.toHexString(0xFF & paramArrayOfByte[i]);
      if (str2.length() == 1) {
        str1 = str1 + "0";
      }
      str1 = str1 + str2;
    }
  }
  
  public static String getSecurityHeader(URL paramURL, String paramString1, String paramString2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(paramURL.getFile()).append("Huawei_ODP").append("portalone").append(paramString1).append(paramString2);
    return EncryptBase64(localStringBuffer.toString());
  }
}



/* Location:           C:\Development\Client\tmg-agent\wrapper\work_GudangApp_1.3.5\classes-injected\

 * Qualified Name:     com.huawei.softclient.xl.megastore.logic.account.AES.SHA256

 * JD-Core Version:    0.7.0.1

 */