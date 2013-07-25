package fi.pyramus.rest.test;

import java.io.File;

import org.apache.commons.lang3.ArrayUtils;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

public class RestfulServiceTest {

	protected static Archive<?> createArchive(Class<?>... testClasses) {
		MavenDependencyResolver resolver = DependencyResolvers
				.use(MavenDependencyResolver.class)
				.loadMetadataFromPom("pom.xml");
				
		return ShrinkWrap.create(WebArchive.class, "test.war")
			.addAsLibraries(getHibernateSearchFiles(resolver))
			.addPackages(true, getPersistencePackages())
		  .addClasses(testClasses)
		  .addClasses(org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver.class, RestfulServiceTest.class)
		  .addAsLibraries(resolver.artifact("org.jboss.shrinkwrap.resolver:shrinkwrap-resolver-impl-maven:1.0.0-beta-7").resolveAsFiles())
      .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
      .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
      .addAsWebInfResource("test-ds.xml");
	}
	
	protected static Package[] getPersistencePackages() {
		Package domainModelPackage = fi.pyramus.domainmodel.DomainModelExtensionDescriptor.class.getPackage();
		Package daoPackage = fi.pyramus.dao.GenericDAO.class.getPackage();
		Package persistenceSearchPackage = fi.pyramus.persistence.search.SearchResult.class.getPackage();
		Package persistenceUserTypesPackage = fi.pyramus.persistence.usertypes.MonetaryAmount.class.getPackage();

		return new Package[] {
		  domainModelPackage, daoPackage, persistenceSearchPackage, persistenceUserTypesPackage 
	  };
	}
	
	protected static File[] getHibernateSearchFiles(MavenDependencyResolver resolver) {
		File[] hibernateSearchFiles = resolver.artifact("org.hibernate:hibernate-search:4.1.0.CR3")
				.exclusions("org.hibernate:hibernate-core:*")
				.resolveAsFiles();
		File[] luceneQueriesFiles = resolver.artifact("org.apache.lucene:lucene-queries:3.5.0").resolveAsFiles();
		
		return ArrayUtils.addAll(hibernateSearchFiles, luceneQueriesFiles);
	}

	protected static void log(String message) {
		System.out.println(message);
	}
	
}
