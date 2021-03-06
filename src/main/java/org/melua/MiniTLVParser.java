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

import static org.melua.MiniTLV.BYTE_SIZE;
import static org.melua.MiniTLV.INPUT_ERROR;
import static org.melua.MiniTLV.INT_SIZE;
import static org.melua.MiniTLV.SHORT_SIZE;
import static org.melua.MiniTLV.TLV_MINSIZE;
import static org.melua.MiniTLV.TYPE_ERROR;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.melua.api.Converter;
import org.melua.api.Parser;

public class MiniTLVParser implements Parser {
	
	private final Converter converter;
	private final ByteArrayOutputStream innerStream = new ByteArrayOutputStream();
	
	protected MiniTLVParser(Converter converter) {
		this.converter = converter;
	}
	
	/**
	 * Read the given stream and extract type and length
	 * according to the extra {@link #EXTENTED_BYTES}.
	 * @param stream to read
	 * @return byte array
	 * @throws IOException
	 */
	private static byte[] getBytes(DataInputStream stream) throws IOException {
		reading:
		for(int bytes = 1; stream.available() >= bytes; bytes *= 2) {
			int input = stream.readByte();
			if (input != MiniTLV.EXTENTED_BYTES) {
				ByteBuffer buffer = ByteBuffer.allocate(INT_SIZE);
				buffer.put((byte) input);
				switch (bytes) {
				default:
					break reading;
				case INT_SIZE:
					buffer.putShort((short) stream.readShort());
				case SHORT_SIZE:
					buffer.put((byte) stream.readByte());
				case BYTE_SIZE:
				}
				
				buffer.flip();
				byte[] result = new byte[buffer.limit()];
				buffer.get(result, 0, buffer.limit());
				return result;
			}
		}
		throw new StreamCorruptedException();
	}
	
	@Override
	public byte[] parse(byte... type) throws IOException {
		
		/*
		 * Convert stream to byte array
		 */
		byte[] tlv = this.innerStream.toByteArray();

		/*
		 * Prevent bad TLV
		 */
		if (tlv.length < TLV_MINSIZE) {
			throw new IllegalArgumentException(INPUT_ERROR);
		}
		
		/*
		 * Prevent bad type
		 */
		if (type.length != BYTE_SIZE && type.length != SHORT_SIZE && type.length != INT_SIZE) {
			throw new IllegalArgumentException(TYPE_ERROR);
		}
		
		/*
		 * Convert type byte array to integer
		 */
		int givenType = getConverter().convertToInt(type);

		try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(tlv))) {

			while(stream.available() >= TLV_MINSIZE) {
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int currentType = getConverter().convertToInt(getBytes(stream));
				
				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int length = getConverter().convertToInt(getBytes(stream));
				
				/*
				 * Read or skip value
				 */
				if (stream.available() >= length) {
					if (currentType == givenType) {
						byte[] value = new byte[length];
						stream.readFully(value);
						return value;
					} else {
						stream.skip(length);
					}
				}
			}
		}

		/*
		 * Type not found
		 */
		return null;
	}
	
	@Override
	public Map<Integer, byte[]> parse() throws IOException {

		/*
		 * Convert stream to byte array
		 */
		byte[] tlv = this.innerStream.toByteArray();

		/*
		 * Prevent bad TLV
		 */
		if (tlv.length < TLV_MINSIZE) {
			throw new IllegalArgumentException(INPUT_ERROR);
		}

		Map<Integer, byte[]> map = new HashMap<>();

		try (DataInputStream stream = new DataInputStream(new ByteArrayInputStream(tlv))) {

			while(stream.available() >= TLV_MINSIZE) {

				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int type = getConverter().convertToInt(getBytes(stream));

				/*
				 * Read 1st byte or next 2, 4-bytes if extended
				 */
				int length = getConverter().convertToInt(getBytes(stream));

				/*
				 * Read or skip value
				 */
				if (stream.available() >= length) {
					if (!map.containsKey(type)) {
						byte[] value = new byte[length];
						stream.readFully(value);
						map.put(type, value);
					} else {
						stream.skip(length);
					}
				}
			}
		}

		return map;
	}
	
	@Override
	public Parser read(byte[] tlv) throws IOException {
		this.innerStream.write(tlv);
		return this;
	}
	
	@Override
	public Converter getConverter() {
		return this.converter;
	}

}
