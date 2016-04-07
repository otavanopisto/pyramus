package fi.otavanopisto.pyramus.plugin.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.building.DefaultModelBuilder;
import org.apache.maven.model.building.DefaultModelBuilderFactory;
import org.apache.maven.repository.internal.DefaultArtifactDescriptorReader;
import org.apache.maven.repository.internal.DefaultVersionRangeResolver;
import org.apache.maven.repository.internal.DefaultVersionResolver;
import org.apache.maven.repository.internal.MavenRepositorySystemSession;
import org.apache.maven.wagon.Wagon;
import org.apache.maven.wagon.providers.http.LightweightHttpWagon;
import org.apache.maven.wagon.providers.http.LightweightHttpWagonAuthenticator;
import org.apache.maven.wagon.providers.http.LightweightHttpsWagon;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.collection.DependencyCollectionException;
import org.sonatype.aether.connector.wagon.WagonProvider;
import org.sonatype.aether.connector.wagon.WagonRepositoryConnectorFactory;
import org.sonatype.aether.graph.Dependency;
import org.sonatype.aether.graph.Exclusion;
import org.sonatype.aether.impl.ArtifactDescriptorReader;
import org.sonatype.aether.impl.ArtifactResolver;
import org.sonatype.aether.impl.MetadataResolver;
import org.sonatype.aether.impl.RemoteRepositoryManager;
import org.sonatype.aether.impl.RepositoryEventDispatcher;
import org.sonatype.aether.impl.SyncContextFactory;
import org.sonatype.aether.impl.UpdateCheckManager;
import org.sonatype.aether.impl.VersionRangeResolver;
import org.sonatype.aether.impl.VersionResolver;
import org.sonatype.aether.impl.internal.DefaultArtifactResolver;
import org.sonatype.aether.impl.internal.DefaultDependencyCollector;
import org.sonatype.aether.impl.internal.DefaultFileProcessor;
import org.sonatype.aether.impl.internal.DefaultLocalRepositoryProvider;
import org.sonatype.aether.impl.internal.DefaultMetadataResolver;
import org.sonatype.aether.impl.internal.DefaultRemoteRepositoryManager;
import org.sonatype.aether.impl.internal.DefaultRepositoryEventDispatcher;
import org.sonatype.aether.impl.internal.DefaultRepositorySystem;
import org.sonatype.aether.impl.internal.DefaultSyncContextFactory;
import org.sonatype.aether.impl.internal.DefaultUpdateCheckManager;
import org.sonatype.aether.impl.internal.SimpleLocalRepositoryManagerFactory;
import org.sonatype.aether.repository.LocalRepository;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactDescriptorException;
import org.sonatype.aether.resolution.ArtifactDescriptorRequest;
import org.sonatype.aether.resolution.ArtifactDescriptorResult;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.resolution.DependencyResolutionException;
import org.sonatype.aether.resolution.VersionRangeRequest;
import org.sonatype.aether.resolution.VersionRangeResolutionException;
import org.sonatype.aether.resolution.VersionRangeResult;
import org.sonatype.aether.resolution.VersionResolutionException;
import org.sonatype.aether.spi.io.FileProcessor;
import org.sonatype.aether.spi.localrepo.LocalRepositoryManagerFactory;
import org.sonatype.aether.util.artifact.DefaultArtifact;
import org.sonatype.aether.version.Version;

/** A class responsible for downloading plugins 
 * and their dependencies using Maven.
 *
 */
public class MavenClient {

  public MavenClient(File localRepositoryDirectory) {
    this(localRepositoryDirectory, null);
  }
  
