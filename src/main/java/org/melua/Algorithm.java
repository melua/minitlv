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

public enum Algorithm {

	AES128(16),
	AES192(24),
	AES256(32);
	
	private int keyLength;
	
	Algorithm(int keyLength) {
		this.keyLength = keyLength;
	}
	
	int getKeyLength() {
		return this.keyLength;
	}
}
