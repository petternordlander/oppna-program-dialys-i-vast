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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author clalu4
 */
@Entity
@Table(name = "Ansvarig")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ansvarig.findAll", query = "SELECT a FROM Ansvarig a")
    , @NamedQuery(name = "Ansvarig.findById", query = "SELECT a FROM Ansvarig a WHERE a.id = :id")
    , @NamedQuery(name = "Ansvarig.findByNamn", query = "SELECT a FROM Ansvarig a WHERE a.namn = :namn")
    , @NamedQuery(name = "Ansvarig.findByMottagningID", query = "SELECT a FROM Ansvarig a WHERE a.mottagningID = :mottagningID")})
public class Ansvarig implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 50)
    @Column(name = "Namn")
    private String namn;
    @Column(name = "MottagningID")
    private Integer mottagningID;

    public Ansvarig() {
    }

    public Ansvarig(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamn() {
        return namn;
    }

    public void setNamn(String namn) {
        this.namn = namn;
    }

    public Integer getMottagningID() {
        return mottagningID;
    }

    public void setMottagningID(Integer mottagningID) {
        this.mottagningID = mottagningID;
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
        if (!(object instanceof Ansvarig)) {
            return false;
        }
        Ansvarig other = (Ansvarig) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "se.vgregion.dialys.i.vast.jpa.requisitions.Ansvarig[ id=" + id + " ]";
    }
    
}
