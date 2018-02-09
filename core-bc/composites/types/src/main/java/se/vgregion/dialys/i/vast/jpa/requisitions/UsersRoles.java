/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.vgregion.dialys.i.vast.jpa.requisitions;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author clalu4
 */
@Entity
@Table(name = "UsersRoles")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersRoles.findAll", query = "SELECT u FROM UsersRoles u")
    , @NamedQuery(name = "UsersRoles.findById", query = "SELECT u FROM UsersRoles u WHERE u.id = :id")
    , @NamedQuery(name = "UsersRoles.findByUsersID", query = "SELECT u FROM UsersRoles u WHERE u.usersID = :usersID")
    , @NamedQuery(name = "UsersRoles.findByRolesID", query = "SELECT u FROM UsersRoles u WHERE u.rolesID = :rolesID")})
public class UsersRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "UsersID")
    private int usersID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RolesID")
    private int rolesID;

    public UsersRoles() {
    }

    public UsersRoles(Integer id) {
        this.id = id;
    }

    public UsersRoles(Integer id, int usersID, int rolesID) {
        this.id = id;
        this.usersID = usersID;
        this.rolesID = rolesID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUsersID() {
        return usersID;
    }

    public void setUsersID(int usersID) {
        this.usersID = usersID;
    }

    public int getRolesID() {
        return rolesID;
    }

    public void setRolesID(int rolesID) {
        this.rolesID = rolesID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UsersRoles)) {
            return false;
        }
        UsersRoles other = (UsersRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.vgregion.dialys.i.vast.jpa.requisitions.UsersRoles[ id=" + id + " ]";
    }
    
}
