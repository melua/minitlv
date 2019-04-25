package org.melua;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
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
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, (byte)0x01).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = new String(MiniTLV.getReader().read(tlv).parse((byte)0x01), StandardCharsets.UTF_8);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec2() throws IOException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, (byte)0x01, (byte)0x01).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = new String(MiniTLV.getReader().read(tlv).parse((byte)0x01, (byte)0x01), StandardCharsets.UTF_8);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}
	
	@Test
	public void encDec3() throws IOException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, (byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = new String(MiniTLV.getReader().read(tlv).parse((byte)0x01, (byte)0x01, (byte)0x01, (byte)0x01), StandardCharsets.UTF_8);
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
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, (short) type).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = new String(MiniTLV.getReader().read(tlv).parse((short) type), StandardCharsets.UTF_8);
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
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, type).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));
		
		String result = new String(MiniTLV.getReader().read(tlv).parse(type), StandardCharsets.UTF_8);
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
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, type).serialize());

		buffer.flip();
		byte[] unCompressedTlv = new byte[buffer.limit()];
		buffer.get(unCompressedTlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(unCompressedTlv));
		System.out.println("original: " + unCompressedTlv.length + "b");
		byte[] compressedTlv = MiniTLV.getCompacter().add(unCompressedTlv).deflate(512);
		System.out.println("compressed: " + compressedTlv.length + "b");
		System.out.println("rate: " + (compressedTlv.length*100)/unCompressedTlv.length + "%");

		String result = new String(MiniTLV.getReader().read(MiniTLV.getCompacter().add(compressedTlv).inflate(512)).parse(type), StandardCharsets.UTF_8);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

	@Test
	public void encDec7() throws IOException, DataFormatException {
		String rand1 = RandomStringUtils.random(RandomUtils.nextInt(100, 500));
		String rand2 = RandomStringUtils.random(RandomUtils.nextInt(100, 500));
		
		Map<byte[], byte[]> input = new HashMap<>();
		input.put(rand1.getBytes(StandardCharsets.UTF_8), new byte[] {0x01, 0x01});
		input.put(rand2.getBytes(StandardCharsets.UTF_8), new byte[] {0x02, 0x02});
		
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.getWriter()
				.write(rand1.getBytes(StandardCharsets.UTF_8), new byte[] {0x01, 0x01})
				.write(rand2.getBytes(StandardCharsets.UTF_8), new byte[] {0x02, 0x02})
				.serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());

		Map<Integer, byte[]> output = MiniTLV.getReader().read(tlv).parseAll();

		Assert.assertEquals(input.size(), output.size());

		/*for (Entry<byte[], byte[]> entry : input.entrySet()) {
			int type = new BigInteger(entry.getValue()).intValue();
			Assert.assertTrue(output.containsKey(type));
			System.out.println(DatatypeConverter.printHexBinary(entry.getKey()) + " : " + output.get(type));
			Assert.assertEquals(String.valueOf(entry.getValue()), output.get(type));
		}*/

	}

	@Test
	public void encDec8() throws IOException, DataFormatException {

		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.getWriter()
				.write(RandomStringUtils.random(RandomUtils.nextInt(100, 500)), StandardCharsets.UTF_8, new byte[] {0x01, 0x01})
				.write(RandomStringUtils.random(RandomUtils.nextInt(100, 500)), StandardCharsets.UTF_8, new byte[] {0x01, 0x01})
				.serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());

		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));

		String value = new String(MiniTLV.getReader().read(tlv).parse((byte)0x01, (byte)0x01), StandardCharsets.UTF_8);
		System.out.println("value = " + value);
		Assert.assertNotNull(value);

		Map<Integer, byte[]> output = MiniTLV.getReader().read(tlv).parseAll();
		for (byte[] val : output.values()) {
			System.out.println("value = " + val);
			Assert.assertEquals(value, new String(val, StandardCharsets.UTF_8));
		}

	}

	@Test
	public void encDec9() throws IOException, GeneralSecurityException {
		System.out.println("length = " + value.length());
		System.out.println("value = " + value);
		ByteBuffer buffer = ByteBuffer.allocate(BUFFER_MAX);
		buffer.put(MiniTLV.getWriter().write(value, StandardCharsets.UTF_8, (byte)0x01).serialize());
		buffer.flip();
		byte[] tlv = new byte[buffer.limit()];
		buffer.get(tlv, 0, buffer.limit());
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(tlv));

		String secret = RandomStringUtils.random(RandomUtils.nextInt(100, 500));

		byte[] encryptedTlv = MiniTLV.getCipher().add(tlv).encrypt(secret);
		System.out.println("encrypted tlv = " + DatatypeConverter.printHexBinary(encryptedTlv));

		byte[] decryptedTlv = MiniTLV.getCipher().add(encryptedTlv).decrypt(secret);
		System.out.println("tlv = " + DatatypeConverter.printHexBinary(decryptedTlv));

		String result = new String(MiniTLV.getReader().read(decryptedTlv).parse((byte)0x01), StandardCharsets.UTF_8);
		System.out.println("decoded tlv = " + result);

		Assert.assertNotNull(result);
		Assert.assertEquals(value, result);
	}

}
