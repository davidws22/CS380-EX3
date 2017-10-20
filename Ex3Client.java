// ============================================================================
// file: Ex3Client.java
// ============================================================================
// Programmer: David Shin
// Date: 10/19/2017
// Class: CS 380 ("Computer Networks")
// Time: TR 3:00 - 4:50pm
// Instructor: Mr. Davarpanah
// Exercise: 3
//
// Description: This program verifies the checksum of a given stream of bytes.
// It first takes in input a single initial byte, which indicates how many bytes
// are expected from the server. The bytes are then read into an array of bytes
// and sent as input to checksum and returns back with 16-bits (short) with the
// correct checksum. These 16 bits are sent back to the server and gets verified
// with a 1 if the checksum is correct, else, 0. 
//
// ============================================================================  

import java.io.IOException;
import java.net.Socket;

public class Ex3Client
{
    
    public static void main(String[] args)
    {
	Socket socket;
	int numberofBytes;
	byte[] numBytes = new byte[1];
	short checkedsum;
	byte[] result = new byte[2];
	//connect to server
	try{
	    socket = new Socket("18.221.102.182", 38103);
	    System.out.println("Connected to server");
	    //get bytes from server
	    socket.getInputStream().read(numBytes);
	    numberofBytes = (int)numBytes[0];
	    numberofBytes &= 0x000000FF; 
	    System.out.println("the number of bytes received from server: " + numberofBytes);
	    byte[] byteArray = new byte[numberofBytes];
	    socket.getInputStream().read(byteArray);
	    for(int i = 0; i < numberofBytes; i++)
	    {
	       if( i % 20 == 0)
	       System.out.printf("\n ");
	       System.out.printf("%01X", byteArray[i]);
	    }
	    //call checksum method
	    checkedsum = checksum(byteArray);
	    System.out.printf("Checksum Calculated: %01X.\n",  checkedsum);
	    result[0] = (byte)((checkedsum & 0xFF00) >>> 8);
	    //System.out.printf("this is the first thing : %01X\n", result[0]);
	    result[1] = (byte)(checkedsum & 0x00FF);
	    //System.out.printf("This is the second thing : %01X\n", result[1]);
	    socket.getOutputStream().write(result);

	    System.out.println();
	    socket.getInputStream().read(numBytes);
	    
	    // System.out.println("numBytes : " + numBytes[0]);
	    int target = (int)numBytes[0];
	    
	    if(target == 1)
		{
		    System.out.println("Response good!");
		}
	    else
		System.out.println("Response Bad");
	   
	    
	}
	catch(IOException e1)
	    {
		e1.printStackTrace();
	    }
    }
     
    public static short checksum(byte[] b)
    {
      
	int length = b.length;
	int index = 0;
	long checks = 0;
	long sum = 0;

	while (length > 1) {
	    sum = sum + ((b[index] << 8 & 0xFF00) | ((b[index + 1]) & 0x00FF));
	    index +=2;
	    length -= 2;
	    if ((sum & 0xFFFF0000) > 0) {
		sum &= 0xFFFF;
		sum++;
	    }
	}
	if (length > 0) {
	    sum += b[index] << 8 & 0xFF00;
	    if ((sum & 0xFFFF0000) > 0) {
		sum &= 0xFFFF;
		sum++;
	    }
	}
	checks = (~((sum & 0xFFFF) + (sum >> 16))) & 0xFFFF;
	System.out.println();
	return (short)checks;
	}
}
