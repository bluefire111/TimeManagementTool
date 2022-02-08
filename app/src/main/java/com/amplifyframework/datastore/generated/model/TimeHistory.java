package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the TimeHistory type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "TimeHistories", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byUser", fields = {"UserID"})
public final class TimeHistory implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField USER_ID = field( "UserID");
  public static final QueryField DATE = field("Date");
  public static final QueryField CHECK_IN = field("CheckIn");
  public static final QueryField DESCRIPTION = field("Description");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String UserID;
  private final @ModelField(targetType="AWSDateTime")
  String Date;
  private final @ModelField(targetType="AWSDateTime")
  String CheckIn;
  private final @ModelField(targetType="String") String Description;
  private @ModelField(targetType="AWSDateTime") Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime") Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return UserID;
  }
  
  public String getDate() {
      return Date;
  }
  
  public String getCheckIn() {
      return CheckIn;
  }
  
  public String getDescription() {
      return Description;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private TimeHistory(String id, String UserID, String Date, String CheckIn, String Description) {
    this.id = id;
    this.UserID = UserID;
    this.Date = Date;
    this.CheckIn = CheckIn;
    this.Description = Description;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      TimeHistory timeHistory = (TimeHistory) obj;
      return ObjectsCompat.equals(getId(), timeHistory.getId()) &&
              ObjectsCompat.equals(getUserId(), timeHistory.getUserId()) &&
              ObjectsCompat.equals(getDate(), timeHistory.getDate()) &&
              ObjectsCompat.equals(getCheckIn(), timeHistory.getCheckIn()) &&
              ObjectsCompat.equals(getDescription(), timeHistory.getDescription()) &&
              ObjectsCompat.equals(getCreatedAt(), timeHistory.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), timeHistory.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getDate())
      .append(getCheckIn())
      .append(getDescription())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("TimeHistory {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("UserID=" + String.valueOf(getUserId()) + ", ")
      .append("Date=" + String.valueOf(getDate()) + ", ")
      .append("CheckIn=" + String.valueOf(getCheckIn()) + ", ")
      .append("Description=" + String.valueOf(getDescription()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static UserIdStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static TimeHistory justId(String id) {
    return new TimeHistory(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      UserID,
      Date,
      CheckIn,
      Description);
  }
  public interface UserIdStep {
    BuildStep userId(String userId);
  }
  

  public interface BuildStep {
    TimeHistory build();
    BuildStep id(String id);
    BuildStep date(String date);
    BuildStep checkIn(String checkIn);
    BuildStep description(String description);
  }
  

  public static class Builder implements UserIdStep, BuildStep {
    private String id;
    private String UserID;
    private String Date;
    private String CheckIn;
    private String Description;
    @Override
     public TimeHistory build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new TimeHistory(
          id,
          UserID,
          Date,
          CheckIn,
          Description);
    }
    
    @Override
     public BuildStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.UserID = userId;
        return this;
    }
    
    @Override
     public BuildStep date(String date) {
        this.Date = date;
        return this;
    }
    
    @Override
     public BuildStep checkIn(String checkIn) {
        this.CheckIn = checkIn;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.Description = description;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String userId, String date, String checkIn, String description) {
      super.id(id);
      super.userId(userId)
        .date(date)
        .checkIn(checkIn)
        .description(description);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder date(String date) {
      return (CopyOfBuilder) super.date(date);
    }
    
    @Override
     public CopyOfBuilder checkIn(String checkIn) {
      return (CopyOfBuilder) super.checkIn(checkIn);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
  }
  
}
