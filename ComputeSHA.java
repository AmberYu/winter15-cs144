import java.io.*;
import java.security.*;

public class ComputeSHA {
    
    public MessageDigest md = null;
    
    public ComputeSHA(String str)
    {
        try
        {
            md = MessageDigest.getInstance(str);
        }
        catch(NoSuchAlgorithmException n)
        {
            n.getStackTrace();
        }
    }
    
    public void readfile(String filePath) throws FileNotFoundException, IOException
    {
        try
        {
            FileInputStream fileStream = new  FileInputStream(filePath);
            byte[] data = new byte[1024];
            //total number of bytes read into the buffer
            int n=0;
            while ((n = fileStream.read(data)) != -1)
            {
                md.update(data, 0, n);
            }
        }
        catch(FileNotFoundException f)
        {
            System.out.println("No such file");
            throw new FileNotFoundException();
        }
    }
    public void printSHA()
    {
        byte arr[] = md.digest();
        System.out.println(byteArrayToHexString(arr));
    }
    
    public String byteArrayToHexString(byte[] b)
    {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result += hex;
        }
        return result;
    }
    
    public static void main(String args[]) throws IOException
    {
        if (args.length != 1)
            throw new IllegalArgumentException();
        String algorithm = "SHA-1";
        ComputeSHA sha1 = new ComputeSHA(algorithm);
        try {
            sha1.readfile(args[0]);
        }
        catch(FileNotFoundException f)
        {
            System.out.println("Error:" + f.getMessage());
        }
        sha1.printSHA();
    }
}