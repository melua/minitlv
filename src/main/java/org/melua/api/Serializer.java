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

import java.nio.charset.Charset;

public interface Serializer {
	
	/**
	 * Retrieve the inner bytes converter
	 * @return Converter
	 */
	Converter getConverter();
	
	/**
	 * Add type/value to buffer
	 * for later serialization
	 *
	 * @param value for the given type
	 * @param type to write
	 * @return this
	 */
	Serializer write(byte[] value, byte... type);
	
	/**
	 * Write a Type-Length-Value and store them as 1, 2 or 4-bytes.
	 * @see #deflate(byte[], int)
	 *
	 * @return bytes in Type-Length-Value representation
	 * @throws IOException 
	 */
	byte[] serialize() throws IOException;

	/**
	 * Write a Type-Length-Value for the given byte type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(byte[] value, byte type) {
		return write(value, new byte[]{type});
	}

	/**
	 * Write a Type-Length-Value for the given short type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(byte[] value, short type) {
		return write(value, getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(byte[] value, int type) {
		return write(value, getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(short value, byte... type) {
		return write(getConverter().convertToBytes(value), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(short value, byte type) {
		return write(getConverter().convertToBytes(value), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(short value, short type) {
		return write(getConverter().convertToBytes(value), getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(short value, int type) {
		return write(getConverter().convertToBytes(value), getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(int value, byte... type) {
		return write(getConverter().convertToBytes(value), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(int value, byte type) {
		return write(getConverter().convertToBytes(value), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(int value, short type) {
		return write(getConverter().convertToBytes(value), getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(int value, int type) {
		return write(getConverter().convertToBytes(value), getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(String value, Charset charset, byte... type) {
		return write(value.getBytes(charset), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(String value, Charset charset, byte type) {
		return write(value.getBytes(charset), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(String value, Charset charset, short type) {
		return write(value.getBytes(charset), getConverter().convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	default Serializer write(String value, Charset charset, int type) {
		return write(value.getBytes(charset), getConverter().convertToBytes(type));
	}

}
