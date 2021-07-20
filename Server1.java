package clientServer;
import java.io.*;
import java.net.*;
import java.sql.*;

//Server class
public class Server1
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(1111);
		while (true)
		{
			Socket s = null;
			try
			{
				s = ss.accept();
				System.out.println("A new client is connected : " + s);
				DataInputStream in = new DataInputStream(s.getInputStream());
				DataOutputStream out = new DataOutputStream(s.getOutputStream());
				Thread t = new ClientHandler(s, in, out);
				t.start();
			}
			catch (Exception e)
			{
				s.close();
				e.printStackTrace();
			}
		}
		
	}
}

class ClientHandler extends Thread
{
	final DataInputStream in;
	final DataOutputStream out;
	final Socket s;
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream out)
	{
		this.s = s;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run()
	{
		try 
		{
			String email = in.readUTF();
			String password = in.readUTF();
			try 
			{
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection myConnect=DriverManager.getConnection("jdbc:mysql://localhost:3306/customerlogin","root","root");
	        	Statement myStmt=myConnect.createStatement();
	        	ResultSet rs = myStmt.executeQuery("SELECT * FROM login WHERE email = '"+email+"' AND password = '"+password+"'");
	        	if(rs.next()) 
	        	{
	        		if(email.equals("admin@gmail.com")) 
	        		{
	        			out.writeUTF("admin");
	        			while(true)
	        			{
	        				String opinion = in.readUTF();
	        				if(opinion.equals("Y")) 
	        				{
	        					String uEmail = in.readUTF();
	        					String uPassword = in.readUTF();
	        					ResultSet r1 = myStmt.executeQuery("SELECT * FROM login WHERE email = '"+uEmail+"'");
	        					if(r1.next()) 
	        					{
	        						out.writeUTF("Inserting values has been failed...");
	        					}
	        					else
	        					{
	        						myStmt.executeUpdate("INSERT INTO login VALUES('"+uEmail+"','"+uPassword+"')");
		        					out.writeUTF("The values has been inserted successfully...");
	        					}
	        				}
	        				else 
	        				{
	        					out.writeUTF("The server is ending...");
	        					break;
	        				}
	        			}
	        		}
	        		else
	        		{
	        			out.writeUTF("You are a normal user");
	        		}
	        	}
	        	else 
	        	{
	        		out.writeUTF("Not a valid user");
	        	}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		try
		{
			this.in.close();
			this.out.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}

