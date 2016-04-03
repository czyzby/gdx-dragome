package com.dragome.gdx;

import java.net.URL;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.ClassPath;
import com.dragome.commons.compiler.PrioritySolver;
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

	public void sortClassPath(ClassPath classPath)
	{
		classPath.sortByPriority(new PrioritySolver()
		{
			public int getPriorityOf(String string)
			{
				if (!string.contains(".jar"))
					return 2;
				else if (string.contains("dragome-"))
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
