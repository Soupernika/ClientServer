package clientServer;
import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Pattern;
public class Client1
{
	public static void main(String[] args) throws IOException
	{
		try
		{
			Scanner input = new Scanner(System.in);
			InetAddress ip = InetAddress.getByName("localhost");
			Socket s = new Socket(ip, 1112);
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			String email;
			while(true) 
			{
				System.out.println("Enter you email : ");
				email = input.nextLine();
				if(emailCheck(email)) 
				{
					out.writeUTF(email);
					break;
				}
				System.out.println("Your email is incorrect...\nTry again...");
			}
			System.out.println("Enter your password : ");
			String password = input.nextLine();
			out.writeUTF(password);
			String status = in.readUTF();
			if(status.equals("pass") && email.equals("admin@gmail.com")) 
			{
				String token = in.readUTF();
				System.out.println(token);
				System.out.println("The admin has successfully logged in...");
				while(true) 
				{
					System.out.println("Want to add user (Y/N)");
					String opinion = input.nextLine();
					out.writeUTF(opinion);
					if(opinion.equals("Y")) 
					{
						while(true)
						{
							System.out.println("Enter the user email : ");
							String uEmail = input.nextLine();
							if(emailCheck(uEmail))
							{
								out.writeUTF(uEmail);
								break;
							}
							System.out.println("Enter a proper email address...");
						}
						System.out.println("Enter the user password : ");
						String uPassword = input.nextLine();
						out.writeUTF(uPassword);
						String result = in.readUTF();
						System.out.println(result);
					}
					else
					{
						out.writeUTF("No");
						break;
					}
				}
			}
			else 
			{
				String r = in.readUTF();
				System.out.println(r);
				System.out.println(status);
			}
			input.close();
			in.close();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	static boolean emailCheck(String email) 
	{
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@" +"(?:[a-zA-Z0-9-]+\\.)+[a-z" +"A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if(pat.matcher(email).matches()) 
		{
			return true;
		}
		return false;
	}
}

