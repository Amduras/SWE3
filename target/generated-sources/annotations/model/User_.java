package model;

import enums.AuthLvl;
import enums.IsActive;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(User.class)
public abstract class User_ {

	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, Long> lastlogin;
	public static volatile SingularAttribute<User, IsActive> isActive;
	public static volatile SingularAttribute<User, AuthLvl> authLvl;
	public static volatile SingularAttribute<User, Integer> userID;
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> username;

}

