package org.melua.api;

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

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Map;

import org.melua.util.Tools;

public interface Parser {
	
	/**
	 * Add Type-Length-Value bytes to buffer
	 * for later parsing
	 *
	 * @param tlv bytes to read
	 * @return this
	 * @throws IOException 
	 */
	Parser read(byte[] tlv) throws IOException;
	
	/**
	 * Read the Type-Length-Value bytes and extract value for the given 1, 2 or 4-bytes type.
	 * From 0x01 (1) to 0xff (255) the type must be represented as one byte.
	 * From 0x0100 (256) to 0xffff (65535) the type must be represented as two bytes,
	 * from 0x010000 (65536) to 0xffffffff (4294967295) the type must be represented as four bytes,
	 * and must be given in {@link ByteOrder#BIG_ENDIAN} order.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 * @throws IOException
	 */
	byte[] parse(byte... type) throws IOException;
	
	/**
	 * Read the Type-Length-Value bytes and extract types and associated values.
	 *
	 * @return values
	 * @throws IOException
	 */
	Map<Integer, byte[]> parse() throws IOException;
	
	/**
	 * Read the Type-Length-Value bytes and extract value for the given byte type.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 * @throws IOException
	 */
	default byte[] parse(byte type) throws IOException {
		return parse(new byte[]{type});
	}
	
	/**
	 * Read the Type-Length-Value bytes and extract value for the given short type.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 * @throws IOException
	 */
	default byte[] parse(short type) throws IOException {
		return parse(Tools.convertToBytes(type));
	}
	
	/**
	 * Read the Type-Length-Value bytes and extract value for the given integer type.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 * @throws IOException
	 */
	default byte[] parse(int type) throws IOException {
		return parse(Tools.convertToBytes(type));
	}

}
