package org.melua;

public class MiniTLV {

	public static final byte EXTENTED_BYTES = 0x00;

	protected static final String TYPE_ERROR = "Type must be represented as 1, 2 or 4 bytes.";
	protected static final String INPUT_ERROR = "Invalid input.";

	protected static final int TLV_MINSIZE = 3;
	protected static final int EXT_MAXSIZE = 2;

	private MiniTLV() {
	}

	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvSerializer getWriter() {
		return new TlvSerializer();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvParser getReader() {
		return new TlvParser();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvCrypto getCipher() {
		return new TlvCrypto();
	}
	
	/**
	 * Retrieve an instance.
	 *
	 * @return a new instance
	 */
	public static TlvCompressor getCompacter() {
		return new TlvCompressor();
	}

}
