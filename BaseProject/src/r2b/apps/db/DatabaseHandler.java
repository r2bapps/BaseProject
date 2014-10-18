/*
 * DatabaseHandler
 * 
 * 0.1.5
 * 
 * 2014/05/16
 * 
 * (The MIT License)
 * 
 * Copyright (c) R2B Apps <r2b.apps@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * 'Software'), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

package r2b.apps.db;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import r2b.apps.R;
import r2b.apps.utils.Cons;
import r2b.apps.utils.logger.Logger;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * NOTE: Foreign key constraints with on delete cascade are supported.
 * It only works since Android 2.2 Froyo which has SQLite 3.6.22
 */
final class DatabaseHandler extends SQLiteOpenHelper {

	/*
	 * The SqliteOpenHelper object holds on to one database connection. 
	 * It appears to offer you a read and write connection, but it really 
	 * doesn't. Call the read-only, and you'll get the write database 
	 * connection regardless.
	 * 
	 * So, one helper instance, one db connection. Even if you use it from 
	 * multiple threads, one connection at a time. The SqliteDatabase object 
	 * uses java locks to keep access serialized. So, if 100 threads have 
	 * one db instance, calls to the actual on-disk database are serialized.
	 * 
	 * So, one helper, one db connection, which is serialized in java code. 
	 * One thread, 1000 threads, if you use one helper instance shared between 
	 * them, all of your db access code is serial. And life is good (ish).
	 * 
	 * If you try to write to the database from actual distinct connections at 
	 * the same time, one will fail. It will not wait till the first is done 
	 * and then write. It will simply not write your change.
	 */

	/**
	 * Database version.
	 */
	public static final int DATABASE_VERSION = Cons.DB.DATABASE_VERSION;
	/**
	 * Database name.
	 */
	public static final String DATABASE_NAME = Cons.DB.DATABASE_NAME;	
	/**
	 * Handler instance.
	 */
	private static DatabaseHandler instance;
	/**
	 * Database instance.
	 */
	private static SQLiteDatabase db;
	/**
	 * Application context.
	 */
	private static Context mContext;
	/**
	 * Incremental key cache.
	 */
	private final HashMap<String, Boolean> incrementalFlagCache = new HashMap<String, Boolean>();
	
	/**
	 * Init the database.
	 * 
	 * WARNING: On Cons.CLEAR_DB_ON_START = true, deletes db file on each start.
	 * 
	 * @param context The application context.
	 * @return The db handler.
	 */
	public synchronized static DatabaseHandler init(final Context context) {
		
		if(Cons.DB.CLEAR_DB_ON_START) {
			
			if (instance != null) {
				instance.close();
			}
			
			final File dbFile = context.getDatabasePath(DATABASE_NAME);
		    if(dbFile != null && dbFile.exists()) {
				boolean exit = context.deleteDatabase(DATABASE_NAME);
				if(exit) {
					Logger.i(DatabaseHandler.class.getSimpleName(), "Deleted database on startup");
				}
				if(!exit) {
					Logger.e(DatabaseHandler.class.getSimpleName(), "Can't delete database on startup");
					throw new RuntimeException("Can't delete database on startup");						
				}
		    }
		}	
		
		if(instance == null) { 						
			instance = new DatabaseHandler(context);
			mContext = context.getApplicationContext();
		}				
		return instance;
	}
	
	/**
	 * Singleton method.
	 * @return The database on writable mode.
	 */
	public synchronized static SQLiteDatabase getDatabase() {
		if (db == null) {
			db = instance.getWritableDatabase();
			Logger.i(DatabaseHandler.class.getSimpleName(), "Getting writable database");
		}
		return db;
	}
	
