package r2b.apps.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

/**
 * Wrapper class for Android's {@link SharedPreferences} interface, which adds a
 * layer of encryption to the persistent storage and retrieval of sensitive
 * key-value pairs of primitive data types.
 * <p>
 * This class provides important - but nevertheless imperfect - protection
 * against simple attacks by casual snooper. It is crucial to remember that
 * even encrypted data may still be susceptible to attacks, especially on rooted
 * or stolen devices!
 * <p>
 */
public class SecurePreferences implements SharedPreferences {
	
	private static SecurePreferences instance;
	private static SharedPreferences prefs;	
	
	private static final class EncryptedEditor implements SharedPreferences.Editor {

		private SharedPreferences.Editor e;
		
		public EncryptedEditor() {
			e = prefs.edit();
		}
		
		@TargetApi(Build.VERSION_CODES.GINGERBREAD)
		@Override
		public void apply() {
			e.apply();
		}

		@Override
		public Editor clear() {
			e.clear();
			return this;
		}

		@Override
		public boolean commit() {
			return e.commit();
		}

		@Override
		public Editor putBoolean(String key, boolean value) {
			e.putString(AESCipher.encrypt(key), AESCipher.encrypt(Boolean.toString(value)));
			return this;
		}

		@Override
		public Editor putFloat(String key, float value) {
			e.putString(AESCipher.encrypt(key), AESCipher.encrypt(Float.toString(value)));
			return this;
		}

		@Override
		public Editor putInt(String key, int value) {
			e.putString(AESCipher.encrypt(key), AESCipher.encrypt(Integer.toString(value)));
			return this;
		}

		@Override
		public Editor putLong(String key, long value) {
			e.putString(AESCipher.encrypt(key), AESCipher.encrypt(Long.toString(value)));
			return this;
		}

		@Override
		public Editor putString(String key, String value) {
			e.putString(AESCipher.encrypt(key), AESCipher.encrypt(value));
			return this;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public Editor putStringSet(String key, Set<String> values) {
			final Set<String> encryptedValues = new HashSet<String>(values.size());
			
			for (String value : values) {
				encryptedValues.add(AESCipher.encrypt(value));
			}
			
			e.putStringSet(AESCipher.encrypt(key), encryptedValues);
			
			return this;
		}

		@Override
		public Editor remove(String key) {
			e.remove(AESCipher.encrypt(key));
			return this;
		}
		
	};
		
	private SecurePreferences (Context context, String name) {
		prefs = context.getApplicationContext().getSharedPreferences(name, Context.MODE_PRIVATE);
	}
	
	/**
	 * Singleton builder.
	 * @param context Application context.
	 * @param name Preferences name.
	 * @return Shared preferences.
	 */
	public static SharedPreferences getSecurePreferences(Context context, String name) {
		instance = new SecurePreferences(context, name);		
		return instance;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#contains(java.lang.String)
	 */
	@Override
	public boolean contains(String key) {
		return prefs.contains(AESCipher.encrypt(key));
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#edit()
	 */
	@Override
	public Editor edit() {
		return new EncryptedEditor();
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getAll()
	 */
	@Override
	public Map<String, ?> getAll() {
		final Map<String, ?> encryptedMap = prefs.getAll();
		final Map<String, String> decryptedMap = new HashMap<String, String>(encryptedMap.size());
		
		for (Entry<String, ?> entry : encryptedMap.entrySet()) {
			decryptedMap.put(AESCipher.decrypt(entry.getKey()),
					AESCipher.decrypt(String.valueOf(entry.getValue())));
		}
		
		return decryptedMap;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defValue) {
		boolean exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Boolean.parseBoolean(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getFloat(java.lang.String, float)
	 */
	@Override
	public float getFloat(String key, float defValue) {
		float exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Float.parseFloat(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defValue) {
		int exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Integer.parseInt(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getLong(java.lang.String, long)
	 */
	@Override
	public long getLong(String key, long defValue) {
		long exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = Long.parseLong(AESCipher.decrypt(auxValue));	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defValue) {
		String exit = defValue;
		
		final String encryptedKey = AESCipher.encrypt(key);
		final String auxValue = prefs.getString(encryptedKey, null);
		
		if(auxValue != null) {
			exit = AESCipher.decrypt(auxValue);	
		}
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#getStringSet(java.lang.String, java.util.Set)
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public Set<String> getStringSet(String key, Set<String> defaultValues) {
		Set<String> exit = defaultValues;
		final Set<String> encryptedSet = prefs.getStringSet(AESCipher.encrypt(key), null);
		
		if (encryptedSet != null) {
			Set<String> decryptedSet = new HashSet<String>(encryptedSet.size());
			
			for (String encryptedValue : encryptedSet) {
				decryptedSet.add(AESCipher.decrypt(encryptedValue));
			}
			
			exit = decryptedSet;
		}		
		
		return exit;
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	@Override
	public void registerOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.registerOnSharedPreferenceChangeListener(listener);
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences#unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener)
	 */
	@Override
	public void unregisterOnSharedPreferenceChangeListener(
			OnSharedPreferenceChangeListener listener) {
		prefs.unregisterOnSharedPreferenceChangeListener(listener);
	}

}
