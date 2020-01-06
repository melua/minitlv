package org.melua;

/*
 * Copyright (C) 2018 Kevin Guignard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.DataInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;

import org.melua.api.Compressor;
import org.melua.api.Converter;
import org.melua.api.Crypto;
import org.melua.api.Parser;
import org.melua.api.Serializer;

public class MiniTLV {
	
	public static final byte EXTENTED_BYTES = 0x00;
	
	protected static final int BYTE_SIZE = 1;
	protected static final int SHORT_SIZE = 2;
	protected static final int INT_SIZE = 4;

	protected static final String TYPE_ERROR = "Type must be represented as 1, 2 or 4 bytes.";
	protected static final String INPUT_ERROR = "Invalid input.";

	protected static final int TLV_MINSIZE = 3;
	protected static final int EXT_MAXSIZE = 2;
	
	private static final int UBYTE_MAXVALUE = 255;
	private static final int USHORT_MAXVALUE = 65_535;
	
	private MiniTLV() {
	}

	/**
	 * Retrieve an instance
	 * of the MiniTLV Serializer
	 *
	 * @param converter the bytes converter
	 * @return a new instance
	 */
	public static Serializer getWriter(Converter converter) {
		return new MiniTLVSerializer(converter);
	}
	
	/**
	 * Retrieve an instance of the MiniTLV Serializer
	 * using {@link org.melua.DefaultConverter DefaultConverter}
	 *
	 * @return a new instance
	 */
	public static Serializer getWriter() {
		return new MiniTLVSerializer(new DefaultConverter());
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Parser
	 * 
	 * @param converter the bytes converter
	 * @return a new instance
	 */
	public static Parser getReader(Converter converter) {
		return new MiniTLVParser(converter);
	}
	
	/**
	 * Retrieve an instance of the MiniTLV Parser
	 * using {@link org.melua.DefaultConverter DefaultConverter}
	 *
	 * @return a new instance
	 */
	public static Parser getReader() {
		return new MiniTLVParser(new DefaultConverter());
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Crypto
	 *
	 * @param algo
	 * @return a new instance
	 */
	public static Crypto getCipher(Algorithm algo) {
		return new MiniTLVCrypto(algo);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Crypto
	 * using {@link Algorithm#AES128 AES128}
	 * algorithm
	 *
	 * @return a new instance
	 */
	public static Crypto getCipher() {
		return new MiniTLVCrypto(Algorithm.AES128);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Compressor
	 *
	 * @param level
	 * @return a new instance
	 */
	public static Compressor getCompacter(Level level) {
		return new MiniTLVCompressor(level);
	}
	
	/**
	 * Retrieve an instance
	 * of the MiniTLV Compressor
	 * using {@link org.melua.Level#BALANCED BALANCED}
	 * compression level
	 *
	 * @return a new instance
	 */
	public static Compressor getCompacter() {
		return new MiniTLVCompressor(Level.BALANCED);
	}
	
	/**
	 * Automatically add extra {@link #EXTENTED_BYTES} for 2 and 4-bytes type.
	 * @param buffer to append
	 * @param type to check
	 */
	protected static void addExtendedType(ByteBuffer buffer, byte[] type) {
		switch (type.length) {
		default:
			throw new IllegalArgumentException();
		case INT_SIZE:
			buffer.put(EXTENTED_BYTES);
		case SHORT_SIZE:
			buffer.put(EXTENTED_BYTES);
		case BYTE_SIZE:
		}
	}
	
	/**
	 * Automatically add extra {@link #EXTENTED_BYTES} for 2 and 4-bytes length.
	 * @param buffer to append
	 * @param length to check
	 */
	protected static void addExtendedLength(ByteBuffer buffer, int length) {
		if (length > UBYTE_MAXVALUE) {
			buffer.put(EXTENTED_BYTES);
			if (length > USHORT_MAXVALUE) {
				buffer.put(EXTENTED_BYTES);
			}
		}
	}
	
	/**
	 * Extract the shortest byte array from the given byte buffer.
	 * @param buffer
	 * @return byte array
	 */
	protected static byte[] minimalBytes(ByteBuffer buffer) {
		buffer.flip();
		byte[] result = new byte[buffer.limit()];
		buffer.get(result, 0, buffer.limit());
		return result;	
	}
	
	/**
	 * Create the shortest buffer from the given integer.
	 * @param value
	 * @return byte buffer
	 */
	protected static ByteBuffer minimalBuffer(int value) {
		ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
		if (value > UBYTE_MAXVALUE) {
			if (value > USHORT_MAXVALUE) {
				buffer.putInt(value);
			} else {
				buffer.putShort((short) value);
			}
		} else {
			buffer.put((byte) value);
		}
		return buffer;
	}
	
	/**
	 * Read the given stream and extract type and length
	 * according to the extra {@link #EXTENTED_BYTES}.
	 * @param stream to read
	 * @return byte buffer
	 * @throws IOException
	 */
	protected static ByteBuffer getBuffer(DataInputStream stream) throws IOException {
		reading:
		for(int bytes = 1; stream.available() >= bytes; bytes *= 2) {
			int result = stream.readByte();
			if (result != EXTENTED_BYTES) {
				ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
				buffer.put((byte) result);
				switch (bytes) {
				default:
					break reading;
				case INT_SIZE:
					buffer.putShort((short) stream.readShort());
				case SHORT_SIZE:
					buffer.put((byte) stream.readByte());
				case BYTE_SIZE:
				}
				return buffer;
			}
		}
		throw new StreamCorruptedException();
	}

}
