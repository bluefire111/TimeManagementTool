package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
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

/** This is an auto generated class representing the Appointments type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Appointments", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byUser", fields = {"UserID"})
public final class Appointments implements Model {
  public static final QueryField ID = field("Appointments", "id");
  public static final QueryField USER_ID = field("Appointments", "UserID");
  public static final QueryField TITLE = field("Appointments", "Title");
  public static final QueryField DESCRIPTION = field("Appointments", "Description");
  public static final QueryField DATEOF_APPO = field("Appointments", "DateofAppo");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String UserID;
  private final @ModelField(targetType="String") String Title;
  private final @ModelField(targetType="String") String Description;
  private final @ModelField(targetType="String") String DateofAppo;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getUserId() {
      return UserID;
  }
  
  public String getTitle() {
      return Title;
  }
  
  public String getDescription() {
      return Description;
  }
  
  public String getDateofAppo() {
      return DateofAppo;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Appointments(String id, String UserID, String Title, String Description, String DateofAppo) {
    this.id = id;
    this.UserID = UserID;
    this.Title = Title;
    this.Description = Description;
    this.DateofAppo = DateofAppo;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Appointments appointments = (Appointments) obj;
      return ObjectsCompat.equals(getId(), appointments.getId()) &&
              ObjectsCompat.equals(getUserId(), appointments.getUserId()) &&
              ObjectsCompat.equals(getTitle(), appointments.getTitle()) &&
              ObjectsCompat.equals(getDescription(), appointments.getDescription()) &&
              ObjectsCompat.equals(getDateofAppo(), appointments.getDateofAppo()) &&
              ObjectsCompat.equals(getCreatedAt(), appointments.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), appointments.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getUserId())
      .append(getTitle())
      .append(getDescription())
      .append(getDateofAppo())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Appointments {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("UserID=" + String.valueOf(getUserId()) + ", ")
      .append("Title=" + String.valueOf(getTitle()) + ", ")
      .append("Description=" + String.valueOf(getDescription()) + ", ")
      .append("DateofAppo=" + String.valueOf(getDateofAppo()) + ", ")
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
  public static Appointments justId(String id) {
    return new Appointments(
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
      Title,
      Description,
      DateofAppo);
  }
  public interface UserIdStep {
    BuildStep userId(String userId);
  }
  

  public interface BuildStep {
    Appointments build();
    BuildStep id(String id);
    BuildStep title(String title);
    BuildStep description(String description);
    BuildStep dateofAppo(String dateofAppo);
  }
  

  public static class Builder implements UserIdStep, BuildStep {
    private String id;
    private String UserID;
    private String Title;
    private String Description;
    private String DateofAppo;
    @Override
     public Appointments build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Appointments(
          id,
          UserID,
          Title,
          Description,
          DateofAppo);
    }
    
    @Override
     public BuildStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.UserID = userId;
        return this;
    }
    
    @Override
     public BuildStep title(String title) {
        this.Title = title;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.Description = description;
        return this;
    }
    
    @Override
     public BuildStep dateofAppo(String dateofAppo) {
        this.DateofAppo = dateofAppo;
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
    private CopyOfBuilder(String id, String userId, String title, String description, String dateofAppo) {
      super.id(id);
      super.userId(userId)
        .title(title)
        .description(description)
        .dateofAppo(dateofAppo);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder dateofAppo(String dateofAppo) {
      return (CopyOfBuilder) super.dateofAppo(dateofAppo);
    }
  }
  
}
