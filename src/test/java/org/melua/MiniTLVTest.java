package org.melua;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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

	@Test
	public void encDec7() throws IOException, DataFormatException {
		Map<byte[], Object> input = new HashMap<>();
		input.put(new byte[] {0x01, 0x01}, RandomStringUtils.random(RandomUtils.nextInt(100, 500)));
		input.put(new byte[] {0x02, 0x02}, RandomStringUtils.random(RandomUtils.nextInt(100, 500)));

		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serializeAll(input, BUFFER_MAX));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());

		Map<Integer, String> output = MiniTLV.parseAll(tlv);

		Assert.assertEquals(input.size(), output.size());

		for (Entry<byte[], Object> entry : input.entrySet()) {
			int type = new BigInteger(entry.getKey()).intValue();
			Assert.assertTrue(output.containsKey(type));
			System.out.println(DatatypeConverter.printHexBinary(entry.getKey()) + " : " + output.get(type));
			Assert.assertEquals(String.valueOf(entry.getValue()), output.get(type));
		}

	}

	@Test
	public void encDec8() throws IOException, DataFormatException {
		Map<byte[], Object> input = new HashMap<>();
		input.put(new byte[] {0x01, 0x01}, RandomStringUtils.random(RandomUtils.nextInt(100, 500)));
		input.put(new byte[] {0x01, 0x01}, RandomStringUtils.random(RandomUtils.nextInt(100, 500)));

		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serializeAll(input, BUFFER_MAX));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());

		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));

		String value = MiniTLV.parse(tlv, (byte)0x01, (byte)0x01);
		System.out.println("value = " + value);
		Assert.assertNotNull(value);

		Map<Integer, String> output = MiniTLV.parseAll(tlv);
		for (String val : output.values()) {
			System.out.println("value = " + val);
			Assert.assertEquals(value,String.valueOf(val));
		}

	}

	@Test
	public void encDec9() throws IOException, GeneralSecurityException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.serialize(value, (byte)0x01));
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));

		String secret = RandomStringUtils.random(RandomUtils.nextInt(100, 500));

		byte[] encryptedTlv = MiniTLV.encrypt(tlv, secret);
		System.out.println("encrypted tlv = " + DatatypeConverter.printHexBinary(encryptedTlv));

		byte[] decryptedTlv = MiniTLV.decrypt(encryptedTlv, secret);
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(decryptedTlv));

		String result = MiniTLV.parse(decryptedTlv, (byte)0x01);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

}
