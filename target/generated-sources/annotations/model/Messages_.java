package model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Messages.class)
public abstract class Messages_ {

	public static volatile SingularAttribute<Messages, Integer> fromUser;
	public static volatile SingularAttribute<Messages, Date> dateSend;
	public static volatile SingularAttribute<Messages, Integer> messagesId;
	public static volatile SingularAttribute<Messages, String> content;

}

