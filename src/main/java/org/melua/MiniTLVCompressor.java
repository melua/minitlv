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
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.melua.api.Compressor;

public class MiniTLVCompressor implements Compressor {
	
	private ByteArrayOutputStream innerStream = new ByteArrayOutputStream();
	
	protected MiniTLVCompressor() {
	}
	
	@Override
	public byte[] deflate(int bufferSize) throws IOException {
		
		/*
		 * Convert stream to byte array
		 */
		byte[] data = this.innerStream.toByteArray();
		
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

	@Override
	public byte[] inflate(int bufferSize) throws IOException, DataFormatException {
		
		/*
		 * Convert stream to byte array
		 */
		byte[] data = this.innerStream.toByteArray();
		
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
	
	@Override
	public Compressor add(byte[] data) throws IOException {
		innerStream.write(data);
		return this;
	}

}
