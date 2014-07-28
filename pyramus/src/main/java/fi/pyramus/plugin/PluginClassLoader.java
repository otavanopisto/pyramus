package fi.pyramus.plugin;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {
	
	public PluginClassLoader(ClassLoader parentClassLoader) {
		super(new URL[] {}, parentClassLoader);
	}

	public void addJar(URL url) {
		addURL(url);
	}
	
	public void addPath(URL url) {
		addURL(url);
	}

	@Override
	protected void addURL(URL url) {
		super.addURL(url);
	}
	
}