	/**
	 * Builder.
	 * @param context The application context.
	 */
	private DatabaseHandler(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Logger.i(DatabaseHandler.class.getSimpleName(), "Database name: " + DATABASE_NAME);
		Logger.i(DatabaseHandler.class.getSimpleName(), "Database version: " + DATABASE_VERSION);
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(final SQLiteDatabase db) {
		Logger.i(DatabaseHandler.class.getSimpleName(), "onCreate");		
		
		try {
			
			db.beginTransaction();

			/**
			 * Is not possible executing multiple statements with SQLiteDatabase.execSQL
			 */
			
		    Properties properties = loadProperties(R.raw.create_table);
		    StringBuilder[] tables = new StringBuilder[properties.size()];
		    tables = createTable(tables, properties);
			
			if(tables != null) {
				for(StringBuilder item : tables) {
					db.execSQL(item.toString());
					item.setLength(0);
				}
								
				
			    properties = loadProperties(R.raw.create_index);
			    ArrayList<StringBuilder> index = new ArrayList<StringBuilder>(properties.size());
			    index = createIndex(index, properties);
				if(index != null) {
					for(StringBuilder item : index) {
						db.execSQL(item.toString());
						item.setLength(0);
					}	
					index.clear();
				}				
				
			}
		
			
			db.setTransactionSuccessful();									

		} catch (SQLException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't create database", e);
			throw new RuntimeException(e);
		} finally {						
			mContext = null;
			db.endTransaction();
			
			if(Cons.DEBUG) {
				
				String query = "SELECT name FROM sqlite_master WHERE type = 'table';";
				Cursor c = db.rawQuery(query, null);
				if (c != null && c.moveToFirst()) {
					do {		
						Logger.i(DatabaseHandler.class.getSimpleName(), 
								"Created database table: " + String.valueOf(c.getString(0)));	    							
					} while (c.moveToNext());
					
					c.close();
				}				
				
				query = "SELECT name FROM sqlite_master WHERE type = 'index';";
				c = db.rawQuery(query, null);
				if (c != null && c.moveToFirst()) {
					do {		
						Logger.i(DatabaseHandler.class.getSimpleName(), 
								"Created database indexes: " + String.valueOf(c.getString(0)));	    							
					} while (c.moveToNext());
					
					c.close();
				}				
				
			}
			
		}
		
	}

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Logger.i(DatabaseHandler.class.getSimpleName(), "onUpgrade");
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onOpen(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	        Logger.i(DatabaseHandler.class.getSimpleName(), "Enabling foreign key constraints");
	        Logger.i(DatabaseHandler.class.getSimpleName(), "Enabling delete on cascade support");
	    }
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#close()
	 */
	@Override
	public synchronized void close() {
		if (db != null) {
			db.close();			
			db = null;
			instance = null;
			incrementalFlagCache.clear();
			Logger.i(DatabaseHandler.class.getSimpleName(), "Close database");
		}
	}
	
	/**
	 * Get the incremental keys by tables.
	 * @return True if has key, false otherwise.
	 */
	synchronized final Map<String, Boolean> getIncrementalKeys() {
		return incrementalFlagCache;
	}

	/**
	 * Add incremental key on flag cache.
	 * @param table The table
	 * @param hasKey True has incremental key, false otherwise.
	 */
	private void addIncrementalKeys(final String table, final Boolean hasKey) {
		incrementalFlagCache.put(table, hasKey);
	}
	
	/**
	 * Read from the /res/raw directory
	 * @param propertiesFileResId
	 * @return
	 */
	private Properties loadProperties(final int propertiesFileResId) {	
		InputStream rawResource = null;
		try {
		    rawResource = mContext.getResources().openRawResource(propertiesFileResId);
		    Properties properties = new Properties();
		    properties.load(rawResource);
		    rawResource.close();
		    return properties;
		} catch (NotFoundException | IOException e) {
			Logger.e(DatabaseHandler.class.getSimpleName(), "Can't read database properties", e);
			throw new RuntimeException(e);
		} finally {
			if(rawResource != null) {
				try {
					rawResource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Build create table query.
	 * Populates incrementalFlagCache.
	 * @param tables
	 * @param properties
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private StringBuilder[] createTable(StringBuilder[] tables, final Properties properties) {
		
		if(properties == null) {
			return null;
		}
		
		final Enumeration<Object> e = properties.keys();
		int i = 0;
		
	    while (e.hasMoreElements()) {
	      String key = (String) e.nextElement();
	      String value = properties.getProperty(key);
	      
	      // Add info of incremental keys
	      if(value.toUpperCase().contains("AUTOINCREMENT")) {
	    	  addIncrementalKeys(key.trim(), true);
		      Logger.i(DatabaseHandler.class.getSimpleName(), 
						"Incremental key on '" + key.trim() + "' is: " + String.valueOf(true));
	      }
	      else {
	    	  addIncrementalKeys(key.trim(), false);
		      Logger.i(DatabaseHandler.class.getSimpleName(), 
						"Incremental key on '" + key.trim() + "' is: " + String.valueOf(false));
	      }	      

	      
	      tables[i] = new StringBuilder();
	      	      
	      tables[i]
	      	.append("CREATE TABLE ")
	      	.append(key.trim());
	      tables[i]
	      	.append(" ( ")
	      	.append(value)
	      	.append(" );");	  
	      
	      i++;
	    }
	    
	    return tables;
	    
	}	
	
	/**
	 * Build create index query.
	 * @param strBuilder
	 * @param properties
	 * @return
	 */
	private ArrayList<StringBuilder> createIndex(ArrayList<StringBuilder> strBuilder, final Properties properties) {
		
		if(properties == null) {
			return null;
		}
		
		final Enumeration<Object> e = properties.keys();
	    while (e.hasMoreElements()) {
	    	String key = (String) e.nextElement();
	    	String value = properties.getProperty(key);
	      
		      String [] index = getIndex(value.trim());     	      
		      
		      if(index != null) {
		    	  for(int i = 0; i < index.length; i++) {
		    		  
		    		  if(index[i] != null && !"".equals(index[i])) {
		    			  
		    			  StringBuilder builder = new StringBuilder();
		    			  
		    			  builder
				    		  .append("CREATE INDEX ")
				    		  .append(key.trim());
			    		  builder
				    		  .append("_")
				    		  .append(index[i].trim());
			    		  builder
				    		  .append("_index")
				    		  .append(" ON ");
			    		  builder
				    		  .append(key.trim())
				    		  .append("( ");
			    		  builder
				    		  .append(index[i].trim())
				    		  .append(" );");
			    		  
			    		  strBuilder.add(builder);
		    		  }
		    		  
		    	  }
		      }
	      
	    }
	    
	    return strBuilder;
	    
	}
	
	/**
	 * Split index
	 * @param value
	 * @return
	 */
	private String[] getIndex(final String value) {
		if (value.split(",").length > 0) {
			String[] args = value.split(",");
			return args;			
		}
		return null;
	}
		
	
}
