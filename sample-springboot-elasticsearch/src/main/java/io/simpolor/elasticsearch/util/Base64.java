package io.simpolor.elasticsearch.util;

import java.io.IOException;
import java.io.OutputStream;

public class Base64 {
	/**
	 * encodingTable
	 */
	private static final byte[] encodingTable = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E', (byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L',
			(byte) 'M', (byte) 'N', (byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W', (byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a',
			(byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f', (byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o', (byte) 'p',
			(byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x', (byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4',
			(byte) '5', (byte) '6', (byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };

	/**
	 * set up the decoding table.
	 */
	private static final byte[] decodingTable;

	static {
		decodingTable = new byte[128];

		for (int i = 0; i < 128; i++) {
			decodingTable[i] = (byte) -1;
		}

		for (int i = 'A'; i <= 'Z'; i++) {
			decodingTable[i] = (byte) (i - 'A');
		}

		for (int i = 'a'; i <= 'z'; i++) {
			decodingTable[i] = (byte) (i - 'a' + 26);
		}

		for (int i = '0'; i <= '9'; i++) {
			decodingTable[i] = (byte) (i - '0' + 52);
		}

		decodingTable['+'] = 62;
		decodingTable['/'] = 63;
	}

	/**
	 * 모듈명 : encode <br>
	 * 상세설명 : 문자열 인코딩 <br>
	 * 
	 * @param str
	 *            the str
	 * @param charSet
	 *            the char set
	 * @return String string
	 * @throws Exception
	 *             the exception
	 */
	public static String encode(String str, String charSet) throws Exception {
		byte[] data = str.getBytes(charSet);
		return new String(encode(data, 0, data.length), charSet);
	}

	/**
	 * 모듈명 : encode <br>
	 * 상세설명 : encode the input data producong a base 64 encoded byte array. <br>
	 * 
	 * @param data
	 *            the data
	 * @return byte[] : a byte array containing the base 64 encoded data.
	 */
	public static byte[] encode(byte[] data) {
		return encode(data, 0, data.length);
	}

	/**
	 * 모듈명 : encode <br>
	 * 상세설명 : encode the input data producong a base 64 encoded byte array. <br>
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return byte[] : a byte array containing the base 64 encoded data.
	 */
	public static byte[] encode(byte[] data, int offset, int length) {
		byte[] bytes;
		int len = offset + length;

		int modulus = length % 3;
		if (modulus == 0) {
			bytes = new byte[4 * length / 3];
		} else {
			bytes = new byte[4 * ((length / 3) + 1)];
		}

		int dataLength = (length - modulus) + offset;
		int a1, a2, a3;
		for (int i = offset, j = 0; i < dataLength; i += 3, j += 4) {
			a1 = data[i] & 0xff;
			a2 = data[i + 1] & 0xff;
			a3 = data[i + 2] & 0xff;

			bytes[j] = encodingTable[(a1 >>> 2) & 0x3f];
			bytes[j + 1] = encodingTable[((a1 << 4) | (a2 >>> 4)) & 0x3f];
			bytes[j + 2] = encodingTable[((a2 << 2) | (a3 >>> 6)) & 0x3f];
			bytes[j + 3] = encodingTable[a3 & 0x3f];
		}

		/*
		 * process the tail end.
		 */
		int b1, b2, b3;
		int d1, d2;

		switch (modulus) {
		case 0: /* nothing left to do */{
			break;
		}
		case 1: {
			d1 = data[len - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = (d1 << 4) & 0x3f;

			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = (byte) '=';
			bytes[bytes.length - 1] = (byte) '=';
			break;
		}
		case 2: {
			d1 = data[len - 2] & 0xff;
			d2 = data[len - 1] & 0xff;

			b1 = (d1 >>> 2) & 0x3f;
			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
			b3 = (d2 << 2) & 0x3f;

			bytes[bytes.length - 4] = encodingTable[b1];
			bytes[bytes.length - 3] = encodingTable[b2];
			bytes[bytes.length - 2] = encodingTable[b3];
			bytes[bytes.length - 1] = (byte) '=';
			break;
		}
		}

		return bytes;
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : decode the base 64 encoded input data <br>
	 * 
	 * @param b64
	 *            the b 64
	 * @param charSet
	 *            the char set
	 * @return String string
	 * @throws Exception
	 *             the exception
	 */
	public static String decode(String b64, String charSet) throws Exception {
		byte[] data = b64.getBytes(charSet);
		return new String(decode(data, 0, data.length), charSet);
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : decode the base 64 encoded input data. <br>
	 * 
	 * @param data
	 *            the data
	 * @return byte[] : a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data) {
		return decode(data, 0, data.length);
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : decode the base 64 encoded input data. <br>
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return byte[] : a byte array representing the decoded data.
	 */
	public static byte[] decode(byte[] data, int offset, int length) {

		byte[] bytes;
		byte b1, b2, b3, b4;
		int dataLength = offset + length;

		// data = discardNonBase64Bytes(data);

		if (data[dataLength - 2] == '=') {
			bytes = new byte[(((length / 4) - 1) * 3) + 1];
		} else if (data[dataLength - 1] == '=') {
			bytes = new byte[(((length / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((length / 4) * 3)];
		}

		for (int i = offset, j = 0; i < dataLength - 4; i += 4, j += 3) {
			b1 = decodingTable[data[i]];
			b2 = decodingTable[data[i + 1]];
			b3 = decodingTable[data[i + 2]];
			b4 = decodingTable[data[i + 3]];

			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | (b4));
		}

		if (data[offset + length - 2] == '=') {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];

			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data[offset + length - 1] == '=') {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];
			b3 = decodingTable[data[dataLength - 2]];

			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];
			b3 = decodingTable[data[dataLength - 2]];
			b4 = decodingTable[data[dataLength - 1]];

			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | (b4));
		}

		return bytes;
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : decode the base 64 encoded String data. <br>
	 * 
	 * @param data
	 *            the data
	 * @return byte[] : a byte array representing the decoded data.
	 */
	public static byte[] decode(String data) {
		return decode(data, 0, data.length());
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : decode the base 64 encoded String data. <br>
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @return byte[] : a byte array representing the decoded data.
	 */
	public static byte[] decode(String data, int offset, int length) {
		byte[] bytes;
		byte b1, b2, b3, b4;
		int dataLength = offset + length;

		// data = discardNonBase64Chars(data);

		if (data.charAt(dataLength - 2) == '=') {
			bytes = new byte[(((length / 4) - 1) * 3) + 1];
		} else if (data.charAt(dataLength - 1) == '=') {
			bytes = new byte[(((length / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((length / 4) * 3)];
		}

		for (int i = offset, j = 0; i < dataLength - 4; i += 4, j += 3) {
			b1 = decodingTable[data.charAt(i)];
			b2 = decodingTable[data.charAt(i + 1)];
			b3 = decodingTable[data.charAt(i + 2)];
			b4 = decodingTable[data.charAt(i + 3)];

			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | (b4));
		}

		if (data.charAt(dataLength - 2) == '=') {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];

			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data.charAt(dataLength - 1) == '=') {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];
			b3 = decodingTable[data.charAt(dataLength - 2)];

			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];
			b3 = decodingTable[data.charAt(dataLength - 2)];
			b4 = decodingTable[data.charAt(dataLength - 1)];

			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | (b4));
		}

		return bytes;
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : <br>
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @param output
	 *            the output
	 * @throws IOException
	 *             the iO exception
	 */
	@SuppressWarnings("unused")
	public static void decode(String data, int offset, int length, OutputStream output) throws IOException {
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		int dataLength = offset + length;

		// data = discardNonBase64Chars(data);

		for (int i = offset, j = 0; i < dataLength - 4; i += 4, j += 3) {
			b1 = decodingTable[data.charAt(i)];
			b2 = decodingTable[data.charAt(i + 1)];
			b3 = decodingTable[data.charAt(i + 2)];
			b4 = decodingTable[data.charAt(i + 3)];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
			output.write((b3 << 6) | (b4));
		}

		if (data.charAt(dataLength - 2) == '=') {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];

			output.write((b1 << 2) | (b2 >> 4));
		} else if (data.charAt(dataLength - 1) == '=') {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];
			b3 = decodingTable[data.charAt(dataLength - 2)];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data.charAt(dataLength - 4)];
			b2 = decodingTable[data.charAt(dataLength - 3)];
			b3 = decodingTable[data.charAt(dataLength - 2)];
			b4 = decodingTable[data.charAt(dataLength - 1)];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
			output.write((b3 << 6) | (b4));
		}
	}

	/**
	 * 모듈명 : decode <br>
	 * 상세설명 : <br>
	 * 
	 * @param data
	 *            the data
	 * @param offset
	 *            the offset
	 * @param length
	 *            the length
	 * @param output
	 *            the output
	 * @throws IOException
	 *             the iO exception
	 */
	@SuppressWarnings("unused")
	public static void decode(char[] data, int offset, int length, OutputStream output) throws IOException {
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		int dataLength = offset + length;

		// data = discardNonBase64Chars(data);

		for (int i = offset, j = 0; i < dataLength - 4; i += 4, j += 3) {
			b1 = decodingTable[data[i]];
			b2 = decodingTable[data[i + 1]];
			b3 = decodingTable[data[i + 2]];
			b4 = decodingTable[data[i + 3]];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
			output.write((b3 << 6) | (b4));
		}

		if (data[dataLength - 2] == '=') {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];

			output.write((b1 << 2) | (b2 >> 4));
		} else if (data[dataLength - 1] == '=') {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];
			b3 = decodingTable[data[dataLength - 2]];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
		} else {
			b1 = decodingTable[data[dataLength - 4]];
			b2 = decodingTable[data[dataLength - 3]];
			b3 = decodingTable[data[dataLength - 2]];
			b4 = decodingTable[data[dataLength - 1]];

			output.write((b1 << 2) | (b2 >> 4));
			output.write((b2 << 4) | (b3 >> 2));
			output.write((b3 << 6) | (b4));
		}
	}

	/**
	 * 모듈명 : computeEncodedSize <br>
	 * 상세설명 : Compute the base64 encoded size for a stream whose size is
	 * specified in <br>
	 * the incoming parameter <br>
	 * 
	 * @param size
	 *            : the original size (>=0)
	 * @return long : the size of the encoded data
	 */
	public static long computeEncodedSize(long size) {
		long encodedSize;
		long modulus = (size % 3);
		if (modulus == 0) {
			encodedSize = (4 * size / 3);
		} else {
			encodedSize = (4 * ((size / 3) + 1));
		}
		return encodedSize;
	}
}
