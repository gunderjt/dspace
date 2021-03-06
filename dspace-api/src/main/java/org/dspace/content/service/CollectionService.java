/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.content.service;

import org.dspace.authorize.AuthorizeException;
import org.dspace.content.Bitstream;
import org.dspace.content.Collection;
import org.dspace.content.Community;
import org.dspace.content.Item;
import org.dspace.core.Context;
import org.dspace.eperson.Group;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.MissingResourceException;

/**
 * Service interface class for the Collection object.
 * The implementation of this class is responsible for all business logic calls for the Collection object and is autowired by spring
 *
 * @author kevinvandevelde at atmire.com
 */
public interface CollectionService extends DSpaceObjectService<Collection>, DSpaceObjectLegacySupportService<Collection> {

    /**
     * Create a new collection with a new ID.
     * Once created the collection is added to the given community
     *
     * @param context
     *            DSpace context object
     *
     * @return the newly created collection
     * @throws java.sql.SQLException
     * @throws org.dspace.authorize.AuthorizeException
     */
    public Collection create(Context context, Community community) throws SQLException,
            AuthorizeException;


    /**
     * Create a new collection with the supplied handle and with a new ID.
     * Once created the collection is added to the given community
     *
     * @param context
     *            DSpace context object
     *
     * @param handle the pre-determined Handle to assign to the new community
     * @return the newly created collection
     * @throws SQLException
     * @throws AuthorizeException
     */
    public Collection create(Context context, Community community, String handle) throws SQLException,
            AuthorizeException;

    /**
     * Get all collections in the system. These are alphabetically sorted by
     * collection name.
     *
     * @param context
     *            DSpace context object
     *
     * @return the collections in the system
     * @throws SQLException
     */
    public List<Collection> findAll(Context context) throws SQLException;

    /**
     * Get all collections in the system. Adds support for limit and offset.
     * @param context
     * @param limit
     * @param offset
     * @return
     * @throws SQLException
     */
    public List<Collection> findAll(Context context, Integer limit, Integer offset) throws SQLException;

    public List<Collection> findAuthorizedOptimized(Context context, int actionID) throws java.sql.SQLException;

    public List<Collection> findDirectMapped(Context context, int actionID) throws java.sql.SQLException;

    public List<Collection> findGroup2CommunityMapped(Context context) throws SQLException;

    public List<Collection> findGroup2GroupMapped(Context context, int actionID) throws SQLException;

    public List<Collection> findGroupMapped(Context context, int actionID) throws java.sql.SQLException;

    /**
     * Set a metadata value
     *
     * @param field
     *            the name of the metadata field to get
     * @param value
     *            value to set the field to
     *
     * @exception IllegalArgumentException
     *                if the requested metadata field doesn't exist
     * @exception java.util.MissingResourceException
     */
    @Deprecated
    public void setMetadata(Context context, Collection collection, String field, String value) throws MissingResourceException, SQLException;

    /**
     * Give the collection a logo. Passing in <code>null</code> removes any
     * existing logo. You will need to set the format of the new logo bitstream
     * before it will work, for example to "JPEG". Note that
     * <code>update</code> will need to be called for the change to take
     * effect.  Setting a logo and not calling <code>update</code> later may
     * result in a previous logo lying around as an "orphaned" bitstream.
     *
     * @param  is the stream to use as the new logo
     *
     * @return   the new logo bitstream, or <code>null</code> if there is no
     *           logo (<code>null</code> was passed in)
     * @throws AuthorizeException
     * @throws IOException
     * @throws SQLException
     */
    public Bitstream setLogo(Context context, Collection collection, InputStream is) throws AuthorizeException,
                IOException, SQLException;

    /**
     * Create a workflow group for the given step if one does not already exist.
     * Returns either the newly created group or the previously existing one.
     * Note that while the new group is created in the database, the association
     * between the group and the collection is not written until
     * <code>update</code> is called.
     *
     * @param step
     *            the step (1-3) of the workflow to create or get the group for
     *
     * @return the workflow group associated with this collection
     * @throws SQLException
     * @throws AuthorizeException
     */
    public Group createWorkflowGroup(Context context, Collection collection, int step) throws SQLException,
            AuthorizeException;

    /**
     * Set the workflow group corresponding to a particular workflow step.
     * <code>null</code> can be passed in if there should be no associated
     * group for that workflow step; any existing group is NOT deleted.
     *
     * @param step
     *            the workflow step (1-3)
     * @param group
     *            the new workflow group, or <code>null</code>
     */
    public void setWorkflowGroup(Collection collection, int step, Group group);