  /** Create a new Maven client.
   * 
   * @param localRepositoryDirectory The directory containing the local Maven repository.
   * @param eclipseWorkspace Eclipse workspace directory. Adding this enables client to lookup project primarily from Eclipse workspace (for development purposes only)
   */
  public MavenClient(File localRepositoryDirectory, String eclipseWorkspace) {
    RepositoryEventDispatcher repositoryEventDispatcher = new DefaultRepositoryEventDispatcher();
    SyncContextFactory syncContextFactory = new DefaultSyncContextFactory();

    UpdateCheckManager updateCheckManager = new DefaultUpdateCheckManager();
    FileProcessor fileProcessor = new DefaultFileProcessor();

    remoteRepositoryManager = createRepositoryManager(updateCheckManager, fileProcessor);
    
    MetadataResolver metadataResolver = createMetadataResolver(remoteRepositoryManager, repositoryEventDispatcher, syncContextFactory, updateCheckManager);
    VersionResolver versionResolver = createVersionResolver(repositoryEventDispatcher, metadataResolver, syncContextFactory);
    VersionRangeResolver versionRangeResolver = createVersionRangeResolver(metadataResolver, repositoryEventDispatcher, syncContextFactory);
    ArtifactResolver artifactResolver = createArtifactResolver(repositoryEventDispatcher, syncContextFactory, remoteRepositoryManager, versionResolver, fileProcessor);
    ArtifactDescriptorReader artifactDescriptorReader = createArtifactDescriptionReader(repositoryEventDispatcher, versionResolver, artifactResolver, remoteRepositoryManager);
    DefaultRepositorySystem repositorySystem = createRepositorySystem(remoteRepositoryManager, artifactDescriptorReader, versionRangeResolver, versionResolver, artifactResolver);

    this.localRepositoryPath = localRepositoryDirectory.getAbsolutePath();
    this.artifactResolver = artifactResolver;
    this.artifactDescriptorReader = artifactDescriptorReader;
    this.systemSession = createSystemSession(repositorySystem, eclipseWorkspace);
    this.repositorySystem = repositorySystem;
    this.versionRangeResolver = versionRangeResolver;
  }

  private DefaultRepositorySystem createRepositorySystem(RemoteRepositoryManager remoteRepositoryManager, ArtifactDescriptorReader artifactDescriptorReader, VersionRangeResolver versionRangeResolver, VersionResolver versionResolver, ArtifactResolver artifactResolver) {
    DefaultDependencyCollector dependencyCollector = new DefaultDependencyCollector();
    dependencyCollector.setVersionRangeResolver(versionRangeResolver);
    dependencyCollector.setArtifactDescriptorReader(artifactDescriptorReader);
    dependencyCollector.setRemoteRepositoryManager(remoteRepositoryManager);
    
    DefaultRepositorySystem result = new DefaultRepositorySystem();
    result.setVersionRangeResolver(versionRangeResolver);
    result.setVersionResolver(versionResolver);
    result.setDependencyCollector(dependencyCollector);
    result.setArtifactResolver(artifactResolver);
    
    return result;
  }

  /** List the versions of a specified artifact.
   * 
   * @param groupId The Maven group ID of the artifact.
   * @param artifactId The Maven artifact ID of the artifact.
   * @return The versions of the specified artifact available to Maven.
   * @throws VersionRangeResolutionException
   */
  public List<Version> listVersions(String groupId, String artifactId) throws VersionRangeResolutionException {
    Artifact artifact = new DefaultArtifact(groupId, artifactId, "jar", "[0.0.0,999.999.999]");
    VersionRangeRequest request = new VersionRangeRequest(artifact, getRemoteRepositories(), null);
    VersionRangeResult rangeResult = versionRangeResolver.resolveVersionRange(systemSession, request);
    return rangeResult.getVersions();
  }
  
  /** 
   * Describe an artifact residing in a remote repository.
   * 
   * @param artifact The artifact.
   * @return A descriptor result describing the artifact, or <code>null</code> if the artifact doesn't exist.
   * @throws ArtifactDescriptorException
   * @throws ArtifactResolutionException
   * @throws VersionResolutionException 
   */
  public ArtifactDescriptorResult describeArtifact(Artifact artifact) throws ArtifactResolutionException, ArtifactDescriptorException, VersionResolutionException {
    return describeArtifact(artifact, getRemoteRepositories());
  }
  
  /** 
   * Describe an artifact residing in a remote repository.
   * 
   * @param artifact The artifact.
   * @param list of repositories used for resolving
   * @return A descriptor result describing the artifact, or <code>null</code> if the artifact doesn't exist.
   * @throws ArtifactDescriptorException
   * @throws ArtifactResolutionException
   * @throws VersionResolutionException 
   */
  public ArtifactDescriptorResult describeArtifact(Artifact artifact, List<RemoteRepository> remoteRepositories) throws ArtifactResolutionException, ArtifactDescriptorException, VersionResolutionException {
    ArtifactResult artifactResult = artifactResolver.resolveArtifact(systemSession, new ArtifactRequest(artifact, remoteRepositories, null));
    ArtifactDescriptorRequest request = new ArtifactDescriptorRequest(artifactResult.getArtifact(), remoteRepositories, null);
    ArtifactDescriptorResult descriptorResult = artifactDescriptorReader.readArtifactDescriptor(systemSession, request);
    
    for (Dependency dependency : descriptorResult.getDependencies()) {
      if ("compile".equals(dependency.getScope())) {
        artifactResolver.resolveArtifact(systemSession, new ArtifactRequest(dependency.getArtifact(), descriptorResult.getRepositories(), null));
      }
    }
    
    return descriptorResult;
  }

