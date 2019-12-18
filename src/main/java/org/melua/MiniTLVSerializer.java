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
import static org.melua.MiniTLV.EXT_MAXSIZE;
import static org.melua.MiniTLV.INPUT_ERROR;
import static org.melua.MiniTLV.INT_SIZE;
import static org.melua.MiniTLV.SHORT_SIZE;
import static org.melua.MiniTLV.TYPE_ERROR;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.melua.api.Converter;
import org.melua.api.Serializer;

public class MiniTLVSerializer implements Serializer {
	
	private final Converter converter;
	private final ByteArrayOutputStream innerStream = new ByteArrayOutputStream();
	private final Map<byte[], byte[]> innerMap = new HashMap<>();
	
	protected MiniTLVSerializer(Converter converter) {
		this.converter = converter;
	}
	
	/**
	 * Write a Type-Length-Value for the given type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * From 0x01 (1) to 0xff (255) type and length are represented as one byte.
	 * From 0x0100 (256) to 0xffff (65535) type and length are represented as two bytes,
	 * from 0x010000 (65536) to 0xffffffff (4294967295) type and length are represented as four bytes,
	 * and must be given in {@link ByteOrder#BIG_ENDIAN} order. An extra
	 * {@link #EXTENTED_BYTES} byte is automatically added for 2 and 4-bytes type and length.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	private byte[] serialize(byte[] value, byte... type) {
		
		/*
		 * Prevent bad value
		 */
		if (value == null) {
			throw new IllegalArgumentException(INPUT_ERROR);
		}
		
		/*
		 * Prevent bad type
		 */
		if (type.length != BYTE_SIZE && type.length != SHORT_SIZE && type.length != INT_SIZE) {
			throw new IllegalArgumentException(TYPE_ERROR);
		}
		
		/*
		 * Prepare type and add extended marks if necessary
		 */
		ByteBuffer tbuffer = ByteBuffer.allocate(EXT_MAXSIZE + INT_SIZE);
		MiniTLV.addExtendedType(tbuffer, type);
		tbuffer.put(type);
		byte[] givenType = MiniTLV.minimalBytes(tbuffer);
		
		/*
		 * Prepare length and add extended marks if necessary
		 */
		ByteBuffer lbuffer = ByteBuffer.allocate(EXT_MAXSIZE + INT_SIZE);
		MiniTLV.addExtendedLength(lbuffer, value.length);
		lbuffer.put(MiniTLV.minimalBytes(MiniTLV.minimalBuffer(value.length)));
		byte[] length = MiniTLV.minimalBytes(lbuffer);
		
		/*
		 * Create Type-Length-Value with calculated size
		 */
		ByteBuffer buffer = ByteBuffer.allocate(givenType.length + length.length + value.length);
		buffer.put(givenType);
		buffer.put(length);
		buffer.put(value);
		return buffer.array();
	}
	
	@Override
	public byte[] serialize() throws IOException {
		for (Entry<byte[], byte[]> entry : this.innerMap.entrySet()) {
			if (entry.getValue() != null) {
				this.innerStream.write(serialize(entry.getKey(), entry.getValue()));
			}
		}
		return this.innerStream.toByteArray();
	}
	
	@Override
	public Serializer write(byte[] value, byte... type) {
		this.innerMap.put(value, type);
		return this;
	}

	@Override
	public Converter getConverter() {
		return this.converter;
	}

}
