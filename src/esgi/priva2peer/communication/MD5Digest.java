package esgi.priva2peer.communication;

import java.security.MessageDigest;

public class MD5Digest
{

	public static void main(String[] args) throws Exception
	{

		if (args.length != 1)
		{
			System.err.println("String to MD5 digest should be first and only parameter");
			return;
		}
		String original = args[0];
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(original.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest)
		{
			sb.append(String.format("%02x", b & 0xff));
		}

		System.out.println("original:" + original);
		System.out.println("digested(hex):" + sb.toString());
	}

}