  /** Returns the JAR fire corresponding to the specified artifact.
   * 
   * @param artifact The artifact whose JAR file is returned.
   * @return The JAR fire corresponding to the specified artifact.
   */
  public File getArtifactJarFile(Artifact artifact) {
    if (artifact.getFile() == null) {
      String pathForLocalArtifact = systemSession.getLocalRepositoryManager().getPathForLocalArtifact(artifact);
      return new File(systemSession.getLocalRepository().getBasedir(), pathForLocalArtifact);
    } else {
      return artifact.getFile();
    }
  }
  
  /** Returns the list of remote repositories.
   * 
   * @return The list of remote repositories.
   */
  public List<RemoteRepository> getRemoteRepositories() {
    return remoteRepositories;
  }
  
  /** Add a remote repository for locating the artifacts.
   * 
   * @param remoteRepository repository to be added
   */
  public void addRepository(RemoteRepository remoteRepository) {
    remoteRepositories.add(remoteRepository);
  }

  /** Add a remote repository for locating the artifacts.
   * 
   * @param id The Id of the repository. 
   * @param url The URL of the repository. 
   */
  public void addRepository(String id, String url) {
    this.addRepository(new RemoteRepository(id, "default", url));
  }
  
  /** Remove a repository (local or remote) for locating the artifacts.
   * 
   * @param url The URL of the repository. If it starts with '/', the
   * repository is assumed to be local, otherwise it's assumed to be 
   * remote.
   */
  public void removeRepository(String url) {
    for (RemoteRepository remoteRepository : remoteRepositories) {
      if ((remoteRepository.getUrl().equals(url))) {
        remoteRepositories.remove(remoteRepository);
        return;
      }
    }
  }

  private ArtifactDescriptorReader createArtifactDescriptionReader(RepositoryEventDispatcher repositoryEventDispatcher, VersionResolver versionResolver,
      ArtifactResolver defaultArtifactResolver, RemoteRepositoryManager remoteRepositoryManager) {

    DefaultModelBuilderFactory modelBuilderFactory = new DefaultModelBuilderFactory();
    DefaultModelBuilder modelBuilder = modelBuilderFactory.newInstance();
    
    DefaultArtifactDescriptorReader artifactDescriptorReader = new DefaultArtifactDescriptorReader();
    artifactDescriptorReader.setVersionResolver(versionResolver);
    artifactDescriptorReader.setArtifactResolver(defaultArtifactResolver);
    artifactDescriptorReader.setModelBuilder(modelBuilder);
    artifactDescriptorReader.setRepositoryEventDispatcher(repositoryEventDispatcher);
    artifactDescriptorReader.setRemoteRepositoryManager(remoteRepositoryManager);
    
    return artifactDescriptorReader;
  }

  private ArtifactResolver createArtifactResolver(RepositoryEventDispatcher repositoryEventDispatcher, SyncContextFactory syncContextFactory,
      RemoteRepositoryManager remoteRepositoryManager, VersionResolver versionResolver, FileProcessor fileProcessor) {
    DefaultArtifactResolver artifactResolver = new DefaultArtifactResolver();
    artifactResolver.setSyncContextFactory(syncContextFactory);
    artifactResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
    artifactResolver.setVersionResolver(versionResolver);
    artifactResolver.setRemoteRepositoryManager(remoteRepositoryManager);
    artifactResolver.setFileProcessor(fileProcessor);
    return artifactResolver;
  }

  private VersionResolver createVersionResolver(RepositoryEventDispatcher repositoryEventDispatcher, MetadataResolver metadataResolver, SyncContextFactory syncContextFactory) {
    DefaultVersionResolver versionResolver = new DefaultVersionResolver();
    versionResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
    versionResolver.setMetadataResolver(metadataResolver);
    versionResolver.setSyncContextFactory(syncContextFactory);
    
    return versionResolver;
  }

  private MetadataResolver createMetadataResolver(RemoteRepositoryManager remoteRepositoryManager, RepositoryEventDispatcher repositoryEventDispatcher,
      SyncContextFactory syncContextFactory, UpdateCheckManager updateCheckManager) {
    DefaultMetadataResolver metadataResolver = new DefaultMetadataResolver();
    metadataResolver.setSyncContextFactory(syncContextFactory);
    metadataResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
    metadataResolver.setRemoteRepositoryManager(remoteRepositoryManager);
    metadataResolver.setUpdateCheckManager(updateCheckManager);
    return metadataResolver;
  }

