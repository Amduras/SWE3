package model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(WorldSettings.class)
public abstract class WorldSettings_ {

	public static volatile SingularAttribute<WorldSettings, Double> jumpgateCooldown;
	public static volatile SingularAttribute<WorldSettings, Double> fleetToDebrisFieldRatio;
	public static volatile SingularAttribute<WorldSettings, Double> defToDebrisFieldRatio;
	public static volatile SingularAttribute<WorldSettings, Integer> gameSpeed;
	public static volatile SingularAttribute<WorldSettings, String> name;
	public static volatile SingularAttribute<WorldSettings, Integer> protection;
	public static volatile SingularAttribute<WorldSettings, Integer> id;
	public static volatile SingularAttribute<WorldSettings, Integer> fleetSpeed;
	public static volatile SingularAttribute<WorldSettings, Integer> startingPlanetSize;

}

