/*
 * Syncany, www.syncany.org
 * Copyright (C) 2011-2014 Philipp C. Heckel <philipp.heckel@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.syncany.config.to.converters;

import com.google.common.base.Charsets;
import org.syncany.config.UserConfigKeys;
import org.syncany.crypto.CipherException;
import org.syncany.crypto.CipherUtil;
import org.syncany.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
 * Passwords (i.e. fields named password) in configuration files are stored encrypted with AES/CBC/PKCS5PADDING using
 * {@link org.syncany.config.UserConfigKeys.System#STORAGE_SECRET} as password and
 * {@link org.syncany.config.UserConfigKeys.System#STORAGE_SECRET} as iv.
 *
 * @author Christian Roth <christian.roth@port17.de>
 */

public class PasswordTypedPropertyElementConverter implements TypedPropertyElementConverter {
	public static final String PROPERTY_NAME = "password";
	private static final String CIPHER_NAME = "AES/CBC/PKCS5PADDING";

	private final Cipher cipher;

	public PasswordTypedPropertyElementConverter() throws NoSuchPaddingException, NoSuchAlgorithmException, CipherException {
		CipherUtil.enableUnlimitedStrength();
		cipher = Cipher.getInstance("AES");
	}

	/**
	 * Decode from base64 and decrypt the password
	 *
	 * @inheritDoc
	 */
	@Override
	public String from(String in) throws Exception {

		if (in == null) {
			return "";
		}

		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(StringUtil.fromHex(System.getProperty(UserConfigKeys.System.STORAGE_SECRET.toString())),
				CIPHER_NAME), new IvParameterSpec(StringUtil.fromHex(System.getProperty(UserConfigKeys.System.STORAGE_IV.toString()))));

		return new String(cipher.doFinal(StringUtil.fromHex(in)));
	}

	/**
	 * Encrypt the password and encode as base64
	 *
	 * @inheritDoc
	 */
	@Override
	public String to(String out) throws Exception {

		if (out == null) {
			return "";
		}
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(StringUtil.fromHex(System.getProperty(UserConfigKeys.System.STORAGE_SECRET.toString())),
				CIPHER_NAME), new IvParameterSpec(StringUtil.fromHex(System.getProperty(UserConfigKeys.System.STORAGE_IV.toString()))));

		return StringUtil.toHex(cipher.doFinal(out.getBytes(Charsets.UTF_8)));
	}
}
