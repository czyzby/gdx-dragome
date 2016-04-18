
package com.dragome.gdx;

import java.net.URL;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.PrioritySolver;
import com.dragome.commons.compiler.classpath.Classpath;
import com.dragome.commons.compiler.classpath.ClasspathEntry;
import com.dragome.web.config.DomHandlerApplicationConfigurator;

/** Allows to configure Dragome compiler. Do not confuse with {@link DragomeApplicationConfiguration}.
 * @author MJ */
@DragomeConfiguratorImplementor(priority = 10)
public class DragomeCompilationConfiguration extends DomHandlerApplicationConfigurator {
	public DragomeCompilationConfiguration () {
	}

	@Override
	public boolean filterClassPath (final String aClassPathEntry) {
		boolean include = super.filterClassPath(aClassPathEntry);
		include |= aClassPathEntry.contains("gdx");
		return include;
	}

	@Override
	public void sortClassPath (final Classpath classPath) {
		classPath.sortByPriority(new PrioritySolver() {
			@Override
			public int getPriorityOf (final ClasspathEntry string) {
				if (!string.getName().contains(".jar")) {
					return 2;
				} else if (string.getName().contains("dragome-")) {
					return 1;
				}
				return 0;
			}
		});
	}

	@Override
	public URL getAdditionalCodeKeepConfigFile () {
		return DragomeCompilationConfiguration.class.getResource("/additional-code-keep.conf");
	}

	@Override
	public boolean isCheckingCast () {
		return false;
	}

	@Override
	public boolean isRemoveUnusedCode () {
		return true;
	}
}
