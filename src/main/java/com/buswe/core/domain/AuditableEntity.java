package com.buswe.core.domain;

import com.buswe.core.security.JPAUserDetails;
import com.buswe.module.core.entity.Userinfo;
import org.springframework.data.domain.Auditable;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.buswe.core.security.SecurityHelper.getCurrentUserDetails;
import org.joda.time.DateTime;

@MappedSuperclass
public abstract class AuditableEntity
  extends IdEntity
  implements Auditable<Userinfo,String>

{
  private static final long serialVersionUID = 5718183941573890588L;
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="creat_user")
  private Userinfo createdBy;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="creat_date")
  private Date createdDate;
  @ManyToOne(fetch=FetchType.LAZY)
  @JoinColumn(name="last_modify_user")
  private Userinfo lastModifiedBy;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="last_modify_date")
  private Date lastModifiedDate;

    /**
     * Returns the user who created this entity.
     *
     * @return the createdBy
     */
   public  Userinfo getCreatedBy()
    {
        JPAUserDetails userDetails= (JPAUserDetails)getCurrentUserDetails();
        return   userDetails.getUserinfo();
    }
public void setCreatedBy(Userinfo createdBy)
{
    this.createdBy=createdBy;
}

    /**
     * Returns the creation date of the entity.
     *
     * @return the createdDate
     */
  public   DateTime getCreatedDate()
  {
      if(createdDate==null)
      {
        return   DateTime.now() ;
      //    return LocalDateTime.now().toLocalDate();
      }
      else
      {
          return new DateTime(createdDate);
      }
  }

    /**
     * Sets the creation date of the entity.
     *
     * @param creationDate the creation date to set
     */
   public void setCreatedDate(DateTime creationDate)
    {

        this.createdDate=new Date(createdDate.getTime());
    }

    /**
     * Returns the user who modified the entity lastly.
     *
     * @return the lastModifiedBy
     */
   public  Userinfo  getLastModifiedBy()
   {
       return  this.lastModifiedBy;
   }

    /**
     * Sets the user who modified the entity lastly.
     *
     * @param lastModifiedBy the last modifying entity to set
     */
   public void setLastModifiedBy(Userinfo lastModifiedBy)
    {
this.lastModifiedBy=lastModifiedBy;
    }

    /**
     * Returns the date of the last modification.
     *
     * @return the lastModifiedDate
     */
 public     DateTime getLastModifiedDate(){

     if(lastModifiedDate==null)
     {
         return   DateTime.now() ;
         //    return LocalDateTime.now().toLocalDate();
     }
     else
     {
         return new DateTime(lastModifiedDate);
     }

 }

    /**
     * Sets the date of the last modification.
     *
     * @param lastModifiedDate the date of the last modification to set
     */
    public void setLastModifiedDate(DateTime lastModifiedDate)
    {
        this.lastModifiedDate=lastModifiedDate.toDate();
    }
}
