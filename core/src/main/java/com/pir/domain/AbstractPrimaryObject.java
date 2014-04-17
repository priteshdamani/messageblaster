package com.pir.domain;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;

/**
 * Created by pritesh on 12/8/13.
 */


@MappedSuperclass
public abstract class AbstractPrimaryObject extends DomainObject {


    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime modifiedDate;

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime deletedDate;

    public DateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DateTime getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(DateTime deletedDate) {
        this.deletedDate = deletedDate;
    }

    @PreUpdate
    protected void onUpdate() {
        modifiedDate = new DateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AbstractPrimaryObject that = (AbstractPrimaryObject) o;

        if (deletedDate != null ? !deletedDate.equals(that.deletedDate) : that.deletedDate != null) return false;
        if (modifiedDate != null ? !modifiedDate.equals(that.modifiedDate) : that.modifiedDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (modifiedDate != null ? modifiedDate.hashCode() : 0);
        result = 31 * result + (deletedDate != null ? deletedDate.hashCode() : 0);
        return result;
    }
}
