package org.melua;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MiniTLVTest {
	
	private static final int BUFFER_MAX = 4096;
	
	private int type;
	private String value;

	@Before	
	public void setUp() {
		type = RandomUtils.nextInt();
		value = RandomStringUtils.random(RandomUtils.nextInt(100, 500));
	}
	
	@Test
	public void encDec1() throws IOException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec2() throws IOException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01, (byte)0x01));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec3() throws IOException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec4() throws IOException {
		System.out.println("type = " + type);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (short) type));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, (short) type);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec5() throws IOException {
		System.out.println("type = " + type);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, type));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = MiniTLV.parse(tlv, type);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

	@Test
	public void encDec6() throws IOException, DataFormatException {
		System.out.println("type = " + type);
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, type));

		buffer.flip();
		byte[] unCompressedTlv = new byte[buffer.limit()];
		buffer.get(unCompressedTlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(unCompressedTlv));
		System.out.println("original: " + unCompressedTlv.length + "b");
		byte[] compressedTlv = MiniTLV.deflate(unCompressedTlv, 512);
		System.out.println("compressed: " + compressedTlv.length + "b");
		System.out.println("rate: " + (compressedTlv.length*100)/unCompressedTlv.length + "%");

		String result = MiniTLV.parse(MiniTLV.inflate(compressedTlv, 512), type);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

}
