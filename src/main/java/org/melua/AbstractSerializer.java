package org.melua;

import java.nio.charset.Charset;

public abstract class AbstractSerializer {
	
	public abstract TlvSerializer write(byte[] value, byte... type);

	/**
	 * Write a Type-Length-Value for the given byte type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(byte[] value, byte type) {
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
	public TlvSerializer write(byte[] value, short type) {
		return write(value, Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and value,
	 * and store them as 1, 2 or 4-bytes.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(byte[] value, int type) {
		return write(value, Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(short value, byte... type) {
		return write(Tools.convertToBytes(value), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(short value, byte type) {
		return write(Tools.convertToBytes(value), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(short value, short type) {
		return write(Tools.convertToBytes(value), Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(short value, int type) {
		return write(Tools.convertToBytes(value), Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(int value, byte... type) {
		return write(Tools.convertToBytes(value), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(int value, byte type) {
		return write(Tools.convertToBytes(value), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(int value, short type) {
		return write(Tools.convertToBytes(value), Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(int value, int type) {
		return write(Tools.convertToBytes(value), Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(String value, Charset charset, byte... type) {
		return write(value.getBytes(charset), type);
	}
	
	/**
	 * Write a Type-Length-Value for the given byte type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(String value, Charset charset, byte type) {
		return write(value.getBytes(charset), new byte[]{type});
	}
	
	/**
	 * Write a Type-Length-Value for the given short type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(String value, Charset charset, short type) {
		return write(value.getBytes(charset), Tools.convertToBytes(type));
	}
	
	/**
	 * Write a Type-Length-Value for the given integer type and integer value.
	 * 
	 * @param value for the given type
	 * @param type to write
	 * @return bytes in Type-Length-Value representation
	 */
	public TlvSerializer write(String value, Charset charset, int type) {
		return write(value.getBytes(charset), Tools.convertToBytes(type));
	}

}
