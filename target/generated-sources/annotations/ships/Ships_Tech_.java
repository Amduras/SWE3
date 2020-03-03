package ships;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Ships_Tech.class)
public abstract class Ships_Tech_ {

	public static volatile SingularAttribute<Ships_Tech, Integer> shield;
	public static volatile SingularAttribute<Ships_Tech, Integer> weapon;
	public static volatile SingularAttribute<Ships_Tech, Integer> hull;
	public static volatile SingularAttribute<Ships_Tech, Integer> cargoCapacity;
	public static volatile SingularAttribute<Ships_Tech, Integer> fuelConsumption;
	public static volatile SingularAttribute<Ships_Tech, Long> speed;
	public static volatile SingularAttribute<Ships_Tech, Integer> shipId;

}

