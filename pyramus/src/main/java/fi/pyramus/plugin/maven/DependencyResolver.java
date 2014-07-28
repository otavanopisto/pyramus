package fi.pyramus.plugin.maven;

import java.util.List;

import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.RequestTrace;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.CollectRequest;
import org.sonatype.aether.collection.CollectResult;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.Exclusion;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyRequest;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.util.DefaultRequestTrace;

public class DependencyResolver {

	public List<ArtifactResult> resolveDependencies(RepositorySystem system, RepositorySystemSession session, List<RemoteRepository> repositories, Artifact artifact, String artifactScope, List<Exclusion> artifactExclusions)
			throws DependencyCollectionException, DependencyResolutionException {

		RequestTrace trace = DefaultRequestTrace.newChild(null, null);
		
		CollectRequest collect = new CollectRequest();
		collect.setRepositories(repositories);
		collect.setRoot(new Dependency(artifact, "compile", false, artifactExclusions));

		DependencyRequest depRequest = new DependencyRequest(collect, null);
		depRequest.setTrace(trace);
		
		collect.setTrace(DefaultRequestTrace.newChild(trace, depRequest));
		
		CollectResult result = system.collectDependencies(session, collect);
		depRequest.setRoot(result.getRoot());
		return system.resolveDependencies(session, depRequest).getArtifactResults();
	}

}
