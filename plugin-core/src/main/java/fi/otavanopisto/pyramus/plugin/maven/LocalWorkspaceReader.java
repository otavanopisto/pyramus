package fi.otavanopisto.pyramus.plugin.maven;


import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.xml.XmlStreamReader;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.codehaus.plexus.util.xml.Xpp3DomBuilder;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.WorkspaceReader;
import org.sonatype.aether.repository.WorkspaceRepository;

public class LocalWorkspaceReader implements WorkspaceReader {
  
  public LocalWorkspaceReader(File workspaceDirectory) {
    this.workspaceDirectory = workspaceDirectory;
    
    readHostedArtifacts();
  }
  
  @Override
  public File findArtifact(Artifact artifact) {
    HostedArtifactMeta hostedArtifactMeta = null;
        
    if ("jar".equals(artifact.getExtension())) {
        hostedArtifactMeta = getHostedArtifactMeta(artifact);
        if (hostedArtifactMeta != null) {
          return hostedArtifactMeta.getClassesFolder();
        }
    } else if ("pom".equals(artifact.getExtension())) {
      hostedArtifactMeta = getHostedArtifactMeta(artifact);
      if (hostedArtifactMeta != null) {
        return hostedArtifactMeta.getPomFile();
      }
    }

    return null;
  }

  @Override
  public List<String> findVersions(Artifact artifact) {
    HostedArtifactMeta hostedArtifactMeta = getHostedArtifactMeta(artifact);
    if (hostedArtifactMeta != null) {
      return Arrays.asList(hostedArtifactMeta.getVersion());
    }
    
    return new ArrayList<String>();
  }
  
  @Override
  public WorkspaceRepository getRepository() {
    return workspaceRepository;
  }

  @SuppressWarnings("deprecation")
  private void readHostedArtifacts() {
    File[] projectFolders = workspaceDirectory.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File workspace, String name) {
        File projectDirectory = new File(workspace, name);
        
        if (projectDirectory.isDirectory()) {
          File pomFile = new File(projectDirectory, "pom.xml");
          return pomFile.exists();
        }
        
        return false;
      }
    });

    for (File projectFolder : projectFolders) {
      File pomFile = new File(projectFolder, "pom.xml");
      try {
        XmlStreamReader pomFileReader = new XmlStreamReader(pomFile);
        try {
          Xpp3Dom dom = Xpp3DomBuilder.build(pomFileReader);
          
          String artifactId = getDomChildValue(dom, "artifactId");
          String groupId = getDomChildValue(dom, "groupId");
          String version = getDomChildValue(dom, "version");
          
          Xpp3Dom parentElement = dom.getChild("parent");

          if (parentElement != null) {
            if (groupId == null) {
              groupId = getDomChildValue(parentElement, "groupId");
            }
            
            if (version == null) {
              version = getDomChildValue(parentElement, "version");
            }
          }

          if ((artifactId != null) && (groupId != null) && (version != null)) {
            hostedArtifacts.add(new HostedArtifactMeta(projectFolder, artifactId, groupId, version));
          }

        } finally {
          pomFileReader.close();
        }
      } catch (XmlPullParserException e) {
        // Could not read artifact, interpreting as non hosted artifact
      } catch (IOException e) {
        // Could not read artifact, interpreting as non hosted artifact
      }
    }
  }
  
  private String getDomChildValue(Xpp3Dom dom, String childName) {
    Xpp3Dom child = dom.getChild(childName);
    if (child != null) {
      return child.getValue();
    }
    
    return null;
  }

  private HostedArtifactMeta getHostedArtifactMeta(Artifact artifact) {
    return getHostedArtifactMeta(artifact.getArtifactId(), artifact.getGroupId(), artifact.getVersion());
  }
  
  private HostedArtifactMeta getHostedArtifactMeta(String artifactId, String groupId, String version) {
    for (HostedArtifactMeta hostedArtifactMeta : hostedArtifacts) {
      if (hostedArtifactMeta.getArtifactId().equals(artifactId) && hostedArtifactMeta.getGroupId().equals(groupId) && hostedArtifactMeta.getVersion().equals(version)) {
        return hostedArtifactMeta;
      }
    }
    
    return null;
  }
  
  private WorkspaceRepository workspaceRepository = new WorkspaceRepository();
  private File workspaceDirectory;
  private List<HostedArtifactMeta> hostedArtifacts = new ArrayList<HostedArtifactMeta>();
  
  private class HostedArtifactMeta {
    
    public HostedArtifactMeta(File projectFolder, String artifactId, String groupId, String version) {
      this.projectFolder = projectFolder;
      this.artifactId = artifactId;
      this.groupId = groupId;
      this.version = version;
    }

    public String getArtifactId() {
      return artifactId;
    }
    
    public String getGroupId() {
      return groupId;
    }
    
    public String getVersion() {
      return version;
    }
    
    public File getProjectFolder() {
      return projectFolder;
    }
    
    public File getPomFile() {
      return new File(getProjectFolder(), "pom.xml");
    }

    public File getClassesFolder() {
      return new File(getProjectFolder(), "target/classes");
    }

    private File projectFolder;
    private String artifactId;
    private String groupId;
    private String version;
  }
}