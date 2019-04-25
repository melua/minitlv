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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class TlvCompressor {
	
	private ByteBuffer buffer = ByteBuffer.allocate(4096);
	
	protected TlvCompressor() {
	}
	
	/**
	 * Compress the given byte array.
	 * @param data to compress
	 * @param bufferSize in bytes
	 * @return compressed data
	 * @throws IOException
	 */
	public byte[] deflate(int bufferSize) throws IOException {
		
		/*
		 * Convert buffer to byte array
		 */
		byte[] data = Tools.minimalBytes(this.buffer);
		
		Deflater deflater = new Deflater();
		deflater.setInput(data);

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {
			deflater.finish();

			byte[] buffer = new byte[bufferSize];
			while (!deflater.finished()) {
				outputStream.write(buffer, 0, deflater.deflate(buffer));
			}
			return outputStream.toByteArray();
		}
	}

	/**
	 * Decompress the given byte array.
	 * @param data to decompress
	 * @param bufferSize in bytes
	 * @return decompressed data
	 * @throws IOException
	 * @throws DataFormatException
	 */
	public byte[] inflate(int bufferSize) throws IOException, DataFormatException {
		
		/*
		 * Convert buffer to byte array
		 */
		byte[] data = Tools.minimalBytes(this.buffer);
		
		Inflater inflater = new Inflater();
		inflater.setInput(data);

		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length)) {

			byte[] buffer = new byte[bufferSize];
			while (!inflater.finished()) {
				outputStream.write(buffer, 0, inflater.inflate(buffer));
			}
			return outputStream.toByteArray();
		}
    }
	
	public TlvCompressor add(byte[] data) {
		buffer.put(data);
		return this;
	}

}
