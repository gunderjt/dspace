/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */
package org.dspace.xmlworkflow.storedcomponents;

import org.dspace.content.Collection;
import org.dspace.eperson.Group;

import javax.persistence.*;
import java.sql.SQLException;

/**
/*
 * Represents a workflow assignments database representation.
 * These assignments describe roles and the groups connected
 * to these roles for each collection
 * 
 * @author Bram De Schouwer (bram.deschouwer at dot com)
 * @author Kevin Van de Velde (kevin at atmire dot com)
 * @author Ben Bosman (ben at atmire dot com)
 * @author Mark Diggory (markd at atmire dot com)

 */
@Entity
@Table(name="cwf_collectionrole")
public class CollectionRole {

    @Id
    @Column(name="collectionrole_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="cwf_collectionrole_seq")
    @SequenceGenerator(name="cwf_collectionrole_seq", sequenceName="cwf_collectionrole_seq", allocationSize = 1)
    private int id;

//    @Column(name = "role_id")
//    @Lob
    @Column(name="role_id", columnDefinition = "text")
    private String roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;


    public CollectionRole() {
    }

    public void setRoleId(String id){
        this.roleId = id;
    }

    public String getRoleId(){
        return roleId;
    }

    public void setCollection(Collection collection){
        this.collection = collection;
    }

    public Collection getCollection(){
        return collection;
    }

    public void setGroup(Group group){
        this.group = group;
    }

    public Group getGroup() throws SQLException {
        return group;
    }

}
