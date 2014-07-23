/*
 * GenericDao
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

/**
 * CRUD operations plus listAll for database.
 * 
 * @param <T> Object
 * @param <K> Key
 */
public interface GenericDao<T, K> {

	/**
	 * Insert T item on db.
	 * @param t Item to insert.
	 * @return T with setted id.
	 * @throws IllegalArgumentException, when T is null.
	 */
	public T create(T t) throws IllegalArgumentException;

    /**
     * Get T item with id.
     * @param id Id of the item to get.
     * @param clazz The class of the entity to retrieve.
     * @return T, null if T is not stored.
     * @throws IllegalArgumentException, id is null.
     */
	public T retrieve(final K id, final Class<T> clazz) throws IllegalArgumentException;

    /**
     * Change the values of T, except id, to new values.
     * @param t Item to update with the new values.
     * @return T updated, null if T is not stored.
     * @throws IllegalArgumentException, T is null.
     */
	public T update(T t) throws IllegalArgumentException;
    
    /**
     * Delete the item if is stored.
     * @param t T The item to delete.
     * @throws IllegalArgumentException, T is null.
     */
	public void delete(final T t) throws IllegalArgumentException;
    

    /**
     * List all elements.
     * @param clazz The class of the entity to retrieve.
     * @return List of items, or an empty list. Never null.
     */
	public List<T> listAll(final Class<T> clazz);

    /**
     * List all elements ordering and with limit, zero if no limits. Null if no order.
     * @param clazz The class of the entity to retrieve.
     * @param row The row to apply order.
	 * @param order The order, ASC or DESC. Null if no order.
	 * @param limit The max number of items. Zero for no limits.
	 * @return List of items, or an empty list. Never null.
	 */
	public List<T> listAll(final Class<T> clazz, String row, String order, int limit);
	
}
