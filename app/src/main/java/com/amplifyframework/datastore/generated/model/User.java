package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.annotations.HasMany;
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

/** This is an auto generated class representing the User type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Users", authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
@Index(name = "byCompany", fields = {"CompanyID"})
public final class User implements Model {
  public static final QueryField ID = field("id");
  public static final QueryField FIRST_NAME = field("First_Name");
  public static final QueryField LAST_NAME = field("Last_Name");
  public static final QueryField USER_ID = field("User_ID");
  public static final QueryField LOGIN_PIN = field("LoginPin");
  public static final QueryField COMPANY_ID = field("CompanyID");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String First_Name;
  private final @ModelField(targetType="String", isRequired = true) String Last_Name;
  private final @ModelField(targetType="String", isRequired = true) String User_ID;
  private final @ModelField(targetType="String", isRequired = true) String LoginPin;
  private final @ModelField(targetType="ID", isRequired = true) String CompanyID;
  private final @ModelField(targetType="TimeHistory") @HasMany(associatedWith = "UserID", type = TimeHistory.class) List<TimeHistory> TimeHistories = null;
  private final @ModelField(targetType="Appointments") @HasMany(associatedWith = "UserID", type = Appointments.class) List<Appointments> Appointments = null;
  private @ModelField(targetType="AWSDateTime") Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime") Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getFirstName() {
      return First_Name;
  }
  
  public String getLastName() {
      return Last_Name;
  }
  
  public String getUserId() {
      return User_ID;
  }
  
  public String getLoginPin() {
      return LoginPin;
  }
  
  public String getCompanyId() {
      return CompanyID;
  }
  
  public List<TimeHistory> getTimeHistories() {
      return TimeHistories;
  }
  
  public List<Appointments> getAppointments() {
      return Appointments;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private User(String id, String First_Name, String Last_Name, String User_ID, String LoginPin, String CompanyID) {
    this.id = id;
    this.First_Name = First_Name;
    this.Last_Name = Last_Name;
    this.User_ID = User_ID;
    this.LoginPin = LoginPin;
    this.CompanyID = CompanyID;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      User user = (User) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getFirstName(), user.getFirstName()) &&
              ObjectsCompat.equals(getLastName(), user.getLastName()) &&
              ObjectsCompat.equals(getUserId(), user.getUserId()) &&
              ObjectsCompat.equals(getLoginPin(), user.getLoginPin()) &&
              ObjectsCompat.equals(getCompanyId(), user.getCompanyId()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getFirstName())
      .append(getLastName())
      .append(getUserId())
      .append(getLoginPin())
      .append(getCompanyId())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("User {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("First_Name=" + String.valueOf(getFirstName()) + ", ")
      .append("Last_Name=" + String.valueOf(getLastName()) + ", ")
      .append("User_ID=" + String.valueOf(getUserId()) + ", ")
      .append("LoginPin=" + String.valueOf(getLoginPin()) + ", ")
      .append("CompanyID=" + String.valueOf(getCompanyId()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static FirstNameStep builder() {
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
  public static User justId(String id) {
    return new User(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      First_Name,
      Last_Name,
      User_ID,
      LoginPin,
      CompanyID);
  }
  public interface FirstNameStep {
    LastNameStep firstName(String firstName);
  }
  

  public interface LastNameStep {
    UserIdStep lastName(String lastName);
  }
  

  public interface UserIdStep {
    LoginPinStep userId(String userId);
  }
  

  public interface LoginPinStep {
    CompanyIdStep loginPin(String loginPin);
  }
  

  public interface CompanyIdStep {
    BuildStep companyId(String companyId);
  }
  

  public interface BuildStep {
    User build();
    BuildStep id(String id);
  }
  

  public static class Builder implements FirstNameStep, LastNameStep, UserIdStep, LoginPinStep, CompanyIdStep, BuildStep {
    private String id;
    private String First_Name;
    private String Last_Name;
    private String User_ID;
    private String LoginPin;
    private String CompanyID;
    @Override
     public User build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new User(
          id,
          First_Name,
          Last_Name,
          User_ID,
          LoginPin,
          CompanyID);
    }
    
    @Override
     public LastNameStep firstName(String firstName) {
        Objects.requireNonNull(firstName);
        this.First_Name = firstName;
        return this;
    }
    
    @Override
     public UserIdStep lastName(String lastName) {
        Objects.requireNonNull(lastName);
        this.Last_Name = lastName;
        return this;
    }
    
    @Override
     public LoginPinStep userId(String userId) {
        Objects.requireNonNull(userId);
        this.User_ID = userId;
        return this;
    }
    
    @Override
     public CompanyIdStep loginPin(String loginPin) {
        Objects.requireNonNull(loginPin);
        this.LoginPin = loginPin;
        return this;
    }
    
    @Override
     public BuildStep companyId(String companyId) {
        Objects.requireNonNull(companyId);
        this.CompanyID = companyId;
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
    private CopyOfBuilder(String id, String firstName, String lastName, String userId, String loginPin, String companyId) {
      super.id(id);
      super.firstName(firstName)
        .lastName(lastName)
        .userId(userId)
        .loginPin(loginPin)
        .companyId(companyId);
    }
    
    @Override
     public CopyOfBuilder firstName(String firstName) {
      return (CopyOfBuilder) super.firstName(firstName);
    }
    
    @Override
     public CopyOfBuilder lastName(String lastName) {
      return (CopyOfBuilder) super.lastName(lastName);
    }
    
    @Override
     public CopyOfBuilder userId(String userId) {
      return (CopyOfBuilder) super.userId(userId);
    }
    
    @Override
     public CopyOfBuilder loginPin(String loginPin) {
      return (CopyOfBuilder) super.loginPin(loginPin);
    }
    
    @Override
     public CopyOfBuilder companyId(String companyId) {
      return (CopyOfBuilder) super.companyId(companyId);
    }
  }
  
}
