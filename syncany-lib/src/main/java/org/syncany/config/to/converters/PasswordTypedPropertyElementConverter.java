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
import org.syncany.crypto.CipherException;
import org.syncany.crypto.CipherUtil;
import org.syncany.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

/**
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
	 */
	@Override
	public String from(String in) throws Exception {
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(StringUtil.fromHex(System.getProperty("storage.passwordsecret")), CIPHER_NAME),
			new IvParameterSpec(StringUtil.fromHex(System.getProperty("storage.passwordiv"))));

		return new String(cipher.doFinal(StringUtil.fromHex(in)));
	}

	/**
	 * Encrypt the password and encode as base64
	 */
	@Override
  public String to(String out) throws Exception {
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(StringUtil.fromHex(System.getProperty("storage.passwordsecret")), CIPHER_NAME),
			new IvParameterSpec(StringUtil.fromHex(System.getProperty("storage.passwordiv"))));

		return StringUtil.toHex(cipher.doFinal(out.getBytes(Charsets.UTF_8)));
	}
}
