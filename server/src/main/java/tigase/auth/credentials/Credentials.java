/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2017 "Tigase, Inc." <office@tigase.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 */
package tigase.auth.credentials;

import tigase.xmpp.jid.BareJID;

/**
 * Interface implemented by classes handling user login credentials.
 * In implementations of this interface multiple credentials for single account may be stored but for single username,
 * ie. different credentials for different authentication mechanisms.
 */
public interface Credentials {

	String DEFAULT_USERNAME = "default";

	/**
	 * Returns bare jid of an account
	 * @return
	 */
	BareJID getUser();

	/**
	 * Checks if account is disabled
	 * @return	true - account is disabled
	 * 			false - account is enabled
	 */
	boolean isAccountDisabled();

	/**
	 * Find a credential for specified mechanism
	 * @param mechanism
	 * @return instance of an entry if available or null
	 */
	Entry getEntryForMechanism(String mechanism);

	/**
	 * Returns first available instance of credentials entry
	 * @return
	 */
	Entry getFirst();

	/**
	 * Interface required to be implemented by classes representing credential entry.
	 */
	interface Entry {

		/**
		 * Name of the mechanism for which it will work
		 * @return
		 */
		String getMechanism();

		/**
		 * Check if plaintext password will match stored credential
		 * @param plain
		 * @return	true - password matches
		 * 			false - password is invalid
		 */
		boolean verifyPlainPassword(String plain);

	}

	/**
	 * Interface implemented by classes used as DTO for credentials read from repository.s
	 */
	interface RawEntry {

		/**
		 * Name of mechanism
		 * @return
		 */
		String getMechanism();

		/**
		 * Encoded value
		 * @return
		 */
		String getValue();

		/**
		 * Check if mechanism name matches
		 * @param mechanism
		 * @return
		 */
		default boolean isForMechanism(String mechanism) {
			return mechanism.equals(getMechanism());
		}

	}

	/**
	 * Interface implemented by credentials encoder converting them from plaintext value to encoded form
	 * stored in the database.
	 */
	interface Encoder {

		/**
		 * Name of mechanism for which encoder works
		 * @return
		 */
		String getName();

		/**
		 * Encrypt plaintext password for user
		 * @param user
		 * @param password to encode
		 * @return
		 */
		String encode(BareJID user, String password);

	}

	/**
	 * Interface implemented by credentials decoder converting from value stored in database to the form
	 * represented by implementation of Entry interface.
	 */
	interface Decoder {

		/**
		 * Name of mechanism for which decoder works
		 * @return
		 */
		String getName();

		/**
		 * Decode password stored in database to more suitable form.
		 * @param user
		 * @param value to decode
		 * @return
		 */
		Entry decode(BareJID user, String value);

	}

}
