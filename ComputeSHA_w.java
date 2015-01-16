import java.security.MessageDigest;
import java.io.*;
import javax.xml.bind.DatatypeConverter;

public class ComputeSHA
{
	public static void main(String args[]) throws Exception
	{
		MessageDigest md = MessageDigest.getInstance("SHA1");

		try (InputStream is = new BufferedInputStream(new FileInputStream(args[0]))) 
		{
      		final byte[] buffer = new byte[1024];
      		for (int read = 0; (read = is.read(buffer)) != -1;) 
      		{
        		md.update(buffer, 0, read);
      		}
    	}

		// byte array to hex string
    	System.out.println(DatatypeConverter.printHexBinary(md.digest()));
	}
}