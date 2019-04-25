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

import java.io.IOException;

public abstract class AbstractParser {
	
	public abstract TlvParser read(byte[] tlv);
	
	public abstract byte[] parse(byte... type) throws IOException;
	
	/**
	 * Read the Type-Length-Value bytes and extract value for the given byte type.
	 * 
	 * @param tlv bytes to read
	 * @param type to search for
	 * @return value for the given type
	 * @throws IOException
	 */
	public byte[] parse(byte type) throws IOException {
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
	public byte[] parse(short type) throws IOException {
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
	public byte[] parse(int type) throws IOException {
		return parse(Tools.convertToBytes(type));
	}
	
}
