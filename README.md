# MiniTLV: a simple TLV parser
MinitTLV let you convert your UTF-8 String into a Type-Length-Value byte array and reciprocally.
The result is as short as possible without compression.
The type and length have a dynamically resolved size (1, 2 or 4 bytes) using the `0x00` reserved byte for extension.
The value field is of variable size (up to 2<sup>32</sup>-1).
Additionally you can compress serialized data.

Method | Description
------ | -----------
*serialize* | Write a Type-Length-Value for the given type and value, and store them as 1, 2 or 4-bytes.
*parse* | Read the Type-Length-Value bytes and extract value for the given 1, 2 or 4-bytes type.
*deflate* | Compress the given byte array.
*inflate* | Decompress the given byte array.