  private VersionRangeResolver createVersionRangeResolver(MetadataResolver metadataResolver, RepositoryEventDispatcher repositoryEventDispatcher,
      SyncContextFactory syncContextFactory) {
    DefaultVersionRangeResolver versionRangeResolver = new DefaultVersionRangeResolver();
    versionRangeResolver.setMetadataResolver(metadataResolver);
    versionRangeResolver.setRepositoryEventDispatcher(repositoryEventDispatcher);
    versionRangeResolver.setSyncContextFactory(syncContextFactory);
    return versionRangeResolver;
  }

  private RemoteRepositoryManager createRepositoryManager(UpdateCheckManager updateCheckManager, FileProcessor fileProcessor) {
    DefaultRemoteRepositoryManager remoteRepositoryManager = new DefaultRemoteRepositoryManager();

    WagonRepositoryConnectorFactory wagonRepositoryConnectorFactory = new WagonRepositoryConnectorFactory();
    wagonRepositoryConnectorFactory.setWagonProvider(new WagonProvider() {
      @Override
      public void release(Wagon wagon) {
      }

      @Override
      public Wagon lookup(String roleHint) throws Exception {
        // TODO: Support for authentication 
        
        switch (roleHint) {
          case "https":
            LightweightHttpsWagon httpsWagon = new LightweightHttpsWagon();
            httpsWagon.setAuthenticator(new LightweightHttpWagonAuthenticator());
            return httpsWagon;
            
          case "http":
            LightweightHttpWagon httpWagon = new LightweightHttpWagon();
            httpWagon.setAuthenticator(new LightweightHttpWagonAuthenticator());
            return httpWagon;
        }
        
        throw new Exception("Could not find wagon");
      }
    });

    wagonRepositoryConnectorFactory.setFileProcessor(fileProcessor);
    remoteRepositoryManager.addRepositoryConnectorFactory(wagonRepositoryConnectorFactory);
    remoteRepositoryManager.setUpdateCheckManager(updateCheckManager);

    return remoteRepositoryManager;
  }

  private RepositorySystemSession createSystemSession(DefaultRepositorySystem repositorySystem, String eclipseWorkspace) {
    repositorySystem.setLocalRepositoryProvider(getLocalRepositoryProvider());
    MavenRepositorySystemSession session = new MavenRepositorySystemSession();
    LocalRepository localRepository = new LocalRepository(localRepositoryPath);
    session.setLocalRepositoryManager(repositorySystem.newLocalRepositoryManager(localRepository));
    
    if (StringUtils.isNotBlank(eclipseWorkspace)) {
      // If Eclipse workspace directory is provided and exists we 
      // add workspace reader for it (for development only)
      
      File eclipseWorkspaceFolder = new File(eclipseWorkspace);
      if (eclipseWorkspaceFolder.exists()) {
        session.setWorkspaceReader(new LocalWorkspaceReader(eclipseWorkspaceFolder));
      }
    }
    
    return session;
  }
 
  /** Returns the repository system used by Maven.
   * 
   * @return the repository system used by Maven.
   */
  public DefaultRepositorySystem getRepositorySystem() {
    return repositorySystem;
  }
 
  private DefaultLocalRepositoryProvider getLocalRepositoryProvider() {
    if (localRepositoryProvider == null) {
      localRepositoryProvider = new DefaultLocalRepositoryProvider();
      localRepositoryProvider.addLocalRepositoryManagerFactory(localRepositoryManagerFactory);
    }
    
    return localRepositoryProvider;
  }
  
  public List<ArtifactResult> resolveDependencies(Artifact artifact, String scope, List<Exclusion> exclusions) throws DependencyCollectionException, DependencyResolutionException {
    DependencyResolver dependencyResolver = new DependencyResolver();
    return dependencyResolver.resolveDependencies(repositorySystem, systemSession, remoteRepositories, artifact, scope, exclusions);
  }

  private RemoteRepositoryManager remoteRepositoryManager;
  private DefaultRepositorySystem repositorySystem;
  private DefaultLocalRepositoryProvider localRepositoryProvider;
  private LocalRepositoryManagerFactory localRepositoryManagerFactory = new SimpleLocalRepositoryManagerFactory();
  private List<RemoteRepository> remoteRepositories = new ArrayList<>();
  private String localRepositoryPath;
  private ArtifactResolver artifactResolver;
  private ArtifactDescriptorReader artifactDescriptorReader;
  private VersionRangeResolver versionRangeResolver;
  private RepositorySystemSession systemSession;
}