package com.dragome.gdx;

import java.net.URL;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.PrioritySolver;
import com.dragome.commons.compiler.classpath.Classpath;
import com.dragome.commons.compiler.classpath.ClasspathEntry;
import com.dragome.web.config.DomHandlerApplicationConfigurator;

@DragomeConfiguratorImplementor(priority= 10)
public class DragomeConfiguration extends DomHandlerApplicationConfigurator
{
	public DragomeConfiguration()
	{
	}

	public boolean filterClassPath(String aClassPathEntry)
	{
		boolean include= super.filterClassPath(aClassPathEntry);
		include|= aClassPathEntry.contains("gdx");
		return include;
	}

	public void sortClassPath(Classpath classPath)
	{
		classPath.sortByPriority(new PrioritySolver()
		{
			public int getPriorityOf(ClasspathEntry string)
			{
				if (!string.getName().contains(".jar"))
					return 2;
				else if (string.getName().contains("dragome-"))
					return 1;
				else
					return 0;
			}
		});
	}

	public URL getAdditionalCodeKeepConfigFile()
	{
		return DragomeConfiguration.class.getResource("/additional-code-keep.conf");
	}

	public boolean isCheckingCast()
	{
		return false;
	}

	public boolean isRemoveUnusedCode()
	{
		return true;
	}
}
