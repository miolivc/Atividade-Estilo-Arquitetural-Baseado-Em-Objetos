import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClientApp {

	private Socket socket;
	private String message;
	private OutputStream out;
	
	/*
	 * 1: Salvar Produto
	 * 2: Salvar Pessoa
	 * 3: Salvar Pedido sem novo vendedor
	 * 4: Salvar Pedido com novo vendedor
	 * 5: Salvar Vendedor 
	 * */

        @Before
	@Test
	public void ct01() throws UnknownHostException, IOException {
		
		message = "1:12:Maçã Vermelha";
		socket = new Socket("localhost", 40999);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertEquals(message, newMessage);
	}
	
        @Before
	@Test
	public void ct02() throws UnknownHostException, IOException {
		
		message = "2:2:João Paulo:83 9 8877 9808";
		socket = new Socket("localhost", 40999);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertEquals(message, newMessage);
	}
	
	@Test
	public void ct03() throws UnknownHostException, IOException {
		
		message = "3:PD001:2:12:20";
		socket = new Socket("localhost", 40997);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertEquals(message, newMessage);
	}
	
        @After
	@Test
	public void ct04() throws UnknownHostException, IOException {
		
		message = "3:PD002:1:12:20";
		socket = new Socket("localhost", 40997);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertNotEquals(message, newMessage);
	}
	
        @After
	@Test
	public void ct05() throws UnknownHostException, IOException {
		
		message = "3:PD002:2:11:20";
		socket = new Socket("localhost", 40997);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertNotEquals(message, newMessage);
	}
	
        @After
	@Test
	public void ct06() throws UnknownHostException, IOException {
		
		message = "4:PD002:3:João Pedro:83 9 8876 0909:12:20";
		socket = new Socket("localhost", 40997);
		out = socket.getOutputStream();
		out.write(message.getBytes());
		InputStream input = socket.getInputStream();
		byte[] b = new byte[1024];
		input.read(b);
		String newMessage = new String(b).trim();
		
		assertEquals(message, newMessage);
	}
	
	@After
	public void close() throws IOException{
		socket.close();
	}
}
