/*
 * DBManager
 * 
 * 0.1
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

import java.util.List;

import r2b.apps.utils.logger.Logger;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


/**
 * 
 * Database manager.
 * 
 * @param <K> Key
 */
public class DBManager<K> {

	/**
	 * Order by clause
	 */
	public static enum ORDER_BY {
		ASC, DESC
	};	
	/**
	 * DB handler.
	 */
	private final DatabaseHandler handler;
	/**
	 * Generic DAO.
	 */
	private GenericDao<DBEntity<K>, K> dao;
	/**
	 * Database.
	 */
	private final SQLiteDatabase db;
	
	/**
	 * Builder.
	 * @param context The application context.
	 */
	public DBManager(final Context context) {
		handler = DatabaseHandler.init(context.getApplicationContext());
		
		db = DatabaseHandler.getDatabase();
		
		dao = new GenericDaoImpl<DBEntity<K>, K>(db, handler.getIncrementalKeys());
	}
	
	/**
	 * Insert item on db.
	 * @param e Item to insert.
	 * @return Item with setted id.
	 * @throws IllegalArgumentException, when item is null.
	 */
	public DBEntity<K> create(DBEntity<K> e) {
		return (DBEntity<K>) dao.create(e);
	}
	
    /**
     * Get item with id.
     * @param id Id of the item to get.
     * @param clazz The class of the entity to retrieve.
     * @return Item, null if is not stored.
     * @throws IllegalArgumentException, id is null.
     */
	@SuppressWarnings("unchecked")
	public DBEntity<K> retrieve(final K id, final Class<? extends DBEntity<K>> clazz) {
		return (DBEntity<K>) dao.retrieve(id, (Class<DBEntity<K>>) clazz);
	}

    /**
     * Change the values of the item, except id, to new values.
     * @param e Item to update with the new values.
     * @return Item updated, null if is not stored.
     * @throws IllegalArgumentException, item is null.
     */
	public DBEntity<K> update(DBEntity<K> e) {
		return (DBEntity<K>) dao.update(e);		
	}
    
    /**
     * Delete the item if is stored.
     * @param e The item to delete.
     * @throws IllegalArgumentException, item is null.
     */
	public void delete(final DBEntity<K> e) {
		dao.delete(e);
	}
    
    /**
     * List all elements.
     * @param clazz The class of the entity to retrieve.
     * @return List of items, or an empty list. Never null.
     */
	@SuppressWarnings("unchecked")
	public List<DBEntity<K>> listAll(final Class<? extends DBEntity<K>> clazz) {
		return dao.listAll((Class<DBEntity<K>>) clazz);
	}
	
    /**
     * List all elements ordering and with limit, zero if no limits. Null if no order.
     * @param clazz The class of the entity to retrieve.
     * @param row The row to apply order.
	 * @param order The order, ASC or DESC. Null if no order.
	 * @param limit The max number of items. Zero for no limits.
	 * @return List of items, or an empty list. Never null.
	 */
	@SuppressWarnings("unchecked")
	public List<DBEntity<K>> listAll(final Class<? extends DBEntity<K>> clazz, String row, ORDER_BY order, int limit) {
		return dao.listAll((Class<DBEntity<K>>) clazz, row, order == null ? null : order.toString(), limit);
	}
	
	/**
	 * Bulk insert properly with transactions.
	 * @param list The items to insert.
	 * @throws IllegalArgumentException, when list is null.
	 */
	public void bulkInsert(List<DBEntity<K>> list) throws IllegalArgumentException {
		
		if(list == null) {
			throw new IllegalArgumentException("list argument is null");
		}
		
		try {
			
			db.beginTransaction();
			
			for(DBEntity<K> item : list) {
				dao.create(item);	
			}			
			
			db.setTransactionSuccessful();									

		} catch (SQLException e) {
			Logger.e(DBManager.class.getSimpleName(), "Can't bulk insert", e);
			throw new RuntimeException(e);
		} finally {						
			db.endTransaction();			
		}
	}
	
	/**
	 * Bulk update properly with transactions.
	 * @param list The items to update.
	 * @throws IllegalArgumentException, when list is null.
	 */
	public void bulkUpdate(List<DBEntity<K>> list) throws IllegalArgumentException {
		
		if(list == null) {
			throw new IllegalArgumentException("list argument is null");
		}
		
		try {
			
			db.beginTransaction();
			
			for(DBEntity<K> item : list) {
				dao.update(item);	
			}			
			
			db.setTransactionSuccessful();									

		} catch (SQLException e) {
			Logger.e(DBManager.class.getSimpleName(), "Can't bulk update", e);
			throw new RuntimeException(e);
		} finally {						
			db.endTransaction();			
		}
	}
	
	/**
	 * Bulk delete properly with transactions.
	 * @param list The items to delete.
	 * @throws IllegalArgumentException, when list is null.
	 */
	public void bulkDelete(List<DBEntity<K>> list) throws IllegalArgumentException {
		
		if(list == null) {
			throw new IllegalArgumentException("list argument is null");
		}
		
		try {
			
			db.beginTransaction();
			
			for(DBEntity<K> item : list) {
				dao.delete(item);	
			}			
			
			db.setTransactionSuccessful();									

		} catch (SQLException e) {
			Logger.e(DBManager.class.getSimpleName(), "Can't bulk delete", e);
			throw new RuntimeException(e);
		} finally {						
			db.endTransaction();			
		}
	}	
	
	/**
	 * Close the db.
	 */
	public void close() {
		handler.close();
		dao = null;
	}
	
}