    /**
     * Get the the workflow group corresponding to a particular workflow step.
     * This returns <code>null</code> if there is no group associated with
     * this collection for the given step.
     *
     * @param step
     *            the workflow step (1-3)
     *
     * @return the group of reviewers or <code>null</code>
     */
    public Group getWorkflowGroup(Collection collection, int step);

    /**
     * Create a default submitters group if one does not already exist. Returns
     * either the newly created group or the previously existing one. Note that
     * other groups may also be allowed to submit to this collection by the
     * authorization system.
     *
     * @return the default group of submitters associated with this collection
     * @throws SQLException
     * @throws AuthorizeException
     */
    public Group createSubmitters(Context context, Collection collection) throws SQLException, AuthorizeException;

    /**
     * Remove the submitters group, if no group has already been created
     * then return without error. This will merely dereference the current
     * submitters group from the collection so that it may be deleted
     * without violating database constraints.
     */
    public void removeSubmitters(Context context, Collection collection) throws SQLException, AuthorizeException;


    /**
     * Create a default administrators group if one does not already exist.
     * Returns either the newly created group or the previously existing one.
     * Note that other groups may also be administrators.
     *
     * @return the default group of editors associated with this collection
     * @throws SQLException
     * @throws AuthorizeException
     */
    public Group createAdministrators(Context context, Collection collection) throws SQLException, AuthorizeException;

    /**
     * Remove the administrators group, if no group has already been created
     * then return without error. This will merely dereference the current
     * administrators group from the collection so that it may be deleted
     * without violating database constraints.
     */
    public void removeAdministrators(Context context, Collection collection) throws SQLException, AuthorizeException;

    /**
     * Get the license that users must grant before submitting to this
     * collection. If the collection does not have a specific license, the
     * site-wide default is returned.
     *
     * @return the license for this collection
     */
    public String getLicense(Collection collection);

    /**
     * Find out if the collection has a custom license
     *
     * @return <code>true</code> if the collection has a custom license
     */
    public boolean hasCustomLicense(Collection collection);

    /**
     * Create an empty template item for this collection. If one already exists,
     * no action is taken. Caution: Make sure you call <code>update</code> on
     * the collection after doing this, or the item will have been created but
     * the collection record will not refer to it.
     *
     * @throws SQLException
     * @throws AuthorizeException
     */
    public void createTemplateItem(Context context, Collection collection) throws SQLException, AuthorizeException;

    /**
     * Remove the template item for this collection, if there is one. Note that
     * since this has to remove the old template item ID from the collection
     * record in the database, the collection record will be changed, including
     * any other changes made; in other words, this method does an
     * <code>update</code>.
     *
     * @throws SQLException
     * @throws AuthorizeException
     * @throws IOException
     */
    public void removeTemplateItem(Context context, Collection collection) throws SQLException, AuthorizeException, IOException;

    /**
     * Add an item to the collection. This simply adds a relationship between
     * the item and the collection - it does nothing like set an issue date,
     * remove a personal workspace item etc. This has instant effect;
     * <code>update</code> need not be called.
     *
     * @param item
     *            item to add
     * @throws SQLException
     * @throws AuthorizeException
     */
    public void addItem(Context context, Collection collection, Item item) throws SQLException, AuthorizeException;

    /**
     * Remove an item. If the item is then orphaned, it is deleted.
     *
     * @param item
     *            item to remove
     * @throws SQLException
     * @throws AuthorizeException
     * @throws IOException
     */
    public void removeItem(Context context, Collection collection, Item item) throws SQLException, AuthorizeException,
            IOException;

    public boolean canEditBoolean(Context context, Collection collection) throws SQLException;

    public boolean canEditBoolean(Context context, Collection collection, boolean useInheritance) throws java.sql.SQLException;

    public void canEdit(Context context, Collection collection) throws SQLException, AuthorizeException;

    public void canEdit(Context context, Collection collection, boolean useInheritance) throws SQLException, AuthorizeException;

    /**
     * return an array of collections that user has a given permission on
     * (useful for trimming 'select to collection' list) or figuring out which
     * collections a person is an editor for.
     *
     * @param context
     * @param comm
     *            (optional) restrict search to a community, else null
     * @param actionID
     *            of the action
     *
     * @return Collection [] of collections with matching permissions
     * @throws SQLException
     */
    public List<Collection> findAuthorized(Context context, Community community, int actionID) throws java.sql.SQLException;

    public Collection findByGroup(Context context, Group group) throws SQLException;
}
